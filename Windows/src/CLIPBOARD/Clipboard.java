package CLIPBOARD;

import CLIPBOARD.Type.Img;
import CLIPBOARD.Type.Text;
import LOG.LogLevel;
import LOG.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class Clipboard implements Text, Img{
    private String ClipObject;
    private ClipType Type;
    private Logger logger;

    private final Object SyncLock = new Object();
    public Clipboard(){
        logger = new Logger(ClipBase.ClipLog,this);
        ClipObject = null;
        Type = ClipType.UNKNOW;
    }


    @Override
    public byte[] getImg() {
        synchronized (SyncLock){
            if (Type == ClipType.IMG) return Base64.getDecoder().decode(ClipObject);
            else return null;
        }
    }
    @Override
    public void putImg(byte[] img) {
        synchronized (SyncLock){
            ClipObject = Base64.getEncoder().encodeToString(img);
            Type = ClipType.IMG;
        }
    }


    @Override
    public String getText() {
        synchronized (SyncLock){
            if (Type == ClipType.TEXT) return ClipObject;
            else return null;
        }
    }
    @Override
    public void putText(@NotNull String text) {
        synchronized (SyncLock){
            ClipObject = text;
            Type = ClipType.TEXT;
        }
    }
    
    public String Sync(String base64OrString){
        return switch (Type){
            case IMG -> Base64.getEncoder().encodeToString(syncImg(Base64.getDecoder().decode(base64OrString)));
            case TEXT -> syncText(base64OrString);
            case UNKNOW -> null;
        };
    }
    
    private byte[] syncImg(byte[] Img){
        synchronized (SyncLock){
            putImg(Img);
            return getImg();
        }
    }
    private String syncText(String text){
        putText(text);
        return getText();
    }
}
