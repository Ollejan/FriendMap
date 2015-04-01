package se.mah.ad0025.inluppg2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Fragment som visar startsidan där man kan skriva in användarnamn och gå med i och skapa nya grupper.
 * @author Jonas Dahlström
 */
public class StartFragment extends Fragment {
    private Controller controller;
    private EditText etUsername, etCreateGroup;
    private SharedPreferences prefs;

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        prefs = getActivity().getSharedPreferences("loginprefs", Context.MODE_PRIVATE);
        Button btnListGroups = (Button)view.findViewById(R.id.listGroups);
        Button btnCreateGroup = (Button)view.findViewById(R.id.createGroup);
        etUsername = (EditText)view.findViewById(R.id.etUsername);
        etCreateGroup = (EditText)view.findViewById(R.id.etCreateGroup);

        btnListGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnListGroupsClicked();
            }
        });

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnCreateGroupClicked();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        etUsername.setText(prefs.getString("username", ""));
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public String getEtUsername() {
        return etUsername.getText().toString();
    }

    public String getEtCreateGroup() {
        return etCreateGroup.getText().toString();
    }

}
