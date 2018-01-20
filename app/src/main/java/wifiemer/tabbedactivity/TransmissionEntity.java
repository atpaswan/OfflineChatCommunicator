package wifiemer.tabbedactivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.*;
/**
 * Created by Atul on 10/27/2017.
 */
public class TransmissionEntity {

    String TransmissionType;
    String objectJson;

    public TransmissionEntity(String transmissionType, String objectJson) {
        TransmissionType = transmissionType;
        this.objectJson = objectJson;
    }

    public String getObjectJson() {
        return objectJson;
    }

    public void setObjectJson(String objectJson) {
        this.objectJson = objectJson;
    }

    public String getTransmissionType() {
        return TransmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        TransmissionType = transmissionType;
    }

    public static TransmissionEntity transEntityRetriever(String strTransEntity)
    {
        Gson gson=new Gson();
        Type type=new TypeToken<TransmissionEntity>(){}.getType();

        TransmissionEntity transmissionEntity=gson.fromJson(strTransEntity,TransmissionEntity.class);

        return transmissionEntity;
    }

    public static String  transEntityGenerator(TransmissionEntity transmissionEntity)
    {

        Gson gson=new Gson();
        Type type=new TypeToken<TransmissionEntity>(){}.getType();

        String jSon=gson.toJson(transmissionEntity, type);

        return jSon;

    }

    public static String InitGenerator(String macId,String usageId,String channelDirection,String SEQ)
    {

        InitRequest initRequest=new InitRequest(macId,usageId,channelDirection,SEQ);
        Gson gson=new Gson();
        Type type=new TypeToken<InitRequest>(){}.getType();

        String jSon=gson.toJson(initRequest, type);

        return jSon;

    }

    public static InitRequest InitRetriever(String jSon)
    {

        Gson gson=new Gson();
        Type type=new TypeToken<InitRequest>(){}.getType();

        InitRequest initRequest=gson.fromJson(jSon, type);

        return initRequest;
    }

    public static AckRequest AckRetriever(String jSon)
    {
        Gson gson=new Gson();
        Type type=new TypeToken<AckRequest>(){}.getType();

        AckRequest ackRequest=gson.fromJson(jSon,type);

        return ackRequest;

    }

    public static String AckGenerator(String macId,String usageId,String SEQ,String response)
    {

        AckRequest ackRequest=new AckRequest(macId,usageId,SEQ,response);
        Gson gson=new Gson();
        Type type=new TypeToken<AckRequest>(){}.getType();

        String jSon=gson.toJson(ackRequest, type);

        return jSon;
    }

    public static String MessageCapsuleGenerator(MessageCapsule messageCapsule)
    {

        Gson gson=new Gson();
        Type type=new TypeToken<MessageCapsule>(){}.getType();

        String json=gson.toJson(messageCapsule,type);

        return json;
    }

    public static MessageCapsule MessageCapsuleRetriever(String jSon)
    {
        Gson gson=new Gson();
        Type type=new TypeToken<MessageCapsule>(){}.getType();

        MessageCapsule messageCapsule=gson.fromJson(jSon,type);

        return messageCapsule;
    }
}
