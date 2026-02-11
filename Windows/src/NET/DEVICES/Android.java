package NET.DEVICES;


public class Android implements NetDevices {
    public Android(){
        NetDevices.LIST__NET_DEVICES.add(this);
    }

    @Override
    public boolean syncingClip() {
        return false;
    }

    @Override
    public boolean syncingNotification() {
        return false;
    }
}
