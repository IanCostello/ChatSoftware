package me.iancostello.chat;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** IanSaxParser
 * Handles reading the Server XML
 */
public class IanSaxParser extends DefaultHandler {
	//Storing Users Read In
	private List<User> users;
	private User tempUser;
	//Variables For Getting Certain Information
	boolean bName;
	boolean bEncrypted;
	boolean bSalt;
	boolean bPubKey;
	boolean bPubMod;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("user")) {
			tempUser = new User();
			if (users == null) {
				users = new ArrayList<>();
			}
		} else if (qName.equalsIgnoreCase("name")) {
			bName = true;
		} else if (qName.equalsIgnoreCase("pass")) {
			bEncrypted = true;
		} else if (qName.equalsIgnoreCase("salt")) {
			bSalt = true;
		} else if (qName.equalsIgnoreCase("pubKey")) {
			bPubKey = true;
		} else if (qName.equalsIgnoreCase("pubMod")) {
			bPubMod = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("user")) {
			users.add(tempUser);
		}
	}
	
	@Override
    public void characters(char ch[], int start, int length) throws SAXException {
		String s = new String(ch, start, length);
		if (bName) {
			tempUser.setName(s);
			bName = false;
		} else if (bEncrypted) {
			tempUser.setEncrypted(s);
			bEncrypted = false;
		} else if (bSalt) {
			tempUser.setSalt(s);
			bSalt = false;
		} else if (bPubKey) {
			tempUser.setPublicKey(s);
			bPubKey = false;
		} else if (bPubMod) {
			tempUser.setPublicMod(s);
			bPubMod = false;
		}
	}

	@Override
	public void endDocument() {
		System.out.println("End Of Server Document");
	}
	
	/** getUserList
	 * returns list of users
	 * @return List users
	 */
	public List<User> getUserList() {
		return users;
	}
}
