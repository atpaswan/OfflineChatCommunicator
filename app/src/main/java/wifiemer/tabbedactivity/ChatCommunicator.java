package wifiemer.tabbedactivity;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Atul on 5/21/2017.
 */
public class ChatCommunicator {

        public String readAndWritefromClient(String wrString)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket socket=null;

            while(true)
            {
                socket=serverSocket.accept();
                try
                {
                    InputStream inputStream=socket.getInputStream();
                    PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
                    printWriter.println(wrString);
                    BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
                    String result=br.readLine();
                    socket.close();
                    return  result;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    System.out.println("Exception in getting the string from the client");
                    return "Exception in getting the string from the client";
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "TestString";
    }



    public void readFromClient(final TextView textView)
    {

        System.out.println("readfromClient");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    ServerSocket serverSocket = new ServerSocket(8088);
                    Socket socket=null;

                    while(true)
                    {
                        socket=serverSocket.accept();
                        System.out.println("accepting socket info");
                        try
                        {
                            InputStream inputStream=socket.getInputStream();
                            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
                            String result=br.readLine();
                            System.out.println("readString "+result);
                            textView.setText(result);
                            socket.close();

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



    public String readAndWriteFromServer(String writeString)
    {

        try
        {

            Socket socket=new Socket("192.168.0.109",8080);
            InputStream inputStream=socket.getInputStream();

            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
            String result=br.readLine();

            PrintWriter printWriter=new PrintWriter(socket.getOutputStream());

            printWriter.println(writeString);

            return result;


        }
        catch(Exception e)
        {
         System.out.println("error in reading from the server");
            e.printStackTrace();

        }

        return "Error";
    }


    public void WriteToServer(final String writeString)
    {

        System.out.println("triggering writeToServer "+writeString);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {

                    Socket socket=new Socket("192.168.0.109",8088);


                    PrintWriter  printWriter=new PrintWriter(socket.getOutputStream());

                    printWriter.println(writeString);
                    printWriter.close();
                    socket.close();



                }
                catch(Exception e)
                {
                    System.out.println("error in writing to the server");
                    e.printStackTrace();

                }

            }
        }).start();



    }

}
