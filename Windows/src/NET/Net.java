package NET;

import LOG.LogLevel;
import LOG.Logger;
import NET.NetActor.Exception.ServerOffline;
import NET.NetActor.NetDevice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import static NET.NetBase.PROT;
import static NET.NetBase.LOG;
import static NET.NetBase.Tag;

public class Net {
    private final Logger logger = new Logger(LOG,this);
    private ServerSocket MainNet = null;
    private final LinkedList<NetDevice> netDevices = new LinkedList<>();

    private final Thread ConnectorListener = new Thread(()->{
        if (MainNet == null){
            logger.printAndWrite(LogLevel.FATAL,new Tag.ServerSocket(),"Server Not Online", new ServerOffline());
            throw new ServerOffline();
        }
        try{
            netDevices.add(new NetDevice(MainNet.accept()));
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "Client Connect Failed", e);
        }
    });

    public Net(){
        try{
            MainNet = new ServerSocket(PROT);
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.ERROR, new Tag.ServerSocket(), "Server Socket Failed", e);
        }

    }
}
