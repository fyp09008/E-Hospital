package cipher;

import java.math.BigInteger;
import java.security.interfaces.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;

public class RSASoftware {
	private String privateKeyExp, publicKeyExp, modulus;
	private RSAPrivateKey privateKey;
	private RSAPublicKey publicKey;
	
	public String getPrivateKeyExp() {
		return privateKeyExp;
	}
	
	public String getPublicKeyExp() {
		return publicKeyExp;
	}
	
	public String getModulus() {
		return modulus;
	}
	
	public boolean setPrivateKey(String exp, String mod) {
		privateKeyExp = exp;
		modulus = mod;
		
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus, 16), new BigInteger(privateKeyExp, 16));
			
			privateKey = (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivateKeySpec);
			
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public boolean setPublicKey(String exp, String mod) {
		publicKeyExp = exp;
		modulus = mod;
		
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus, 16), new BigInteger(publicKeyExp, 16));
			
			publicKey = (RSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
			
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public byte[] encrypt(byte[] plaintext, int length) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] ciphertext = new byte[128];
			
			cipher.doFinal(plaintext, 0, length, ciphertext);
				
			return ciphertext;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public byte[] decrypt(byte[] ciphertext, int length) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			byte[] plaintext = new byte[128];
			
			int len = cipher.doFinal(ciphertext, 0, length, plaintext);
			
			byte[] result = new byte[len];
			for (int i = 0; i < len; ++i)
				result[i] = plaintext[i];
				
			return result;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public boolean genKey() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair myPair = kpg.generateKeyPair();
			
			privateKey = (RSAPrivateKey) myPair.getPrivate();
			publicKey = (RSAPublicKey) myPair.getPublic();
		} catch (NoSuchAlgorithmException ex) {
			return false;
		}
		
		privateKeyExp = privateKey.getPrivateExponent().toString(16);
		publicKeyExp = publicKey.getPublicExponent().toString(16);
		modulus = publicKey.getModulus().toString(16);
		
		return true;
	}
}
