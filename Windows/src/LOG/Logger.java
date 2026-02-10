package LOG;

import Base.ErrorCode;
import Base.Tag;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Logger extends Log{
    private FileOutputStream logOutput;
    private final String LoggerClass, LoggerPackage;
    public Logger(@NotNull File logFile, @NotNull Object source) {
        super();
        if (!logFile.exists()){
            try{
                boolean _ = logFile.createNewFile();
            } catch (IOException e) {
                Stack_printAndWrite(e);
                System.exit(ErrorCode.File.CreateFailed);
            }
        }
        try {
            logOutput = new FileOutputStream(logFile,true);
        } catch (IOException e) {
            Stack_printAndWrite(e);
            System.exit(ErrorCode.File.Unknown);
        }
        try {
            logOutput.write("Time,level, Package, Class, Tag, Context,Supplement\n".getBytes());
        }catch (IOException e){
            Stack_printAndWrite(e);
            System.exit(ErrorCode.File.WriteFailed);
        }
        this.LoggerClass = source.getClass().getName();
        this.LoggerPackage = source.getClass().getPackageName();
    }

    public void print(){
        switch (super.getLevel()){
            case STEP, INFO -> printSafe();
            case ERROR, FATAL -> printUnsafe();
        }
    }

    private void printAndWrite(Exception e){
        switch (super.getLevel()){
            case STEP, INFO -> printSafe();
            case ERROR, FATAL -> printUnsafe();
        }
        try{
            logOutput.write(super.toString().getBytes());
        } catch (IOException ex) {
            if (e != null){
                Stack_printAndWrite(e);
            }
            Stack_printAndWrite(ex);
            System.exit(ErrorCode.File.WriteFailed);
        }
    }
    private void printSafe(){
        System.out.println(super.toString());
    }
    private void printUnsafe(){
        System.err.println(super.toString());
        super.getException().printStackTrace(System.err);
    }

    public void printAndWrite(LogLevel level, Tag Tag, String Context, String Supplement,Exception e){
        super.setLevel(level);
        super.setPackage(LoggerPackage);
        super.setClass(LoggerClass);
        super.setTag(Tag.toString());
        super.setContext(Context);
        super.setSupplement(Supplement);
        super.setException(e);
        printAndWrite(e);
    }

    public void printAndWrite(Tag Tag, String Context, String Supplement, Exception e){
        printAndWrite(LogLevel.STEP,Tag,Context,Supplement,e);
    }
    public void printAndWrite(Tag Tag, String Context,Exception e){
        printAndWrite(LogLevel.STEP,Tag,Context,"",e);
    }
    public void printAndWrite(LogLevel level,Tag Tag, String Context){
        printAndWrite(level,Tag,Context,"",null);
    }
    public void printAndWrite(LogLevel level, Tag Tag, String Context,Exception e){
        printAndWrite(level,Tag,Context,"",e);
    }

    public static void Stack_print(@NotNull Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace(System.out);
    }

    public static void Stack_printAndWrite(@NotNull Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace(System.out);

        File logFile = new File("Fatal.log");
        if (!logFile.exists()) {
            try {
                boolean _ = logFile.createNewFile();
            } catch (IOException ex) {
                Stack_print(ex);
                System.exit(ErrorCode.File.CreateFailed);
            }
        }

        boolean _ = logFile.setWritable(true);

        FileOutputStream logOutput = null;
        try {
            logOutput = new FileOutputStream(logFile);
        } catch (IOException ex) {
            Stack_print(ex);
            System.exit(ErrorCode.File.WriteFailed);
        } finally {
            try{
                if (logOutput != null) logOutput.close();
            } catch (IOException ex) {
                Stack_print(ex);
                System.exit(ErrorCode.File.Unknown);
            }
        }

        try{
            logOutput.write(e.getMessage().getBytes());
        } catch (IOException ex) {
            Stack_print(ex);
            System.exit(ErrorCode.File.WriteFailed);
        }
    }
}