package wifiemer.tabbedactivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Atul on 3/12/2017.
 */
public class WifiHotSpotAccess {

    Context context;

    public boolean setHotspotwithName(String hotSpotName,Context context)
    {
        this.context=context;

        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(false);

        try {
            Method[] wifiMethods=wifiManager.getClass().getMethods();
            Method getWifiAPMethod=null;

            for(Method method:wifiMethods)
            {
                if(method.getName().equals("getWifiApConfiguration")) {
                    getWifiAPMethod=method;
                    break;
                }
            }
           getWifiAPMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
           WifiConfiguration myConfig=(WifiConfiguration)getWifiAPMethod.invoke(wifiManager);

            myConfig.SSID=hotSpotName;


            Method setWifiAPMethod=wifiManager.getClass().getMethod("setWifiApConfiguration",WifiConfiguration.class);
            setWifiAPMethod.invoke(wifiManager,myConfig);

            Method setWifiAPEnabled=null;

            for(Method method:wifiMethods)
            {
                System.out.println(method.getName());
            }

          setWifiAPEnabled=  wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);

            setWifiAPEnabled.invoke(wifiManager,myConfig,false);  // firstly turn it off
            setWifiAPEnabled.invoke(wifiManager,myConfig,true);

            // need to start the server for listening to messages

            context.startService(new Intent(context,CheckOutgoingServiceServer.class));
            context.startService(new Intent(context,CheckIncomingServiceServer.class));


        }

        catch(Exception e)
        {
          e.printStackTrace();
            return false;
        }

      return true;
    }

    public boolean connectToHotspot(String macId,Context context)
    {
        System.out.println("connectToHotspot called "+macId);
        WifiConfiguration conf=new WifiConfiguration();
        conf.BSSID=macId;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        if(!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);

        wifiManager.addNetwork(conf);
        List<WifiConfiguration> wifiConfigurationList=wifiManager.getConfiguredNetworks();

        for(WifiConfiguration i:wifiConfigurationList)
        wifiManager.disableNetwork(i.networkId);

        for(WifiConfiguration i:wifiConfigurationList)
        {
            if(i.BSSID==null)
                continue;

            if(i.BSSID.equals(macId))
            {
                try
                {
                    System.out.println("BSSID equalled "+i.SSID);
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    return true;
                }
                catch(Exception e)
                {
                    System.out.println("catching connectToWifi Exception");
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
