package wifiemer.tabbedactivity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;

import java.nio.channels.Channel;
import android.net.wifi.p2p.WifiP2pManager.*;

/**
 * Created by Atul on 4/28/2017.
 */
public class WiFip2pOps {

    private final IntentFilter intentFilter=new IntentFilter();
    Context context;

    public WiFip2pOps(Context context)
    {
        this.context=context;

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        WifiP2pManager wifiP2pManager=(WifiP2pManager)context.getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel=wifiP2pManager.initialize(context, Looper.getMainLooper(),null);


        System.out.println("calling wifiP2PManager discover peers");

        context.registerReceiver(new WifiDirectBroadCastReceiver(wifiP2pManager,channel),intentFilter);
        wifiP2pManager.discoverPeers(channel, new ActionListener() {
            @Override
            public void onSuccess() {

                System.out.println("peers discovered");
            }

            @Override
            public void onFailure(int reason) {

                System.out.println("cant find peers. Failure in finding");
            }
        });




    }
}


