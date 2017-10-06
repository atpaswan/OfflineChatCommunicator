package wifiemer.tabbedactivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 9/4/2017.
 */
public class SendingType implements Serializable  {
    String TypeString;
    SendingCode[] codeArr;

    public SendingType(String typeString, SendingCode[] codeArr) {
        TypeString = typeString;
        this.codeArr = codeArr;
    }

    public SendingType() {

    }


    public String getTypeString() {
        return TypeString;
    }

    public void setTypeString(String typeString) {
        TypeString = typeString;
    }

    public SendingCode[] getCodeArr() {
        return codeArr;
    }

    public void setCodeArr(SendingCode[] codeArr) {
        this.codeArr = codeArr;
    }

    public static List<SendingType> getSendingTypeList(Context context)
    {
        InputStream is=context.getResources().openRawResource(R.raw.sendingcodeload);
        InputStreamReader inputStreamReader=new InputStreamReader(is);

        String jSon="";
        int ch;
        try {
            while ((ch = inputStreamReader.read()) != -1) {
                jSon += (char) ch;
            }
            Log.d("TAGGER","jSon Read\n" + jSon);
            Gson gson = new Gson();
            Type type = new TypeToken<SendingCodes>() {
            }.getType();
            SendingCodes readCodes = gson.fromJson(jSon, type);

            SendingType[] sendingtypeArr = readCodes.getTypeArr();

            List<SendingType> sendingTypeList = new ArrayList<SendingType>();

            for (int i = 0; i < sendingtypeArr.length; i++)
                sendingTypeList.add(sendingtypeArr[i]);

            inputStreamReader.close();
            is.close();
            return sendingTypeList;

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("jSon read");
            return  null;
        }

    }


}
