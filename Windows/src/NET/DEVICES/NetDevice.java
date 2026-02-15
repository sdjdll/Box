package NET.DEVICES;

import CLIPBOARD.ClipType;
import CLIPBOARD.Clipboard;
import LOG.LogLevel;
import LOG.Logger;
import Main.Server;
import NET.NetBase;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
        this.ListenerThread = new Thread(this::syncFrom);
        try{
            this.NetDevice_In = socket.getInputStream();
            this.NetDevice_Out = socket.getOutputStream();
        } catch (IOException e) {
            this.logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.NetDevice(), "Connection Interrupted.", e);
        }

        this.ListenerThread.start();
    }

    public void SyncTo(@NotNull Clipboard clipboard){
        switch (clipboard.getType()) {
            case TEXT -> SyncTextTo(clipboard.getText());
            case IMG -> SyncImgTo(clipboard.getImg());
        }
    }
    private void SyncTextTo(@NotNull String text){
        byte[] temp = text.getBytes();
        byte[] data = new byte[temp.length+8];
        byte[] buffer = new byte[NetBase.PACKAGE_SIZE];
        System.arraycopy(NetBase.IdenData.TEXT,0,data,0,4);
        System.arraycopy(temp,0,data,4,temp.length);
        System.arraycopy(NetBase.IdenData.END,0,data,temp.length+4,4);
        try {
            for (int i = 0; i < data.length; i += NetBase.PACKAGE_SIZE) {
                Arrays.fill(buffer,(byte) 0x0);
                System.arraycopy(data,i,buffer,0, Math.min(NetBase.PACKAGE_SIZE, data.length - i));
                NetDevice_Out.write(buffer);
                NetDevice_Out.flush();
            }
        } catch (IOException e) {
            this.logger.printAndWrite(LogLevel.ERROR, new NET.NetBase.Tags.ClientSocket(), "Sending Text Failed.", e);
            this.ErrorTime++;
            if (this.ErrorTime >= NetDeviceBase.MAX_ERROR_TIME){
                Server.net.removeConnect(this.ip);
                try {
                    this.socket.close();
                } catch (IOException ex) {
                    this.logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.ClientSocket(), "Connection Close Failed.", e);
                }
                this.ListenerThread.interrupt();
            }
        }
    }
    private void SyncImgTo(byte[] img){}

    private void syncFrom(){
        while (true){
            int size = 0;
            if (Thread.currentThread().isInterrupted()) return;
            byte[] buffer = new byte[NetBase.PACKAGE_SIZE];
            try {
                size = this.NetDevice_In.read(buffer, 0, NetBase.PACKAGE_SIZE);
            } catch (IOException e) {
                this.logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.NetDevice(), "Connection Interrupted.", e);
                this.ErrorTime++;
                if (this.ErrorTime >= NetDeviceBase.MAX_ERROR_TIME) {
                    Server.net.removeConnect(this.ip);
                    try {
                        this.socket.close();
                        return;
                    } catch (IOException ex) {
                        this.logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.ClientSocket(), "Connection Close Failed.", e);
                        return;
                    }
                }
            }

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
        this.ND_Data = new String(date, StandardCharsets.UTF_8);
        this.ND_Type = ClipType.TEXT;
        MainClipboard.putText(this.ND_Data);
    }
    private void img(byte[] buffer){
        byte[] date = Arrays.copyOfRange(buffer,4,buffer.length);
        ND_Data = new String(date, StandardCharsets.UTF_8);
        ND_Type = ClipType.IMG;
        MainClipboard.putImg(ND_Data.getBytes());
    }
    private void add(byte[] buffer,int size){
        byte[] data = new byte[size];
        System.arraycopy(buffer, 0, data, 0, size);
        ND_Data += new String(data, StandardCharsets.UTF_8);
    }

    public Clipboard getDateAsClip(){
        return new Clipboard(this.ND_Data,this.ND_Type);
    }
}
