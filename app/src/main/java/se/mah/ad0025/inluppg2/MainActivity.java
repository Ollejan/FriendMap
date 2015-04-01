package se.mah.ad0025.inluppg2;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;


public class MainActivity extends Activity {
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        SharedPreferences prefs = getSharedPreferences("loginprefs", Context.MODE_PRIVATE);
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        controller = new Controller(this, fragmentManager, prefs, lm);
        controller.startLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        controller.stopLocationUpdates();
        controller.disconnectFromServer();
        super.onDestroy();
    }

}
