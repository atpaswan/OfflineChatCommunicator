package wifiemer.tabbedactivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 6/23/2017.
 */
public class SenderDevice {

    String MacID;
    byte[] imageBytes;
    String aliasName="";

    public SenderDevice(String macID, byte[] imageBytes, String aliasName) {
        MacID = macID;
        this.imageBytes = imageBytes;
        this.aliasName = aliasName;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getMacID() {
        return MacID;
    }

    public void setMacID(String macID) {
        MacID = macID;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public boolean insertIntoDatabase(SenderDevice senderDevice,Context context)
    {
        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase, Context.MODE_PRIVATE,null);
        String DbQuery="INSERT INTO SENDER_DEVICE VALUES(?,?,?)";

        try {
            sqLiteDatabase.execSQL(DbQuery,new Object[]{senderDevice.getMacID(),senderDevice.getImageBytes(),senderDevice.getAliasName()});
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("SenderDevice: insertIntoDatabase failed ");
            return false;
        }
    }

    public static List<SenderDevice> getSenderDeviceList(String query,Context context)
    {
        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase(CommonSettings.appDatabase,Context.MODE_PRIVATE,null);
        Cursor senderDeviceCursor=sqLiteDatabase.rawQuery(query,null);

        senderDeviceCursor.moveToFirst();

        List<SenderDevice> senderDeviceList=new ArrayList<SenderDevice>();

        while(!senderDeviceCursor.isAfterLast())
        {
            SenderDevice senderDevice=new SenderDevice(senderDeviceCursor.getString(0),senderDeviceCursor.getBlob(1),senderDeviceCursor.getString(2));
            senderDeviceList.add(senderDevice);

            senderDeviceCursor.moveToNext();

        }


        senderDeviceCursor.close();
        sqLiteDatabase.close();

        return senderDeviceList;

    }
}
