package se.mah.ad0025.inluppg2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * Fragment som visar huvudmenyn med knappar för att navigera sig runt i applikationen.
 * @author Jonas Dahlström
 */
public class MenuFragment extends Fragment {
    private Controller controller;
    private TextView tvLoggedInAs;
    private String loggedInUser;
    private Uri imageUri;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        Button btnShowMap = (Button)view.findViewById(R.id.btnShowMap);
        Button btnShowJoinedGroups = (Button)view.findViewById(R.id.btnShowJoinedGroups);
        Button btnMessages = (Button)view.findViewById(R.id.btnMessages);
        Button btnTakePicture = (Button)view.findViewById(R.id.btnTakePicture);
        tvLoggedInAs = (TextView)view.findViewById(R.id.tvLoggedInUser);
        updateTvLoggedInUser();

        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnShowMapClicked();
            }
        });

        btnShowJoinedGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnShowJoinedGroupsClicked();
            }
        });

        btnMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnMessagesClicked();
            }
        });

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File cachePath = new File(Environment.getExternalStorageDirectory() + "/DCIM/FriendMap/");
                if(!cachePath.exists())
                    cachePath.mkdir();
                try {
                    Controller.mFile = File.createTempFile("myImage", ".jpg", cachePath);
                    Controller.mFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = Uri.fromFile(Controller.mFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 123);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            controller.makeBitmapToByteArray();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void updateTvLoggedInUser() {
        tvLoggedInAs.setText(loggedInUser);
    }

    public void setTvLoggedInAs(String user) {
        this.loggedInUser = user;
    }
}
