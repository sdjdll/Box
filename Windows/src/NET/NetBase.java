package NET;

import Base.Tag;

import java.io.File;

public class NetBase {
    public static NetPort NetProt_TransIn = new NetPort(10001,"NetProt_TransIn");
    public static File File_NetLog = new File("./Logs/Net/Net.log");
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
    }
}
