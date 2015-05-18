package chatC;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.iancostello.chat.ChatServer;

public class Main {
	public static final boolean DEBUG = true;
	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}
	public void run() {
		//Start the server
		//Start Local ChatServer For Debug
		InetAddress serverIP = null;
		if (DEBUG) {
			ChatServer server = new ChatServer();
			serverIP = server.getServerIP();
			Thread t = new Thread(server);
			t.start();
		} else {
			try {
				serverIP = InetAddress.getByName("iancostello.me");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		//Start normal server
		if (DEBUG) {
			new ClientThread(0).start();
			new ClientThread(1).start();	
		} else {
			new ClientThread(-1).start();	
		}
	}

	public class ClientThread extends Thread {
		int version;
		public ClientThread(int version) {
			this.version = version;
		}
		public void run() {
			GraphicsOut go = new GraphicsOut(version);
		}
	}
}
