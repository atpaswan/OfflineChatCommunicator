package wifiemer.tabbedactivity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Atul on 2/26/2017.
 */
public class FragmentAliasActivity extends Fragment
{

    ArrayList<Alias> aliasArrayList=new ArrayList<Alias>();
    LayoutInflater inflater;
        public static FragmentAliasActivity newInstance() {
            FragmentAliasActivity fragment = new FragmentAliasActivity();

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            this.inflater=inflater;
            View rootView = inflater.inflate(R.layout.fragment_tab2alias, container, false);
            populateAliasList();
            populateListView(rootView);



            return rootView;
        }


    public void populateListView(View rootView)
    {
        ListView listView=(ListView)rootView.findViewById(R.id.listViewAlias);

        ArrayAdapter<Alias> adapter=new AliasAdapter(this.getContext(),R.layout.list_item_alias,aliasArrayList);

        listView.setAdapter(adapter);


    }

    public void populateAliasList()
    {
        Alias alias=new Alias(R.drawable.alias_photo,"First Broadcast ID","Username");
        aliasArrayList.add(alias);

        alias=new Alias(R.drawable.alias_photo,"Second Broadcast ID","UserName");
        aliasArrayList.add(alias);

        alias=new Alias(R.drawable.alias_photo,"Third Broadcast ID","UserName");
        aliasArrayList.add(alias);

        alias=new Alias(R.drawable.alias_photo,"Fourth Broadcast ID","UserName");
        aliasArrayList.add(alias);


    }


   private class AliasAdapter extends ArrayAdapter<Alias>
    {
        ArrayList<Alias> aliasArrayList;
        public AliasAdapter(Context context,int imageID,ArrayList<Alias> aliasArrayList)
        {
            super(context,imageID,aliasArrayList);
            this.aliasArrayList=aliasArrayList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.list_item_alias,parent,false);
            }

            Alias alias=aliasArrayList.get(position);

            ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);
            TextView wifiBroadCastID=(TextView)convertView.findViewById(R.id.wifiBroadCastID);
            TextView wifiAlias=(TextView)convertView.findViewById(R.id.wifiAlias);

            imageView.setImageResource(alias.getImage_id());
            wifiBroadCastID.setText(alias.getBroadcastID());
            wifiAlias.setText(alias.getUserName());

            return convertView;
        }
    }
}






