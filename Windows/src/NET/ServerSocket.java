package NET;

import java.io.IOException;

public class ServerSocket extends java.net.ServerSocket {
    public ServerSocket(NetPort port) throws IOException {
        super(port.getProt());
    }
}
