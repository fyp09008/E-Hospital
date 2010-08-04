package ehospital.client.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * High level encapsulation for all handlers and provide utility for handlers.  Handlers are responsible for communicating with the server and return feedback  to the client. Note to developers: the Utility Class in this package does provide utilities. Please use Utility Class for the sake of clarity. But some of the 'old functions' has been  using the utility functions here, they were kept to maintain stability.
 * @author   Gilbert
 */
public class Handler {
	
	protected static byte[] fingerprint = {(byte)0x16,(byte)0x27,(byte)0xac,(byte)0xa5,
		(byte)0x76,(byte)0x28,(byte)0x2d,(byte)0x36,
		(byte)0x63,(byte)0x1b,(byte)0x56,(byte)0x4d,(byte)0xeb,(byte)0xdf,(byte) 0xa6 , (byte)0x48 };

	/**
	 * @uml.property  name="pub"
	 */
	private final String pub = "10001";
	/**
	 * @uml.property  name="mod"
	 */
	private final String mod = "a89877a7e5150456b696d40a9a35ac5ce72cf331ed6463bb05a658a98962739a244d770e78f70e0dd1c07404e2e77aaf9dba6ff3ee21a38a5555c1cbd28a2f7fed603b25a9cf8a6ff1a330503c882b300d855a9c315aa7eec4fca5ee3e7ca351b7e086309de90d2ad4183a606352b052b0c990856df7b3a106f76a48ea004a19";


	private static final byte[] key = {-19, -11, 122, 111, -37, -13, 16, -47, -65, 78, -126, -128, -88, 54, 101, 86};
	private static final SecretKeySpec ProgramKey = new SecretKeySpec(key, "AES");
	
	//private JCard card;

	/**
	 * @param obj Object that is serializable
	 * @return byte array
	 * @deprecated
	 */
	public byte[] objToBytes(Object obj){
	      ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	      ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush(); 
			oos.close(); 
			bos.close();
			byte [] data = bos.toByteArray();
			return data;
		} catch (NotSerializableException e){
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			return null;
		}catch (IOException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} return null;
	}
	
	/**
	 * @param b
	 * @return Object
	 * @deprecated
	 */
	public Object bytesToObj(byte[] b){
	      ByteArrayInputStream bis = new ByteArrayInputStream(b); 
	      ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bis);
			Object obj = ois.readObject();
			ois.close(); 
			bis.close();
			return obj;
		} catch (IOException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (ClassNotFoundException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} 
	    return null;
	}
	
	/**
	 * @param b1
	 * @param b2
	 * @return true if they have the same content
	 * @deprecated
	 */
	protected boolean cmpByteArray(byte[] b1, byte[] b2) {
		if(b1.length != b2.length) {
			return false;
		}
		for(int i = 0; i < b1.length; i++) {
			if(b1[i] != b2[i]) {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * @param plaintext
	 * @return ciphertext
	 * @deprecated
	 */
	public byte[] encryptAES(byte[] plaintext) {
		try {
			Cipher cipher = Cipher.getInstance("aes");
			cipher.init(Cipher.ENCRYPT_MODE, Client.getInstance().getSkeySpec());			
			byte[] ciphertext = cipher.doFinal(plaintext);
			return ciphertext;
		} catch (NoSuchAlgorithmException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			
		} catch (NoSuchPaddingException e) {
			
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			
		} catch (InvalidKeyException e) {
			
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (IllegalBlockSizeException e) {
	
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (BadPaddingException e) {
	
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		return null;
		
	}
	
	/**
	 * @param ciphertext
	 * @return plaintext
	 * @deprecated
	 */
	public byte[] decryptAES(byte[] ciphertext) {
		try {
			Cipher cipher = Cipher.getInstance("aes");
			cipher.init(Cipher.DECRYPT_MODE, Client.getInstance().getSkeySpec());
			
			byte[] plaintext = cipher.doFinal(ciphertext);
			return plaintext;
		} catch (NoSuchAlgorithmException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			
		} catch (NoSuchPaddingException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			
		} catch (InvalidKeyException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (IllegalBlockSizeException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (BadPaddingException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		return null;
		
	}

	/**
	 * encrypted with program key
	 * @param plaintext
	 * @return ciphertext
	 * @deprecated
	 */
	public byte[] encryptPAES(byte[] plaintext) {
		try {
			Cipher cipher = Cipher.getInstance("aes");
			cipher.init(Cipher.ENCRYPT_MODE, ProgramKey);
			
			byte[] ciphertext = cipher.doFinal(plaintext);
			return ciphertext;
		} catch (NoSuchAlgorithmException e) {
			
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			
		} catch (NoSuchPaddingException e) {
	
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			
		} catch (InvalidKeyException e) {
	
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (IllegalBlockSizeException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (BadPaddingException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		return null;
		
	}
	
	/**
	 * Decrypted with program key
	 * @param ciphertext
	 * @return plaintext
	 * @deprecated
	 */
	public byte[] decryptPAES(byte[] ciphertext) {
		try {
			Cipher cipher = Cipher.getInstance("aes");
			cipher.init(Cipher.DECRYPT_MODE, ProgramKey);
			
			byte[] plaintext = cipher.doFinal(ciphertext);
			return plaintext;
		} catch (NoSuchAlgorithmException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			
		} catch (NoSuchPaddingException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			
		} catch (InvalidKeyException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (IllegalBlockSizeException e) {
		
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (BadPaddingException e) {

			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		return null;
		
	}
	
	/**
	 * @param ciphertext
	 * @return plaintext
	 * @deprecated
	 */
	public byte[] decryptRSA(byte[] ciphertext) {
		
		Client.getInstance().getRSAHard().initJavaCard("285921800006");
			
		byte plaintext[] = Client.getInstance().getRSAHard().decrypt(ciphertext, ciphertext.length);
		return plaintext;
	
	}
	/**
	 * Change a resultset object into string[][]
	 * @param result
	 * @return a set of data in 2d array of string
	 * @throws SQLException
	 * @deprecated
	 */
	public static String[][] RSparse(ResultSet result) throws SQLException
	{
		String[][] s = null;
		ArrayList<String[]> slist = new ArrayList<String[]>();
		while (result.next())
		{
			String[] tmp = new String[result.getMetaData().getColumnCount()];
			for (int i = 0; i < result.getMetaData().getColumnCount(); i++)
			{
				tmp[i] = result.getString(i+1);
			}
			slist.add(tmp);
		}
		s = new String[slist.size()][result.getMetaData().getColumnCount()];
		for (int i = 0; i < slist.size(); i++)
		{
			s[i] = slist.get(i);
		}
		return s;
		
	}

	/**
	 * @return   the pub
	 * @uml.property  name="pub"
	 */
	public String getPub() {
		return pub;
	}

	/**
	 * @return   the mod
	 * @uml.property  name="mod"
	 */
	public String getMod() {
		return mod;
	}

	
	
}
