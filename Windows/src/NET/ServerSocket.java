package NET;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ServerSocket extends java.net.ServerSocket {
    public ServerSocket(@NotNull NetPort port) throws IOException {
        super(port.getProt());
    }
}
