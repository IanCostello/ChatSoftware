package chatC;

import java.net.InetAddress;

import me.iancostello.chat.ChatSocket;

/** ClientThread
 *		Send client commands to & from the Server
 *		This is what normally runs on the client
 */
public class ClientSocket extends ChatSocket {
	public int count=-1;

	/** Constructor */
	public ClientSocket(InetAddress serverIP, int serverPort) {
		super(serverIP,serverPort);
	}

}