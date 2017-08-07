package wifiemer.tabbedactivity;

import android.content.BroadcastReceiver;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public static String macID="macID";

    public static String getPresentTime()
    {
        return (new SimpleDateFormat(CommonVars.defaultDateFormat)).format(new Date());
    }
}
