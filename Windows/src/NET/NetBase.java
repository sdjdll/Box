package NET;

import NET.NetActor.Exception.ClientAdvanceDisconnect;
import NET.NetActor.Exception.NetDataCorruption;
import com.sun.jdi.InvalidTypeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.InetAddress;

public class NetBase {
    public static final int PROT = 10001;
    public static final int MAX_ERROR_TIME = 5;
    public static final File LOG = new File("./Log/Net.log");

    public static class Tag{
        public static final class ServerSocket implements Base.Tag {
            @Override
            public String toString() {
                return "ServerSocket";
            }
        }

        public static final class ClientSocket implements Base.Tag{
            @Override
            public String toString() {
                return "ClientSocket";
            }
        }

        public static final class NetDevice implements Base.Tag{
            private InetAddress ia;
            public NetDevice(InetAddress ia){this.ia = ia;}
            public String toString() {
                return "NetDevice: " + this.ia.toString();
            }
        }
    }

    public static class Identifier{
        public static final byte[] CLIP_TEXT = {0x0, 0x1, 0x0, 0x1};
        public static final byte[] CLIP_IMG = {0x0, 0x1, 0x0, 0x2};
        public static final byte[] FILE_ANY = {0x0, 0x2, 0x0, 0x0};
        public static final byte[] END = {0xf, 0xf, 0xf, 0xf};

        public static final int ClipText = 0x00010001;
        public static final int ClipImg = 0x00010002;
        public static final int FileAny = 0x00020000;
        public static final int End = 0x0f0f0f0f;

        public static final int Unknow = 0xffffffff;
    }

    public static @NotNull NetData getNetData(byte @NotNull [] buffer) throws ClientAdvanceDisconnect, NetDataCorruption, ArrayIndexOutOfBoundsException {
        int identifier = bytes2Int(buffer,0);
        int length = bytes2Int(buffer, 4);

        byte[] data = new byte[length];

        int realLength = length + 8;

        if (buffer.length < realLength + 4) throw new ClientAdvanceDisconnect();

        if (buffer[realLength]     != Identifier.END[0] || buffer[realLength + 1] != Identifier.END[1] ||
            buffer[realLength + 2] != Identifier.END[2] || buffer[realLength + 3] != Identifier.END[3]) throw new ClientAdvanceDisconnect();

        System.arraycopy(buffer, 8, data, 0, length);

        NetData nd = new NetData();
        nd.setIdentifier(identifier);
        nd.setData(data);

        if (!nd.VerifyLength(length)) throw new NetDataCorruption();

        return nd;
    }

    public static byte[] int2Bytes(int value) {
        byte[] result = new byte[4];
        result[0] = (byte) ((value >> 24) & 0xFF);
        result[1] = (byte) ((value >> 16) & 0xFF);
        result[2] = (byte) ((value >> 8) & 0xFF);
        result[3] = (byte) (value & 0xFF);
        return result;
    }

    public static int bytes2Int(byte[] value) throws InvalidTypeException {
        if (value.length != 4) throw new InvalidTypeException();
        return (value[0] & 0xFF) << 24 | (value[1] & 0xFF) << 16 | (value[2] & 0xFF) << 8  | (value[3] & 0xFF);
    }

    public static int bytes2Int(byte @NotNull [] value, int start) throws ArrayIndexOutOfBoundsException {
        if (start < 0 | start + 4 > value.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return ( value[start]     & 0xFF) << 24 |
                (value[start + 1] & 0xFF) << 16 |
                (value[start + 2] & 0xFF) << 8  |
                (value[start + 3] & 0xFF);
    }
}
