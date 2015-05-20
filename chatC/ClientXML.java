package chatC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.iancostello.util.ByteBuffer;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** ClientXML
 * Handles reading in the XML file, writing it, and user storage
 */
public class ClientXML {
	//Single User
	private String filepath;
	private ArrayList<ClientUserNode> nodes = new ArrayList<ClientUserNode>();
	private String rootNode;
	//private ArrayList<ClientUser> users;
	private ArrayList<ClientUser> users;
	private ClientUser localUser = new ClientUser();
	private DataInterface data;
	
	/** Constructor */
	public ClientXML(String fileLocation, String rootNode, DataInterface data) {
		users = new ArrayList<ClientUser>();
		filepath = fileLocation;
		this.rootNode = rootNode;
		this.data = data;
	}

	/** getLocalUser */
	public String getLocalUserName() {
		return localUser.getUsername();
	}
	
	/** setLocalUser */
	public void setLocalUser(String user) {
		localUser.setUsername(user);
	}
	
	/** addUserNodes
	 * Adds a new user to the nodes arrayList
	 * @param user
	 */
	private void addUserNodes(ClientUser user) {
		nodes.add(new ClientUserNode("user", 1, true));
		nodes.add(new ClientUserNode(2, "name", user.getName()));
		nodes.add(new ClientUserNode(2, "username", user.getUsername()));
		nodes.add(new ClientUserNode(2, "pubKey", user.getPublicKeyString()));
		nodes.add(new ClientUserNode(2, "pubMod", user.getPublicModString()));
		nodes.add(new ClientUserNode(2, "messages", user.getFileMessages()));
		nodes.add(new ClientUserNode("user", 1, false));
	}

	/** addUser */
	public void addUser(ClientUser user) {
		users.add(user);
	}
	
	/** addUser */
	public void addUser(String name, String username) {
		ClientUser tempUser = new ClientUser();
		tempUser.setName(name);
		tempUser.setUsername(username);
		users.add(tempUser);
	}
	
	/** addCustomNode
	 * Adds Passed Node
	 * @param node
	 */
	public void addCustomNode(ClientUserNode node) {
		nodes.add(node);
	}
	
	/** getUsers() */
	public ArrayList<ClientUser> getUsers() {
		return users;
	}

	/** write
	 * Loops through the arraylist and adds it to a ByteBuffer then writes it to file
	 */
	public void write() {
		nodes.clear();
		ClientUserNode node = new ClientUserNode(rootNode, 0, true);
		nodes.add(node);
		for (int i = 0; i < users.size(); i+=1) {
			ClientUser tempUser = users.get(i);
			addUserNodes(tempUser);
		}
		//Create a bytebuffer that will be the entire XML file
		ByteBuffer toWrite = new ByteBuffer(); 
		//Add top node
		nodes.add(new ClientUserNode(rootNode, 0, false));
		//Add sub nodes to the path
		toWrite.append(nodes.get(0).getFormattedNode());
		//Local User
		toWrite.append(addLocalUser());
		//Add each user
		for (int i = 1; i < nodes.size(); i+=1) {
			toWrite.append(nodes.get(i).getFormattedNode());
		}
		//Write the file
		try {
			//Write the most upto date version
			toWrite.write(new File(filepath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** getLocalUser() */
	public ClientUser getLocalUser() {
		return localUser;
	}
	
	/** addLocalUser */
	private ByteBuffer addLocalUser() {
		ByteBuffer toWrite = new ByteBuffer();
		//Start
		toWrite.append("\t<localUser>\n");
		//Name
		toWrite.append("\t\t<localName>");
		if (localUser!=null) {
			 toWrite.append(localUser.getUsername());
		}
		toWrite.append("</localName>\n");
		//Pub Key
		toWrite.append("\t\t<localPubKey>");
		if (localUser!=null) {
			 toWrite.append(localUser.getPublicKey().toString());
		}
		toWrite.append("</localPubKey>\n");
		//Pub Mod
		toWrite.append("\t\t<localPubMod>");
		if (localUser!=null) {
			 toWrite.append(localUser.getPublicMod().toString());
		}
		toWrite.append("</localPubMod>\n");
		//Priv Key
		toWrite.append("\t\t<localPrivKey>");
		if (localUser!=null) {
			 toWrite.append(localUser.getPrivKey().toString());
		}
		toWrite.append("</localPrivKey>\n");
		//Priv Mod
		toWrite.append("\t\t<localPrivMod>");
		if (localUser!=null) {
			 toWrite.append(localUser.getPrivMod().toString());
		}
		toWrite.append("</localPrivMod>\n");
		//Last User
		toWrite.append("\t\t<lastUser>");
		toWrite.append(data.getCurrentUser());
		toWrite.append("</lastUser>\n");
		//End
		toWrite.append("\t</localUser>\n");
		return toWrite;
	}
	
	/** read() 
	 * reads an xml file and then loads it in as a properly formatted array
	 */
	public void read() {
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
			ClientSaxParser parser = new ClientSaxParser(data);
			saxParser.parse(is, parser);
			ArrayList<ClientUser> users2 = parser.getUserList();
			if (users2 != null) {
				users = users2;
			}
			localUser = parser.getLocalUser();
		} catch (FileNotFoundException e) {
			File file = new File(filepath);
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println("File Probably Is Empty");
			//e.printStackTrace();
		}
	}

	/** getUser */
	public ClientUser getUser(String name) {
		for (int i = 0; i < users.size(); i+=1) {
			if (users.get(i).getUsername().equals(name)) {
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
