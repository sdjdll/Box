import sdjini.lib.tcp.Client;
import sdjini.lib.tcp.exception.EOSException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;

void main() throws IOException, UnsupportedFlavorException {
    Client c = new Client("test_socketclip", InetAddress.getByName("localhost"), 11011);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    new Thread(() -> {
        while (true) {
            try {
                String s = new String(c.receive(), StandardCharsets.UTF_8);
                System.out.println("Received: "+s);
                clipboard.setContents(new StringSelection(s), null);
            } catch (EOSException e) {
                System.err.println(e.getMessage());
            }
        }
    }).start();
}