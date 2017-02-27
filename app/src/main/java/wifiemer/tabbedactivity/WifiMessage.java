package wifiemer.tabbedactivity;

/**
 * Created by Atul on 2/27/2017.
 */
public class WifiMessage {

    int icon_id;
    String WifiName;

    public WifiMessage(String wifiName, String lastMessage, int icon_id) {
        WifiName = wifiName;
        LastMessage = lastMessage;
        this.icon_id = icon_id;
    }

    String LastMessage;

    public int getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }

    public String getWifiName() {
        return WifiName;
    }

    public void setWifiName(String wifiName) {
        WifiName = wifiName;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }
}
