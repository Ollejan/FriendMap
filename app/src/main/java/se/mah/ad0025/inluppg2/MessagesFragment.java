package se.mah.ad0025.inluppg2;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Fragment som visar textmeddelande i en lista och låter användaren skicka nya meddelanden.
 * @author Jonas Dahlström
 */
public class MessagesFragment extends Fragment {
    private Controller controller;
    private ListView lvMessages;
    private EditText etMessage;
    private ArrayList<String> content = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;

    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        lvMessages = (ListView)view.findViewById(R.id.lvMessages);
        etMessage = (EditText)view.findViewById(R.id.etMessage);
        arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, content);
        Button btnSendMessage = (Button)view.findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etMessage.getText() != null) {
                    controller.btnSendMessageClicked(etMessage.getText().toString());
                    etMessage.setText("");
                }
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

    public void addMessageToList(String member, String textMessage) {
        content.add(0, member + " - " + textMessage);
        if(content.size() >= 30) {
            content.remove(content.size() - 1);
        }
        if(lvMessages != null) {
            populateList();
        }
    }

    private void populateList() {
        lvMessages.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

}
