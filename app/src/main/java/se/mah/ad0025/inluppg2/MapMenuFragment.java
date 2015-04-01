package se.mah.ad0025.inluppg2;

import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Klass som visar karta och komponent för att välja olika grupper som ska visas på kartan.
 * @author Jonas Dahlström
 */
public class MapMenuFragment extends Fragment {
    private Controller controller;
    private Spinner spinnerMap;

    public MapMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_menu, container, false);
        spinnerMap = (Spinner)view.findViewById(R.id.spinnerMap);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addSpinnerGroups();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void addSpinnerGroups() {
        Resources res = getResources();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMap.setAdapter(spinnerAdapter);
        ArrayList<String> joinedGroups = controller.getJoinedGroups();
        spinnerAdapter.add(res.getString(R.string.textAll));
        for(int i = 0; i < joinedGroups.size(); i++) {
            spinnerAdapter.add(joinedGroups.get(i));
        }
    }

    public String getSpinnerGroup() {
        return spinnerMap.getSelectedItem().toString();
    }

}
