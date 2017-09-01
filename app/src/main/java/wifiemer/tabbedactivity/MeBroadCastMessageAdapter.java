package wifiemer.tabbedactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 9/2/2017.
 */
public class MeBroadCastMessageAdapter extends ArrayAdapter<BroadCastMessage> {

    List<BroadCastMessage> broadCastMessageList=new ArrayList<BroadCastMessage>();

    public MeBroadCastMessageAdapter(Context context, int resource,List<BroadCastMessage> broadCastMessageList) {


        super(context, resource,broadCastMessageList);
        this.broadCastMessageList=broadCastMessageList;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=null;
        if(convertView!=null)
        {
            view=convertView;
        }
        else {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.melayout_item, parent, false);
        }
            TextView broadCastShowTextView = (TextView) view.findViewById(R.id.LastMessage);
            TextView timeStampTextView = (TextView) view.findViewById(R.id.timestampTextView);

            BroadCastMessage broadCastMessage = broadCastMessageList.get(position);

            broadCastShowTextView.setText(broadCastMessage.getMessage());
            timeStampTextView.setText(broadCastMessage.getTimestamp());

            return view;


    }
}
