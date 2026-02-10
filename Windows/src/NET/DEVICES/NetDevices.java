package NET.DEVICES;

import java.util.LinkedList;

public interface NetDevices {
    LinkedList<NetDevices> LIST__NET_DEVICES = new LinkedList<>();
    boolean syncingClip();
    boolean syncingNotification();
    default boolean syncingAll(){
        return syncingClip() && syncingNotification();
    }
}
