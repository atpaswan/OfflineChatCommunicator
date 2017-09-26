package wifiemer.tabbedactivity;

/**
 * Created by Atul on 9/4/2017.
 */
import java.io.Serializable;

/**
 * Created by Atul on 9/2/2017.
 */
public class SendingCode implements Serializable{

    String writeString;
    char replacementCode;
    int FieldNumber;
    int CodeNumber;
    String FieldSeparator="";

    public int getCodeNumber() {
        return CodeNumber;
    }

    public void setCodeNumber(int codeNumber) {
        CodeNumber = codeNumber;
    }

    public SendingCode(int fieldNumber, char replacementCode, String writeString,int codeNumber,String fieldSeparator) {
        FieldNumber = fieldNumber;
        this.replacementCode = replacementCode;
        this.writeString = writeString;
        CodeNumber=codeNumber;
        FieldSeparator=fieldSeparator;
    }

    public String getFieldSeparator() {
        return FieldSeparator;
    }

    public void setFieldSeparator(String fieldSeparator) {
        FieldSeparator = fieldSeparator;
    }

    public String getWriteString() {
        return writeString;
    }

    public void setWriteString(String writeString) {
        this.writeString = writeString;
    }

    public char getReplacementCode() {
        return replacementCode;
    }

    public void setReplacementCode(char replacementCode) {
        this.replacementCode = replacementCode;
    }

    public int getFieldNumber() {
        return FieldNumber;
    }

    public void setFieldNumber(int fieldNumber) {
        FieldNumber = fieldNumber;
    }
}
