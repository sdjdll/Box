import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final String localhost = "127.0.0.1";
    private static final int port = 10001; // 注意：变量名通常用 port 而不是 prot

    public Main() throws IOException {
        Socket socket = new Socket(localhost, port);
        System.out.println("连接成功！等待服务端消息...");

        InputStream is = socket.getInputStream();

        // 定义一个缓冲区，大小为 1024 字节
        byte[] buffer = new byte[1024];

        while (true) {
            // read 方法会阻塞，直到有数据到来，或者连接关闭
            // 返回值 len 是实际读取到的字节数，-1 表示连接断开
            int len = is.read(buffer);

            if (len == -1) {
                System.out.println("\n服务端断开了连接。");
                break;
            }

            // 将读取到的字节数组（从0到len）转换为字符串打印
            // 假设服务端发送的是 UTF-8 编码的文本
            String message = new String(buffer, 0, len);
            System.out.print(message);
        }

        // 关闭 socket
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}
