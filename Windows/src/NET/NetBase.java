package NET;

import java.io.File;

public class NetBase {
    public static NetPort NetProt_TransIn = new NetPort(10001,"NetProt_TransIn");
    public static File File_NetLog = new File("./Logs/Net/Net.log");
}
