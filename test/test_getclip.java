import sdjini.lib.tcp.Client;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

private String lastString = "";
void main() throws IOException, UnsupportedFlavorException {
    Client c = new Client("test_getclip", InetAddress.getByName("localhost"), 11011);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    while (true) {
        String t = clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor).toString();
        if(!lastString.equals(t)) {
            System.out.printf("get new data[%s], sending\n", t);
            lastString = t;
            c.send(t.getBytes(StandardCharsets.UTF_8));
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {}
    }
}