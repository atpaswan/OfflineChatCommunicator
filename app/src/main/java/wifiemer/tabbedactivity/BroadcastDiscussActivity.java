package wifiemer.tabbedactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BroadcastDiscussActivity extends Activity {

    List<BroadCastMessage> broadCastMessageList=new ArrayList<BroadCastMessage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_discuss);

        ListView broadCastListView=(ListView)findViewById(R.id.broadCastListView);
        Button NavButton=(Button)findViewById(R.id.NavButton);

        NavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),BroadcastSenderActivity.class);
                startActivity(intent);
            }
        });
        broadCastMessageList=BroadCastMessage.getBroadCastList("select * from broadcastmessage where macID='self'",getApplicationContext());

        BroadCastAdapter broadCastAdapter=new BroadCastAdapter(getApplicationContext(),broadCastMessageList);

        if(broadCastListView==null)
            System.out.println("BroadcastListView null");


        broadCastListView.setAdapter(broadCastAdapter);


    }


    private class BroadCastAdapter extends ArrayAdapter<BroadCastMessage>
    {
        List<BroadCastMessage> broadCastMessageList=new ArrayList<BroadCastMessage>();

        public BroadCastAdapter(Context context,List<BroadCastMessage> broadCastMessageList) {
            super(context, R.layout.list_item, broadCastMessageList);
            this.broadCastMessageList=broadCastMessageList;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view=getLayoutInflater().inflate(R.layout.list_item,parent,false);
            BroadCastMessage broadCastMessage=broadCastMessageList.get(position);

            ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
            TextView wifiNameTextView=(TextView)view.findViewById(R.id.WifiName);
            TextView LastMessageTextView=(TextView)view.findViewById(R.id.LastMessage);
            TextView LastTimeStampTextView=(TextView)view.findViewById(R.id.LastTimeStampTextView);

            imageView.setImageResource(R.drawable.image001);
            wifiNameTextView.setText("SELF");
            LastMessageTextView.setText(broadCastMessage.getMessage());
            LastTimeStampTextView.setText(broadCastMessage.getTimestamp());

            LastMessageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(getContext(),BroadcastSenderActivity.class);
                    startActivity(intent);
                }
            });



            return view;

        }
    }
}
