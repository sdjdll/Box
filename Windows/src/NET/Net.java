package NET;

import LOG.LogLevel;
import LOG.Logger;

import java.io.IOException;
import java.net.ServerSocket;

import static NET.NetBase.PROT;
import static NET.NetBase.LOG;
import static NET.NetBase.Tag;

public class Net {
    protected ServerSocket MainNet;
    private final Logger logger = new Logger(LOG,this);

    public Net(){
        try{
            MainNet = new ServerSocket(PROT);
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.ERROR, new Tag.ServerSocketFailed(), "", e);
        }
    }
}
