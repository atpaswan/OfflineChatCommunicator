package wifiemer.tabbedactivity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.database.sqlite.*;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.annotations.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;



/**
 * Created by Atul on 2/26/2017.
 */
public class FragmentMessagesActivity extends Fragment {



    List<SenderDevice> senderDeviceList=new ArrayList<SenderDevice>();
    List<BroadCastMessage> broadCastMessageList=new ArrayList<BroadCastMessage>();
    LayoutInflater inflater;
    WifiManager wifiManager;
    View rootView;
    boolean permissiongrant=true;
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

        CommonVars.context=getContext();

        WifiManager wifiManager=(WifiManager)getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();

        System.out.println("macID checker "+wifiInfo.getMacAddress()+" , "+wifiInfo.getBSSID());
        System.out.println("New MacID checker "+CommonVars.getMacAddr());

        if(CommonVars.messageActivity) {
            senderDeviceList = CommonVars.senderDeviceList;
            broadCastMessageList = CommonVars.broadCastMessageList;
            CommonVars.messageActivity=false;
        }
        else   // load the senderDeviceList as well as broadCastMessageList from the Database
        {
            try {

                SQLiteDatabase sqLiteDatabase=getContext().openOrCreateDatabase(CommonSettings.appDatabase, getActivity().MODE_PRIVATE, null);

                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SENDER_DEVICE(MACID VARCHAR,IMAGEBYTES BLOB,ALIASNAME VARCHAR);");
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS CHATMESSAGE(MACID VARCHAR,USAGEID VARCHAR,DATATYPE VARCHAR,DATA BLOB,MESSAGE VARCHAR,READCONDITION VARCHAR,TIMESTAMP VARCHAR,CHATTYPE CHAR);");
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS BROADCASTMESSAGE(MACID VARCHAR,MESSAGE VARCHAR,REC_TIMESTAMP TIMESTAMP,UNENCSTRING VARCHAR);");
                //sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS MESSAGECAPSULE(MACID VARCHAR,MESSAGE VARCHAR,DATATYPE VARCHAR,RAWDATA BLOB,SEQ VARCHAR,PARTNUM INTEGER,TOTPARTS INTEGER,MSGCODE VARCHAR");
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS CHATUNSENT(MACID VARCHAR,USAGEID VARCHAR,DATATYPE VARCHAR,DATA BLOB,MESSAGE VARCHAR,READCONDITION VARCHAR,TIMESTAMP VARCHAR,CHATTYPE CHAR);");

                Cursor senderDeviceCursor=sqLiteDatabase.rawQuery("select * from sender_device",null);
              //  Cursor broadCastMessageCursor=sqLiteDatabase.rawQuery("select * from broadcastmessage",null);

                senderDeviceCursor.moveToFirst();

                while(!senderDeviceCursor.isAfterLast())
                {
                    SenderDevice senderDevice=new SenderDevice(senderDeviceCursor.getString(0),senderDeviceCursor.getBlob(1),senderDeviceCursor.getString(2));
                    senderDeviceList.add(senderDevice);

                    Cursor broadCastMessageCursor=sqLiteDatabase.rawQuery("select  * from (select * from broadcastmessage where macid='"+senderDevice.getMacID()+"' order by rec_timestamp desc) limit 1",null);
                    broadCastMessageCursor.moveToFirst();

                    BroadCastMessage broadCastMessage=new BroadCastMessage(broadCastMessageCursor.getString(0),broadCastMessageCursor.getString(1),broadCastMessageCursor.getString(2),broadCastMessageCursor.getString(3));

                    broadCastMessageList.add(broadCastMessage);
                    senderDeviceCursor.moveToNext();
                }

                sqLiteDatabase.close();

            }
            catch(Exception e)
            {
                System.out.println("Database cannot be read");
                e.printStackTrace();
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
        CommonVars.senderDeviceList=senderDeviceList;
        CommonVars.broadCastMessageList=broadCastMessageList;
        CommonVars.view=rootView;
        CommonVars.messageActivity=true;
        System.out.println("OnPause called");
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

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

        ArrayAdapter<SenderDevice> adapter=new SenderDeviceAdapter(rootView.getContext(),R.layout.list_item,senderDeviceList);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
}

    public byte[] resourceToImageBytes(int resourceID)
    {
        Bitmap bitmap=BitmapFactory.decodeResource(getContext().getResources(),resourceID);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

private class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marshasmallow and newer versions

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

        new Thread()
        {

            public void run() {
                if (scanResults == null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                          //  byte[] imageBytes=resourceToImageBytes(R.drawable.alias_photo);

                          // senderDeviceList.add(new SenderDevice("No Sender devices scanned",resourceToImageBytes(R.drawable.alias_photo),"No Results"));
                        }
                    });

                } else

                {
                   CommonVars.isItemAdded=false;

                    for (int i = 0; i < scanResults.size(); i++) {

                        Boolean addFlag = false;
                        String wifiString = scanResults.get(i).SSID.toString();
                        try                                                      // try block for filtering the WIFI SSID to check if it is actually the message decoding the encryption
                        {

                            byte[] encryptBytes = Base64.decode(wifiString, Base64.DEFAULT);

                            byte[] decryptBytes = (new CryptEncrypt()).Decrypt(encryptBytes);
                            wifiString = new String(decryptBytes);
                            //System.out.println("tryExecute");
                            addFlag = true;
                        } catch (Exception e) {
                            //System.out.println("padding Exception " + wifiString);
                        }

                        if(isMessage(wifiString)) {                   // needs refinement, not the final prod version
                            wifiString=extractMessage(wifiString);
                            addFlag = true;
                        }

                        if (addFlag) {
                            final BroadCastMessage broadCastMessage=new BroadCastMessage(scanResults.get(i).BSSID,wifiString,(new SimpleDateFormat(CommonVars.defaultDateFormat)).format(new Date()),scanResults.get(i).SSID);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(getMessageAdded(broadCastMessage)) {
                                        System.out.println("broadcastmessage size " + broadCastMessageList.size());
                                       // senderDeviceList.add(new SenderDevice(broadCastMessage.getMacID(),resourceToImageBytes(R.drawable.alias_photo),""));
                                        CommonVars.isItemAdded=true;
                                        System.out.println("wifimessages added "+broadCastMessageList.size());
                                    }
                                 }
                            });


                            //wifiMessages.add(new WifiMessage("Wifi Scan results not available","nothing",R.drawable.alias_photo));
                        }
                    }

                    //System.out.println("setting progress");
                    try {
                        Thread.sleep(2000);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // progress.setProgress(100);
                            if(CommonVars.isItemAdded)
                            {
                                populateListViews(rootView);
                            }

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
        String formatString=wifiString.substring(0,2);
        String actString=wifiString.substring(2,wifiString.length());

        System.out.println("checker" + formatString);
        int formatPos=Integer.parseInt(formatString);

        List<SendingType> sendingTypeList=SendingType.getSendingTypeList(getContext());
        SendingCode sendingCode=SendingCode.getSendingCode(sendingTypeList, formatPos);

        String fieldSeparator=sendingCode.getFieldSeparator();
        String[] fieldSeparatorString=fieldSeparator.split("_");
        int[]  fieldSeparatorInt=new int[fieldSeparatorString.length];
        int curr=0;

        for(String s:fieldSeparatorString)
        {
            fieldSeparatorInt[curr] = Integer.parseInt(s);
            curr++;
        }

        String[] fieldString=new String[fieldSeparatorInt.length];
        int lastCurr=0;

        for(int i=0;i<fieldSeparatorInt.length;i++)
        {
           fieldString[i]=actString.substring(lastCurr,fieldSeparatorInt[i]);
            lastCurr+=fieldSeparatorInt[i];

            String saneString="";

            for(int j=0;j<fieldString[i].length();j++)
            {
                if(fieldString[i].charAt(j)!=CommonVars.fillerChar)
                    saneString+=fieldString[i].charAt(j);
            }

            fieldString[i]=saneString;
        }

        String writeString=sendingCode.getWriteString();
        char replacementCode=sendingCode.getReplacementCode();
        String resultString="";
        int incCounter=0;

        for(int i=0;i<writeString.length();i++)
        {
            if(writeString.charAt(i)!=replacementCode)
                resultString+=writeString.charAt(i);
            else
            {
                resultString+=fieldString[incCounter];
                incCounter++;
            }
        }

        System.out.println("resultString: "+resultString);

        return resultString;
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

    private class SenderDeviceAdapter extends ArrayAdapter<SenderDevice> {
    List<SenderDevice> senderDeviceListArg;

    public SenderDeviceAdapter(Context context, int position,List<SenderDevice> senderDeviceListArg) {
        super(context, R.layout.list_item, senderDeviceListArg);
        this.senderDeviceListArg=senderDeviceListArg;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;

            if (rootView == null) {
                rootView = mActivity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

            final SenderDevice senderDevice=senderDeviceListArg.get(position);
            Bitmap bitmap= BitmapFactory.decodeByteArray(senderDevice.getImageBytes(),0,senderDevice.getImageBytes().length);
            imageView.setImageBitmap(bitmap);

            TextView textView = (TextView) rootView.findViewById(R.id.WifiName);
            textView.setText(senderDevice.getMacID());

            final TextView lastMessage = (TextView) rootView.findViewById(R.id.LastMessage);
            final TextView timestampTextView=(TextView)rootView.findViewById(R.id.timestampTextView);
            final TextView wifiNameTextView=(TextView)rootView.findViewById(R.id.WifiName);

            if(senderDevice.aliasName.equals(""))
                wifiNameTextView.setText(senderDevice.getMacID());
            else
            wifiNameTextView.setText(senderDevice.getAliasName());

            timestampTextView.setText(broadCastMessageList.get(position).getTimestamp());

            lastMessage.setText(broadCastMessageList.get(position).getMessage());

            final int currPosition=position;

           lastMessage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Intent intent=new Intent(getContext(),FragmentReceivedBroadcastActivity.class);
                   intent.putExtra("macID",senderDeviceList.get(currPosition).getMacID());
                   System.out.println("Executing the activity movement");
                   startActivity(intent);
               }
           });

            return rootView;


    }
};

    public boolean getMessageAdded(BroadCastMessage broadCastMessage)   // to determine whether the filtered message is an already received message or not
    {
        boolean doAdd=true;


        // just load all the BroadCastMessageObjects from the Db and compare them with the received arg

        Boolean firstAdd=false;

        SQLiteDatabase sqLiteDatabase= getContext().openOrCreateDatabase(CommonSettings.appDatabase, getActivity().MODE_PRIVATE, null);

        Cursor broadCastCursor=sqLiteDatabase.rawQuery("select * from broadcastmessage where macid='" + broadCastMessage.getMacID() + "'", null);

        System.out.println("getMessageAdded beg" + broadCastCursor.getCount());

        if(broadCastCursor.getCount()==0)
        {
            String DbQuery="INSERT INTO SENDER_DEVICE VALUES(?,?,?)";
            sqLiteDatabase.execSQL(DbQuery,new Object[]{broadCastMessage.getMacID(),resourceToImageBytes(R.drawable.alias_photo),""});
            senderDeviceList.add(new SenderDevice(broadCastMessage.getMacID(), resourceToImageBytes(R.drawable.alias_photo), ""));
            broadCastMessageList.add(broadCastMessage);
            firstAdd=true;
        }


        broadCastCursor.moveToFirst();

        while(!broadCastCursor.isAfterLast())
        {
            String arg1=broadCastCursor.getString(0);
            String arg2=broadCastCursor.getString(1);
            String arg3=broadCastCursor.getString(2);
            String arg4=broadCastCursor.getString(3);

            Date date1;
            Date date2;

            try {
                 date1 = (new SimpleDateFormat(CommonVars.defaultDateFormat)).parse(arg3);
                 date2=(new SimpleDateFormat(CommonVars.defaultDateFormat)).parse(broadCastMessage.getTimestamp());
            }
            catch(Exception e)
            {
                System.out.println("Unhandled parse Exception in getMessageAdded");
                e.printStackTrace();
                continue;
            }


               if(arg2.equals(broadCastMessage.getMessage()))
                {
                    Calendar calendar= GregorianCalendar.getInstance();
                    calendar.setTime(date1);
                    calendar.add(GregorianCalendar.MINUTE, CommonSettings.intermediatedMinutesBetweenBroadCast);

                    date1=new Date(calendar.getTimeInMillis());


                    if(date1.compareTo(date2)<0)
                        doAdd=true;
                    else
                        doAdd=false;
                }

            broadCastCursor.moveToNext();


        }

        if(doAdd)
        {
            String DbQuery="INSERT INTO BROADCASTMESSAGE VALUES(?,?,?,?)";
            sqLiteDatabase.execSQL(DbQuery,new Object[]{broadCastMessage.getMacID(),broadCastMessage.getMessage(),broadCastMessage.getTimestamp(),broadCastMessage.getUnEncString()});

            if(!firstAdd)
            {
                int position=-1;

                for(int i=0;i<broadCastMessageList.size();i++)
                {
                    if(broadCastMessageList.get(i).getMacID().equals(broadCastMessage.getMacID()))
                    {
                        position=i;
                        break;
                    }
                }

                broadCastMessageList.remove(position);
                broadCastMessageList.add(position,broadCastMessage);
            }
        }

        sqLiteDatabase.close();

        System.out.println("getMessageAdded completed "+doAdd);

        return doAdd;

    }


}


