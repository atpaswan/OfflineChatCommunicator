package wifiemer.tabbedactivity;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.Bundle;

        import java.util.ArrayList;
        import java.util.List;
        import android.database.sqlite.*;
        import android.text.Layout;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;

public class FragmentReceivedBroadcastActivity extends Activity {

    String macID="";
    List<BroadCastMessage> broadCastMessageList=new ArrayList<BroadCastMessage>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_received_broadcast);

        macID=getIntent().getStringExtra("macID");

        SQLiteDatabase sqLiteDatabase=getApplicationContext().openOrCreateDatabase(CommonSettings.appDatabase,MODE_PRIVATE,null);

        Cursor broadCastMessageCursor=sqLiteDatabase.rawQuery("select  * from (select * from broadcastmessage where macid='"+macID+"' order by rec_timestamp desc) ",null);

        broadCastMessageCursor.moveToFirst();

        while(!broadCastMessageCursor.isAfterLast())
        {
            BroadCastMessage broadCastMessage=new BroadCastMessage(broadCastMessageCursor.getString(0),broadCastMessageCursor.getString(1),broadCastMessageCursor.getString(2),broadCastMessageCursor.getString(3));

            broadCastMessageList.add(broadCastMessage);

            broadCastMessageCursor.moveToNext();
        }


        sqLiteDatabase.close();


        ListView listview=(ListView)findViewById(R.id.broadcastListView);
        BroadCastMessageLoadAdapter broadCastMessageLoadAdapter=new BroadCastMessageLoadAdapter(getApplicationContext(),R.layout.center_broadcast_show,broadCastMessageList);
        listview.setAdapter(broadCastMessageLoadAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("macID", macID);
                startActivity(intent);

            }
        });


    }



    private class BroadCastMessageLoadAdapter extends ArrayAdapter<BroadCastMessage>
    {


        List<BroadCastMessage> broadCastMessageList=new ArrayList<BroadCastMessage>();

        public BroadCastMessageLoadAdapter(Context context, int resource,List<BroadCastMessage> broadCastMessageList) {


            super(context, resource,broadCastMessageList);
            this.broadCastMessageList=broadCastMessageList;


        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view =getLayoutInflater().inflate(R.layout.center_broadcast_show,parent,false);

            TextView broadCastShowTextView=(TextView)view.findViewById(R.id.broadcastShowTextView);
            TextView timeStampTextView=(TextView)view.findViewById(R.id.timestampTextView);

            BroadCastMessage broadCastMessage=broadCastMessageList.get(position);

            broadCastShowTextView.setText(broadCastMessage.getMessage());
            timeStampTextView.setText(broadCastMessage.getTimestamp());

            return view;


        }
    }



}
