package me.iancostello.chatC;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.iancostello.chat.ChatServer;

/** Main
 * Main Class
 */
public class Main {
	public static final boolean DEBUG = true;
	
	/** Main */
	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}
	
	/** Run */
	@SuppressWarnings("unused")
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
		//Start Clients if debug or create just one
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
			@SuppressWarnings("unused")
			GraphicsOut go = new GraphicsOut(version);
		}
	}
}
