package NET.DEVICES;

import CLIPBOARD.ClipType;
import CLIPBOARD.Clipboard;
import LOG.LogLevel;
import LOG.Logger;
import NET.NetBase;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import static Main.Server.MainClipboard;

public class NetDevice {

    private final Logger logger;
    private final InetAddress ip;
    private final Socket socket;
    private final Thread ListenerThread;
    private int ErrorTime;
    private InputStream NetDevice_In;
    private OutputStream NetDevice_Out;
    private String ND_Data;
    private ClipType ND_Type;
    public NetDevice(@NotNull Socket socket) {

        this.logger = new Logger(NetBase.File_NetLog,this);
        this.socket = socket;
        this.ip = socket.getInetAddress();
        this.ErrorTime = 0;
        this.ListenerThread = new Thread(this::sync);
        try{
            NetDevice_In = socket.getInputStream();
            NetDevice_Out = socket.getOutputStream();
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.NetDevice(), "Connection Interrupted.", e);
        }

        this.ListenerThread.start();
    }

    private void sync(){
        while (true){
            byte[] buffer = new byte[NetBase.PACKAGE_SIZE];
            try {
                int _ = NetDevice_In.read(buffer, 0, NetBase.PACKAGE_SIZE);
            } catch (IOException e) {
                logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.NetDevice(), "Connection Interrupted.", e);
                ErrorTime++;
            }

            int size = 0;
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);

            for (; size < NetBase.PACKAGE_SIZE - 4; size += 4)
                if (byteBuffer.getInt(size) == NetBase.Identifier.END) break;

            switch ((buffer[0] & 0xFF) << 24 | (buffer[1] & 0xFF) << 16 | (buffer[2] & 0xFF) << 8 | (buffer[3] & 0xFF)) {
                case NetBase.Identifier.TEXT -> text(buffer);
                case NetBase.Identifier.IMG -> img(buffer);
                default -> add(buffer, size);
            }
        }
    }
    private void text(byte[] buffer){
        byte[] date = Arrays.copyOfRange(buffer,4,buffer.length);
        ND_Data = new String(date, StandardCharsets.UTF_8);
        MainClipboard.putText(ND_Data);
    }
    private void img(byte[] buffer){
        byte[] date = Arrays.copyOfRange(buffer,4,buffer.length);
        ND_Data = new String(date, StandardCharsets.UTF_8);
        MainClipboard.putImg(ND_Data.getBytes());
    }
    private void add(byte[] buffer,int size){
        byte[] data = new byte[size];
        System.arraycopy(buffer, 0, data, 0, size);
        ND_Data += new String(buffer, StandardCharsets.UTF_8);
    }

    public Clipboard getDateAsClip(){
        return new Clipboard(this.ND_Data,this.ND_Type);
    }
}
