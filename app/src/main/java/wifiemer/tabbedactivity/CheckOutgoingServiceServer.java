package wifiemer.tabbedactivity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class CheckOutgoingServiceServer extends Service {
    public CheckOutgoingServiceServer() {
    }
    @Override
        public void onStart(Intent intent, int startId) {
            super.onStart(intent, startId);

            // code checking the incoming connections and storing the messages

            CommonVars.fillVars();
            final int[] writePortNumber=CommonVars.writePortNumber;

            for(int i=0;i<writePortNumber.length;i++)
            {

                final int j=i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while(true) {   /** the while will keep the thread running and the connections will get accepted anytime */
                            try {

                                //System.out.println("CheckOutgoingServer CheckDebug "+writePortNumber[j]);
                                ServerSocket serverSocket = new ServerSocket();
                                serverSocket.setReuseAddress(true);
                                serverSocket.bind(new InetSocketAddress(writePortNumber[j]));
                                Socket socket = serverSocket.accept();

                                InputStream inputStream = socket.getInputStream();

                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                BufferedReader br = new BufferedReader(inputStreamReader);

                                String currLine = "";
                                String currResponse="";

                                System.out.println("Before the info request");

                                currLine=br.readLine();

                                System.out.println("received the info request "+currLine);

                                String[] infoID = currLine.split(":");

                                String macID = infoID[0];
                                String usageID = infoID[1];

                                System.out.println("CheckOutgoingServer accepted firstInputStream "+infoID[0]+","+infoID[1]);

                                List<ChatMessage> chatMessageList = ChatMessage.getChatMessageList("select * from chatmessage where macId='" + macID + "' and readcondition='NOT_SENT';", getApplicationContext());
                                chatMessageList = ChatMessage.modifyList(chatMessageList, CommonVars.getMacAddr(), CommonVars.usageId);

                                OutputStream outputStream=socket.getOutputStream();
                                PrintWriter printWriter = new PrintWriter(outputStream);

                                if(chatMessageList.size()>0)
                                printWriter.println(ChatMessage.convertListToJson(chatMessageList));
                                else
                                printWriter.println("EMPTY_RESPONSE");

                                System.out.println("CheckOutgoingServer wrote the outputstream");

                                ChatMessage.executeQuery("update chatmessage set readcondition='SENT' where readcondition='NOT_SENT' and macId='"+macID+"';", getApplicationContext());

                                inputStream.close();
                                outputStream.close();
                                socket.close();
                                serverSocket.close();

                            } catch (Exception e) {
                              // e.printStackTrace();
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
