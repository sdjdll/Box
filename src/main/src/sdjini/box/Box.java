package sdjini.box;

import sdjini.lib.file.StringFile;
import sdjini.lib.logger.FileLogger;
import sdjini.lib.tcp.Server;
import sdjini.lib.tcp.StateCore;
import sdjini.lib.tcp.exception.EOSException;
import sdjini.lib.tcp.exception.SocketInitializeException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Box {
	public Box(int prot) throws SocketInitializeException{
		FileLogger lg = new FileLogger(Box.class, new StringFile("./log/Box.log", StandardCharsets.UTF_8));
		Server server = new Server(prot);

		new Thread(()->{
			while(true) {
				try {
					server.accept();
				} catch (IOException e) {
					lg.error(new StateCore.Tag.Server.Connect(this),"Client Connect Failed", e);
				}
			}
		}).start();

		server.forward((source,clients)->{
			byte[] temp;
			try {
				temp = source.receive();
			} catch (EOSException e) {
				lg.error(new StateCore.Tag.Server.Receive(this),"Client Connect Failed", e);
				clients.remove(source.Name, source);
				temp = new byte[0];
			}
			final byte[] data = temp;
			clients.forEach((key, client) -> {
				if(key.equals(source.Name)) client.send(data);
			});
		});
	}

	static void main(String[] args) throws SocketInitializeException{
		if(args.length > 1) throw new IllegalArgumentException("Too many arguments");
		if(args.length == 0) new Box(11011);
		else new Box(Integer.parseInt(args[0]));
	}
}
