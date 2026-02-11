package CLIPBOARD;

import Base.Tag;

import java.io.File;

public class ClipBase {
    public static File ClipLog = new File("./Log/Clip/Clipboard.log");

    public static class Tags{
        public static class Img implements Tag{
            @Override
            public String toString() {
                return "Img";
            }
        }
    }
}
