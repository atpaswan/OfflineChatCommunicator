package wifiemer.tabbedactivity;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

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

            myConfig.SSID="EngineeredSSID";


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
        }

        catch(Exception e)
        {
          e.printStackTrace();
            return false;
        }

      return true;
    }
}
