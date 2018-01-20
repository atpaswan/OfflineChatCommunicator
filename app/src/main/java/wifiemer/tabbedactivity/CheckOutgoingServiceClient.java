package wifiemer.tabbedactivity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by Atul on 12/17/2017.
 */
public class CheckOutgoingServiceClient extends Service {


    String hotSpotIp;
    String macId;
    String usageId;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        // implement the trying of connections to the multiple ports of the server and then at last connecting to it.

        hotSpotIp=intent.getStringExtra("hotSpotIp");
        macId=intent.getStringExtra("macId");
        usageId=intent.getStringExtra("usageId");

        CommonVars.fillVars();
        final int[] writePortNumber=CommonVars.readPortNumber;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {
                    for (int i = 0; i < writePortNumber.length; i++) {

                        final int currPort = writePortNumber[i];
                        Socket socket=null;
                        try {

                            List<ChatMessage> chatMessageList = ChatMessage.getChatMessageList("select * from chatmessage where macId='" + macId + "' and readcondition='NOT_SENT';", CommonVars.context);


                            if(chatMessageList.size()>0) {
                                //System.out.println("CheckOutgoingServiceClient CheckDebug " + hotSpotIp + " " + currPort);
                                socket = new Socket(hotSpotIp, currPort);
                                //System.out.println("Success in CheckOutgoingServiceClient connectted to Socket " + hotSpotIp + " " + currPort);
                                OutputStream outputStream = socket.getOutputStream();
                                PrintWriter printWriter = new PrintWriter(outputStream);

                                chatMessageList = ChatMessage.modifyList(chatMessageList, CommonVars.getMacAddr(), CommonVars.usageId);

                                printWriter.println(ChatMessage.convertListToJson(chatMessageList));
                                if(printWriter.checkError())
                                    System.out.println("Printwriter has encountered an error");
                                else
                                System.out.println("Printwriter is correct");

                                System.out.println("found a chatter" + ChatMessage.convertListToJson(chatMessageList));

                                ChatMessage.executeQuery("update chatmessage set readcondition='SENT' where readcondition='NOT_SENT' and macId='" + macId + "';", CommonVars.context);
                                outputStream.close();
                                socket.close();
                            }


                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                        finally {
                            try {
                                socket.close();
                            }
                            catch (Exception e)
                            {
                                //e.printStackTrace();
                                //System.out.println("can't close socket");
                            }
                        }
                    }
                }


            }
        }).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
