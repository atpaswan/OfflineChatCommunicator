package wifiemer.tabbedactivity;

import java.security.Timestamp;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.database.sqlite.*;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;


/**
 * Created by Atul on 6/23/2017.
 */
public class ChatMessage {

    String macID;
    String usageId;
    String datatype;
    byte[] data;
    String message;
    ReadCondition readCondition;
    String timestamp;
    char chatType;

    public ChatMessage(String macID,String usageId, String datatype, byte[] data, String message, ReadCondition readCondition, String timestamp, char chatType) {
        this.macID = macID;
        this.usageId=usageId;
        this.datatype = datatype;
        this.data = data;
        this.message = message;
        this.readCondition = readCondition;
        this.timestamp = timestamp;
        this.chatType = chatType;
    }

    public String getMacID() {
        return macID;
    }

    public void setMacID(String macID) {
        this.macID = macID;
    }

    public void setUsageId(String usageId){this.usageId=usageId;}

    public String getUsageId(){return usageId;}

    public char getChatType() {
        return chatType;
    }

    public void setChatType(char chatType) {
        this.chatType = chatType;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReadCondition getReadCondition() {
        return readCondition;
    }

    public void setReadCondition(ReadCondition readCondition) {
        this.readCondition = readCondition;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public static boolean insertIntoDatabase(ChatMessage chatMessage,Context context)
    {

        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,context.MODE_PRIVATE,null);
        String DbQuery="INSERT INTO CHATMESSAGE VALUES(?,?,?,?,?,?,?,?)";

        String str_readCondition="";

        if(chatMessage.getReadCondition().equals(ReadCondition.QUEUED))
            str_readCondition="QUEUED";
        else if(chatMessage.getReadCondition().equals(ReadCondition.NOT_SENT))
            str_readCondition="NOT_SENT";
        else if(chatMessage.getReadCondition().equals(ReadCondition.SENT))
            str_readCondition="SENT";
        else
        str_readCondition="SEEN";


        try {
            sqLiteDatabase.execSQL(DbQuery, new Object[]{chatMessage.getMacID(),chatMessage.getUsageId(), chatMessage.getDatatype(), chatMessage.getData(), chatMessage.getMessage(), str_readCondition, chatMessage.getTimestamp(), chatMessage.getChatType()});
            System.out.println("Database insertion done "+str_readCondition);
            sqLiteDatabase.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ChatMessage: insertIntoDatabase failed ");
            sqLiteDatabase.close();
            return false;
        }



    }


    public boolean insertListIntoDatabase(List<ChatMessage> chatMessageList,Context context)
    {

        for(int i=0;i<chatMessageList.size();i++)
        {
            ChatMessage chatMessage=chatMessageList.get(i);
            insertIntoDatabase(chatMessage,context);
        }
        return true;
    }

    public static List<ChatMessage> modifyList(List<ChatMessage> chatMessageList,String macId,String usageId)
    {
        List<ChatMessage> modList=new ArrayList<ChatMessage>();

        for(int i=0;i<chatMessageList.size();i++)
        {
            ChatMessage chatMessage=chatMessageList.get(i);
            chatMessage.setMacID(macId);
            chatMessage.setUsageId(usageId);

            modList.add(chatMessage);
        }

        return modList;
    }

    public static boolean insertIntoDatabaseUnsent(ChatMessage chatMessage,Context context)
    {

        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,context.MODE_PRIVATE,null);
        String DbQuery="INSERT INTO CHATUNSENT VALUES(?,?,?,?,?,?,?,?)";

        String str_readCondition="";

        if(chatMessage.getReadCondition()==ReadCondition.QUEUED)
            str_readCondition="QUEUED";
        else if(chatMessage.getReadCondition()==ReadCondition.NOT_SENT)
            str_readCondition="NOT_SENT";
        else if(chatMessage.getReadCondition()==ReadCondition.SENT)
            str_readCondition="SENT";
        else
            str_readCondition="SEEN";


        try {
            sqLiteDatabase.execSQL(DbQuery, new Object[]{chatMessage.getMacID(),chatMessage.getUsageId(), chatMessage.getDatatype(), chatMessage.getData(), chatMessage.getMessage(), str_readCondition, chatMessage.getTimestamp(), chatMessage.getChatType()});
            sqLiteDatabase.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ChatMessage: insertIntoDatabase failed ");
            sqLiteDatabase.close();
            return false;
        }

    }

    public static boolean executeQuery(String query,Context context)
    {
        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,context.MODE_PRIVATE,null);

        try {
            sqLiteDatabase.execSQL(query);
            sqLiteDatabase.close();
            return true;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ChatMessage: insertIntoDatabase failed ");
            sqLiteDatabase.close();
            return false;

        }

    }

    public static boolean deletefromChatUnsent(String macID,String usageId,Context context)
    {

        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,context.MODE_PRIVATE,null);
        String DbQuery="delete * from CHATUNSENT where macId=? and usageId=?;";

        try {
            sqLiteDatabase.execSQL(DbQuery, new Object[]{macID,usageId});
            sqLiteDatabase.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ChatMessage: deletefromDatabase failed ");
            sqLiteDatabase.close();
            return false;
        }

    }

    public static List<ChatMessage> getChatMessageList(String query,Context context)
    {
        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,Context.MODE_PRIVATE,null);
        Cursor chatMessageCursor=sqLiteDatabase.rawQuery(query,null);

        chatMessageCursor.moveToFirst();

        //System.out.println("ChatMessageCursor count "+chatMessageCursor.getCount());

        List<ChatMessage> chatMessageList=new ArrayList<ChatMessage>();

        while(!chatMessageCursor.isAfterLast())
        {
            String str_readCondition=chatMessageCursor.getString(5);
            ReadCondition readCondition;
            if(str_readCondition.equals("SENT"))
                readCondition=ReadCondition.SENT;
            else if(str_readCondition.equals("NOT_SENT"))
                readCondition=ReadCondition.NOT_SENT;
            else
            readCondition=ReadCondition.SEEN;

            ChatMessage chatMessage=new ChatMessage(chatMessageCursor.getString(0),chatMessageCursor.getString(1),chatMessageCursor.getString(2),chatMessageCursor.getBlob(3),chatMessageCursor.getString(4),readCondition,chatMessageCursor.getString(6),chatMessageCursor.getString(7).charAt(0));
            chatMessageList.add(chatMessage);

            chatMessageCursor.moveToNext();

        }


        chatMessageCursor.close();
        sqLiteDatabase.close();

        return chatMessageList;



    }

    public static ChatMessage retrieveObjectFromJson(String json)
    {

        Gson gson=new Gson();
        Type type=new TypeToken<ChatMessage>(){}.getType();

        ChatMessage chatMessage=gson.fromJson(json,type);

        return chatMessage;
    }

    public static String convertObjectToJson(ChatMessage chatMessage)
    {
        Gson gson=new Gson();
        Type type=new TypeToken<ChatMessage>(){}.getType();

        String json=gson.toJson(chatMessage, type);

        return json;
    }

    public static List<ChatMessage> retrieveListfromJson(String json)
    {
        Gson gson=new Gson();
        Type type=new TypeToken<List<ChatMessage>>(){}.getType();

        List<ChatMessage> chatMessageList=gson.fromJson(json, type);

        return chatMessageList;
    }

    public static String convertListToJson(List<ChatMessage> chatMessageList)
    {
        Gson gson=new Gson();
        Type type=new TypeToken<List<ChatMessage>>(){}.getType();

        String json=gson.toJson(chatMessageList,type);

        return json;
    }


}

