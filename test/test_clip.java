import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;

void main() throws IOException, UnsupportedFlavorException {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Scanner input = new Scanner(System.in);
    clipboard.setContents(new StringSelection(input.nextLine()), null);
    System.out.println(clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor));
}