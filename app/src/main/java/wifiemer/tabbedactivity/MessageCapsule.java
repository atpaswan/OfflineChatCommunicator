package wifiemer.tabbedactivity;

/**
 * Created by Atul on 10/6/2017.
 */
public class MessageCapsule {

    String macID;
    String message;
    String type;
    Byte[] rawData;

    public MessageCapsule(String macID, String message, String type, Byte[] rawData) {
        this.macID = macID;
        this.message = message;
        this.type = type;
        this.rawData = rawData;
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

    public Byte[] getRawData() {
        return rawData;
    }

    public void setRawData(Byte[] rawData) {
        this.rawData = rawData;
    }
}
