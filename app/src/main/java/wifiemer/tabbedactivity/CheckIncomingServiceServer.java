package wifiemer.tabbedactivity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Provider;
import java.util.List;

/**
 * Created by Atul on 10/21/2017.
 */
public class CheckIncomingServiceServer extends Service {


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        // code checking the incoming connections and storing the messages

            CommonVars.fillVars();
           final int[] readPortNumber=CommonVars.readPortNumber;
           int[] writePortNumber=CommonVars.writePortNumber;

        /**  firstly storing the read messages into a chat message into a queued format */

        for(int i=0;i<readPortNumber.length;i++)
        {

            final int j=i;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    while(true) {    /** the while will keep the thread running and keep accepting the connections */
                        try {

                            //System.out.println("CheckIncomingServer CheckDebug ");
                            ServerSocket serverSocket = new ServerSocket();
                            serverSocket.setReuseAddress(true);
                            serverSocket.bind(new InetSocketAddress(readPortNumber[j]));
                            Socket socket = serverSocket.accept();

                            InputStream inputStream = socket.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader br = new BufferedReader(inputStreamReader);
                            if(!br.ready())
                                System.out.println("Buffered reader is not good");
                            else
                            System.out.println("Buffered reader is good");

                            String objectjSon = "";
                            String currLine = "";

                            while ((currLine = br.readLine()) != null) {
                                objectjSon += currLine;
                            }

                            System.out.println("CheckIncomingServer acceptedInputStream "+objectjSon);

                            List<ChatMessage> chatMessageList = ChatMessage.retrieveListfromJson(objectjSon);

                            for (int curr = 0; curr < chatMessageList.size(); curr++) {
                                ChatMessage chatMessage = chatMessageList.get(curr);
                                chatMessage.setReadCondition(ReadCondition.QUEUED);
                                ChatMessage.insertIntoDatabase(chatMessage, getApplicationContext());
                            }

                            inputStream.close();
                            socket.close();
                            serverSocket.close();

                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }

                }
            }).start();
        }



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
