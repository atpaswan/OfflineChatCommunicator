package wifiemer.tabbedactivity;

import java.io.Serializable;

/**
 * Created by Atul on 9/4/2017.
 */
public class SendingCodes implements Serializable {
    SendingType[] typeArr;

    public SendingCodes(SendingType[] typeArr) {
        this.typeArr = typeArr;
    }

    public SendingType[] getTypeArr() {
        return typeArr;
    }

    public void setTypeArr(SendingType[] typeArr) {
        this.typeArr = typeArr;
    }
}
