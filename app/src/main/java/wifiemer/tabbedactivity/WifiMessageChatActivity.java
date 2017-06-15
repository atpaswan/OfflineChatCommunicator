package wifiemer.tabbedactivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import wifiemer.tabbedactivity.R;

public class WifiMessageChatActivity extends Activity {

  private boolean isAServer=false;
  private String wifiHotspotName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        final TextView ReceivedTextView=(TextView)findViewById(R.id.ReceivedTextView);
        final TextView SendTextView=(TextView)findViewById(R.id.SendTextView);
        final Button ReceivedMessageButton=(Button)findViewById(R.id.ReceivedMessageButton);
        final Button SendMessageButton=(Button)findViewById(R.id.SendMessageButton);
        final Button StartServerButton=(Button)findViewById(R.id.StartServerButton);
        final Button StartReceivingButton=(Button)findViewById(R.id.StartReceivingButton);

        Intent intent=getIntent();
        wifiHotspotName=intent.getStringExtra("wifiHotSpot");

        (new WifiHotSpotAccess()).connectToHotspot(wifiHotspotName, getApplicationContext());



        StartServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonVars.chatCommunicator = new ChatCommunicator();
                isAServer = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CommonVars.chatCommunicator.readFromClient(ReceivedTextView);
                    }
                }).start();
            }
        });

        StartReceivingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonVars.chatCommunicator=new ChatCommunicator(CommonVars.defaultHotSpotIPAddress);
                isAServer=false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        CommonVars.chatCommunicator.ReadFromServer(ReceivedTextView);

                    }
                }).start();
            }
        });


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAServer)
                {
                    CommonVars.chatCommunicator.writeToClient(SendTextView.getText().toString());
                }
                else
                {
                    if(CommonVars.chatCommunicator==null)
                    {
                        CommonVars.chatCommunicator=new ChatCommunicator(CommonVars.defaultHotSpotIPAddress);
                    }

                    CommonVars.chatCommunicator.WriteToServer(SendTextView.getText().toString());
                }
            }
        });


        (new Thread(new Runnable() {
            @Override
            public void run() {

                if(isAServer)
                {
                    if(CommonVars.chatCommunicator==null)
                        CommonVars.chatCommunicator=new ChatCommunicator();

                    CommonVars.chatCommunicator.readFromClient(ReceivedTextView);
                }
                else
                {
                    if(CommonVars.chatCommunicator==null)
                        CommonVars.chatCommunicator=new ChatCommunicator(CommonVars.defaultHotSpotIPAddress);

                    CommonVars.chatCommunicator.ReadFromServer(ReceivedTextView);
                }

            }
        })).start();











    }

}
