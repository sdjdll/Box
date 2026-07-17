import sdjini.lib.tcp.Client;

void main() throws IOException {
	Client c = new Client("test_sender", InetAddress.getByName("localhost"), 11011);
	Scanner in = new Scanner(System.in);
	while (true)
		c.send(in.nextLine().getBytes(StandardCharsets.UTF_8));
}