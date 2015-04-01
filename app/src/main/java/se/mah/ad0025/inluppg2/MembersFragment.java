package se.mah.ad0025.inluppg2;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Fragment som visar samtliga medlemmar i vald grupp. Innehåller en knapp för att lämna gruppen.
 * @author Jonas Dahlström
 */
public class MembersFragment extends Fragment {
    private Controller controller;
    private TextView tvMembersInGroup;
    private String memberText = "";

    public MembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_members, container, false);
        tvMembersInGroup = (TextView)view.findViewById(R.id.tvMembersInGroup);
        Button btnLeaveGroup = (Button)view.findViewById(R.id.btnLeaveGroup);
        btnLeaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnLeaveGroupClicked();
            }
        });
        return view;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void updateTextView(ArrayList<String> membersInGroup) {
        tvMembersInGroup.setText("");
        memberText = "";
        for(int i = 0; i < membersInGroup.size(); i++) {
            memberText += membersInGroup.get(i) + "\n";
        }
        populateTextView();
    }

    private void populateTextView() {
        tvMembersInGroup.setText(memberText);
    }
}
