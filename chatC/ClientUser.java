package chatC;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import me.iancostello.sec.CostelloKeyPair;
import me.iancostello.util.ByteBuffer;

public class ClientUser {
	private String name;
	private String username;
	private ArrayList<String> messages = new ArrayList<String>();
	private BigInteger publicKey;
	private BigInteger publicMod;
	private BigInteger privKey;
	private BigInteger privMod;
	private boolean online;
	
	public void generateKeys() {
		CostelloKeyPair kp = new CostelloKeyPair();
		BigInteger[] keys = kp.getKeys(512);
		publicMod = keys[0];
		publicKey = keys[1];
		privMod = keys[2];
		privKey = keys[3];
	}
	
	public BigInteger getPublicKey() {
		if (publicKey == null) {
			return null;
		}
		return publicKey;
	}

	public String getPublicKeyString() {
		if (publicKey==null) {
			return "";
		}
		return publicKey.toString();
	}
	
	public void setPublicKey(String publicKey) {
		BigInteger bi = new BigInteger(publicKey);
		this.publicKey = bi;
	}

	public String getPublicModString() {
		if (publicMod==null) {
			return "";
		}
		return publicMod.toString();
	}
	
	public BigInteger getPublicMod() {
		return publicMod;
	}

	public void setPublicMod(String publicMod) {
		BigInteger bi = new BigInteger(publicMod);
		this.publicMod = bi;
	}
	
	public BigInteger getPrivKey() {
		return privKey;
	}

	public void setPrivKey(String privKey) {
		BigInteger bi = new BigInteger(privKey);
		this.privKey = bi;
	}

	public BigInteger getPrivMod() {
		return privMod;
	}

	public void setPrivMod(String privMod) {
		BigInteger bi = new BigInteger(privMod);
		this.privMod = bi;
	}
	
	public void setMessages(String s) {
		int start = 0;
		for (int i = 0; i < s.length(); i+=1) {
			if (s.charAt(i) == ('^') && (s.charAt(i+1) == '`')) {
				messages.add(s.substring(start, i));
				start = i+2;
				i = start;
			}
		}
	}
	
	/** Getters and Setters */
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getFileMessages() {
		String s = "";
		for (int i = 0; i < messages.size(); i+=1) {
			s += messages.get(i);
			s += "^`";
		}
		return s;
	}
	
	public ArrayList<String> getMessages() {
		return messages;
	}
	
	public void addMessage(String s, boolean userMessage) {
		String t = "";
		if (userMessage) { t += "0"; }
		else { t+= "1";}
		messages.add(t+s);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the online
	 */
	public boolean isOnline() {
		if (username.equals("welcomeBot")) {
			return true;
		} else {
			return online;
		}
	}
	
	public void setActive(boolean online) {
		this.online = online;
	}
}
