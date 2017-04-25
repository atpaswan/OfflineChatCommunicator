package wifiemer.tabbedactivity;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;

/**
 * Created by Atul on 4/15/2017.
 */
class Alias implements Serializable
{
    int image_id;
    String imageType;
    String broadcastID; // Broadcast ID is the wifi BSSID in the Alias Object
    String UserName;
    byte[] imageByteArray;

    public Alias(int image_id, String broadcastID, String userName) {
        this.image_id = image_id;
        this.broadcastID = broadcastID;
        UserName = userName;
    }

    public byte[] getImageByteArray() {
        return imageByteArray;
    }

    public void setImageByteArray(byte[] imageByteArray) {
        this.imageByteArray = imageByteArray;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getBroadcastID() {
        return broadcastID;
    }

    public void setBroadcastID(String broadcastID) {
        this.broadcastID = broadcastID;
    }



    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }


}
