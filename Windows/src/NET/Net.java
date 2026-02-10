package NET;

import LOG.LogLevel;
import LOG.Logger;

import java.io.IOException;

public class Net {
    private ServerSocket ServerSocket;
    private Logger logger;
    public Net() {
        logger = new Logger(NetBase.File_NetLog,this);

        try{
            ServerSocket = new ServerSocket(NetBase.NetProt_TransIn);
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.FATAL,"");
        }
    }
}
