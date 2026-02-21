package NET.NetActor;

import LOG.LogLevel;
import LOG.Logger;
import NET.NetBase;
import NET.NetData;

import java.net.Socket;

public class NetDevice {
    public boolean haveMessage = false;

    private final NetReceiver netReceiver;
    private final NetSender netSender;
    private final Object Lock = new Object();
    private final Logger logger = new Logger(NetBase.LOG, this);

    private NetData DeviceData;
    public NetDevice(Socket DeviceSocket) {
        this.netReceiver = new NetReceiver(DeviceSocket);
        this.netSender = new NetSender(DeviceSocket);
        new Thread(()->{
            this.logger.printAndWrite(LogLevel.INFO,new NetBase.Tag.NetDevice(DeviceSocket.getInetAddress()),"");
            while (true){
                synchronized (this.Lock){
                    this.DeviceData = this.netReceiver.Receive();
                    this.haveMessage = true;
                    try {
                        this.Lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    this.haveMessage = false;
                }
            }
        }).start();
    }

    public NetData ReceiveMessage(){
        if (haveMessage) return this.DeviceData;
        return new NetData();
    }

    public boolean SendMessage(NetData netData){
        return netSender.send(netData);
    }
}
