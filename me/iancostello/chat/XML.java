package me.iancostello.chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.iancostello.sec.SHAHash;
import me.iancostello.util.ByteBuffer;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** XML
 * Handles storage of users on the server
 */
public class XML {
	private String filepath;
	private ArrayList<UserNode> nodes = new ArrayList<UserNode>();
	private String rootNode2;
	List<User> users;
	
	/** Constructor */
	public XML(String fileLocation, String rootNode) {
		filepath = fileLocation;
		rootNode2 = rootNode;
		if (nodes.size()==0) {
			UserNode node = new UserNode(rootNode, 0, true);
			nodes.add(node);
		}
	}

	/** authenticate */
	public boolean authenticate(String username, String password) {
		User userToAuth = getUser(username);
		if (userToAuth != null) {
			ByteBuffer saltHex = new ByteBuffer(userToAuth.getSalt());
			saltHex.hexToBytes();
			String salt = saltHex.toString();
			SHAHash hasher = new SHAHash();
			ByteBuffer hash = new ByteBuffer(hasher.get512Hash(password, salt));
			if (hash.equals(userToAuth.getEncrypted())) {
				return true;
			}
		}
		return false;
	}
	
	/** addUser
	 * Adds a new user, not very adaptive, but it works
	 * @param user
	 * @param pass
	 * @param salt
	 * @param IP
	 */
//	//private void addUser(String user, String pass, String salt) {
//		nodes.add(new UserNode("user", 1, true));
//		nodes.add(new UserNode(2, "name", user));
//		nodes.add(new UserNode(2, "pass", pass));
//		nodes.add(new UserNode(2, "salt", salt));
//		nodes.add(new UserNode("user", 1, false));
//	}

	public void newUser(String user, String pass, String publicKey, String publicMod) {
		User newUser = new User();
		SHAHash hasher = new SHAHash();
		//Get the salt
		String salt=null;
		String saltHex=null;
		try {
			salt = hasher.getSalt();
			ByteBuffer bBuf = new ByteBuffer();
			byte[] buf = salt.getBytes();
			bBuf.append(buf,0,buf.length);
			ByteBuffer bBuf2 = new ByteBuffer();
			bBuf2.appendHex(bBuf);
			saltHex = bBuf2.toString();
			
			ByteBuffer bBuf3 = new ByteBuffer(saltHex);
			bBuf3.hexToBytes();
			String salt2 = new String(bBuf3.getBytes(),0,bBuf3.end());
			if (!salt.equals(salt2)) {
				System.out.println("foobar");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		newUser.setName(user);
		newUser.setSalt(saltHex);
		newUser.setEncrypted(hasher.get512Hash(pass, salt));
		newUser.setPublicKey(publicKey);
		newUser.setPublicMod(publicMod);
		users.add(newUser);
		addUser(newUser);
	}

	
	public void addUser(User user) {
		nodes.add(new UserNode("user", 1, true));
		nodes.add(new UserNode(2, "name", user.getName()));
		nodes.add(new UserNode(2, "pass", user.getEncrypted()));
		nodes.add(new UserNode(2, "salt", user.getSalt()));
		nodes.add(new UserNode(2, "pubKey", user.getPublicKey()));
		nodes.add(new UserNode(2, "pubMod", user.getPublicMod()));
		nodes.add(new UserNode("user", 1, false));
	}

	/** addCustomNode
	 * Adds Passed Node
	 * @param node
	 */
	public void addCustomNode(UserNode node) {
		nodes.add(node);
	}

	/** write
	 * Loops through the arraylist and adds it to a ByteBuffer then writes it to file
	 */
	public synchronized void write() {
		//Create a bytebuffer that will be the entire XML file
		ByteBuffer toWrite = new ByteBuffer(); 
		//Add top node
		nodes.add(new UserNode(rootNode2, 0, false));
		//Add sub nodes to the path
		for (int i = 0; i < nodes.size(); i+=1) {
			toWrite.append(nodes.get(i).getFormattedNode());
		}
		//Write the file
		try {
			//Write the most upto date version
			toWrite.write(new File(filepath));
			//Create a backup of the data
//			String sdf = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss").format(new Date());
//			String backupData = "/Users/iancostello/Documents/mainProjects/chat/data/backups/users" + sdf + ".xml";
//			toWrite.write(new File(backupData));
		} catch (IOException e) {
			e.printStackTrace();
		}
		nodes.remove(nodes.size()-1);
	}

	/** read() 
	 * reads an xml file and then loads it in as a properly formatted array
	 */
	public synchronized void read() {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			//For UTF-8
			File file = new File(filepath);
			InputStream inputStream= new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream,"UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");

			//Init parser
			SAXParser saxParser = saxParserFactory.newSAXParser();
			IanSaxParser parser = new IanSaxParser();
			saxParser.parse(is, parser);
			//Get Users list
			users = parser.getUserList();
			//print User information
			if (users!=null) {
				for(User user : users) {
					//Add back to arraylist
					addUser(user);
//					System.out.println(user.toString());
				}
			} else {
				users = new ArrayList<User>();
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void clearUsers() {
		
	}
	
	/** getUser */
	public User getUser(String name) {
		for (int i = 0; i < users.size(); i+=1) {
			if (users.get(i).getName().equals(name)) {
				return users.get(i);
			}
		}
		return null;
	}
	/** getFilePath
	 * @return String filepath
	 */
	public String getFilePath() {
		return filepath;
	}
}
