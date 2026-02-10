package Base;

public class ErrorCode {
    public static class File{
        public static int CreateFailed = 0x0000000000000011;
        public static int NotFound = 0x0000000000000021;
        public static int ReadFailed = 0x0000000000000031;
        public static int WriteFailed = 0x0000000000000041;

        public static int Unknown = 0x0000000000000001;
    }
}
