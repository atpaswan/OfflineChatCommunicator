package wifiemer.tabbedactivity;

import android.app.Activity;
import android.content.Context;
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
                else
                readCondition=ReadCondition.SEEN;

                ChatMessage chatMessage=new ChatMessage(chatMessageCursor.getString(0),chatMessageCursor.getString(1),chatMessageCursor.getBlob(2),chatMessageCursor.getString(3),readCondition,chatMessageCursor.getString(5),chatMessageCursor.getString(6).charAt(0));
                chatMessageList.add(chatMessage);

                chatMessageCursor.moveToNext();

            }

            chatMessageCursor.close();

        }
        catch (Exception e)
        {
            System.out.println("ChatActivity:Error in reading Database");
            e.printStackTrace();
        }

        final ChatMessageAdapter chatMessageAdapter=new ChatMessageAdapter(getApplicationContext(),R.layout.left_broadcast_show,chatMessageList);
        listview.setAdapter(chatMessageAdapter);

        // connect to the wifi network

        new Thread(new Runnable() {
            @Override
            public void run() {

                wifiName=(new ChatCommunicator("",activity)).getWifiName(macID,getApplicationContext());
                boolean result=(new WifiHotSpotAccess()).connectToHotspot(wifiName,getApplicationContext());

                System.out.println("trying to connect to the wifiName "+wifiName+" ,"+result+" macID: "+macID);

                String display="";

                if(!result)
                    display="Could not connect to the current Sender Device !!!!";
                else
                    display="Connected to the current Sender Device !!!";

               // Toast toast=Toast.makeText(ChatActivity.this,display,Toast.LENGTH_SHORT);
                //toast.show();
            }
        }).start();


        // need to constantly listen to the chatmessages received

       new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {

                    ChatCommunicator chatCommunicator = new ChatCommunicator(CommonVars.defaultHotSpotIPAddress, activity);
                    String readString = chatCommunicator.readFromServer(1086);

                    if(!readString.equals("")) {
                        ChatMessage chatMessage = new ChatMessage(macID, DataType.TEXT, null, readString, ReadCondition.SEEN, CommonVars.getPresentTime(), 'L');
                        chatMessageList.add(chatMessage);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                chatMessageAdapter.notifyDataSetChanged();
                                listview.setSelection(listview.getCount()-1);
                            }
                        });
                        boolean res = ChatMessage.insertIntoDatabase(chatMessage, getApplicationContext());
                        if (res)
                            System.out.println("ChatMessage received and inserted");
                        else
                            System.out.println("ChatMessage could not be inserted");
                    }
                }
            }
        }).start();


        final EditText sendEditText=(EditText)findViewById(R.id.sendEditText);
        final Button sendChatButton=(Button)findViewById(R.id.sendChatButton);


        sendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ChatMessage chatMessage=new ChatMessage(macID,"text",null,sendEditText.getText().toString(),ReadCondition.NOT_SENT,(new SimpleDateFormat(CommonVars.defaultDateFormat)).format(new Date()),'R');
                chatMessageList.add(chatMessage);
                chatMessageAdapter.notifyDataSetChanged();
                listview.setSelection(listview.getCount()-1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ChatCommunicator chatCommunicator=new ChatCommunicator("",activity);
                        String wifiName=chatCommunicator.getWifiName(macID,getApplicationContext());
                        chatCommunicator.setWifiHotSpot(CommonVars.defaultHotSpotIPAddress);
                        boolean result=chatCommunicator.WriteToServer(chatMessage.getMessage(),1087);

                        if(result)
                            chatMessage.setReadCondition(ReadCondition.SENT);

                        Boolean res=ChatMessage.insertIntoDatabase(chatMessage,getApplicationContext());

                    }
                }).start();

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
