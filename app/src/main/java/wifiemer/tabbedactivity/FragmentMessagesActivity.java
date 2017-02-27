package wifiemer.tabbedactivity;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Atul on 2/26/2017.
 */
public class FragmentMessagesActivity extends Fragment {


    public static FragmentMessagesActivity newInstance() {
        FragmentMessagesActivity fragment = new FragmentMessagesActivity();
        // Heightened Engineering

        // Heightened Science, d
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab1messages, container, false);
        populateListView(rootView);
        registerClickListener(rootView);


        return rootView;
    }

    public void registerClickListener(final View rootView)
    {
        ListView listView=(ListView)rootView.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(rootView,"You have clicked on the laauda item "+position,Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void populateListView(View rootView)
    {
        TextView textView = (TextView) rootView.findViewById(R.id.textView1);

        ListView listView=(ListView)rootView.findViewById(R.id.listView);
        String[] strings1={"Lauda","Lahsun","Greater","Chutiyapa","Bhosdika"};

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this.getContext(),R.layout.list_item,strings1);

        listView.setAdapter(adapter);
    }

}


