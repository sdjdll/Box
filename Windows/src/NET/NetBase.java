package NET;

import Base.Tag;
import NET.NetActor.Exception.ClientAdvanceDisconnect;
import NET.NetActor.Exception.NetDataCorruption;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.InetAddress;

public class NetBase {
    public static final int PROT = 10001;
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
    }
    public static class identify{
//        public static final int[] Example = new int[]{起始位置，长度};
        public static final int[] Identifier = new int[]{0, 4};
        public static final int[] Length = new int[]{4, 4};
        public static final int[] Data = new int[]{8, -1};
    }

    public static @NotNull NetData getNetData(byte @NotNull [] buffer) throws ClientAdvanceDisconnect, NetDataCorruption {
        int identifier = (buffer[0] & 0xFF) << 24 | (buffer[1] & 0xFF) << 16 | (buffer[2] & 0xFF) << 8  | (buffer[3] & 0xFF);
        int length = (buffer[4] & 0xFF) << 24 | (buffer[5] & 0xFF) << 16 | (buffer[6] & 0xFF) << 8  | (buffer[7] & 0xFF);

        byte[] data = new byte[length];

        int realLength = length + 8;

        if (buffer.length < realLength + 4) throw new ClientAdvanceDisconnect();

        if (buffer[realLength]     != Identifier.END[0] || buffer[realLength + 1] != Identifier.END[1] ||
            buffer[realLength + 2] != Identifier.END[2] || buffer[realLength + 3] != Identifier.END[3]) throw new ClientAdvanceDisconnect();

        System.arraycopy(buffer, 8, data, 0, length);

        NetData nd = new NetData();
        nd.setIdentifier(switch (identifier){
            case Identifier.ClipText -> "CLIP_TEXT";
            case Identifier.ClipImg -> "CLIP_IMG";
            case Identifier.FileAny -> "FILE_ANY";
            case Identifier.End -> "END";
            default -> throw new IllegalStateException("Unexpected value: " + identifier);
        });
        nd.setData(data);

        if (!nd.VerifyLength(length)) throw new NetDataCorruption();

        return nd;
    }

}
