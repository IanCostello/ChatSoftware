package me.iancostello.sec;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import me.iancostello.util.ByteBuffer;

public class SHAHash {
	@SuppressWarnings("unused")
	private static byte[] PasswordPad = { 
		0x28, (byte)0xBF, 0x4E, 0x5E, 0x4E, 0x75, (byte)0x8A, 0x41, 0x64, 0x00, 0x4E, 0x56, (byte)0xFF, (byte)0xFA, 0x01, 0x08,
		0x2E, 0x2E, 0x00, (byte)0xB6, (byte)0xD0, 0x68, 0x3E, (byte)0x80, 0x2F, 0x0C, (byte)0xA9, (byte)0xFE, 0x64, 0x53, 0x69, 0x7A };	

	/**
	  * 
	  * @param passwordToHash
	  * @param salt
	  * @return
	  */
	public String get256Hash(String passwordToHash, String salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;	}

	public String get512Hash(String passwordToHash, String salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			ByteBuffer bBuf = new ByteBuffer(salt);
			bBuf.hexToBytes();
			md.update(bBuf.getBytes(),bBuf.start(),bBuf.end());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;	
	}

	public String get512Hash(String passwordToHash) {
		String salt2 = "";
		try {
			salt2 = getSalt();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		System.out.println(salt2);
		return get512Hash(passwordToHash, salt2);
	}
	
	//Add salt
	public String getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		try {
			return new String(salt, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}