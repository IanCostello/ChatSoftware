package me.iancostello.sec;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import me.iancostello.util.ByteBuffer;

public class CostelloKeyPair {

	/** Constructor */
	public CostelloKeyPair () {

	}

	/** getKeys 
	 * Format pub(mod, exponent) priv(mod, exponent)
	 */
	public BigInteger[] getKeys(int size) {
		KeyPairGenerator kpg;
		try {
			//Generates a KeyGeneratorPair
			kpg = KeyPairGenerator.getInstance("RSA");
			//Inits it with a specific size
			kpg.initialize(size);
			//Generates the keyPair
			KeyPair kp = kpg.genKeyPair();
			//Creates the parameters for the keys
			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
			BigInteger[] keys = new BigInteger[]{pub.getModulus(), pub.getPublicExponent(), priv.getModulus(), priv.getPrivateExponent()};
			return keys;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		} 
		return null;
	}

	public byte[] encrypt(String s, BigInteger encryptionKey, BigInteger encrytionMod) {
		try {
			//Read the Keys in
			BigInteger m = encrytionMod;
			BigInteger e = encryptionKey;
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] cipherData = cipher.doFinal(s.getBytes());
			return cipherData;
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public byte[] decryptHex(ByteBuffer data, BigInteger encryptionKey, BigInteger encrytionMod) {
		try {
			//Convert from hex to binary
			data.hexToBytes();
			//Read the Keys in
			BigInteger m = encrytionMod;
			BigInteger e = encryptionKey;
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey privKey = fact.generatePrivate(keySpec);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			byte[] returnBytes = cipher.doFinal(data.getBytes(), data.start(), data.length());
			return returnBytes;
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public byte[] decrypt(byte[] data, BigInteger encryptionKey, BigInteger encrytionMod) {
		try {
			//Read the Keys in
			BigInteger m = encrytionMod;
			BigInteger e = encryptionKey;
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey privKey = fact.generatePrivate(keySpec);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			byte[] returnBytes = cipher.doFinal(data);
			return returnBytes;
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
