package wifiemer.tabbedactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 7/24/2017.
 */
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    List<ChatMessage> chatMessageList=new ArrayList<ChatMessage>();

    public ChatMessageAdapter(Context context, int resource, List<ChatMessage> chatMessageList) {
        super(context, resource, chatMessageList);
        this.chatMessageList=chatMessageList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMessage chatMessage=chatMessageList.get(position);
        View view=null;

        LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(chatMessage.getChatType()=='L')
        {
            view=inflater.inflate(R.layout.left_broadcast_show,parent,false);
        }
        else
            view=inflater.inflate(R.layout.right_broadcast_show,parent,false);


        TextView broadCastShowTextView=(TextView)view.findViewById(R.id.broadcastShowTextView);
        TextView timeStampTextView=(TextView)view.findViewById(R.id.timestampTextView);

        broadCastShowTextView.setText(chatMessage.getMessage());
        timeStampTextView.setText(chatMessage.getTimestamp());

        return view;
    }
}
