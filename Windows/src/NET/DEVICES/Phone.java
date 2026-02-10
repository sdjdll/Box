package NET.DEVICES;

public class Phone implements NetDevices{
    public Phone(){
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
