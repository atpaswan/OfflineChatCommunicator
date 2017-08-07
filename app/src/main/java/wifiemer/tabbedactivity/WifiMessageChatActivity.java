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
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        activity=this;

        final TextView ReceivedTextView=(TextView)findViewById(R.id.ReceivedTextView);
        final TextView SendTextView=(TextView)findViewById(R.id.SendTextView);
        final Button ReceivedMessageButton=(Button)findViewById(R.id.ReceivedMessageButton);
        final Button SendMessageButton=(Button)findViewById(R.id.SendMessageButton);
        final Button StartServerButton=(Button)findViewById(R.id.StartServerButton);
        final Button StartReceivingButton=(Button)findViewById(R.id.StartReceivingButton);

        Intent intent=getIntent();
        wifiHotspotName=intent.getStringExtra("HotSpotName");

        try {
            (new WifiHotSpotAccess()).connectToHotspot(wifiHotspotName, getApplicationContext());
        }
        catch(Exception e)
        {
            System.out.println("catching connecToHotspot exception. Might have been the server ");
            e.printStackTrace();
        }


        StartServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonVars.chatCommunicator = new ChatCommunicator(activity);
                isAServer = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CommonVars.chatCommunicator.readFromClient(ReceivedTextView,8086);
                    }
                }).start();
            }
        });

        StartReceivingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonVars.chatCommunicator=new ChatCommunicator(CommonVars.defaultHotSpotIPAddress,activity);
                isAServer=false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        CommonVars.chatCommunicator.ReadFromServer(ReceivedTextView,8087);


                    }
                }).start();
            }
        });


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAServer) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            CommonVars.chatCommunicator.writeToClient(SendTextView.getText().toString(),8087);

                        }
                    }).start();

                } else {
                    if (CommonVars.chatCommunicator == null) {
                        CommonVars.chatCommunicator = new ChatCommunicator(CommonVars.defaultHotSpotIPAddress,activity);
                    }

                    CommonVars.chatCommunicator.WriteToServer(SendTextView.getText().toString(),8086);
                }
            }
        });

    }

}
