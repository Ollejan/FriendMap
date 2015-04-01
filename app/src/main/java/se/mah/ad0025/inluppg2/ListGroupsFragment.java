package se.mah.ad0025.inluppg2;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Fragment som visar samtliga grupper som finns på servern.
 * @author Jonas Dahlström
 */
public class ListGroupsFragment extends Fragment {
    private Controller controller;
    private ListView listViewGroups;
    private ArrayList<String> content = new ArrayList<String>();

    public ListGroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_groups, container, false);
        listViewGroups = (ListView)view.findViewById(R.id.listViewGroups);
        listViewGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                controller.listViewItemClicked(content.get(i));
            }
        });
        return view;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    public void updateListViewGroups(JSONArray jsonArray) {
        content.clear();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject group = jsonArray.getJSONObject(i);
                content.add(group.getString("group"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        populateList();
    }

    private void populateList() {
        listViewGroups.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, content));
    }
}
