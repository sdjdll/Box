package NET.NetActor;

import LOG.LogLevel;
import LOG.Logger;
import Main.Server;
import NET.NetActor.Exception.ClientAdvanceDisconnect;
import NET.NetActor.Exception.NetDataCorruption;
import NET.NetBase;
import NET.NetData;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static NET.NetBase.LOG;
import static NET.NetBase.Tag;

public class NetReceiver {
    private final Logger logger = new Logger(LOG, this);
    private final Socket ClientSocket;
    private int ErrorTime = 0;
    private InputStream NetInputStream;

    public NetReceiver(Socket ClientSocket){
        this.ClientSocket = ClientSocket;
        try{
            this.NetInputStream = this.ClientSocket.getInputStream();
        } catch (IOException e) {
            this.logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "ClientSocket Connect Error", e);
        }
    }

    public byte[] ReceiveSource() {
        try{
            return this.NetInputStream.readAllBytes();
        }catch (IOException e){
            this.logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "ClientSocket Disconnect", e);
            ErrorTime++;
            if (ErrorTime > NetBase.MAX_ERROR_TIME) Disconnect();
            return new byte[0];
        }
    }

    public NetData Receive(){
        byte[] buffer = new byte[0];
        try{
            buffer = this.NetInputStream.readAllBytes();
        } catch (IOException e) {
            this.logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "ClientSocket connector error", e);
        }
        try{
            return NetBase.getNetData(buffer);
        } catch (NetDataCorruption e) {
            this.logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "Data not Complete", e);
        } catch (ClientAdvanceDisconnect e) {
            this.logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "Transmission interrupted");
        }
        return new NetData();
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