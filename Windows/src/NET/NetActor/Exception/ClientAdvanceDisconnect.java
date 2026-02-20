package NET.NetActor.Exception;

import java.io.IOError;
import java.io.IOException;

public class ClientAdvanceDisconnect extends IOException {
    public ClientAdvanceDisconnect() {
        super("Client Advance Disconnect");
    }
}
