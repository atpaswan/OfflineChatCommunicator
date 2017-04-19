package wifiemer.tabbedactivity;

import android.content.BroadcastReceiver;
import android.view.View;

import java.util.ArrayList;
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
}
