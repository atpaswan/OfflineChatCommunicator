package wifiemer.tabbedactivity;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Atul on 5/21/2017.
 */
public class ChatCommunicator {


    private String wifiHotSpot = "";
    private Activity activity;
    public ChatCommunicator(String wifiHotSpot,Activity activity) {

            this.wifiHotSpot = wifiHotSpot;
            this.activity=activity;
    }

    public ChatCommunicator(Activity activity) {
        this.activity=activity;

    }

    public void readFromClient(final TextView textView) {

        System.out.println("triggering readfromClient");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {



                    while (true) {

                           ServerSocket serverSocket = new ServerSocket(8086);
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

    public void writeToClient(String writeString) {
        System.out.println("triggering writeToClient");

        try {

            ServerSocket serverSocket = new ServerSocket(8087);

                Socket socket = serverSocket.accept();

            System.out.println("accepting socket info");

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

            printWriter.println(writeString);
            printWriter.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("writeToClient excpetion");
            e.printStackTrace();
        }


    }


    public void WriteToServer(final String writeString) {

        System.out.println("triggering writeToServer " + writeString);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Socket socket = new Socket(wifiHotSpot, 8086);


                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

                    printWriter.println(writeString);
                    printWriter.close();
                    System.out.println("WritetoServer Complete");
                    socket.close();

                } catch (Exception e) {
                    System.out.println("error in writing to the server");
                    e.printStackTrace();

                }

            }
        }).start();


    }

    public void ReadFromServer(final TextView readTextView) {
        System.out.println("triggering readfromServer");



        while (true) {
            try {

                Socket socket = new Socket(wifiHotSpot, 8087);

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

}
