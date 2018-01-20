package wifiemer.tabbedactivity;

/**
 * Created by Atul on 10/28/2017.
 */
public class AckRequest {

    String macId;
    String usageId;
    String SEQ;
    String response;

    public AckRequest(String macId, String usageId, String SEQ,String response) {
        this.macId = macId;
        this.usageId = usageId;
        this.SEQ = SEQ;
        this.response=response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String SEQ) {
        this.SEQ = SEQ;
    }

    public String getUsageId() {
        return usageId;
    }

    public void setUsageId(String usageId) {
        this.usageId = usageId;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }
}
