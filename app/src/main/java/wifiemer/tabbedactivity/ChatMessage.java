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


/**
 * Created by Atul on 6/23/2017.
 */
public class ChatMessage {

    String macID;
    String datatype;
    byte[] data;
    String message;
    ReadCondition readCondition;
    String timestamp;
    char chatType;

    public ChatMessage(String macID, String datatype, byte[] data, String message, ReadCondition readCondition, String timestamp, char chatType) {
        this.macID = macID;
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
        String DbQuery="INSERT INTO CHATMESSAGE VALUES(?,?,?,?,?,?,?)";

        String str_readCondition="";

        if(chatMessage.getReadCondition()==ReadCondition.NOT_SENT)
            str_readCondition="NOT_SENT";
        else if(chatMessage.getReadCondition()==ReadCondition.SENT)
            str_readCondition="SENT";
        else
        str_readCondition="SEEN";


        try {
            sqLiteDatabase.execSQL(DbQuery, new Object[]{chatMessage.getMacID(), chatMessage.getDatatype(), chatMessage.getData(), chatMessage.getMessage(), str_readCondition, chatMessage.getTimestamp(), chatMessage.getChatType()});
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ChatMessage: insertIntoDatabase failed ");
            return false;
        }



    }

    public static List<ChatMessage> getChatMessageList(String query,Context context)
    {
        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,Context.MODE_PRIVATE,null);
        Cursor chatMessageCursor=sqLiteDatabase.rawQuery(query,null);

        chatMessageCursor.moveToFirst();

        System.out.println("ChatMessageCursor count "+chatMessageCursor.getCount());

        List<ChatMessage> chatMessageList=new ArrayList<ChatMessage>();

        while(!chatMessageCursor.isAfterLast())
        {
            String str_readCondition=chatMessageCursor.getString(4);
            ReadCondition readCondition;
            if(str_readCondition.equals("SENT"))
                readCondition=ReadCondition.SENT;
            else if(str_readCondition.equals("NOT_SENT"))
                readCondition=ReadCondition.NOT_SENT;
            else
            readCondition=ReadCondition.SEEN;

            ChatMessage chatMessage=new ChatMessage(chatMessageCursor.getString(0),chatMessageCursor.getString(1),chatMessageCursor.getBlob(2),chatMessageCursor.getString(3),readCondition,chatMessageCursor.getString(5),chatMessageCursor.getString(6).charAt(0));
            chatMessageList.add(chatMessage);

            chatMessageCursor.moveToNext();

        }


        chatMessageCursor.close();
        sqLiteDatabase.close();

        return chatMessageList;



    }


}

