package me.iancostello.chatC;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** ClientSaxParser
 * Handles the actual reading of XML files server side 
 */
public class ClientSaxParser extends DefaultHandler {
	//Storing Users Read In
	private ArrayList<ClientUser> users;
	private ClientUser tempUser;
	private ClientUser localUser = new ClientUser();
	private DataInterface data;
	//Variables For Getting Certain Information
	boolean bName;
	boolean bUser;
	boolean bMessages;
	boolean bPubKey;
	boolean bPubMod;
	boolean bLocalName;
	boolean bLPubKey;
	boolean bLPubMod;
	boolean bLPrivKey;
	boolean bLPrivMod;
	boolean bLastUser;
	
	public ClientSaxParser(DataInterface data) {
		this.data = data;
	}
	
	/** startElement */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("user")) {
			tempUser = new ClientUser();
			if (users == null) {
				users = new ArrayList<ClientUser>();
			}
		} else if (qName.equalsIgnoreCase("username")) {
			bUser = true;
		} else if (qName.equalsIgnoreCase("name")) {
			bName = true;
		} else if (qName.equalsIgnoreCase("messages")) {
			bMessages = true;
		} else if (qName.equalsIgnoreCase("pubKey")) {
			bPubKey = true;
		} else if (qName.equalsIgnoreCase("pubMod")) {
			bPubMod = true;
		} else if (qName.equalsIgnoreCase("localName")) {
			bLocalName = true;
		} else if (qName.equalsIgnoreCase("localPubKey")) {
			bLPubKey = true;
		} else if (qName.equalsIgnoreCase("localPubMod")) {
			bLPubMod = true;
		} else if (qName.equalsIgnoreCase("localPrivKey")) {
			bLPrivKey = true;
		} else if (qName.equalsIgnoreCase("localPrivMod")) {
			bLPrivMod = true;
		} else if (qName.equalsIgnoreCase("lastUser")) {
			bLastUser = true;
		}
	}

	/** endElement */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("user")) {
			users.add(tempUser);
		}
	}
	
	/** characters*/
	@Override
    public void characters(char ch[], int start, int length) throws SAXException {
		String s = new String(ch, start, length);
		if (bName) {
			tempUser.setName(s);
			bName = false;
		} else if (bUser) {
			tempUser.setUsername(s);
			bUser = false;
		} else if (bMessages) {
			tempUser.setMessages(s);
			bMessages = false;
		} else if (bPubKey) {
			tempUser.setPublicKey(s);
			bPubKey = false;
		} else if (bPubMod) {
			tempUser.setPublicMod(s);
			bPubMod = false;
		}  else if (bLocalName) {
			localUser.setUsername(s);
			bLocalName = false;
		}  else if (bLPubKey) {
			localUser.setPublicKey(s);
			bLPubKey = false;
		}  else if (bLPubMod) {
			localUser.setPublicMod(s);
			bLPubMod = false;
		}  else if (bLPrivKey) {
			localUser.setPrivKey(s);
			bLPrivKey = false;
		}  else if (bLPrivMod) {
			localUser.setPrivMod(s);
			bLPrivMod = false;
		} else if (bLastUser) {
			int number = 0;
			try {
				number = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			data.setCurrentUser(number);
			bLastUser = false;
		}
	}

	/** endDocument */
	@Override
	public void endDocument() {
		System.out.println("End Of Client Document");
	}
	
	/** getUserList
	 * returns list of users
	 * @return List users
	 */
	public ArrayList<ClientUser> getUserList() {
		return users;
	}
	
	/** getLocalUser */
	public ClientUser getLocalUser() {
		return localUser;
	}
}
