package wifiemer.tabbedactivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 10/6/2017.
 */
public class MessageCapsule implements Serializable {

    String macID;
    String message;
    String type;
    byte[] rawData;
    String SEQ;
    int partNum;
    int totParts;
    MessageDirection msgDirection;

    public MessageCapsule(String macID, String message, String type, byte[] rawData,String SEQ,int partNum,int totParts,MessageDirection msgDirection) {
        this.macID = macID;
        this.message = message;
        this.type = type;
        this.rawData = rawData;
        this.SEQ = SEQ;
        this.partNum = partNum;
        this.totParts = totParts;
        this.msgDirection = msgDirection;
    }

    public MessageDirection getMsgDirection() {
        return msgDirection;
    }

    public void setMsgDirection(MessageDirection msgDirection) {
        this.msgDirection = msgDirection;
    }

    public String getMacID() {
        return macID;
    }

    public void setMacID(String macID) {
        this.macID = macID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String SEQ) {
        this.SEQ = SEQ;
    }

    public int getPartNum() {
        return partNum;
    }

    public void setPartNum(int partNum) {
        this.partNum = partNum;
    }

    public int getTotParts() {
        return totParts;
    }

    public void setTotParts(int totParts) {
        this.totParts = totParts;
    }

    public static boolean insertIntoDatabase(MessageCapsule messageCapsule,Context context)
    {

        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase, Context.MODE_PRIVATE,null);
        String DbQuery="INSERT INTO MESSAGECAPSULE VALUES(?,?,?,?,?,?,?,?)";

        try {
            sqLiteDatabase.execSQL(DbQuery,new Object[]{messageCapsule.getClass(),messageCapsule.getMessage(),messageCapsule.getType(),messageCapsule.getRawData(),messageCapsule.getSEQ(),messageCapsule.getPartNum(),messageCapsule.getTotParts(),MessageDirection.getStringOfMessageDirection(messageCapsule.getMsgDirection())});
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("MessageCapsule: insertIntoDatabase failed ");
            return false;
        }
    }

    public static List<MessageCapsule> retrieveCapsule(String MacID,String type,Context context)
    {
        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,Context.MODE_PRIVATE,null);
        Cursor messageCapsuleCursor=sqLiteDatabase.rawQuery("Select * from Messagecapsule where macID='" + MacID + "' and type='" + type + "';", null);

        messageCapsuleCursor.moveToFirst();

        List<MessageCapsule> messageCapsuleList=new ArrayList<MessageCapsule>();

        while(!messageCapsuleCursor.isAfterLast())
        {
            MessageDirection msgDirection=null;
            if(messageCapsuleCursor.getString(7).equals("INCOMING"))
                msgDirection=MessageDirection.INCOMING;
            else
            msgDirection=MessageDirection.OUTGOING;

            MessageCapsule messageCapsule=new MessageCapsule(messageCapsuleCursor.getString(0),messageCapsuleCursor.getString(1),messageCapsuleCursor.getString(2),messageCapsuleCursor.getBlob(3),messageCapsuleCursor.getString(4),messageCapsuleCursor.getInt(5),messageCapsuleCursor.getInt(6),msgDirection);
            messageCapsuleList.add(messageCapsule);

            messageCapsuleCursor.moveToNext();

        }


        messageCapsuleCursor.close();
        sqLiteDatabase.close();

        return messageCapsuleList;

    }
}
