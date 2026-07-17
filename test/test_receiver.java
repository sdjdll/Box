import sdjini.lib.tcp.Client;
import sdjini.lib.tcp.exception.EOSException;

void main() throws IOException {
	Client c = new Client("test_receiver", InetAddress.getByName("localhost"), 11011);
	new Thread(()->{
		try {
			while(true) System.out.println(new String(c.receive(), StandardCharsets.UTF_8));
		} catch (EOSException _) {}
	}).start();
}