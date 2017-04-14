package wifiemer.tabbedactivity;

/**
 * Created by Atul on 4/15/2017.
 */
class Alias
{
    int image_id;
    String broadcastID;
    String UserName;

    public Alias(int image_id, String broadcastID, String userName) {
        this.image_id = image_id;
        this.broadcastID = broadcastID;
        UserName = userName;
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
