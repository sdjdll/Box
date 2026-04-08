package sdjini.box.net.net;

import sdjini.box.net.NetInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class TcpCore {
    private ServerSocket socket;
    public TcpCore() throws IOException {
        socket = new ServerSocket(NetInfo.TCPPROT);
    }
    public Socket touch() throws IOException {
        return socket.accept();
    }
    public void send(Socket socket, byte[] data, int length) throws IOException {
        OutputStream os = socket.getOutputStream();
        byte[] temp = ByteBuffer.allocate(4).putLong(length).array();
        // 先传长度再传数据
        os.write(temp);
        os.flush();
        os.write(data);
        os.flush();
    }
    public byte[] receive(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        // 先接长度后接数据
        byte[] temp = is.readNBytes(4);
        int len = ByteBuffer.wrap(temp).getInt();
        temp = is.readNBytes(len);
        return temp;
    }
}
