import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    private static final String localhost = "127.0.0.1";
    private static final int port = 10001;

    public Main() throws IOException {
        Socket socket = new Socket(localhost, port);
        System.out.println("连接成功！等待服务端消息...");

        InputStream is = socket.getInputStream();

        byte[] buffer = new byte[1024];

        while (true) {
            int len = is.read(buffer);

            if (len == -1) {
                System.out.println("\n服务端断开了连接。");
                break;
            }

            String message = new String(buffer, 0, len,StandardCharsets.US_ASCII);
            System.out.println(message);
            System.out.println(Arrays.toString(buffer));
        }

        socket.close();
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}
