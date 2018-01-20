package wifiemer.tabbedactivity;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Atul on 9/30/2017.
 */
public class CommonMethods {

    public static String getRandomSequence()
    {

        List<Integer> Number=new ArrayList<Integer>();

        for(int i=0;i<10;i++)
            Number.add(i);

        Collections.shuffle(Number);
        String sequence="";

        for(int i=0;i<Number.size();i++)
            sequence+=Number.get(i);

        return sequence;
    }


    public static String convertToJson(Object O,ObjectType objectype)
    {
        Gson gson=new Gson();
        Type type=null;

        if(objectype.equals(ObjectType.TRANSMISSIONENTITY)) {
            type = new TypeToken<TransmissionEntity>() {}.getType();
            TransmissionEntity transmissionEntity=(TransmissionEntity)O;

            return gson.toJson(transmissionEntity,type);
        }
        else
            return "";

    }

    public static boolean determineServer(Context context)   /** Determining whether the app is a current Server or not */
    {
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        Method getWifiApState=null;
        WifiConfiguration wifiConfiguration=null;

        try {
            getWifiApState = wifiManager.getClass().getMethod("getWifiApState");
            int apStatus=(int)getWifiApState.invoke(wifiManager);

            System.out.println("Apstatus "+apStatus);

            if(apStatus>10)
                apStatus-=10;

            if(apStatus==3)
                return true;
            else
                return  false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }


    }




}
