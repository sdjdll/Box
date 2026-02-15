package NET;

import Base.Tag;

import java.io.File;

public class NetBase {
    public static final NetPort NetProt_TransIn = new NetPort(10001,"NetProt_TransIn");
    public static final File File_NetLog = new File("./Logs/Net/Net.log");
    public static class Tags{
        public static class ServerSocket implements Tag {
            @Override
            public String toString() {
                return "ServerSocket";
            }
        }
        public static class ClientSocket implements Tag{
            @Override
            public String toString() {
                return "ClientSocket";
            }
        }
        public static class Network implements Tag{
            @Override
            public String toString() {
                return "Network";
            }
        }
        public static class NetDevice implements Tag{
            @Override
            public String toString() {
                return "NetDevice";
            }
        }
    }
    public static final int PACKAGE_SIZE = 1024;
    public static class Identifier{
        public static final int TEXT = 0x00000001;
        public static final int IMG = 0x00000002;

        public static final int END = 0xffffffff;
    }
    public static class IdenData{
        public static final byte[] TEXT = {0x0, 0x0, 0x0, 0x1};
        public static final byte[] IMG = {0x0, 0x0, 0x0, 0x2};

        public static final byte[] END = {0xf, 0xf, 0xf, 0xf};
    }
}
