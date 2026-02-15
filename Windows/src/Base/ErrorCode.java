package Base;

public class ErrorCode {
    public static class File{
        public static final int CreateFailed = 0x11;
        public static final int NotFound = 0x21;
        public static final int ReadFailed = 0x31;
        public static final int WriteFailed = 0x41;

        public static int Unknown = 0x01;
    }

    public static class Net{
        public static int CreateSocketFailed = 0x12;
        public static int NetworkError = 0x22;

        public static int Unknow = 0x02;
    }
}
