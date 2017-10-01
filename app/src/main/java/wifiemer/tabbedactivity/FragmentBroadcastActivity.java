package wifiemer.tabbedactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.annotations.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.w3c.dom.Text;

/**
 * Created by Atul on 2/26/2017.
 */
public class FragmentBroadcastActivity extends Fragment{

    ArrayList<String> listHeader=new ArrayList<String>();
    HashMap<String,List<String>> childNodeList=new HashMap<String,List<String>>();
    LayoutInflater inflater;
    List<SendingType> sendingTypeList=new ArrayList<SendingType>();
    public boolean readList=false;

    public static FragmentBroadcastActivity newInstance() {
        FragmentBroadcastActivity fragment = new FragmentBroadcastActivity();
        System.out.println("My first git change. Hurray");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View  rootView = inflater.inflate(R.layout.fragment_tab3broadcast, container, false);
        this.inflater=inflater;
     //   ExpandableListView listView=(ExpandableListView)rootView.findViewById(R.id.expandableListView);

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (!readList)
                    populateArrList();
                populatelistView(rootView);
            }
        }
        ).run();

        return rootView;
    }

    public void populatelistView(View rootView)
    {
        ExpandableListView expandableListView=(ExpandableListView)rootView.findViewById(R.id.expandableListView);
        BaseExpandableListAdapter baseExpandableListAdapter=new BroadCastItemAdapter(rootView.getContext(),sendingTypeList);

        expandableListView.setAdapter(baseExpandableListAdapter);

    }

    public void populateArrList()
    {sendingTypeList=SendingType.getSendingTypeList(getContext());}



    private class BroadCastItemAdapter extends BaseExpandableListAdapter
    {
        public BroadCastItemAdapter(Context context,List<SendingType> sendingTypeList) {
            super();
        }

        @Override
        public int getGroupCount() {
            return sendingTypeList.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return sendingTypeList.get(groupPosition);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return sendingTypeList.get(groupPosition).getCodeArr().length;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            SendingCode[] sendingCodeArr= sendingTypeList.get(groupPosition).getCodeArr();
            return sendingCodeArr[childPosition];
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

            SendingCode[] sendingCodeArr=sendingTypeList.get(groupPosition).getCodeArr();
            final SendingCode sendingCode=sendingCodeArr[childPosition];

            final int fieldCount=sendingCode.getFieldNumber();

            switch(fieldCount)
            {
                case 1:{ convertView=inflater.inflate(R.layout.sendcodeload_item1,null); Contextify(1,convertView,sendingCode);break;}
                case 2:{ convertView=inflater.inflate(R.layout.sendcodeload_item2,null); Contextify(2,convertView,sendingCode);break;}
                case 3:{ convertView=inflater.inflate(R.layout.sendcodeload_item3,null);Contextify(3,convertView,sendingCode); break;}
                case 4:{ convertView=inflater.inflate(R.layout.sendcodeload_item4,null);Contextify(4,convertView,sendingCode); break;}
                case 5:{ convertView=inflater.inflate(R.layout.sendcodeload_item5,null); Contextify(5,convertView,sendingCode);break;}
                default:{ convertView=inflater.inflate(R.layout.sendcodeload_item1,null);Contextify(sendingCode.getFieldNumber(),convertView,sendingCode); break;}

            }

            Button sendButton=(Button)convertView.findViewById(R.id.sendMessageButton);
            final View view=convertView;

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String[] messages=new String[fieldCount];

                    System.out.println("entering sendButton");

                    for(int i=0;i<fieldCount;i++)
                    {
                        String key="";
                        EditText editText=null;

                        if(i==0)
                         editText=(EditText)view.findViewById(R.id.editText1);
                        else if(i==1)
                            editText=(EditText)view.findViewById(R.id.editText2);
                        else if(i==2)
                            editText=(EditText)view.findViewById(R.id.editText3);
                        else if(i==3)
                            editText=(EditText)view.findViewById(R.id.editText4);
                        else
                            editText=(EditText)view.findViewById(R.id.editText5);

                        key=editText.getText().toString();

                        messages[i]=key;
                    }

                    String BuiltMessage=buildMessage(messages,sendingCode);

                    (new WifiHotSpotAccess()).setHotspotwithName(BuiltMessage,getContext());
                    BroadCastMessage.insertIntoDatabase(new BroadCastMessage("self", BuiltMessage,CommonVars.getPresentTime(),""),getContext());

                }
            });

            return convertView;

        }

        public String buildMessage(String[] messages,SendingCode sendingCode)
        {
            System.out.println("Entering BuildMessage");
            int fieldCount=sendingCode.getFieldNumber();
            int codeNumber=sendingCode.getCodeNumber();
            String[] fieldSeparator=sendingCode.getFieldSeparator().split("_");

            String fieldString="";

            if(codeNumber<10)
                fieldString="0"+codeNumber;
            else
            fieldString=codeNumber+"";

            String builtMessage=fieldString+"";

            for(int i=0;i<fieldCount;i++)
            {
                builtMessage+=padOrLimit(messages[i],Integer.parseInt(fieldSeparator[i]));
            }

            return  builtMessage;

        }

        public String padOrLimit(String uncheckedString,int limit)
        {
            System.out.println("Entering padorLimit ");
            if(uncheckedString.length()>=limit)
                return uncheckedString.substring(0,limit);
            else
            {
                int remLen=limit-uncheckedString.length();
                String padString="";
                for(int i=1;i<=remLen;i++)
                    padString+=CommonVars.fillerChar;

                uncheckedString+=padString;

                return  uncheckedString;
            }
        }

        public void Contextify(int fieldNumber,View convertView,SendingCode sendingCode)
        {
            String parseString=sendingCode.getWriteString();
            String fieldSeparator=sendingCode.getFieldSeparator();

            String[] fieldSeparatorArr=fieldSeparator.split("_");
            int[] fieldSeparatorInt=new int[fieldSeparatorArr.length];
            for(int i=0;i<fieldSeparatorArr.length;i++)
                fieldSeparatorInt[i]=Integer.parseInt(fieldSeparatorArr[i]);

            List<String> sepStrings=Parsify(parseString,sendingCode.getReplacementCode());

            System.out.println("Calling Contextify :"+fieldNumber);
            if(fieldNumber>=1)
            {
                TextView textView1=(TextView)convertView.findViewById(R.id.textView1);
                TextView textView2=(TextView)convertView.findViewById(R.id.textView2);
                EditText editText1=(EditText)convertView.findViewById(R.id.editText1);
                editText1.setImeOptions(EditorInfo. IME_ACTION_NEXT);
                textView1.setText(sepStrings.get(0));
                //editText1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(fieldSeparatorInt[0])});
                textView2.setText(sepStrings.get(1));
            }

            if(fieldNumber>=2)
            {
                TextView textView3=(TextView)convertView.findViewById(R.id.textView3);
                EditText editText2=(EditText)convertView.findViewById(R.id.editText2);

                //editText2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(fieldSeparatorInt[1])});
                textView3.setText(sepStrings.get(2));
            }

            if(fieldNumber>=3)
            {
                TextView textView4=(TextView)convertView.findViewById(R.id.textView4);
                textView4.setText(sepStrings.get(3));

                EditText editText3=(EditText)convertView.findViewById(R.id.editText3);
                editText3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(fieldSeparatorInt[2])});
            }

            if(fieldNumber>=4)
            {
                TextView textView5=(TextView)convertView.findViewById(R.id.textView5);
                textView5.setText(sepStrings.get(4));

                EditText editText4=(EditText)convertView.findViewById(R.id.editText4);
                editText4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(fieldSeparatorInt[3])});
            }

            if(fieldNumber>=5)
            {
                TextView textView6=(TextView)convertView.findViewById(R.id.textView6);
                textView6.setText(sepStrings.get(5));

                EditText editText5=(EditText)convertView.findViewById(R.id.editText5);
                editText5.setFilters(new InputFilter[]{new InputFilter.LengthFilter(fieldSeparatorInt[4])});
            }
        }

        public List<String> Parsify(String parseString,char replacementCode)
        {
            List<String> parseStringList=new ArrayList<String>();

            System.out.println("parseString "+parseString);

            for(int i=0;i<parseString.length();i++)
            {
                String word="";

                while(parseString.charAt(i)!=replacementCode)
                {
                    word+=parseString.charAt(i);
                    i++;
                    if(i>=parseString.length())
                        break;
                }

                parseStringList.add(word);
            }

            for(int i=0;i<parseStringList.size();i++)
            {
                System.out.println("parseList: "+parseStringList.get(i).toString());
            }
            return  parseStringList;

        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            SendingType sendingType=sendingTypeList.get(groupPosition);
            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.list_group,parent,false);

            }


            String groupString=sendingType.getTypeString();

            TextView textView=(TextView)convertView.findViewById(R.id.textView);

            textView.setText(groupString);

            return convertView;
        }
    }



}
