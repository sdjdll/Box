package NET;

import org.jetbrains.annotations.NotNull;

public class NetData {
    public int identifier;
    public byte[] data;
    public int length;

    public NetData(){
        this.identifier = 0x0f0f0f0f;
        this.data = new byte[0];
        this.length = 0;
    }

    public NetData(int identifier,byte @NotNull [] data){
        this.identifier = identifier;
        this.data = data;
        this.length = data.length;
    }

    public void setData(byte @NotNull [] data){
        this.data = data;
        this.length = data.length;
    }

    public void setIdentifier(int identifier){
        this.identifier = identifier;
    }

    public boolean VerifyLength(int length){
        return this.length == length;
    }
}
