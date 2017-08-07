package wifiemer.tabbedactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Atul on 2/26/2017.
 */
public class FragmentBroadcastActivity extends Fragment{

    ArrayList<String> listHeader=new ArrayList<String>();
    HashMap<String,List<String>> childNodeList=new HashMap<String,List<String>>();
    LayoutInflater inflater;

    public static FragmentBroadcastActivity newInstance() {
        FragmentBroadcastActivity fragment = new FragmentBroadcastActivity();
        System.out.println("My first git change. Hurray");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab3broadcast, container, false);
        this.inflater=inflater;
     //   ExpandableListView listView=(ExpandableListView)rootView.findViewById(R.id.expandableListView);



        populateArrList();
        populatelistView(rootView);

        return rootView;
    }

    public void populatelistView(View rootView)
    {
        ExpandableListView expandableListView=(ExpandableListView)rootView.findViewById(R.id.expandableListView);
        BaseExpandableListAdapter baseExpandableListAdapter=new BroadCastItemAdapter(rootView.getContext(),listHeader,childNodeList);

        expandableListView.setAdapter(baseExpandableListAdapter);

    }

    public void populateArrList()
    {
        listHeader=new ArrayList<String>();
        String headerItem1="ALERTS";
        String headerItem2="WARNINGS";
        String headerItem3="HAPPY SITUATION";
        String headerItem4="SAD SITUATION";

        listHeader.add(headerItem1);
        listHeader.add(headerItem2);
        listHeader.add(headerItem3);
        listHeader.add(headerItem4);

        List<String> subs=new ArrayList<String>();

        subs.add("A PERSON NAMED KALLU IS IN 50  METRES DIAMETER OF THIS AREAS");
        subs.add("Ineligible");

        childNodeList.put(headerItem1, subs);

        subs=new ArrayList<String>();

        subs.add(" PLEASE KEEP AWAY FROM THIS PLACE. THERE ARE ZOMBIES HERE");
        subs.add("Ineligible");

        childNodeList.put(headerItem2, subs);

        subs=new ArrayList<String>();

        subs.add("A PERSON NAMED SASURA HAS A BIRTHDAY CELEBRATION WITHIN 50 METRES OF THIS VICINITY");
        subs.add("Ineligible");

        childNodeList.put(headerItem3,subs);

        subs=new ArrayList<String>();

        subs.add(" A PERSON NAMED SASURA HAS DIED WITHIN 50 METRES OF THIS VICINITY. PLEASE STAY QUIET");
        subs.add("Ineligible");
        childNodeList.put(headerItem4,subs);




    }



    private class BroadCastItemAdapter extends BaseExpandableListAdapter
    {
        public BroadCastItemAdapter(Context context,List<String> listheader,HashMap<String,List<String>> childNodesList) {
            super();
        }

        @Override
        public int getGroupCount() {
            return listHeader.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listHeader.get(groupPosition);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childNodeList.get(listHeader.get(groupPosition)).size();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childNodeList.get(listHeader.get(groupPosition));
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.unit_item,parent,false);
            }



            String childString=childNodeList.get(listHeader.get(groupPosition)).get(childPosition);

            TextView textView2=(TextView)convertView.findViewById(R.id.textView2);
            final EditText inputText=(EditText)convertView.findViewById(R.id.inputText);
            Button sendButton=(Button)convertView.findViewById(R.id.sendButton);

            String input1=inputText.getText().toString();

            String[][] passWord=new String[4][4];

            String pass="SamarthSenaSuraksha";

            for(int i=0;i<4;i++)
            {
                for(int j=0;j<4;j++)
                    passWord[i][j]=pass+(i+1)+(j+1);  // filling the passWord array with the appropriate variations

            }

            System.out.println(sendButton.getText() + " sendbutton");

           sendButton.setOnClickListener(new View.OnClickListener() {   // Triggering the hotspot with the given input characteristics using the send button
                @Override
                public void onClick(View v) {
                    System.out.println(" EnterString: " );


                    if(inputText==null)
                        System.out.println("currentEdittext null");

                if(inputText!=null) {
                    String enterString = "01"+inputText.getText().toString();

                 /*   try {
                        byte[] encryptebytes = (new CryptEncrypt()).Encrypt(enterString);
                        enterString= Base64.encodeToString(encryptebytes,Base64.DEFAULT);
                    }
                    catch(Exception e)
                    {

                    } */

                    System.out.println("Entering if"+enterString);

                    WifiHotSpotAccess wifiHotSpotAccess = new WifiHotSpotAccess();
                    boolean getState = wifiHotSpotAccess.setHotspotwithName(enterString, getContext());

                    if (getState) {
                        System.out.println("Hotspot triggered");
                    }
                }

                }
            });

            Button ForwardButton=(Button)convertView.findViewById(R.id.ForwardButton);

            ForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(),BroadcastDiscussActivity.class);
                intent.putExtra("macID","self");
                startActivity(intent);
            }
        });

            textView2.setText(childString);

           return convertView;

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.list_group,parent,false);

            }


            String groupString=listHeader.get(groupPosition);

            TextView textView=(TextView)convertView.findViewById(R.id.textView);

            textView.setText(groupString);

            return convertView;
        }
    }



}
