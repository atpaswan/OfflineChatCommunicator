package wifiemer.tabbedactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Atul on 7/24/2017.
 */
public class SenderDeviceAdapter extends ArrayAdapter<SenderDevice> {

    List<SenderDevice> senderDeviceList;
    List<BroadCastMessage> broadCastMessageList;
    Activity activity;
    Class movementActivity;



    public SenderDeviceAdapter(Context context, int position,List<SenderDevice> senderDeviceList) {
        super(context, R.layout.list_item, senderDeviceList);
        this.senderDeviceList=senderDeviceList;

    }

    public List<SenderDevice> getSenderDeviceList() {
        return senderDeviceList;
    }

    public void setSenderDeviceList(List<SenderDevice> senderDeviceList) {
        this.senderDeviceList = senderDeviceList;
    }

    public List<BroadCastMessage> getBroadCastMessageList() {
        return broadCastMessageList;
    }

    public void setBroadCastMessageList(List<BroadCastMessage> broadCastMessageList) {
        this.broadCastMessageList = broadCastMessageList;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Class getMovementActivity() {
        return movementActivity;
    }

    public void setMovementActivity(Class movementActivity) {
        this.movementActivity = movementActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.list_item, parent, false);
        }

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

        final SenderDevice senderDevice=senderDeviceList.get(position);
        Bitmap bitmap= BitmapFactory.decodeByteArray(senderDevice.getImageBytes(), 0, senderDevice.getImageBytes().length);
        imageView.setImageBitmap(bitmap);

        TextView textView = (TextView) rootView.findViewById(R.id.WifiName);
        textView.setText(senderDevice.getMacID());

        final TextView lastMessage = (TextView) rootView.findViewById(R.id.LastMessage);
        final TextView timestampTextView=(TextView)rootView.findViewById(R.id.timestampTextView);
        final TextView wifiNameTextView=(TextView)rootView.findViewById(R.id.WifiName);

        if(senderDevice.aliasName.equals(""))
            wifiNameTextView.setText(senderDevice.getMacID());
        else
            wifiNameTextView.setText(senderDevice.getAliasName());

        timestampTextView.setText(broadCastMessageList.get(position).getTimestamp());

        lastMessage.setText(broadCastMessageList.get(position).getMessage());

        final int currPosition=position;

        lastMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(),movementActivity);
                intent.putExtra("macID", senderDeviceList.get(currPosition).getMacID());
                System.out.println("Executing the activity movement");
                activity.startActivity(intent);
            }
        });

        return rootView;


    }
}
