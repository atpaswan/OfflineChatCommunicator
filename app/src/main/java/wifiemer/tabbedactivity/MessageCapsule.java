package wifiemer.tabbedactivity;

import java.io.Serializable;

/**
 * Created by Atul on 10/6/2017.
 */
public class MessageCapsule implements Serializable {

    String macID;
    String message;
    String type;
    Byte[] rawData;
    String SEQ;
    int partNum;
    int totParts;

    public MessageCapsule(String macID, String message, String type, Byte[] rawData,String SEQ,int partNum,int totParts) {
        this.macID = macID;
        this.message = message;
        this.type = type;
        this.rawData = rawData;
        this.SEQ=SEQ;
        this.partNum=partNum;
        this.totParts=totParts;
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
}
