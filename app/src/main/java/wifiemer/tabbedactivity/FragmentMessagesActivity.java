package wifiemer.tabbedactivity;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Atul on 2/26/2017.
 */
public class FragmentMessagesActivity extends Fragment {



    List<WifiMessage> wifiMessages=new ArrayList<WifiMessage>();
    LayoutInflater inflater;
    WifiManager wifiManager;
    View rootView;
    boolean permissiongrant=true;
    List<WifiMessage> wifiMessagesReceived=new ArrayList<WifiMessage>();
    static FragmentMessagesActivity fragmentObj;
    boolean restoreFlag=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(CommonVars.messageActivity) {
            wifiMessages = CommonVars.wifiMessages;
            wifiMessagesReceived = CommonVars.wifiMessagesReceived;
        }
        else   // load the wifiMessagesReceived from the file
        {
            try {
                FileInputStream fis = getContext().openFileInput(getResources().getString(R.string.wifi_FileName));
                ObjectInputStream ois= new ObjectInputStream(fis);
                wifiMessagesReceived =(List<WifiMessage>)ois.readObject();
                wifiMessages=wifiMessagesReceived;
                System.out.print("reading wifiMessages");
            }
            catch(Exception e)
            {
                System.out.println("wifiMessagesObject cannot be contructed");
            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

       // CommonVars.wifiMessages=wifiMessages;
        //CommonVars.wifiMessagesReceived=wifiMessagesReceived;
        System.out.println("OnsaveInstanceState called");
    }

    public static FragmentMessagesActivity newInstance() {

        if(fragmentObj==null)
         fragmentObj = new FragmentMessagesActivity();

        // Heightened Engineering

        // Heightened Science, d
        return fragmentObj;
    }


    @Override
    public void onPause() {
        CommonVars.wifiMessages=wifiMessages;
        CommonVars.wifiMessagesReceived=wifiMessagesReceived;
        CommonVars.view=rootView;
        CommonVars.messageActivity=true;
        System.out.println("OnPause called");
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FileOutputStream fos = getContext().openFileOutput(getResources().getString(R.string.wifi_FileName),Context.MODE_PRIVATE);
                    ObjectOutputStream oos=new ObjectOutputStream(fos);
                    oos.writeObject(wifiMessagesReceived);
                }
                catch(Exception e)
                {
                    System.out.println("error writing the wifi message to the file");
                }

            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         this.inflater=inflater;
         rootView = inflater.inflate(R.layout.fragment_tab1messages, container, false);
        if(CommonVars.messageActivity) {
            System.out.println("rootView restoreFlag called");
            populateListViews(rootView);
            CommonVars.messageActivity=false;
            rootView=CommonVars.view;
        }

        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    populateWifiMessageList();
                }
            }).start();
        }

        //  registerClickListener(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

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

    public void populateWifiMessageList() // this will determine what messages will be shown
    {
         wifiManager=(WifiManager)getContext().getSystemService(Context.WIFI_SERVICE);

        if(!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }

        WifiReceiver wifiReceiver=new WifiReceiver();

        getContext().registerReceiver(wifiReceiver, new IntentFilter(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifiManager.startScan();

       // getContext().unregisterReceiver(wifiReceiver);
    }


    public void populateListViews(View rootView)
    {

        System.out.println("populateListView");
        ListView listView=(ListView)rootView.findViewById(R.id.listView);

        ArrayAdapter<WifiMessage> adapter=new WifiMessageAdapter(rootView.getContext(),R.layout.list_item,wifiMessages);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
}


private class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marshamallow and newer versions

            System.out.println("Entering permission grant");

            if (getActivity().checkSelfPermission(Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 123);
            }   //  WIFI CHANGE STATE

            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 456);
            }   //  ACCESS LOCATION
        }

      while(!permissiongrant);

       final List<ScanResult> scanResults = wifiManager.getScanResults();

        //(new CryptEncrypt()).performOp();
        System.out.println("After execute Op");

        /*final ProgressDialog progress=new ProgressDialog(getContext());
        progress.setMessage("Receving messages");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(20);
        progress.show();
        */

        new Thread()
        {

            public void run() {
                if (scanResults == null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wifiMessages.add(new WifiMessage("Wifi Scan results not available ", "nothing", R.drawable.alias_photo,new Date()));
                        }
                    });

                } else

                {
                    for (int i = 0; i < scanResults.size(); i++) {

                        Boolean addFlag = false;
                        String wifiString = scanResults.get(i).SSID.toString();
                        try                                                      // try block for filtering the WIFI SSID to check if it is actually the message
                        {

                            byte[] encryptBytes = Base64.decode(wifiString, Base64.DEFAULT);

                            byte[] decryptBytes = (new CryptEncrypt()).Decrypt(encryptBytes);
                            wifiString = new String(decryptBytes);
                            System.out.println("tryExecute");
                            addFlag = true;
                        } catch (Exception e) {
                            System.out.println("padding Exception " + wifiString);
                        }

                        if (wifiString.charAt(0) == 'u')    // For testing purposes only, not for production
                            addFlag = true;

                        if (addFlag) {
                            final WifiMessage wifiMessage = new WifiMessage(scanResults.get(i).BSSID.toString(),wifiString, R.drawable.alias_photo,new Date());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(getMessageAdded(wifiMessage)) {
                                        wifiMessages.add(wifiMessage);
                                        System.out.println("wifimessages added");
                                    }
                                }
                            });


                            //wifiMessages.add(new WifiMessage("Wifi Scan results not available","nothing",R.drawable.alias_photo));
                        }
                    }

                    System.out.println("setting progress");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // progress.setProgress(100);
                            populateListViews(rootView);
                        }
                    });
                }


            }
        }.start();


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
};

    public boolean getMessageAdded(WifiMessage wifiMessage)   // to determine whether the filtered message is an already received message or not
    {
        boolean isReceived=false;


        for(int i=0;i<wifiMessagesReceived.size();i++)
        {
            WifiMessage currWifiMessage=wifiMessagesReceived.get(i);
            System.out.println("wifiMessage display: "+currWifiMessage.getLastMessage());

            if(wifiMessage.getLastMessage().equals(currWifiMessage.getLastMessage()))
            {
                isReceived=true;
                break;
            }
        }

        if (isReceived)
        return false;
        else {
            wifiMessagesReceived.add(wifiMessage);
            return true;
        }
    }
}


