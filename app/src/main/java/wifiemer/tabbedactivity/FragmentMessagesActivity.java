package wifiemer.tabbedactivity;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 2/26/2017.
 */
public class FragmentMessagesActivity extends Fragment {


    ArrayList<WifiMessage> wifiMessages;
    LayoutInflater inflater;
    public static FragmentMessagesActivity newInstance() {
        FragmentMessagesActivity fragment = new FragmentMessagesActivity();
        // Heightened Engineering

        // Heightened Science, d
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater=inflater;
        View rootView = inflater.inflate(R.layout.fragment_tab1messages, container, false);
        populateWifiMessageList();
        populateListView(rootView);
      //  registerClickListener(rootView);


        return rootView;
    }

    public void registerClickListener(final View rootView)
    {
        ListView listView=(ListView)rootView.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(rootView, "You have clicked on the lauda item " + position, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void populateWifiMessageList()
    {
        wifiMessages=new ArrayList<WifiMessage>();

        System.out.println("populateWiifList");
        WifiMessage wifiMessage=new WifiMessage("First Wifi","Got your message",android.R.drawable.ic_dialog_email);
        wifiMessages.add(wifiMessage);

        wifiMessage=new WifiMessage("Second Wifi","Got your message",android.R.drawable.ic_dialog_email);
        wifiMessages.add(wifiMessage);

        wifiMessage=new WifiMessage("Third Wifi","Got your message", android.R.drawable.ic_dialog_email);
        wifiMessages.add(wifiMessage);

        wifiMessage=new WifiMessage("Fourth Wifi","Got your message",android.R.drawable.ic_dialog_email);
        wifiMessages.add(wifiMessage);

    }
    public void populateListView(View rootView)
    {

        System.out.println("populateListView");
        ListView listView=(ListView)rootView.findViewById(R.id.listView);

        ArrayAdapter<WifiMessage> adapter=new WifiMessageAdapter(rootView.getContext(),R.layout.list_item,wifiMessages);

        listView.setAdapter(adapter);
}






private class WifiMessageAdapter extends ArrayAdapter<WifiMessage> {
    ArrayList<WifiMessage> wifiMessages;

    public WifiMessageAdapter(Context context, int position, ArrayList<WifiMessage> wifiMessages) {
        super(context, R.layout.list_item, wifiMessages);
        this.wifiMessages = wifiMessages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;

        if (rootView == null) {
            rootView = getActivity().getLayoutInflater().inflate(R.layout.list_item, parent, false);
        }

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

        WifiMessage wifiMessage = wifiMessages.get(position);

        imageView.setImageResource(wifiMessage.getIcon_id());

        TextView textView = (TextView) rootView.findViewById(R.id.WifiName);
        textView.setText(wifiMessage.getWifiName());

        TextView lastMessage = (TextView) rootView.findViewById(R.id.LastMessage);
        lastMessage.setText(wifiMessage.getLastMessage());


        return rootView;

    }
}
    ;
}


