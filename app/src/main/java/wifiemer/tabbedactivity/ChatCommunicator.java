package wifiemer.tabbedactivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

/**
 * Created by Atul on 5/21/2017.
 */
public class ChatCommunicator {


    private String wifiHotSpot = "";
    private Activity activity;
    List<ScanResult> scanResultList=new ArrayList<ScanResult>();
    boolean scanAvailable=false;
    WifiManager wifiManager;


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getWifiHotSpot() {
        return wifiHotSpot;
    }

    public void setWifiHotSpot(String wifiHotSpot) {
        this.wifiHotSpot = wifiHotSpot;
    }

    public List<ScanResult> getScanResultList() {
        return scanResultList;
    }

    public void setScanResultList(List<ScanResult> scanResultList) {
        this.scanResultList = scanResultList;
    }

    public boolean isScanAvailable() {
        return scanAvailable;
    }

    public void setScanAvailable(boolean scanAvailable) {
        this.scanAvailable = scanAvailable;
    }

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    public void setWifiManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public ChatCommunicator(String wifiHotSpot,Activity activity) {

            this.wifiHotSpot = wifiHotSpot;
            this.activity=activity;
    }

    public ChatCommunicator(Activity activity) {
        this.activity=activity;

    }

    public String getWifiName(String macID,Context context)
    {
         wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            WifiReceiver wifiReceiver = new WifiReceiver();
            context.registerReceiver(wifiReceiver, new IntentFilter(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

            wifiManager.startScan();

        while(!scanAvailable);

        scanAvailable=false;

        for(int i=0;i<scanResultList.size();i++)
        {
            System.out.println("ScanResult BSSID "+scanResultList.get(i).BSSID.toString());
            if(scanResultList.get(i).BSSID.toString().equals(macID))
                return scanResultList.get(i).SSID.toString();
        }


        return "";
    }

    private class WifiReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            scanResultList=wifiManager.getScanResults();

            scanAvailable=true;

        }
    }

    public boolean writeToServer(String objectJson)
    {

        CommonVars.fillWritePortNumber();
        int[] portNumber=CommonVars.writePortNumber;
        Socket socket=null;

        for(int i=0;i<portNumber.length;i++)
        {
            try
            {
                socket=new Socket(wifiHotSpot,portNumber[i]);
                break;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if(socket==null)
            return false;

        String totStringwithID=objectJson;

        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            printWriter.println(totStringwithID);
            printWriter.close();
            outputStream.close();
            socket.close();

            return  true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;


    }

    public String readFromServer()  throws Exception
    {

        CommonVars.fillReadPortNumber();
        int[] portNumber=CommonVars.readPortNumber;
        Socket socket=null;

        for(int i=0;i<portNumber.length;i++)   // connecting through a socket to the server for making a communication
        {
            try {
                socket = new Socket(wifiHotSpot, portNumber[i]);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                System.out.println("Exception in opening socket for reading from the server");
            }

            if(socket!=null)
                break;
        }

        if(socket==null)
            throw new Exception("Not able to connect to any of the socket");

        OutputStream outputStream=socket.getOutputStream();
        PrintWriter printWriter=new PrintWriter(outputStream);

        printWriter.println(CommonVars.macID + ":" + CommonVars.usageId);

        InputStream inputStream=socket.getInputStream();
        InputStreamReader inputStreamreader=new InputStreamReader(inputStream);
        BufferedReader br=new BufferedReader(inputStreamreader);

        String responseJson="";
        String currLine="";

        while((currLine=br.readLine())!=null)
        {
            responseJson+=currLine;
        }

        inputStream.close();
        socket.close();

        return responseJson;

    }

    public static List<ChatMessage> readfromClient(String macId,String usageId)
    {
        List<ChatMessage> chatMessageList=ChatMessage.getChatMessageList("select * from chatmessage where macId='"+macId+"' and usageId='"+usageId+"' where readcondition='QUEUED' ;",CommonVars.context);
        return chatMessageList;
    }

    public static void writetoClient(List<ChatMessage> chatMessageList)

    {
        for(int i=0;i<chatMessageList.size();i++)           // inserting into the queue for writing it later throught the WriteService
        {
            ChatMessage chatMessage=chatMessageList.get(i);
            chatMessage.setReadCondition(ReadCondition.NOT_SENT);

            ChatMessage.insertIntoDatabase(chatMessage,CommonVars.context);
        }
    }


}
