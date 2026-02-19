package NET;

import java.io.File;

public class NetBase {
    public static final int PROT = 10001;
    public static final File LOG = new File("./Log/Net.log");
    public static class Tag{
        public static final class ServerSocketFailed implements Base.Tag {
            @Override
            public String toString() {
                return "ServerSocketFailed";
            }
        }
    }
}
