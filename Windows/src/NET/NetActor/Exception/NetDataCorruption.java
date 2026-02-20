package NET.NetActor.Exception;

import java.io.IOException;

public class NetDataCorruption extends IOException {
    public NetDataCorruption(String message) {
        super(message);
    }
    public NetDataCorruption(){
        super("Net Data Corruption");
    }
}
