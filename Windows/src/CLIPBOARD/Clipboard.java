package CLIPBOARD;

import LOG.Logger;
import NET.NetBase;
import NET.NetData;
import org.jetbrains.annotations.NotNull;

public class Clipboard{
    public boolean update = false;

    private final Object SyncLock = new Object();
    private final Logger logger = new Logger(ClipBase.ClipLog, this);

    private byte[] ClipObject;
    private ClipType Type;

    public Clipboard(){
        ClipObject = null;
        Type = ClipType.Unknow;
    }

    public Clipboard(byte[] ClipObject, ClipType Type){
        this.ClipObject = ClipObject;
        this.Type = Type;
    }

    public void putClip(@NotNull NetData nd){
        if (nd.data == ClipObject) return;
        update = true;
        synchronized (SyncLock){
            this.ClipObject = nd.data;
            this.Type = switch (nd.identifier) {
                case NetBase.Identifier.ClipText -> ClipType.ClipText;
                case NetBase.Identifier.ClipImg -> ClipType.ClipImg;
                case NetBase.Identifier.FileAny -> ClipType.FileAny;
                default -> ClipType.Unknow;
            };
        }
    }

    public NetData getClip(){
        synchronized (SyncLock){
            update = false;
            return new NetData(switch (this.Type) {
                case ClipText -> NetBase.Identifier.ClipText;
                case ClipImg -> NetBase.Identifier.ClipImg;
                case FileAny -> NetBase.Identifier.FileAny;
                case Unknow -> NetBase.Identifier.Unknow;
            }, this.ClipObject);
        }
    }
}
