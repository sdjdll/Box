package NET.NetActor;

import LOG.LogLevel;
import LOG.Logger;
import NET.Net;
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
    private final Socket Client;
    private InputStream NetInputStream;
    protected final Logger logger = new Logger(LOG, this);

    public NetReceiver(Socket Client){
        this.Client = Client;
        try{
            this.NetInputStream = this.Client.getInputStream();
        } catch (IOException e) {
            this.logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "Client Connect Error", e);
        }
    }

    public byte[] ReceiveSource() {
        try{
            return this.NetInputStream.readAllBytes();
        }catch (IOException e){
            this.logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "Client Disconnect", e);
            return new byte[0];
        }
    }

    public NetData Receive(){
        byte[] buffer = new byte[0];
        try{
            buffer = this.NetInputStream.readAllBytes();
        } catch (IOException e) {
            this.logger.printAndWrite(LogLevel.ERROR, new Tag.ClientSocket(), "Client connector error", e);
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
}