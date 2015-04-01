package se.mah.ad0025.inluppg2;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller-klass som sköter om logiken i applikationen.
 * @author Jonas Dahlström on 2014-10-12.
 */
public class Controller {
    private MainActivity activity;
    private FragmentManager fragmentManager;
    private StartFragment startFragment;
    private ListGroupsFragment listGroupsFragment;
    private MenuFragment menuFragment;
    private MyMapFragment myMapFragment;
    private JoinedGroupsFragment joinedGroupsFragment;
    private MembersFragment membersFragment;
    private MessagesFragment messagesFragment;
    private MapMenuFragment mapMenuFragment;
    private ImageFragment imageFragment;
    private TCPConnection connection;
    private JsonConverter jsonConverter;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private LocationManager locationManager;
    private GoogleMap myMap;
    private boolean connected = false;
    private String userName, selectedGroup, currentGroup;
    private double longitude, latitude;
    private ArrayList<String> joinedGroups = new ArrayList<String>();
    private ArrayList<String> membersInGroup = new ArrayList<String>();
    private ArrayList<JSONObject> jsonGroupIDs = new ArrayList<JSONObject>();
    private ArrayList<Locations> locationsArray = new ArrayList<Locations>();
    public ArrayList<MemberImage> imageArray = new ArrayList<MemberImage>();
    public static File mFile = null;
    private byte[] uploadArray;
    private HashMap<String, MemberImage> imageHashMap = new HashMap<String, MemberImage>();

    public Controller(MainActivity activity, FragmentManager fragmentManager, SharedPreferences prefs, LocationManager locationManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.prefs = prefs;
        this.locationManager = locationManager;
        this.editor = prefs.edit();
        ReceiveListener listener = new RL();
        startFragment = new StartFragment();
        listGroupsFragment = new ListGroupsFragment();
        menuFragment = new MenuFragment();
        myMapFragment = MyMapFragment.newInstance();
        joinedGroupsFragment = new JoinedGroupsFragment();
        membersFragment = new MembersFragment();
        messagesFragment = new MessagesFragment();
        mapMenuFragment = new MapMenuFragment();
        imageFragment = new ImageFragment();
        jsonConverter = new JsonConverter();
        fragmentManager.beginTransaction().replace(R.id.masterLayout, startFragment).commit();
        startFragment.setController(this);
        listGroupsFragment.setController(this);
        myMapFragment.setController(this);
        menuFragment.setController(this);
        membersFragment.setController(this);
        joinedGroupsFragment.setController(this);
        messagesFragment.setController(this);
        mapMenuFragment.setController(this);
        imageFragment.setController(this);
        connection = new TCPConnection("195.178.232.7", 7117, listener);
        connection.connect();
        userName = prefs.getString("username", "");
    }

    public void disconnectFromServer() {
        if(connected) {
            connection.disconnect();
        }
    }

    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            String jsonString = null;
            for(int i = 0; i < jsonGroupIDs.size(); i++) {
                try {
                    jsonString = jsonConverter.sendLocation(jsonGroupIDs.get(i).getString("id"), String.valueOf(longitude), String.valueOf(latitude));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                connection.send(jsonString);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    };

    public void startLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    public double getUserLatitude() {
        return latitude;
    }

    public double getUserLongitude() {
        return longitude;
    }

    public String getUserName() {
        return userName;
    }

    public void btnListGroupsClicked() {
        if(startFragment.getEtUsername().equals("")) {
            Toast.makeText(startFragment.getActivity(), R.string.toastNoUsername, Toast.LENGTH_SHORT).show();
        } else {
            editor.putString("username", startFragment.getEtUsername());
            editor.commit();
            connection.send(jsonConverter.getGroups());
            fragmentManager.beginTransaction().replace(R.id.masterLayout, listGroupsFragment).addToBackStack(null).commit();
        }
    }

    public void btnCreateGroupClicked() {
        if(startFragment.getEtUsername().equals("") && startFragment.getEtCreateGroup().equals("")) {
            Toast.makeText(startFragment.getActivity(), R.string.toastNoUsernameAndGroupname, Toast.LENGTH_SHORT).show();
        } else if(startFragment.getEtCreateGroup().equals("")) {
            Toast.makeText(startFragment.getActivity(), R.string.toastNoGroupname, Toast.LENGTH_SHORT).show();
        } else if (startFragment.getEtUsername().equals("")) {
            Toast.makeText(startFragment.getActivity(), R.string.toastNoUsername, Toast.LENGTH_SHORT).show();
        } else {
            editor.putString("username", startFragment.getEtUsername());
            editor.commit();
            String newGroupName = startFragment.getEtCreateGroup();
            String jsonString = jsonConverter.registerGroup(newGroupName, prefs.getString("username", ""));
            connection.send(jsonString);
            selectedGroup = newGroupName;
            fragmentManager.beginTransaction().replace(R.id.masterLayout, menuFragment).addToBackStack(null).commit();
            setLoggedInUser(prefs.getString("username", ""));
        }
    }

    public void listViewItemClicked(String listViewItem) {
        String jsonString = jsonConverter.registerGroup(listViewItem, prefs.getString("username", ""));
        connection.send(jsonString);
        selectedGroup = listViewItem;
        fragmentManager.beginTransaction().replace(R.id.masterLayout, menuFragment).addToBackStack(null).commit();
        setLoggedInUser(prefs.getString("username", ""));
    }

    private void setLoggedInUser(String user) {
        menuFragment.setTvLoggedInAs(user);
    }

    public void btnShowMapClicked() {
        fragmentManager.beginTransaction().replace(R.id.masterLayout, mapMenuFragment).addToBackStack(null).replace(R.id.mapFrame, myMapFragment).commit();
    }

    public void btnShowJoinedGroupsClicked() {
        fragmentManager.beginTransaction().replace(R.id.masterLayout, joinedGroupsFragment).addToBackStack(null).commit();
    }

    public void btnMessagesClicked() {
        fragmentManager.beginTransaction().replace(R.id.masterLayout, messagesFragment).addToBackStack(null).commit();
    }

    public ArrayList<String> getJoinedGroups() {
        joinedGroups.clear();
        for(int i = 0; i < jsonGroupIDs.size(); i++) {
            try {
                joinedGroups.add(jsonGroupIDs.get(i).getString("group"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return joinedGroups;
    }

    public void listViewItemJoinedGroupsClicked(String listViewItem) {
        currentGroup = listViewItem;
        String jsonString = jsonConverter.getMembers(listViewItem);
        connection.send(jsonString);
        fragmentManager.beginTransaction().replace(R.id.masterLayout, membersFragment).addToBackStack(null).commit();
    }

    public void btnLeaveGroupClicked() {
        for(int i = 0; i < jsonGroupIDs.size(); i++) {
            try {
                if(currentGroup.equals(jsonGroupIDs.get(i).getString("group"))) {
                    String jsonString = jsonConverter.unregisterGroup(jsonGroupIDs.get(i).getString("id"));
                    connection.send(jsonString);
                    jsonGroupIDs.remove(i);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.masterLayout, joinedGroupsFragment).commit();
    }

    public void btnSendMessageClicked(String message) {
        try {
            String jsonString = jsonConverter.sendText(jsonGroupIDs.get(0).getString("id"), message);
            connection.send(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void makeBitmapToByteArray() {
        Bitmap bm = BitmapResizer.decodeFile(mFile, 128);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        uploadArray = stream.toByteArray();
        fragmentManager.beginTransaction().replace(R.id.masterLayout, imageFragment).addToBackStack(null).commit();
        imageFragment.setImageView(bm);
    }

    public void btnSendImageClicked() {
        String jsonString = "";
        try {
            jsonString = jsonConverter.sendImage(jsonGroupIDs.get(0).getString("id"), imageFragment.getEtImageText(), String.valueOf(longitude), String.valueOf(latitude));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.send(jsonString);
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.masterLayout, menuFragment).commit();
    }

    private class RL implements ReceiveListener {
        public void newMessage(final String answer) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Exception e = connection.getException();
                    if ("CONNECTED".equals(answer)) {
                        connected = true;
                    } else if ("CLOSED".equals(answer)) {
                        connected = false;
                    } else if ("EXCEPTION".equals(answer) && e != null) {
                        Log.d("EXCEPTION", e.toString());
                    }
                }
            });
        }
        public void newJsonMessage(final JSONObject jsonObject) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Resources res = startFragment.getResources();
                    String type;
                    JSONObject memberLocation, memberName;
                    String member, longitude, latitude;
                    try {
                        type = jsonObject.getString("type");
                    if(type.equals("groups")) {
                        JSONArray groups = jsonObject.getJSONArray("groups");
                        listGroupsFragment.updateListViewGroups(groups);
                    } else if(type.equals("register")) {
                        JSONObject jsonGroupId = new JSONObject(jsonConverter.makeGroupIdJson(selectedGroup, jsonObject.getString("id")));
                        jsonGroupIDs.add(jsonGroupId);
                    } else if(type.equals("locations")) {
                        String group = jsonObject.getString("group");
                        JSONArray locations = jsonObject.getJSONArray("location");
                        boolean memberAlreadyInArray = false;
                        for (int i = 0; i < locations.length(); i++) {
                            memberLocation = locations.getJSONObject(i);
                            member = memberLocation.getString("member");
                            longitude = memberLocation.getString("longitude");
                            latitude = memberLocation.getString("latitude");

                            for(int j = 0; j < locationsArray.size(); j++) {
                                if(locationsArray.get(j).getGroup().equals(group) && locationsArray.get(j).getMember().equals(member)) {
                                    locationsArray.get(j).setLongitude(longitude);
                                    locationsArray.get(j).setLatitude(latitude);
                                    memberAlreadyInArray = true;
                                }
                            }
                            if(!memberAlreadyInArray) {
                                locationsArray.add(new Locations(member, group, longitude, latitude));
                            }
                        }
                        if (myMapFragment != null) {
                            myMap = myMapFragment.getMap();
                            if(myMap != null) {
                                myMap.clear();
                                String chosenGroup = mapMenuFragment.getSpinnerGroup();
                                for(int i = 0; i < locationsArray.size(); i++) {
                                    if(chosenGroup.equals(res.getString(R.string.textAll))) {
                                        myMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(locationsArray.get(i).getLatitude()), Double.parseDouble(locationsArray.get(i).getLongitude()))).title(locationsArray.get(i).getMember()));
                                    } else if(chosenGroup.equals(locationsArray.get(i).getGroup())) {
                                        myMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(locationsArray.get(i).getLatitude()), Double.parseDouble(locationsArray.get(i).getLongitude()))).title(locationsArray.get(i).getMember()));
                                    }
                                }
                                if(imageArray.size() > 0) {
                                    Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                                    Bitmap bmp = Bitmap.createBitmap(140, 100, conf);
                                    Canvas canvas = new Canvas(bmp);
                                    imageHashMap.clear();

                                    for(int j = 0; j < imageArray.size(); j++) {
                                        canvas.drawBitmap(imageArray.get(j).getSmallImage(), 0, 0, null);
                                        Marker marker = myMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(imageArray.get(j).getLatitude()), Double.parseDouble(imageArray.get(j).getLongitude()))).title(imageArray.get(j).getMember()).icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                                        imageHashMap.put(marker.getId(), imageArray.get(j));
                                    }
                                }
                                myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        MemberImage memberImage = imageHashMap.get(marker.getId());
                                        if(memberImage != null) {
                                            Resources res = activity.getResources();
                                            ImageView image = new ImageView(activity);
                                            image.setMinimumHeight(450);
                                            image.setMinimumWidth(300);
                                            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                            image.setImageBitmap(memberImage.getImage());
                                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                            builder.setMessage(memberImage.getImageText());
                                            builder.setPositiveButton(res.getString(R.string.alertDialogClose),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            builder.setView(image);
                                            builder.create().show();
                                        }
                                    }
                                });
                            }
                        }
                    } else if(type.equals("location")) {
                        Log.d("LOCATION", "Location: " + jsonObject.getString("longitude") + " , " + jsonObject.getString("latitude"));
                    } else if(type.equals("members")) {
                        membersInGroup.clear();
                        JSONArray members = jsonObject.getJSONArray("members");
                        for(int i = 0; i < members.length(); i++) {
                            memberName = members.getJSONObject(i);
                            membersInGroup.add(memberName.getString("member"));
                        }
                        membersFragment.updateTextView(membersInGroup);
                    } else if(type.equals("textchat")) {
                        messagesFragment.addMessageToList(jsonObject.getString("member"), jsonObject.getString("text"));
                    } else if(type.equals("upload")) {
                        String imageID = jsonObject.getString("imageid");
                        String port = jsonObject.getString("port");
                        new UploadImage(imageID, Integer.parseInt(port)).execute(uploadArray);
                    } else if(type.equals("imagechat")) {
                        new DownloadImage().execute(jsonObject);
                    } else if(type.equals("exception")) {
                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private class UploadImage extends AsyncTask<byte[], Void, String> {
        private String imageID;
        private int port;

        public UploadImage(String imageID, int port) {
            this.imageID = imageID;
            this.port = port;
        }

        @Override
        protected String doInBackground(byte[]... bytes) {
            byte[] uploadArray = bytes[0];
            Socket socket;
            Resources res = activity.getResources();
            String result;
            try {
                socket = new Socket(TCPConnection.address, port);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();
                output.writeUTF(imageID);
                output.flush();
                output.writeObject(uploadArray);
                output.flush();
                socket.close();
                result = res.getString(R.string.imageUploadedToastSuccess);
            } catch (IOException e) {
                e.printStackTrace();
                result = res.getString(R.string.imageUploadedToastFail);
            }
                return result;
        }

        protected void onPostExecute(String result) {
            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadImage extends AsyncTask<JSONObject, Void, byte[]> {
        String imageMember, imageGroup, imageText, imageLongitude, imageLatitude, imageID = "", port = "";
        Bitmap bitmap, smallBitmap;

        @Override
        protected byte[] doInBackground(JSONObject... jsonObjects) {
            try {
                imageMember = jsonObjects[0].getString("member");
                imageGroup = jsonObjects[0].getString("group");
                imageText = jsonObjects[0].getString("text");
                imageLongitude = jsonObjects[0].getString("longitude");
                imageLatitude = jsonObjects[0].getString("latitude");
                imageID = jsonObjects[0].getString("imageid");
                port = jsonObjects[0].getString("port");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] downloadArray = null;
            Socket socket;
            try {
                socket = new Socket(TCPConnection.address, Integer.parseInt(port));
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();
                output.writeUTF(imageID);
                output.flush();
                downloadArray = (byte[])input.readObject();
                socket.close();
                bitmap = BitmapFactory.decodeByteArray(downloadArray, 0, downloadArray.length);
                smallBitmap = bitmap;
                int newWidth = 140, newHeight = 100;
                smallBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

                float ratioX = newWidth / (float) bitmap.getWidth();
                float ratioY = newHeight / (float) bitmap.getHeight();
                float middleX = newWidth / 2.0f;
                float middleY = newHeight / 2.0f;

                Matrix scaleMatrix = new Matrix();
                scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

                Canvas canvas = new Canvas(smallBitmap);
                canvas.setMatrix(scaleMatrix);
                canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return downloadArray;
        }

        protected void onPostExecute(byte[] downloadArray) {
            imageArray.add(new MemberImage(imageMember, imageGroup, imageText, imageLongitude, imageLatitude, bitmap, smallBitmap));
        }
    }
}
