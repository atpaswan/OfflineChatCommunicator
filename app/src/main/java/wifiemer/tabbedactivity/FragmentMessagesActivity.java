package wifiemer.tabbedactivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
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

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 2/26/2017.
 */
public class FragmentMessagesActivity extends Fragment {


    List<WifiMessage> wifiMessages=new ArrayList<WifiMessage>();
    LayoutInflater inflater;
    WifiManager wifiManager;
    View rootView;
    boolean permissiongrant=true;

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
         rootView = inflater.inflate(R.layout.fragment_tab1messages, container, false);
         populateWifiMessageList();

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
         wifiManager=(WifiManager)getContext().getSystemService(Context.WIFI_SERVICE);

        if(!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }

        WifiReceiver wifiReceiver=new WifiReceiver();

        getContext().registerReceiver(wifiReceiver, new IntentFilter(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifiManager.startScan();


    }
    public void populateListViews(View rootView)
    {

        System.out.println("populateListView");
        ListView listView=(ListView)rootView.findViewById(R.id.listView);

        ArrayAdapter<WifiMessage> adapter=new WifiMessageAdapter(rootView.getContext(),R.layout.list_item,wifiMessages);

        listView.setAdapter(adapter);
}


private class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marshamallow and newer versions

            System.out.println("Entering permission grant");

            if (getActivity().checkSelfPermission(Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 123);
            }   //  WIFI CHANGE STATE

            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 456);
            }   //  WIFI CHANGE STATE
        }

      while(!permissiongrant);

        List<ScanResult> scanResults = wifiManager.getScanResults();

        wifiMessages=new ArrayList<WifiMessage>();

        if (scanResults == null) {
            wifiMessages.add(new WifiMessage("Wifi Scan results not available ", "nothing", R.drawable.alias_photo));
        } else {
            for (int i = 0; i < scanResults.size(); i++) {
                WifiMessage wifiMessage = new WifiMessage(scanResults.get(i).SSID.toString(), scanResults.get(i).BSSID.toString(), R.drawable.alias_photo);
                wifiMessages.add(wifiMessage);
                //wifiMessages.add(new WifiMessage("Wifi Scan results not available","nothing",R.drawable.alias_photo));
            }


        }
        populateListViews(rootView);
    }



}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==123)
        {
            if(grantResults.length>0 &&  grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                permissiongrant=true;
            }
        }
    }

    private class WifiMessageAdapter extends ArrayAdapter<WifiMessage> {
    List<WifiMessage> wifiMessages;

    public WifiMessageAdapter(Context context, int positionm,List<WifiMessage> wifiMessages) {
        super(context, R.layout.list_item, wifiMessages);
        this.wifiMessages=wifiMessages;

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


