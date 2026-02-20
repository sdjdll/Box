package NET;

import org.jetbrains.annotations.NotNull;

public class NetData {
    public String identifier;
    public byte[] data;
    public int length;

    public NetData(){
        this.identifier = null;
        this.data = new byte[0];
        this.length = 0;
    }

    public NetData(String identifier,byte @NotNull [] data){
        this.identifier = identifier;
        this.data = data;
        this.length = data.length;
    }

    public void setData(byte @NotNull [] data){
        this.data = data;
        this.length = data.length;
    }

    public void setIdentifier(String identifier){
        this.identifier = identifier;
    }

    public boolean VerifyLength(int length){
        return this.length == length;
    }
}
