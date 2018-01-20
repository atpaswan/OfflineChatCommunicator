package wifiemer.tabbedactivity;

/**
 * Created by Atul on 10/21/2017.
 */
public enum MessageDirection {

    INCOMING,OUTGOING;

    public static String getStringOfMessageDirection(MessageDirection messageDirection)
    {
        if(messageDirection.equals(MessageDirection.INCOMING))
            return "INCOMING";
        else
            if(messageDirection.equals(MessageDirection.OUTGOING))
                return "OUTGOING";
        else
                return "DEFAULT";
    }
}
