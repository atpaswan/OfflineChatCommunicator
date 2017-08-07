package wifiemer.tabbedactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BroadcastSenderActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_sender);

        ListView senderListView=(ListView)findViewById(R.id.senderListView);
        Button NavButton=(Button)findViewById(R.id.NavButton);

        NavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),BroadcastChatActivity.class);
                startActivity(intent);
            }
        });

        List<SenderDevice> senderDeviceList=SenderDevice.getSenderDeviceList("select * from sender_device",getApplicationContext());
        List<BroadCastMessage> broadCastMessageList=new ArrayList<BroadCastMessage>();

        for(int i=0;i<senderDeviceList.size();i++)
        {
            SenderDevice senderDevice=senderDeviceList.get(i);
            List<BroadCastMessage> tempBroadCastList=new ArrayList<BroadCastMessage>();
            tempBroadCastList=BroadCastMessage.getBroadCastList("select  * from (select * from broadcastmessage where macid='"+senderDevice.getMacID()+"' order by rec_timestamp desc) limit 1",getApplicationContext());
            broadCastMessageList.add(tempBroadCastList.get(0));
        }

        SenderDeviceAdapter senderDeviceAdapter=new SenderDeviceAdapter(getApplicationContext(),R.layout.list_item,senderDeviceList);
        senderDeviceAdapter.setActivity(this);
        senderDeviceAdapter.setBroadCastMessageList(broadCastMessageList);
        senderDeviceAdapter.setMovementActivity(BroadcastChatActivity.class);
        senderListView.setAdapter(senderDeviceAdapter);


    }
}
