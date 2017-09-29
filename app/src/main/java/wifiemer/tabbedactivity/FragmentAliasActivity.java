package wifiemer.tabbedactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
    ImageView currImageView;

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


    public void writeToAliasFile()
    {
        try {
            FileOutputStream fos=getContext().openFileOutput(getResources().getString(R.string.aliasFileName),Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(aliasArrayList);
            System.out.println("writetoAlias done "+aliasArrayList.size());

            fos.close();
        }
        catch(Exception e)
        {
            System.out.println("error in writing Alias File");
            e.printStackTrace();
        }

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

            final ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);

            imageView.setContentDescription(position+"");

            // setting the onclick listener for the imageview

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");

                    System.out.println("You have clicked on the image at item " + imageView.getContentDescription());
                    CommonVars.currListPosition=Integer.parseInt(imageView.getContentDescription().toString());
                    //imageView.setImageResource(R.drawable.image001);
                    //aliasArrayList.get(currListPosition).setImage_id(R.drawable.image001);
                    startActivityForResult(Intent.createChooser(intent, "Please Select An Image"), 1);
                }
            });



            TextView wifiBroadCastID=(TextView)convertView.findViewById(R.id.wifiBroadCastID);
            TextView wifiAlias=(TextView)convertView.findViewById(R.id.wifiAlias);

            if(alias.getImageByteArray()==null)
            imageView.setImageResource(alias.getImage_id());
            else
            {
                Bitmap bitmap= BitmapFactory.decodeByteArray(alias.getImageByteArray(),0,alias.getImageByteArray().length);
                imageView.setImageBitmap(bitmap);
            }
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
                try
                {
                System.out.println("triggering OnActivity Result "+CommonVars.currListPosition);
                Uri uri=data.getData();
                InputStream imageStream=getContext().getContentResolver().openInputStream(uri);

                    Bitmap bitmap= BitmapFactory.decodeStream(imageStream);
                    if(bitmap!=null)
                        System.out.println("Bitmap is not null");

                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    aliasArrayList.get(CommonVars.currListPosition).setImageByteArray(byteArrayOutputStream.toByteArray());
                    writeToAliasFile();
                    if(currImageView==null)
                        System.out.println("currImageView is null");


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






