package me.iancostello.chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import me.iancostello.util.ByteBuffer;

/** ChatServer
 * Used Handle User Login Requests and Pass them off to a server thread
 */
public class ChatServer implements Runnable {
	private static int backlog=100;						// Allow 100 pending requests
	public static int serverPort = 2021;				// Bind to this port

	private InetAddress serverIP;
	private ConcurrentHashMap<String,ServerThread> threadsByUsername;
	private final String xmlFilepath;
	private XML xml;
	private static final boolean DEBUG = true;

	/** main */
	public static void main(String[] args) {
		if (!DEBUG) {
			ChatServer server = new ChatServer();
			server.run();
		}
	}

	/** Constructor */
	public ChatServer() {
		if (DEBUG) {
			xmlFilepath = "/Users/iancostello/Documents/mainProjects/chat/data/users.xml";
		} else {
			xmlFilepath = "/home2/ian/chat/users.xml";
		}
		xml = new XML(xmlFilepath, "Users");
		xml.read();
		try {
			if (!DEBUG) {
				serverIP = InetAddress.getByName("iancostello.me");
				serverIP = getExternalIP();
			} else {
				serverIP = InetAddress.getByName("127.0.0.1");
			}
		} catch (IOException e) {
			System.out.println("Can not lookup http://iancostello.me");
			try {
				serverIP = InetAddress.getByName("127.0.0.1");
			} catch (UnknownHostException e2) {
				// NOP
			}
		}
		threadsByUsername = new ConcurrentHashMap<String, ServerThread>();
	}

	/** run
	 * 		Runs a ChatServer
	 * 		Main server loop.
	 * 		Wait for connections and check login credentials
	 */
	@SuppressWarnings("resource")
	public void run() {
		try {
			// Bind to serverSocket and process requests
			System.out.println("Binding server to "+serverIP+":"+serverPort);
			ServerSocket serverSocket = new ServerSocket(serverPort,backlog,serverIP);
			//Start the socket closer
			ThreadCloser tc = new ThreadCloser();
			Thread t = new Thread(tc);
			t.start();
			
			int connectionNum=0;
			while (true) {
				Socket socket = serverSocket.accept();
				ServerThread serverThread = new ServerThread(socket);
				System.out.println("Connection from "+serverThread.getRemoteUser());
				connectionNum += 1;
				serverThread.setLocalUser("Server"+connectionNum);
				if (serverThread.login()) {
					Thread thread = new Thread(serverThread);
					thread.start();
				}
			}
		} catch (Exception e) {
			StringWriter writer = new StringWriter(600);
			e.printStackTrace(new PrintWriter(writer));
			System.out.println("LockServer error: "+e+"\n"+writer);
		}
	}

	/** getServerIP
	 *		Get IP address for server, or localhost if none

	 * 		@return InetAddress
	 */
	private InetAddress getExternalIP() throws IOException {
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration<InetAddress> ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress ipAddr = (InetAddress) ee.nextElement();
					byte[] ip = ipAddr.getAddress();
					int ip0 = ip[0]&0xff; 
					int ip1 = ip[1]&0xff; 
					boolean isLocal = (ip.length>4) ||
							(ip0==127) ||									// 127.0.0.1
							(ip0==10) ||									// 10.0.0.0-10.255.255.255
							((ip0==172) && (16<=ip1) && (ip1<=31)) ||		// 172.16.0.0-172.31.255.255
							((ip0==192) && (ip1==168));					// 192.168.0.0-192.168.255.255
					if (!isLocal) {
						return ipAddr;
					}
				}
			}
		} catch (SocketException e) {
			System.out.println("No network interfaces");
		}
		return InetAddress.getByName("127.0.0.1");
	}

	/** authenticate
	 *		Check if user exists
	 *
	 *		@param ServerThread serverThread
	 *		@param String username
	 *		@param String password
	 */
	public synchronized boolean authenticate(ServerThread serverThread, String username, String password) {
		if (xml.authenticate(username, password)) {
			threadsByUsername.put(username,serverThread);
			return true;
		}
		return false;
	}	

	/** terminate
	 *		Check if user exists
	 *
	 *		@param ServerThread serverThread
	 */
	public synchronized void terminate(ServerThread serverThread) {
		User user = xml.getUser(serverThread.getRemoteUser());
		if (user!=null) {
			String username = user.getName();
			serverThread.log("Closing "+username);
			serverThread.close();
			String message = "inactive " + username + "\n";
			ArrayList<String> activeUsers = user.getFriends();
			for (int i = activeUsers.size()-1; i >= 0; i-=1) {
				String friendName = activeUsers.get(i);
				ServerThread st = threadsByUsername.get(friendName);
				if (st!=null) {
					st.write(message);
				}
			}
		}
		threadsByUsername.remove(serverThread.getRemoteUser());
	}	

	/** getServerIP
	 * @return InetAddress serverIP
	 */
	public InetAddress getServerIP() {
		return serverIP;
	}

	/** ServerThread
	 *		Process commands to and from the Server
	 */
	public class ServerThread extends ChatSocket implements Runnable {
		/** Constructor */
		public ServerThread(Socket s) {
			super(s);
		}

		/** login
		 *		Authenticate a user
		 *		Format "login username password"
		 */
		public boolean login() {
			boolean isValidUser=false;
			try {
				ByteBuffer bBuf = read(10000);
				if (bBuf==null) return false;
				ByteBuffer token = new ByteBuffer();
				bBuf.getToken(token,ByteBuffer.WHITE_SPACE);
				String cmd=token.toString();
				bBuf.getToken(token,ByteBuffer.WHITE_SPACE);
				String remoteUser = token.toString();
				setRemoteUser(remoteUser);			
				bBuf.getToken(token,ByteBuffer.WHITE_SPACE);
				String password = token.toString();
				User user = xml.getUser(remoteUser);
				//Get public mod
				bBuf.getToken(token, ByteBuffer.WHITE_SPACE);
				String publicKey = token.toString();
				bBuf.getToken(token, ByteBuffer.WHITE_SPACE);
				String publicMod = token.toString();
				if (user!=null) {
					isValidUser = authenticate(this, remoteUser, password);
				}

				if (isValidUser) {
					write(cmd+" ok\n");
				} else if (cmd.equals("login")) {
					if (user==null) {
						write("login Username does not exist on server!\n");
					} else {
						write("login Incorrect password\n");
					}
				} else if (cmd.equals("create")) {
					if (user!=null) {
						write ("create User Already Exists\n");
					} else {
						xml.newUser(remoteUser, password, publicKey, publicMod);
						xml.write();
						isValidUser = true;
						write("create ok\n");
						authenticate(this, remoteUser, password);
					}
				}
			} catch (IOException e) {
			}
			return isValidUser;
		}

		/** run
		 *		Loop reading and writing to client
		 */
		public void run() {
			ByteBuffer oBuf = new ByteBuffer();
			ByteBuffer token = new ByteBuffer();

			while (true) {
				try {
					// Blocking read
					ByteBuffer iBuf = read(-1);

					// Relay the Message
					iBuf.getToken(token,ByteBuffer.WHITE_SPACE);
					//Format msg fromWho message
					if (token.equals("msg")) {
						iBuf.getToken(token,ByteBuffer.WHITE_SPACE);
						String username = token.toString();
						ServerThread userThread = threadsByUsername.get(username);
						User user = xml.getUser(username);
						boolean userExists= user!=null;
						boolean isBlocked=false;
						if (!userExists) {
							write("msg system "+username+" is not a user.\n");
						} else if (isBlocked) {
							write("msg system "+username+" has blocked your messages.\n");
						} else if (userThread==null) {
							write("msg system "+username+" is not logged in.\n");
						} else {
							// Relay the message.
							oBuf.clear().append("msg ").append(getRemoteUser()).append(' ').append(iBuf).append('\n');
							userThread.write(oBuf);
						}

					// Logout
					} else if (token.equals("logout")) {
						write(token+" ok\n");
						terminate(this);
						log(this.getRemoteUser() + " logged off");
						
					// Notify all friends that user is active
					//Format friends friend1 friend2 ...
					} else if (token.equals("friends")) {
						// Notify friends that remoteUser came online
						String message = "active " + getRemoteUser() + "\n";
						ByteBuffer message2 = new ByteBuffer("active");
						ArrayList<String> friends = new ArrayList<String>();
						User user = xml.getUser(getRemoteUser());
						user.setFriends(friends);
						while (iBuf.getToken(token, ByteBuffer.WHITE_SPACE)) {
							String username = token.toString();
							friends.add(username);
							ServerThread userThread = threadsByUsername.get(username);
							if (userThread!=null) {
								userThread.write(message);
								message2.append(' ').append(token);
							}
						}
						// Notify remoteUser which of his friends are online.
						if (message2.indexOf(' ')>0) {
							message2.append('\n');
							write(message2);
						}

					// Add A Friend Format: addFriend name
					} else if (token.equals("addFriend"))  {
						iBuf.getToken(token, ByteBuffer.WHITE_SPACE);
						String username = token.toString();
						User user = xml.getUser(username);
						if (username.equals(getRemoteUser())) {
							write("addFriend Can't add yourself as a friend.\n");
						} else if (user!=null) {
							user.addFriend(username);
							ServerThread st = threadsByUsername.get(username);
							ByteBuffer message = new ByteBuffer();
							message.append("addFriend ok ").append(username);
							if (st!=null) {
								message.append(" active ");
							} else {
								message.append(" inactive ");
							}
							message.append(user.getPublicKey()).append(' ');
							message.append(user.getPublicMod());
							message.append('\n');
							write (message);
						} else {
							write ("addFriend User Does Not Exist\n");
						}

					//Error Message
					} else {
						write(token+" illegal command\n");
					}
				} catch (IOException e) {
					log(this.getRemoteUser() + " logged off");
					close();
					terminate(this);
					break;
				}
			}
		}	
	}
	
	/** ThreadCloser
	 * Makes sure that closed sockets are actually closed 
	 */
	public class ThreadCloser implements Runnable {
		public void run() {
			Collection<ServerThread> threadList = threadsByUsername.values();
			ServerThread[] threads = threadList.toArray(new ServerThread[threadList.size()]);
			for (int i=threads.length-1; i>=0; i-=1) {
				ServerThread st = threads[i];
				if (!st.isConnected()) {
					terminate(st);
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
	}
}

