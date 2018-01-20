package wifiemer.tabbedactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BroadcastChatActivity extends Activity {

    String macID="";
    List<ChatMessage> chatMessageList=new ArrayList<ChatMessage>();
    Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_chat);

        final ListView chatListView=(ListView)findViewById(R.id.chatListView);
        macID=getIntent().getStringExtra("macID");
        chatMessageList=ChatMessage.getChatMessageList("select * from chatmessage where macid='"+macID+"'",getApplicationContext());
        final ChatMessageAdapter chatMessageAdapter=new ChatMessageAdapter(getApplicationContext(),R.layout.left_broadcast_show,chatMessageList);

        chatListView.setAdapter(chatMessageAdapter);


        // need to constantly listen to the chat messages received

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {
                    ChatCommunicator chatCommunicator = new ChatCommunicator("", activity);
                    String readString = "";

                     ChatMessage chatMessage = new ChatMessage(macID, CommonVars.usageId,DataType.TEXT, null, readString, ReadCondition.NOT_SEEN, CommonVars.getPresentTime(), 'L');
                    chatMessageList.add(chatMessage);
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          chatMessageAdapter.notifyDataSetChanged();
                                          chatListView.setSelection(chatListView.getCount() - 1);
                                      }
                    });

                    ChatMessage.insertIntoDatabase(chatMessage,getApplicationContext());
                }
            }
        }).start();

        final EditText sendEditText=(EditText)findViewById(R.id.sendEditText);
        Button sendChatButton=(Button)findViewById(R.id.sendChatButton);

        sendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        ChatCommunicator chatCommunicator=new ChatCommunicator("",activity);
                        String writeString=sendEditText.getText().toString();
                        //sendEditText.clearComposingText();

                        boolean result=true;
                        ReadCondition readCondition;
                        if(result)
                        {
                            System.out.println("writetoClient passed");
                            readCondition=ReadCondition.SENT;
                        }
                        else {
                            System.out.println("writetoClient failed");
                            readCondition=ReadCondition.NOT_SENT;
                        }
                        final ChatMessage chatMessage=new ChatMessage(macID,CommonVars.usageId,DataType.TEXT,null,writeString,readCondition, CommonVars.getPresentTime(),'R');
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                chatMessageList.add(chatMessage);
                            }
                        });

                    }
                }).start();

            }
        });
    }
}
