package wifiemer.tabbedactivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 6/23/2017.
 */
public class BroadCastMessage {

    String MacID;
    String Message;
    String timestamp;
    String unEncString;

    public BroadCastMessage(String macID, String message, String timestamp,String unEncString) {
        MacID = macID;
        Message = message;
        this.timestamp = timestamp;
        this.unEncString=unEncString;
    }

    public String getUnEncString() {
        return unEncString;
    }

    public void setUnEncString(String unEncString) {
        this.unEncString = unEncString;
    }

    public String getMacID() {
        return MacID;
    }

    public void setMacID(String macID) {
        MacID = macID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static boolean insertIntoDatabase(BroadCastMessage broadCastMessage,Context context)
    {

        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,Context.MODE_PRIVATE,null);
        String DbQuery="INSERT INTO BROADCASTMESSAGE VALUES(?,?,?,?)";

        try {
            sqLiteDatabase.execSQL(DbQuery,new Object[]{broadCastMessage.getMacID(),broadCastMessage.getMessage(),broadCastMessage.getTimestamp(),broadCastMessage.getUnEncString()});
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("BroadcastMessage: insertIntoDatabase failed ");
            return false;
        }



    }


    public static List<BroadCastMessage> getBroadCastList(String query,Context context)
    {
        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,Context.MODE_PRIVATE,null);
        Cursor broadCastMessageCursor=sqLiteDatabase.rawQuery(query,null);

        broadCastMessageCursor.moveToFirst();

        List<BroadCastMessage> broadCastMessageList=new ArrayList<BroadCastMessage>();

        while(!broadCastMessageCursor.isAfterLast())
        {
            BroadCastMessage broadCastMessage=new BroadCastMessage(broadCastMessageCursor.getString(0),broadCastMessageCursor.getString(1),broadCastMessageCursor.getString(2),broadCastMessageCursor.getString(3));
            broadCastMessageList.add(broadCastMessage);

            broadCastMessageCursor.moveToNext();

        }


        broadCastMessageCursor.close();
        sqLiteDatabase.close();

        return broadCastMessageList;



    }
}
