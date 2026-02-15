package NET;

import Base.ErrorCode;
import CLIPBOARD.Clipboard;
import LOG.LogLevel;
import LOG.Logger;
import NET.DEVICES.NetDevice;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class Net {
    private final HashMap<InetAddress,NetDevice> NetDevices = new HashMap<>();
    private ServerSocket Socket_Server;
    private final Logger logger;

    public Net() {
        logger = new Logger(NetBase.File_NetLog, this);

        try{
            Socket_Server = new ServerSocket(NetBase.NetProt_TransIn);
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.FATAL, new NetBase.Tags.ServerSocket(), "Create ServerSocket Failed!", e);
            System.exit(ErrorCode.Net.CreateSocketFailed);
        }

        Thread Connector = new Thread(this::connector);
        Connector.start();
    }

    private void connector(){
        for (;;){

            Socket temp = new Socket();
            try{
                logger.printAndWrite(LogLevel.STEP,new NetBase.Tags.ClientSocket(),"Listening connection.");
                temp = Socket_Server.accept();

                NetDevices.put(temp.getInetAddress(),new NetDevice(temp));
                logger.printAndWrite(LogLevel.INFO,new NetBase.Tags.ClientSocket(),"Have connection.");
            } catch (IOException e) {
                logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.ClientSocket(), "Connect Failed!", e);
                NetDevices.remove(temp.getInetAddress());
                continue;
            }

            try{
                logger.printAndWrite(LogLevel.INFO,new NetBase.Tags.ClientSocket(),"Sending BaseInfo.");
                OutputStream is = temp.getOutputStream();
                is.write(temp.getInetAddress().getAddress());
                is.flush();
            } catch (IOException e) {
                logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.ClientSocket(), "Send BaseInfo Failed, Connect Failed!", e);
            }

        }
    }

    public void removeConnect(InetAddress ip){
        NetDevices.remove(ip);
    }

    public void SyncClip(Clipboard cb){
        if (NetDevices.isEmpty()) return;
        Set<InetAddress> set = NetDevices.keySet();
        for(InetAddress i : set){
            NetDevices.get(i).SyncTo(cb);
        }
    }
}