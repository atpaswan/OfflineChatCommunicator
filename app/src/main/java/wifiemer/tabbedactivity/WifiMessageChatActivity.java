package wifiemer.tabbedactivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import wifiemer.tabbedactivity.R;

public class WifiMessageChatActivity extends Activity {

  private boolean isAServer=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_message_chat);

        final Button sendMessage=(Button)findViewById(R.id.sendMessageButton);
        final Button receivedMessage=(Button)findViewById(R.id.receivedMessageButton);
        final EditText sendText=(EditText)findViewById(R.id.sendMessageText);
        final TextView receivedText=(TextView)findViewById(R.id.receivedMessageText);
        final Button connectClientButton=(Button)findViewById(R.id.connectClientButton);


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatCommunicator chatCommunicator=new ChatCommunicator();
                String writeString=sendText.getText().toString();

                chatCommunicator.WriteToServer(writeString);

            }
        });

        connectClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hotspotName=getIntent().getStringExtra("HotSpotName");

                WifiHotSpotAccess  wifiHotSpotAccess=new WifiHotSpotAccess();

                if(wifiHotSpotAccess.connectToHotspot(hotspotName,getApplicationContext()))
                {
                    System.out.println("HotSpotConnected");
                }
                else
                    System.out.println("Cannot connect to hotspot");
            }
        });


        receivedMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (new ChatCommunicator()).readFromClient(receivedText);


            }
        });


    }

}
