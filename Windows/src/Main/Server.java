package Main;

import CLIPBOARD.Clipboard;
import LOG.Logger;
import NET.Net;
import NET.NetActor.NetDevice;
import NET.NetData;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static final Clipboard MainClipboard = new Clipboard();
    public static final Net net = new Net();

    private final Logger logger = new Logger(ServerBase.MainLog, this);
    public static void main(String[] args) {
        Thread netThread = net.ListenerStart();
        new Thread(()->{
            List<InetAddress> Address = new ArrayList<>();
            NetData netData;
            while (true){
                Address.addAll(net.netDevices.keySet());
                for (InetAddress ia : Address){
                    NetDevice netDevice = net.netDevices.get(ia);
                    if (netDevice.haveMessage) {
                        netData = netDevice.ReceiveMessage();
                        MainClipboard.putClip(netData);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _) {}
            }
        });
        new Thread(()->{
            List<InetAddress> Address = new ArrayList<>();
            NetData netData;
            while (true){
                if (MainClipboard.update){
                    Address.addAll(net.netDevices.keySet());
                    netData = MainClipboard.getClip();
                    for (InetAddress ia : Address) net.netDevices.get(ia).SendMessage(netData);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _) {}
            }
        }).start();
    }
}
