package se.mah.ad0025.inluppg2;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Fragment som visar de grupper användaren är ansluten till.
 * @author Jonas Dahlström
 */
public class JoinedGroupsFragment extends Fragment {
    private Controller controller;
    private ListView lvJoinedGroups;
    private ArrayList<String> content = new ArrayList<String>();

    public JoinedGroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_joined_groups, container, false);
        lvJoinedGroups = (ListView)view.findViewById(R.id.lvJoinedGroups);
        lvJoinedGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                controller.listViewItemJoinedGroupsClicked(content.get(i));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void populateList() {
        content = controller.getJoinedGroups();
        lvJoinedGroups.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, content));
    }

}
