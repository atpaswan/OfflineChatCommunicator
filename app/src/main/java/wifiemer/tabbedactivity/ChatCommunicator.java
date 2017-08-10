package wifiemer.tabbedactivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 5/21/2017.
 */
public class ChatCommunicator {


    private String wifiHotSpot = "";

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

    private Activity activity;
    List<ScanResult> scanResultList=new ArrayList<ScanResult>();
    boolean scanAvailable=false;
    WifiManager wifiManager;

    public ChatCommunicator(String wifiHotSpot,Activity activity) {

            this.wifiHotSpot = wifiHotSpot;
            this.activity=activity;
    }

    public ChatCommunicator(Activity activity) {
        this.activity=activity;

    }

    public void readFromClient(final TextView textView,final int portNumber) {

        System.out.println("triggering readfromClient");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {



                    while (true) {

                           ServerSocket serverSocket = new ServerSocket(portNumber);
                           Socket socket = serverSocket.accept();

                        System.out.println("accepting socket info");
                        try {
                            InputStream inputStream = socket.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                            final String result = br.readLine();
                            System.out.println("readString " + result);

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    textView.setText(result);
                                }
                            });
                            socket.close();
                            serverSocket.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Exception in getting the string from the client");
                            //return "Exception in getting the string from the client";
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Throwing the second error. Catch !!!!");
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public String readFromClient(final int portNumber) {

        System.out.println("triggering readfromClient");


        try {

                        ServerSocket serverSocket = new ServerSocket(portNumber);
                        Socket socket = serverSocket.accept();

                        System.out.println("accepting socket info");
                        try {
                            InputStream inputStream = socket.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                            final String result = br.readLine();
                            System.out.println("readString " + result);

                            socket.close();
                            serverSocket.close();
                            return result;

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Exception in getting the string from the client");
                            return "";
                        }

                } catch (Exception e) {
                    System.out.println("Throwing the second error. Catch !!!!");
                    e.printStackTrace();
                    return "";
                }

    }

    public boolean writeToClient(String writeString,final int portNumber) {
        System.out.println("triggering writeToClient");

        try {

            ServerSocket serverSocket = new ServerSocket(portNumber);

                Socket socket = serverSocket.accept();

            System.out.println("accepting socket info");

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

            printWriter.println(writeString);
            printWriter.close();
            socket.close();
            serverSocket.close();

            return  true;
        } catch (Exception e) {
            System.out.println("writeToClient exception");
            e.printStackTrace();
            return false;
        }


    }


    public boolean WriteToServer(final String writeString,final int portNumber) {

        System.out.println("triggering writeToServer " + writeString);

              try {

                    Socket socket = new Socket(wifiHotSpot, portNumber);


                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

                    printWriter.println(writeString);
                    printWriter.close();
                    System.out.println("WritetoServer Complete");
                    socket.close();

                    return true;

                } catch (Exception e) {
                    System.out.println("error in writing to the server");
                    e.printStackTrace();

                  return false;
                }

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

    public void ReadFromServer(final TextView readTextView,final int portNumber) {
        System.out.println("triggering readfromServer");



        while (true) {
            try {

                Socket socket = new Socket(wifiHotSpot, portNumber);

                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                final String readString = bufferedReader.readLine();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        readTextView.setText(readString);
                    }
                });
                System.out.println("before closing socket");
                socket.close();
                System.out.println("readfrom Server complete");

            } catch (Exception e) {
                System.out.println("readfromServer exception");
                e.printStackTrace();

            }
        }


    }

    public String readFromServer(final int portNumber) {
        System.out.println("triggering readfromServer");

        Socket socket=null;

        try {

                 socket = new Socket(wifiHotSpot, portNumber);

                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                final String readString = bufferedReader.readLine();
                System.out.println("before closing socket");
                socket.close();
                System.out.println("readfrom Server complete");

                return  readString;

            } catch (Exception e) {
                System.out.println("readfromServer exception");
               // e.printStackTrace();
                return "";

            }

    }

}
