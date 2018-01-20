package wifiemer.tabbedactivity;

import java.io.Serializable;

/**
 * Created by Atul on 10/28/2017.
 */
public class InitRequest implements Serializable {
    String macId;
    String usageId;
    String channelDirection;
    String SEQ;

    public InitRequest(String macId, String usageId,String channelDirection,String SEQ) {
        this.macId = macId;
        this.usageId = usageId;
        this.channelDirection=channelDirection;
        this.SEQ=SEQ;
    }

    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String SEQ) {
        this.SEQ = SEQ;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getUsageId() {
        return usageId;
    }

    public void setUsageId(String usageId) {
        this.usageId = usageId;
    }

    public String getChannelDirection() {
        return channelDirection;
    }

    public void setChannelDirection(String channelDirection) {
        this.channelDirection = channelDirection;
    }
}
