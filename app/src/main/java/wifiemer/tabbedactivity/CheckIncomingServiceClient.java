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
public class CheckIncomingServiceClient extends Service {

    String hotSpotIp;
    String macId;
    String usageId;

    /** for implementation of the reading the messages from the hotspot */

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);


        hotSpotIp=intent.getStringExtra("hotSpotIp");
        macId=intent.getStringExtra("macId");
        usageId=intent.getStringExtra("usageId");

        CommonVars.fillVars();
        final int[] readPortNumber=CommonVars.writePortNumber;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for (int i = 0; i < readPortNumber.length; i++) {
                        final int currPort = readPortNumber[i];

                        Socket socket=null;
                        try {
                            //System.out.println("CheckIncomingServiceClient CheckDebug " + hotSpotIp + " " + currPort);
                            Thread.sleep(2000);
                            socket = new Socket(hotSpotIp, currPort);
                            System.out.println("Success in CheckIncomingServiceClient connectted to Socket " + hotSpotIp + " " + currPort);
                            OutputStream outputStream = socket.getOutputStream();
                            InputStream inputStream=socket.getInputStream();
                            PrintWriter printWriter = new PrintWriter(outputStream);

                            printWriter.println(CommonVars.getMacAddr() + ":" + CommonVars.usageId+" ");
                            if(printWriter.checkError())
                                System.out.println("PrintWriter has error");

                            System.out.println("wrote the info request");

                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader br = new BufferedReader(inputStreamReader);

                            String currLine = "";
                            String json = "";

                                System.out.println("Before reading the response ");
                          /*  while ((currLine = br.readLine()) != null) {
                                json += currLine;
                            }
                            */
                            json=br.readLine();

                            System.out.println("json"+json);

                            if(!json.equals("EMPTY_RESPONSE")) {
                                List<ChatMessage> chatMessageList = ChatMessage.retrieveListfromJson(json);

                                for (int curr = 0; curr < chatMessageList.size(); curr++) {
                                    ChatMessage chatMessage = chatMessageList.get(curr);
                                    chatMessage.setReadCondition(ReadCondition.QUEUED);
                                    ChatMessage.insertIntoDatabase(chatMessage, getApplicationContext());
                                }
                            }
                            else
                            System.out.println("jSon equals empty response");

                            inputStream.close();
                            outputStream.close();
                            socket.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally{
                            try
                            {
                                socket.close();
                            }
                            catch(Exception e)
                            {
                                //System.out.println("Unable to close the socket");
                                e.printStackTrace();
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
