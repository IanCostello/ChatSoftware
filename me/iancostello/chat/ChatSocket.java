package me.iancostello.chat;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import chatC.DataInterface;
import me.iancostello.util.ByteBuffer;

/** ChatSocket
 *		Methods to read and write from socket
 */
public class ChatSocket {
	private InetAddress serverIP;
	private int serverPort;
	private Socket socket;
	private InputStream input;
	private OutputStream output;
	private ByteBuffer iBuf;			// Input buffer
	private String localUser;			// Local username. eg: "Server1" or "ian"
	private String remoteUser;			// Remote username. eg: "ian" or "mark"
	private DataInterface data;
	private final DateFormat dateFormat;
	
	/** Constructor
	 *		Server side constructor 
	 */
	public ChatSocket(Socket socket) {
		dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		this.socket = socket;
		try {
			input = socket.getInputStream();
			output = socket.getOutputStream();
		} catch (IOException e) {
		}
		
		iBuf = new ByteBuffer();
		localUser = "server";
		remoteUser = socket.getRemoteSocketAddress().toString();
	}
	
	/** Constructor 
	 *		Client side constructor
	 */
	public ChatSocket(InetAddress serverIP, int serverPort) {
		dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		this.serverIP=serverIP;
		this.serverPort=serverPort;
		this.socket=null;
		iBuf = new ByteBuffer();
		localUser = "client";
		remoteUser = serverIP+":"+serverPort;
	}
	
	/** connect 
	 *		Connect/Reconnect to a socket
	 */
	public void connect() {
		try {
			close();
			socket = new Socket(serverIP,serverPort);
			output = socket.getOutputStream();
			input = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** getSocket */
	public Socket getSocket() {
		return socket; 
	}
	
	/** setLocalUser
	 *		@param String localUsername
	 */
	public void setLocalUser(String s) {
		localUser = s;
	}
	
	/** getLocalUser
	 *		@return String localUsername
	 */
	public String getLocalUser() {
		return localUser;
	}
	
	/** setRemoteUser
	 *		@param String remoteUsername
	 */
	public void setRemoteUser(String s) {
		remoteUser=s;
	}
	
	/** getRemoteUser
	 *		@return String remoteUsername
	 */
	public String getRemoteUser() {
		return remoteUser;
	}
	
	/** close
	 *		Closes the socket
	 */
	public void close() {
		if (input!=null) {
			try {
				input.close();
			} catch (IOException e) {
			}
			input=null;
		}
		if (output!=null) {
			try {
				output.close();
			} catch (IOException e) {
			}
			output=null;
		}
		if (socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
			socket=null;
		}
	}
	
	/** log
	 * logs a message into the console
	 * @param message
	 */
	public void log(String message) {
		Date date = new Date();
		System.out.println(dateFormat.format(date) + ": " + message);
	}
	
	/** isConnected 
	 * Socket will be null before login.
	 * Attempt to login will create socket.
	 * When server accepts the connection for the first time, socket.isConnected() will become true (and remain true).
	 * If connection lost, for any reason, then socket.isClosed() will become false.
	*/
	public boolean isConnected() {
	    return ((socket!=null) && socket.isConnected() && !socket.isClosed());
	}
	
	/** read
	 *		Read from socket input.
	 *		Return null if nothing is there.
	 *		@param long maxDelay. Time to wait. -1 is forever.
	 */
	public ByteBuffer read(long maxDelay) throws IOException {
		//InputStream input = socket.getInputStream();
		int end = iBuf.end();
		long startTime=0;
		while (true) {
			// Even if socket is closed, there may be characters in inputStream
			int avail = (input==null) ? 0 : input.available();
			if (avail>0) {
				iBuf.ensureCapacity(end+avail);
				byte[] buf = iBuf.getBytes();
				int numRead = input.read(buf,end,buf.length-end);
				if (numRead<0) break;
				end+=numRead;
				iBuf.moveEnd(end);
			}
				
			// Return characters upto CR
			int crIndex = iBuf.indexOf('\n');
			if (crIndex>=0) {
				ByteBuffer iBuf2 = iBuf;
				iBuf = new ByteBuffer();
				iBuf.append(iBuf2.getBytes(),crIndex+1,end);
				iBuf2.moveEnd(crIndex);
				
				if (localUser.startsWith("Server")) {
					System.out.println("From "+remoteUser+" to "+localUser+": "+iBuf2);
				}
				
				return iBuf2;

			// If socket is closed, throw an IOException to server
			} else if ((socket==null) || socket.isClosed()) {
				throw new IOException("Stream closed");
				
			//Ifi there is no readout time
			} else if (maxDelay<0) {
				iBuf.ensureCapacity(end+512);
				byte[] buf = iBuf.getBytes();
				int numRead = input.read(buf,end,buf.length-end);
				if (numRead<0) {
					throw new IOException("Stream closed");
				}
				end+=numRead;
				iBuf.moveEnd(end);

			// No wait
			} else if (maxDelay==0) {
				break;
				
			// Wait, then try again
			} else if (maxDelay>0) {
				long time = System.currentTimeMillis();
				if (startTime==0) {
					startTime = time;
				} else if ((time-startTime) >= maxDelay) {
					break;
				}
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// NOP
				}
			}
		}
		return null;
	}
	
	/** write
	 *		Write to socket, but don't display the message in console. 
	 *		Should be used when a user creates an account or any other sensitive info
	 *		'\n' is expected at the end
	 */
	public void privWrite(String s) {
		byte[] buf = s.getBytes();
		write(buf,0,buf.length, false);
	}
	
	/** write
	 *		Write to socket.
	 *		'\n' is expected at the end
	 *		Make this synchronized so two simultaneous messages do not get scrambled.
	 */
	public void privWrite(ByteBuffer bBuf) {
		write(bBuf.getBytes(),bBuf.start(),bBuf.end(), false);
	}
	/** write
	 *		Write to socket, but don't display the message in console. 
	 *		Should be used when a user creates an account or any other sensitive info
	 *		'\n' is expected at the end
	 */
	public void write(ByteBuffer bBuf) {
		write(bBuf.getBytes(),bBuf.start(),bBuf.end(), true);
	}
	
	/** write
	 *		Write to socket.
	 *		'\n' is expected at the end
	 *		Make this synchronized so two simultaneous messages do not get scrambled.
	 */
	public void write(String s) {
		byte[] buf = s.getBytes();
		write(buf,0,buf.length, true);
	}
	
	/** write
	 * 		Syncronized so everything outputs as a block
	 */
	private synchronized void write(byte[] buf, int start, int end, boolean display) {
		try {
			if (socket!=null) {
				//OutputStream output = socket.getOutputStream();
				output.write(buf, start, end-start);
				//output.write('\n');
				output.flush();
				
				//if (localUser.startsWith("Server")) {
				if (display) {
					log("From "+localUser+" to "+remoteUser+": "+new String(buf,start,end-start-1));
				}
				//}
			}
		} catch (IOException e) {
			e.printStackTrace();
			close();
			if (data!=null) {
				//TODO Logout
			}
		}
	}
	
	/** setDataInterface
	 * Sets the data interface
	 * @param data
	 */
	public void setDataInterface(DataInterface data) {
		this.data = data;
	}
}
	