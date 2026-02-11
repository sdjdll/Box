package NET.DEVICES;

public class Windows implements NetDevices {

    @Override
    public boolean syncingClip() {
        return false;
    }

    @Override
    public boolean syncingNotification() {
        return false;
    }
}
