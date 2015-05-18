package chatC;

import java.io.IOException;
import java.net.InetAddress;

import me.iancostello.chat.ChatSocket;
import me.iancostello.util.ByteBuffer;

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

	/** run */
//	public void run() {
//		ByteBuffer token = new ByteBuffer();
//		while (true) {
//			try {
//				// Login to server
////				if (!login()) {
////					break;
////				}
//
//				// Debug
//				count=0;
//
//				while (true) {
//					boolean isIdle=true;
//					ByteBuffer iBuf = read(0);
//					if (iBuf!=null) {
//						isIdle=false;
//						count+=1;
//
//						// Receive any messages
//						// Pitch anything else
//						if (iBuf.startsWith("msg ")) {
//							iBuf.getToken(token,ByteBuffer.WHITE_SPACE);
//							iBuf.getToken(token,ByteBuffer.WHITE_SPACE);
//							String msg = "Message from "+token+": "+iBuf;
//							System.out.println(msg);
//						}
//					}
//
//					// Any user input?
//					boolean userInput=false;
//					if (userInput) {
//						isIdle=false;
//					}
//
//					// If idle, sleep for 250ms
//					if (isIdle) {
//						Thread.sleep(250);
//					} 
//				}
//			} catch (InterruptedException e) {
//				// NOP
//			}	
//		}
//	}

	/** login
	 *		Login to server
	 */
//	public boolean login() {
//		while (true) {
//			try {					
//				// Connect & login to server
//				connect();
//				write("login "+getLocalUser()+" myPassword\n");
//
//				while (true) {
//					ByteBuffer bBuf = read(15000);
//					if (bBuf == null) {
//						System.out.println("No response from server. Sleep & reconnect.");
//						Thread.sleep(15000);
//					} else if (bBuf.equals("login OK")) {
//						return true;
//					} else if (bBuf.startsWith("login ")) {
//						System.out.println(bBuf);
//						return false;
//					}
//				}
//			} catch (InterruptedException e) {
//				// NOP
//			}
//		}
//	}

}