package cipher;

import java.security.interfaces.*;
import java.security.*;

//from Dan Chan
import hku.hk.cs.javacard.JavaCardHelper;
import hku.hk.cs.javacard.JavaCardManager;

//from java card
import com.ibm.jc.JCException;
import com.ibm.jc.JCInfo;
import com.ibm.jc.JCard;

public class RSAHardware {
	private JCard card01;
	private String privateKeyExp, publicKeyExp, modulus;
	private RSAPrivateKey privateKey;
	private RSAPublicKey publicKey;
	
	// post: send the command to the card and return the response in a byte array
	private byte[] sendCmd(int CLA, int INS, int P1, int P2, int LC, byte[] DATA, int LE)
	{	
		// Offcard API: Send data to the card via the current logical channel
		byte[] cardResponse = card01
				.send(0, CLA, INS, P1, P2, LC, DATA, 0, LE);
		
		byte [] data = null;
		
		// Check response from card response
		if (JavaCardHelper.checkStatusWord(cardResponse)) {
			// Remove status word from response data
			data = JavaCardHelper.extractDateFromAPDU(cardResponse);
			//System.out.println(JCInfo.dataToString(data));
		} else {
			System.out.println(JCInfo.toHex(cardResponse));
			System.exit(1);
		}
		
		return data;
	}
	
	// pre: str represents an integer in Hex
	// post: return a byte array of str
	private static byte[] strToByteArray(String s) {
		String HEX_NUMBER = "0123456789abcdef";
		if (s == null)
			return null;
		if (s.length() % 2 != 0)
			s = "0" + s;
		byte[] result = new byte[s.length() / 2];
		for (int i = 0; i < s.length(); i += 2) {
			int i1 = HEX_NUMBER.indexOf(s.charAt(i));
			if (i1 == -1)
				throw new RuntimeException("invalid number");
			int i2 = HEX_NUMBER.indexOf(s.charAt(i + 1));
			if (i2 == -1)
				throw new RuntimeException("invalid number");
			result[i / 2] = (byte) ((i1 << 4) | i2);
		}

		return result;
	}
	
	public int initJavaCard() {
		JavaCardManager jcm = null;

		try {
			// PCSC Card Reader
			jcm = new JavaCardManager(JavaCardManager.JCM_PCSC, "ACS ACR38U 0", "285921800006");
		} catch (JCException ex) {
			return -1;
		}

		card01 = jcm.getCard();
		
		return 0;
	}
	
	public int initJavaCard(String aid) {
		JavaCardManager jcm = null;

		try {
			// PCSC Card Reader
			jcm = new JavaCardManager(JavaCardManager.JCM_PCSC, "ACS ACR38U 0", aid);
		} catch (JCException ex) {
			return -1;
		}

		card01 = jcm.getCard();
		
		return 0;
	}
	
	public String getGeneratedPrivateKeyExp() {
		return privateKeyExp;
	}
	
	public String getGeneratedPublicKeyExp() {
		return publicKeyExp;
	}
	
	public String getGeneratedModulus() {
		return modulus;
	}
	
	public void setPrivateKey(String exp, String mod) {
		privateKeyExp = exp;
		modulus = mod;
		
		byte[] privateKeyExp_byte = strToByteArray(privateKeyExp);
		byte[] modulus_byte = strToByteArray(modulus);
		
		sendCmd(0x13, 0x01, 0x00, 0x00, privateKeyExp_byte.length, privateKeyExp_byte, 0x00);
		sendCmd(0x13, 0x02, 0x00, 0x00, modulus_byte.length, modulus_byte, 0x00);
	}
	
	public void setPublicKey(String exp, String mod) {
		publicKeyExp = exp;
		modulus = mod;
		
		byte[] publicKeyExp_byte = strToByteArray(publicKeyExp);
		byte[] modulus_byte = strToByteArray(modulus);
		
		sendCmd(0x13, 0x03, 0x00, 0x00, publicKeyExp_byte.length, publicKeyExp_byte, 0x00);
		sendCmd(0x13, 0x02, 0x00, 0x00, modulus_byte.length, modulus_byte, 0x00);
	}
	
	public byte[] encrypt(byte[] plaintext, int length) {
		try {
			byte[] ciphertext = sendCmd(0x13, 0x04, 0x00, 0x00, length, plaintext, 128);
			
			return ciphertext;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public byte[] decrypt(byte[] ciphertext, int length) {
		try {
			byte[] plaintext = sendCmd(0x13, 0x05, 0x00, 0x00, length, ciphertext, 128);
			
			return plaintext;
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	public byte[] sign(byte[] plaintext, int length) {
		try {
			byte[] ciphertext = sendCmd(0x13, 0x04, 0x00, 0x00, length, plaintext, 128);
			
			return ciphertext;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public byte[] unsign(byte[] ciphertext, int length) {
		try {
			byte[] plaintext = sendCmd(0x13, 0x05, 0x00, 0x00, length, ciphertext, 128);
			
			return plaintext;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public int genKey() {
		try {
			do {
				KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
				kpg.initialize(1024);
				KeyPair myPair = kpg.generateKeyPair();
				
				privateKey = (RSAPrivateKey) myPair.getPrivate();
				publicKey = (RSAPublicKey) myPair.getPublic();
				
				privateKeyExp = privateKey.getPrivateExponent().toString(16);
				publicKeyExp = publicKey.getPublicExponent().toString(16);
				modulus = publicKey.getModulus().toString(16);
			} while (privateKeyExp.length() != 256 || publicKeyExp.length() != 5 ||
					modulus.length() != 256);
		} catch (NoSuchAlgorithmException ex) {
			return -1;
		}
		
		setPrivateKey(privateKeyExp, modulus);
		setPublicKey(publicKeyExp, modulus);
		
		return 0;
	}
}
