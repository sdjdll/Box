package sdjini.box.net.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static sdjini.box.net.NetInfo.UDPPROT;
import static sdjini.box.net.StaticNetCore.DevicesList;

public class UdpCore {
    private final DatagramSocket Ds;
    private final DatagramPacket Dp;

    public UdpCore(int port) throws SocketException {
        Ds = new DatagramSocket(UDPPROT);
        Dp = new DatagramPacket(new byte[0], 0);
    }

    public void send() throws IOException {
        Dp.setPort(UDPPROT);
        for (InetAddress device : DevicesList){
            Dp.setAddress(device);
            Ds.send(Dp);
        }
    }
    public void receive() throws IOException {
        Ds.receive(Dp);
        DevicesList.add(Ds.getInetAddress());
    }
}
