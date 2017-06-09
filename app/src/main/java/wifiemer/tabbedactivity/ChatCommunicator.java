package wifiemer.tabbedactivity;

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

    private Socket socket=null;
    private String wifiHotSpot="";
    private ServerSocket serverSocket=null;

    public ChatCommunicator(String wifiHotSpot)
    {
        try {
            this.wifiHotSpot = wifiHotSpot;
            socket = new Socket(this.wifiHotSpot, 8088);
        }
        catch(Exception e)
        {
            System.out.println("Unable to open scoket");
            e.printStackTrace();

            socket=null;
        }
    }

    public ChatCommunicator()
    {
        try
        {
            serverSocket=new ServerSocket(8088);
            socket=serverSocket.accept();

        }
        catch(Exception e)
        {
            System.out.println("triggering ChatCommunicator() exception");
            e.printStackTrace();
        }
    }

    public void readFromClient(final TextView textView)
    {

        System.out.println("triggering readfromClient");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    if(serverSocket==null)
                     serverSocket = new ServerSocket(8088);

                    while(true)
                    {
                        if(socket==null)
                        socket=serverSocket.accept();

                        System.out.println("accepting socket info");
                        try
                        {
                            InputStream inputStream=socket.getInputStream();
                            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
                            String result=br.readLine();
                            System.out.println("readString " + result);
                            textView.setText(result);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("Exception in getting the string from the client");
                            //return "Exception in getting the string from the client";
                        }
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Throwing the second error. Catch !!!!");
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void writeToClient(String writeString)
    {
        System.out.println("triggering writeToClient");

        try
        {
            if(serverSocket==null)
                serverSocket=new ServerSocket(8088);

            if(socket==null)
                socket=serverSocket.accept();

            System.out.println("accepting socket info");

            PrintWriter printWriter=new PrintWriter(socket.getOutputStream());

            printWriter.println(writeString);
            printWriter.close();
        }
        catch(Exception e)
        {
            System.out.println("writeToClient excpetion");
            e.printStackTrace();
        }


    }



    public void WriteToServer(final String writeString)
    {

        System.out.println("triggering writeToServer "+writeString);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {

                    if(socket==null)
                     socket=new Socket(wifiHotSpot,8088);


                    PrintWriter  printWriter=new PrintWriter(socket.getOutputStream());

                    printWriter.println(writeString);
                    printWriter.close();

                }
                catch(Exception e)
                {
                    System.out.println("error in writing to the server");
                    e.printStackTrace();

                }

            }
        }).start();



    }

    public void ReadFromServer(TextView readTextView)
    {
        System.out.println("triggering readfromServer");

        while(true) {
            try {
                if (socket == null)
                    socket = new Socket(wifiHotSpot, 8088);

                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String readString = bufferedReader.readLine();
                readTextView.setText(readString);


            } catch (Exception e) {
                System.out.println("readfromServer exception");
                e.printStackTrace();

            }
        }


    }

    public boolean socketClose()
    {
        try {
            if (socket != null)
                socket.close();

            return true;
        }
        catch(Exception e)
        {
            System.out.println("unable to close socket");
            e.printStackTrace();

            return false;
        }


    }

}
