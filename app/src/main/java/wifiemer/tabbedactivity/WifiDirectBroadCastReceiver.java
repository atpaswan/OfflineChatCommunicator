package wifiemer.tabbedactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by Atul on 4/29/2017.
 */
public class WifiDirectBroadCastReceiver extends BroadcastReceiver{
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;

    public WifiDirectBroadCastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel) {
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();

        System.out.println("Wifi P2P Peers are discovered. Access them now "+action);

        if(wifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {

        }
        else if(wifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {

        }
        else if(wifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {

        }
        else
        {
            // for wifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
        }
    }
}
