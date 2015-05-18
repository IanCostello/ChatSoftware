package me.iancostello.chat;

import java.math.BigInteger;
import java.util.ArrayList;

public class User {
	//Variables
	private String name;
	private String encrypted;
	private String salt;
	private String IP;
	private int port;
	private ArrayList<String> friends;
	private BigInteger publicKey, publicMod;
	
	/** Blank Constructor */
	public User() {
		 setFriends(new ArrayList<String>());
	}
	
	/** Filled Constructor */
	public User(String name) {
		this.name = name;
	}
	
	/** toString */
	public String toString() {
		String s = "Name: " + getName() + " Encrypted: "+ getEncrypted() + " Salt: "+ getSalt() + " IP: "+ getIP();
		return s;
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
	 * @return the encrypted
	 */
	public String getEncrypted() {
		return encrypted;
	}
	/**
	 * @param encrypted the encrypted to set
	 */
	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}
	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	/**
	 * @return the iP
	 */
	public String getIP() {
		return IP;
	}
	/**
	 * @param iP the iP to set
	 */
	public void setIP(String iP, int port) {
		IP = iP;
		this.port = port;
	}
	
	public void setIP(String iP) {
		IP = iP;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}

	public ArrayList<String> getFriends() {
		return friends;
	}

	public void addFriend(String user) {
		friends.remove(user);
		friends.add(user);
	}
	
	public void removeFriend(String user) {
		friends.remove(user);
	}
	
	public void setFriends(ArrayList<String> friends) {
		this.friends = friends;
	}

	public String getPublicKey() {
		if (publicKey == null) {
			return "";
		}
		return publicKey.toString();
	}

	public void setPublicKey(String publicKey) {
		BigInteger bi = new BigInteger(publicKey);
		this.publicKey = bi;
	}

	public String getPublicMod() {
		if (publicMod == null) {
			return "";
		}
		return publicMod.toString();
	}

	public void setPublicMod(String publicMod) {
		BigInteger bi = new BigInteger(publicMod);
		this.publicMod = bi;
	}
	
}
