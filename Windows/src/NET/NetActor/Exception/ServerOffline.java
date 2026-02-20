package NET.NetActor.Exception;

public class ServerOffline extends IllegalStateException {
    public ServerOffline(){super("Server not online");}
}
