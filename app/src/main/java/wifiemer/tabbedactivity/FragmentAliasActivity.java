package wifiemer.tabbedactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;

/**
 * Created by Atul on 2/26/2017.
 */
public class FragmentAliasActivity extends Fragment
{

    ArrayList<Alias> aliasArrayList=new ArrayList<Alias>();
    int currListPosition=-1;

    LayoutInflater inflater;
        public static FragmentAliasActivity newInstance() {
            FragmentAliasActivity fragment = new FragmentAliasActivity();

            return fragment;
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        aliasArrayList=loadfromAliasFile();
    }

    @Override
    public void onPause() {
        super.onPause();

       /* try
        {

            FileOutputStream fos=getContext().openFileOutput(getResources().getString(R.string.aliasFileName),Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(aliasArrayList);
            System.out.println("OnPause aliasArraylist");

            fos.flush();
            fos.close();
        }
        catch(Exception e)
        {
            System.out.println("error in writing objects to the alias file");
        }
        */
    }

    public ArrayList<Alias> loadfromAliasFile()
    {
        try {
            FileInputStream fis = getContext().openFileInput(getResources().getString(R.string.aliasFileName));
            ObjectInputStream ois=new ObjectInputStream(fis);
            aliasArrayList=(ArrayList<Alias>)ois.readObject();
            System.out.println("loadfromAliasFile done "+aliasArrayList.size());

            fis.close();
            }
        catch(Exception e)
        {
            System.out.println("error in reading Alias File");
            e.printStackTrace();
        }
        return aliasArrayList;
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            this.inflater=inflater;
            View rootView = inflater.inflate(R.layout.fragment_tab2alias, container, false);
            populateAliasList();
            populateListView(rootView);



           System.out.println("onCreateView Called in FragmentAliasActivity");

            return rootView;
        }


    public void populateListView(View rootView)
    {
        ListView listView=(ListView)rootView.findViewById(R.id.listViewAlias);

        ArrayAdapter<Alias> adapter=new AliasAdapter(this.getContext(),R.layout.list_item_alias,aliasArrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  currListPosition=position;
               System.out.println("triggering onItemClickListener");
            }
        });

    }

    public void populateAliasList()
    {
        aliasArrayList=loadfromAliasFile();

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

            convertView.setFocusable(false);   // so that the item click listener event is intercepted
            Alias alias=aliasArrayList.get(position);

            ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);

            // setting the onclick listener for the imageview

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");

                    startActivityForResult(Intent.createChooser(intent,"Please Select An Image"),1);
                }
            });

            TextView wifiBroadCastID=(TextView)convertView.findViewById(R.id.wifiBroadCastID);
            TextView wifiAlias=(TextView)convertView.findViewById(R.id.wifiAlias);

            imageView.setImageResource(alias.getImage_id());
            wifiBroadCastID.setText(alias.getBroadcastID());
            wifiAlias.setText(alias.getUserName());

            return convertView;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 1: if(data!=null)
            {
                System.out.println("triggering OnActivity Result "+currListPosition);
                Uri uri=data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                }
                catch(Exception e)
                {
                    System.out.println("Exception on selecting the image");
                    e.printStackTrace();
                }


            }
        }
    }
}






