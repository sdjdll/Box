package NET;

public class NetPort {
    private final int prot;
    private final String protName;
    public NetPort(int prot){
        this.prot = prot;
        this.protName = "";
    }

    public NetPort(int prot,String protName){
        this.prot = prot;
        this.protName = protName;
    }

    public int getProt() {
        return prot;
    }

    public String getProtName() {
        return protName;
    }

    public static boolean compare(NetPort A, NetPort B){
        return A.prot == B.prot;
    }
}
