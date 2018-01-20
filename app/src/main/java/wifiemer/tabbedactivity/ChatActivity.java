package wifiemer.tabbedactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.database.sqlite.*;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {

    String macID="";
    String wifiName="";
    boolean wifiNameGet=false;
    Activity activity;

    List<ChatMessage> chatMessageList=new ArrayList<ChatMessage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity=this;

        final ListView listview=(ListView)findViewById(R.id.chatListView);

        macID=getIntent().getStringExtra("macID");

        SQLiteDatabase sqLiteDatabase=openOrCreateDatabase(CommonSettings.appDatabase, MODE_PRIVATE, null);

        try {
            Cursor chatMessageCursor = sqLiteDatabase.rawQuery("select * from chatmessage where macID='" + macID + "'", null);
            chatMessageCursor.moveToFirst();

            while(!chatMessageCursor.isAfterLast())
            {
                ReadCondition readCondition;
                if(chatMessageCursor.getString(4).equals("NOT_SENT"))
                    readCondition=ReadCondition.NOT_SENT;
                else if(chatMessageCursor.getString(4).equals("SENT"))
                readCondition=ReadCondition.SENT;
                else if(chatMessageCursor.getString(4).equals("QUEUED"))
                readCondition=ReadCondition.QUEUED;
                else
                readCondition=ReadCondition.SEEN;

                ChatMessage chatMessage=new ChatMessage(chatMessageCursor.getString(0),chatMessageCursor.getString(1),chatMessageCursor.getString(2),chatMessageCursor.getBlob(3),chatMessageCursor.getString(4),readCondition,chatMessageCursor.getString(6),chatMessageCursor.getString(7).charAt(0));
                chatMessageList.add(chatMessage);

                chatMessageCursor.moveToNext();

            }

            chatMessageCursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e)
        {
            System.out.println("ChatActivity:Error in reading Database");
            e.printStackTrace();
        }

        final ChatMessageAdapter chatMessageAdapter=new ChatMessageAdapter(getApplicationContext(),R.layout.left_broadcast_show,chatMessageList);
        listview.setAdapter(chatMessageAdapter);

        // connect to the wifi network


        if(!CommonMethods.determineServer(getApplicationContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    // just for PROD reasons, commenting it for a while
                    boolean result=true;
                    //boolean result = (new WifiHotSpotAccess()).connectToHotspot(macID, getApplicationContext());

                    System.out.println("trying to connect to the wifiName " + wifiName + " ," + result + " macID: " + macID);

                    String display = "";

                    if (!result)
                        display = "Could not connect to the current Sender Device !!!!";
                    else
                        display = "Connected to the current Sender Device !!!";

                    // Toast toast=Toast.makeText(ChatActivity.this,display,Toast.LENGTH_SHORT);
                    //toast.show();
                }
            }).start();
        }


        // checking whether the activity opened has to be connected through a client or a server

        if(!CommonMethods.determineServer(getApplicationContext()))  /** if false, then a client Service has to be started for listening to the messages */
        {
            Intent outGoingIntent=new Intent(this,CheckOutgoingServiceClient.class);
            outGoingIntent.putExtra("macId", macID);
            outGoingIntent.putExtra("hotSpotIp", CommonVars.defaultHotSpotIPAddress);
            outGoingIntent.putExtra("usageId", "");
            getApplicationContext().startService(outGoingIntent);

            Intent incomingIntent=new Intent(this,CheckIncomingServiceClient.class);
            incomingIntent.putExtra("macId",macID);
            incomingIntent.putExtra("hotSpotIp",CommonVars.defaultHotSpotIPAddress);
            incomingIntent.putExtra("usageId","");
            CommonVars.context.startService(incomingIntent);
        }
        else
        {
            Intent outgoingIntent=new Intent(this,CheckOutgoingServiceServer.class);
            getApplicationContext().startService(outgoingIntent);

            Intent incomingIntent=new Intent(this,CheckIncomingServiceServer.class);
            getApplicationContext().startService(incomingIntent);
        }

        // need to constantly listen to the chatmessages received

       new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {

                    final List<ChatMessage> tempChatMessageList=ChatMessage.getChatMessageList("select * from chatmessage where macId='"+macID+"' and readcondition='QUEUED';",CommonVars.context);
                    if(tempChatMessageList.size()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatMessageList.addAll(tempChatMessageList);
                                chatMessageAdapter.notifyDataSetChanged();
                                listview.setSelection(listview.getCount() - 1);
                            }
                        });
                    }

                    if(tempChatMessageList.size()>0)
                        ChatMessage.executeQuery("update chatmessage set readcondition='SEEN' where macID='"+macID+"' and readcondition='QUEUED';",getApplicationContext());

                    try {
                        Thread.sleep(2000);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        final EditText sendEditText=(EditText)findViewById(R.id.sendEditText);
        final Button sendChatButton=(Button)findViewById(R.id.sendChatButton);


        sendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ChatMessage chatMessage=new ChatMessage(macID,CommonVars.usageId,"text",null,sendEditText.getText().toString(),ReadCondition.NOT_SENT,(new SimpleDateFormat(CommonVars.defaultDateFormat)).format(new Date()),'R');
                chatMessageList.add(chatMessage);
                chatMessageAdapter.notifyDataSetChanged();
                listview.setSelection(listview.getCount()-1);

                /** Need to write it to the database with not_sent status */

                ChatMessage.insertIntoDatabase(chatMessage,CommonVars.context);

            }
        });

    }

    private class ChatMessageAdapter extends ArrayAdapter<ChatMessage>
    {
        List<ChatMessage> chatMessageList=new ArrayList<ChatMessage>();

        public ChatMessageAdapter(Context context, int resource, List<ChatMessage> chatMessageList) {
            super(context, resource, chatMessageList);
            this.chatMessageList=chatMessageList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ChatMessage chatMessage=chatMessageList.get(position);
            View view=null;

            if(chatMessage.getChatType()=='L')
            {
                view=getLayoutInflater().inflate(R.layout.left_broadcast_show,parent,false);
            }
            else
                view=getLayoutInflater().inflate(R.layout.right_broadcast_show,parent,false);


            TextView broadCastShowTextView=(TextView)view.findViewById(R.id.broadcastShowTextView);
            TextView timeStampTextView=(TextView)view.findViewById(R.id.timestampTextView);

            broadCastShowTextView.setText(chatMessage.getMessage());
            timeStampTextView.setText(chatMessage.getTimestamp());

            return view;
        }
    }





}
