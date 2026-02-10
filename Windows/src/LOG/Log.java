package LOG;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private LogLevel level;
    private String Package, Class, Tag, Context, Supplement;
    private Exception e = null;

    protected Log(){
        this(null,null,null,null,null,null);
    }

    public Log(LogLevel level, String Package, String Class, String Tag, String Context, String Supplement) {
        this.level = level;
        this.Package = Package;
        this.Class = Class;
        this.Tag = Tag;
        this.Context = Context;
        this.Supplement = Supplement;
    }

    public Log(LogLevel level, String Package, String Class, String Tag, String Context) {
        this(level, Package, Class, Tag, Context, "");
    }

    public Log(LogLevel level, String Class, String Tag, String Context) {
        this(level, "", Class, Tag, Context, "");
    }

    public Log(LogLevel level, String Class) {
        this(level, "", Class, "", "", "");
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void setPackage(String Package) {
        this.Package = Package;
    }

    public void setClass(String Class) {
        this.Class = Class;
    }

    public void setTag(String Tag) {
        this.Tag = Tag;
    }

    public void setException(Exception e){
        this.e = e;
    }

    public void setContext(String Context) {
        this.Context = Context;
    }

    public void setSupplement(String Supplement) {
        this.Supplement = Supplement;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getPackage() {
        return Package;
    }

    public String getClassName() {
        return Class;
    }

    public String getTag() {
        return Tag;
    }

    public String getContext() {
        return Context;
    }

    public String getSupplement() {
        return Supplement;
    }

    public Exception getException(){
        return e;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), level, Package, Class, Tag, Context, Supplement);
    }
}
