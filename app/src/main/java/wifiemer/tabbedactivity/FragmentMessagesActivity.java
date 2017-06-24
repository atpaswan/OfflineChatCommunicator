package wifiemer.tabbedactivity;

import android.Manifest;
import android.app.Activity;
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

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
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
    Activity mActivity;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(CommonVars.messageActivity) {
            wifiMessages = CommonVars.wifiMessages;
            wifiMessagesReceived = CommonVars.wifiMessagesReceived;
            CommonVars.messageActivity=false;
        }
        else   // load the wifiMessagesReceived from the file
        {
            try {
                FileInputStream fis = getContext().openFileInput(getResources().getString(R.string.wifi_FileName));
                ObjectInputStream ois= new ObjectInputStream(fis);
                wifiMessagesReceived =(List<WifiMessage>)ois.readObject();

                for(int i=0;i<wifiMessagesReceived.size();i++)
                    wifiMessages.add(wifiMessagesReceived.get(i));

                System.out.println("reading wifiMessages");

                fis.close();
            }
            catch(Exception e)
            {
                System.out.println("wifiMessagesObject cannot be constructed");
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
                    fos.flush();
                    fos.close();
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

    public void populateWifiMessageList() // this will determine what messages will be shown
    {
         wifiManager=(WifiManager)getContext().getSystemService(Context.WIFI_SERVICE);

        if(!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }

        WifiReceiver wifiReceiver=new WifiReceiver();

        getContext().registerReceiver(wifiReceiver, new IntentFilter(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        CommonVars.wifiReceiver=wifiReceiver;

        wifiManager.startScan();
        populateListViews(rootView);
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
                   CommonVars.isItemAdded=false;

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

                        if(isMessage(wifiString)) {                   // needs refinement, not the final prod version
                            wifiString=extractMessage(wifiString);
                            addFlag = true;
                        }

                        if (addFlag) {
                            final WifiMessage wifiMessage = new WifiMessage(scanResults.get(i).BSSID.toString(),wifiString, R.drawable.alias_photo,new Date());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(getMessageAdded(wifiMessage)) {
                                        System.out.println("wifi size "+wifiMessages.size());
                                        wifiMessages.add(wifiMessage);
                                        CommonVars.isItemAdded=true;
                                        System.out.println("wifimessages added "+wifiMessages.size());
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
                            if(CommonVars.isItemAdded)
                            populateListViews(rootView);

                            wifiManager.startScan();
                        }
                    });
                }


            }
        }.start();
    }
}


    public boolean isMessage(String wifiString)
    {
      if((wifiString.charAt(0)>=48 && wifiString.charAt(0)<=57) && (wifiString.charAt(1)>=48 && wifiString.charAt(1)<=57) )
          return true;
        else
          return false;
    }

    public String extractMessage(String wifiString)
    {
        String formatString=wifiString.charAt(0)+wifiString.charAt(1)+"";
        int formatPos=Integer.parseInt(formatString);

        return wifiString.substring(2);
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
                rootView = mActivity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

            WifiMessage wifiMessage = wifiMessages.get(position);

            imageView.setImageResource(wifiMessage.getIcon_id());

            TextView textView = (TextView) rootView.findViewById(R.id.WifiName);
            textView.setText(wifiMessage.getWifiName());

            final TextView lastMessage = (TextView) rootView.findViewById(R.id.LastMessage);
            lastMessage.setText(wifiMessage.getLastMessage());

           lastMessage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Intent intent=new Intent(getContext(),WifiMessageChatActivity.class);
                   intent.putExtra("HotSpotName",lastMessage.getText());
                   System.out.println("Executing the activity movement");
                   startActivity(intent);
               }
           });

            return rootView;


    }
};

    public boolean getMessageAdded(final WifiMessage wifiMessage)   // to determine whether the filtered message is an already received message or not
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
            new Thread(new Runnable() {
                @Override
                public void run() {

                    System.out.println("Calling insertAliasPersistData");
                    insertAliasPersistData(wifiMessage);
                }
            }).start();

            System.out.println("return true");
            return true;
        }
    }





    public void insertAliasPersistData(WifiMessage wifiMessage)
    {
        try {
            FileInputStream fis = getContext().openFileInput(getResources().getString(R.string.aliasFileName));
            ObjectInputStream ois=new ObjectInputStream(fis);
            ArrayList<Alias> aliasArrayList=(ArrayList<Alias>)ois.readObject();
            fis.close();

            boolean foundFlag=false;
            int size=aliasArrayList.size();

            for(int i=0;i<size;i++)
            {
                Alias currAlias=aliasArrayList.get(i);

                if(wifiMessage.getWifiName().equals(currAlias.getBroadcastID()))
                {
                    foundFlag=true;
                    break;
                }
            }


            if(!foundFlag)
            {
                Alias newAlias=new Alias(R.drawable.alias_photo,wifiMessage.getBSSID(),wifiMessage.getLastMessage());
                aliasArrayList.add(newAlias);  // new AliasObject added after checking whether it is already present in the Alias Persist data

                FileOutputStream fos=getContext().openFileOutput(getResources().getString(R.string.aliasFileName),Context.MODE_PRIVATE);
                ObjectOutputStream oos=new ObjectOutputStream(fos);
                oos.writeObject(aliasArrayList);     // persisting the new Alias data
                System.out.println(aliasArrayList.size());
                fos.flush();
                fos.close();

                ArrayList<Alias> aliasArrayList1=(new FragmentAliasActivity()).loadfromAliasFile();
                aliasArrayList1=(new FragmentAliasActivity()).loadfromAliasFile();
            }

        }
        catch(Exception e)
        {
            System.out.println("Error in writing the alias File for validation operations.");  // This means we need to create a new file for persisting the Alias Data

            e.printStackTrace();
            try {
                FileOutputStream fos = getContext().openFileOutput(getResources().getString(R.string.aliasFileName), Context.MODE_PRIVATE);
                ObjectOutputStream oos=new ObjectOutputStream(fos);
                ArrayList<Alias> arrayList=new ArrayList<Alias>();
                Alias newAlias=new Alias(R.drawable.alias_photo,wifiMessage.getBSSID(),wifiMessage.getLastMessage());
                arrayList.add(newAlias);
                oos.writeObject(arrayList);
                System.out.println("Completing writing the Alias persist data " + arrayList.size());

                fos.flush();
                fos.close();
            }
            catch(Exception excp)
            {
                System.out.println("Error in creating the Alias file persist data");
            }
        }
    }
}


