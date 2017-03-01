package wifiemer.tabbedactivity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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
        listHeader.add("COUNTRIES");
        listHeader.add("SCIENTISTS");
        listHeader.add("ENGINEERS");
        listHeader.add("POLICE PERSONS");

        List<String> subs=new ArrayList<String>();

        subs.add("Option 1");
        subs.add("Option 2");
        subs.add("Option 3");
        subs.add("Option 4");


        childNodeList.put("COUNTRIES", subs);
        childNodeList.put("SCIENTISTS", subs);
        childNodeList.put("ENGINEERS",subs);
        childNodeList.put("POLICE PERSONS",subs);




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

            textView2.setText(childString+" child "+childPosition+", "+groupPosition);

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
