package se.mah.ad0025.inluppg2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Fragment som hanterar kartan.
 * @author Jonas Dahlström
 */
public class MyMapFragment extends MapFragment {
    private static MyMapFragment fragment;
    private Controller controller;

    public MyMapFragment() {
        super();
    }

    public static MyMapFragment newInstance() {
        fragment = new MyMapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        return super.onCreateView(arg0, arg1, arg2);
    }

    @Override
    public void onResume() {
        super.onResume();
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(6).target(new LatLng(controller.getUserLatitude(), controller.getUserLongitude())).build();
        GoogleMap map = fragment.getMap();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.addMarker(new MarkerOptions().position(new LatLng((controller.getUserLatitude()), (controller.getUserLongitude()))).title(controller.getUserName()));
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
