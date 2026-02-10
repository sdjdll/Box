package NET;

import Base.ErrorCode;
import LOG.LogLevel;
import LOG.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;

public class Net {
    private ServerSocket ServerSocket;
    private int connectingDevices = 0;
    private final HashMap<Integer,Socket> DeviceSocket = new HashMap<>();
    private final Logger logger;
    private final Object NetLock = new Object();

    public Net() {
        logger = new Logger(NetBase.File_NetLog,this);

        try{
            ServerSocket = new ServerSocket(NetBase.NetProt_TransIn);
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.FATAL,new NetBase.Tags.ServerSocket(), "Create ServerSocket Failed!",e);
            System.exit(ErrorCode.Net.CreateSocketFailed);
        }

        Thread netDevicesThread = new Thread(()->{
            Socket socket = DeviceSocket.get(connectingDevices);
            try {
                OutputStream socketOutput = socket.getOutputStream();
                socket.setKeepAlive(true);
                socketOutput.write(connectingDevices);
                socketOutput.flush();
            } catch (SocketException e) {
                logger.printAndWrite(LogLevel.FATAL, new NetBase.Tags.Network(), "Connection Error", e);
            } catch (IOException e) {
                logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.Network(), "Failed to send message", e);
            }
        });
        Thread netMainThread = new Thread(()->{
            try {
                while (true) {
                    connectingDevices++;
                    synchronized (NetLock){
                        DeviceSocket.put(connectingDevices, ServerSocket.accept());
                        logger.printAndWrite(LogLevel.INFO, new NetBase.Tags.Network(), "Device Connected!");
                        netDevicesThread.start();
                        NetLock.wait();
                    }
                }
            } catch (IOException e) {
                logger.printAndWrite(LogLevel.FATAL, new NetBase.Tags.Network(), "Connection Error", e);
            } catch (InterruptedException e) {
                logger.printAndWrite(LogLevel.INFO, new NetBase.Tags.Network(),"interruption");
            }
        });
        netMainThread.start();
    }
}
