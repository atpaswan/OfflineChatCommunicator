package wifiemer.tabbedactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Atul on 4/2/2017.
 */
public class CommonVars {

    public static List<WifiMessage> wifiMessagesReceived=new ArrayList<WifiMessage>();
    public static List<WifiMessage> wifiMessages=new ArrayList<WifiMessage>();
    public static View view;
    public static boolean  messageActivity=false;
    public static BroadcastReceiver wifiReceiver;
    public static int currListPosition=-1;
    public static boolean isItemAdded=false;
    public static String selectedHotSpotName;
    public static ChatCommunicator chatCommunicator=null;
    public static String defaultHotSpotIPAddress="192.168.43.1";
    public static List<SenderDevice> senderDeviceList=new ArrayList<SenderDevice>();
    public static List<BroadCastMessage> broadCastMessageList=new ArrayList<BroadCastMessage>();
    public static String defaultDateFormat="yyyy-MM-dd hh:mm:ss";
    public static String macID="";
    public static char fillerChar='\'';
    public static int[] readPortNumber=null;
    public static int[] writePortNumber=null;
    public static String usageId="";
    public static Context context=null;
    public static boolean debugMode=false;


    public static String getPresentTime()
    {
        return (new SimpleDateFormat(CommonVars.defaultDateFormat)).format(new Date());
    }

    public static String getmacID()
    {
        return macID;
    }

    public static String getUsageId()
    {
        usageId="setId";
        return usageId;
    }

    public static void fillVars()
    {
        fillWritePortNumber();
        fillReadPortNumber();
    }

    public static void fillWritePortNumber()
    {
        int minPort=12000;
        int maxPort=12004;

        int len=maxPort-minPort+1;
        int counter=0;

        writePortNumber=new int[len];

        for(int i=minPort;i<=maxPort;i++) {
            writePortNumber[counter]=i;
            counter++;
        }
    }

    public static void fillReadPortNumber()
    {
        int minPort=12300;
        int maxPort=12304;

        int len=maxPort-minPort+1;
        int counter=0;

        readPortNumber=new int[len];

        for(int i=minPort;i<=maxPort;i++)
        {
            readPortNumber[counter]=i;
            counter++;
        }
    }

    public static String getMacID(Context context)
    {
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();

        return wifiInfo.getMacAddress().trim();
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString().toLowerCase();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


}
