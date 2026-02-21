package NET;

import Base.ErrorCode;
import LOG.LogLevel;
import LOG.Logger;
import NET.NetActor.Exception.ServerOffline;
import NET.NetActor.NetDevice;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;

import static NET.NetBase.PROT;
import static NET.NetBase.LOG;
import static NET.NetBase.Tag;

public class Net {
    public final LinkedHashMap<InetAddress,NetDevice> netDevices = new LinkedHashMap<>();

    private final Logger logger = new Logger(LOG,this);
    private ServerSocket MainNet = null;

    private final Thread ConnectorListener = new Thread(()->{
        while (true){
            if (this.MainNet == null) {
                this.logger.printAndWrite(LogLevel.FATAL, new Tag.ServerSocket(), "Server Not Online", new ServerOffline());
                System.exit(ErrorCode.Net.NetworkError);
            }
            try {
                Socket client = MainNet.accept();
                this.netDevices.put(client.getInetAddress(), new NetDevice(client));
            } catch (IOException e) {
                this.logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "Client Connect Failed", e);
            }
        }
    });

    public Net(){
        try{
            this.MainNet = new ServerSocket(PROT);
        } catch (IOException e) {
            this.logger.printAndWrite(LogLevel.ERROR, new Tag.ServerSocket(), "Server Socket Failed", e);
        }
    }

    public Thread ListenerStart(){
        this.ConnectorListener.start();
        return this.ConnectorListener;
    }
}
