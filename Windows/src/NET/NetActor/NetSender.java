package NET.NetActor;

import LOG.LogLevel;
import LOG.Logger;
import Main.Server;
import NET.Net;
import NET.NetActor.Exception.NetDataCorruption;
import NET.NetBase;
import NET.NetData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class NetSender {
    private final Logger logger = new Logger(NetBase.LOG, this);
    private final Socket ClientSocket;
    private final OutputStream NetOutputStream;
    private int ErrorTime = 0;

    public NetSender(@NotNull Socket ClientSocket) {
        this.ClientSocket = ClientSocket;
        OutputStream os;
        try {
            os = this.ClientSocket.getOutputStream();
        } catch (IOException e) {
            os = null;
            logger.printAndWrite(LogLevel.ERROR, new NetBase.Tag.ClientSocket(), "Cannot Send data", e);
        }
        NetOutputStream = os;
    }

    public boolean send(@NotNull NetData nd){
        byte[] Identifier, Length, Data, buffer;
        try {
            Identifier = switch (nd.identifier) {
                case NetBase.Identifier.ClipText -> NetBase.Identifier.CLIP_TEXT;
                case NetBase.Identifier.ClipImg -> NetBase.Identifier.CLIP_IMG;
                case NetBase.Identifier.FileAny -> NetBase.Identifier.FILE_ANY;
                default -> throw new NetDataCorruption();
            };
            Length = NetBase.int2Bytes(nd.length);
            Data = nd.data;
            buffer = new byte[nd.length + 12];
            System.arraycopy(Identifier, 0, buffer, 0, 4);
            System.arraycopy(Length, 0, buffer, 4, 4);
            System.arraycopy(Data, 0, buffer, 8, nd.length);
            System.arraycopy(NetBase.Identifier.END, 0, buffer, nd.length + 8, 4);
            NetOutputStream.write(buffer);
            NetOutputStream.flush();
        } catch (NetDataCorruption e) {
            logger.printAndWrite(LogLevel.ERROR, new NetBase.Tag.ClientSocket(), "Data Identifier Unknow", e);
            return false;
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.ERROR, new NetBase.Tag.ClientSocket(), "Cannot Send Data Out", e);
            ErrorTime++;
            if (ErrorTime > NetBase.MAX_ERROR_TIME) Disconnect();
            return false;
        }
        ErrorTime = 0;
        return true;
    }

    private void Disconnect(){
        try {
            ClientSocket.close();
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.ERROR, new NetBase.Tag.ClientSocket(), "Close Failed", e);
        } finally {
            Server.net.netDevices.remove(this.ClientSocket.getInetAddress());
        }
    }
}
