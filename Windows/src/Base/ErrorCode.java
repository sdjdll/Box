package Base;

public class ErrorCode {
    public static class File{
        public static int CreateFailed = 0x11;
        public static int NotFound = 0x21;
        public static int ReadFailed = 0x31;
        public static int WriteFailed = 0x41;

        public static int Unknown = 0x01;
    }

    public static class Net{
        public static int CreateSocketFailed = 0x12;
        public static int NetworkError = 0x22;

        public static int Unknow = 0x02;
    }
}
