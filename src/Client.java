import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import message.RequestMessage;
import cipher.RSASoftware;
import message.*;

import com.ibm.jc.JCard;



public class Client {
	//temp
	private String name = "qwer";
	private String password = "1234";
	private int port = 8899;
	private String server = "localhost";
	private RSASoftware rsa;
	//perm
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private JCard card;
	private SecretKeySpec skeySpec;
	
	public Client() {
		rsa = new RSASoftware();
	}
	
	public boolean connect() {
		try {
			System.out.println("Connecting...");
			s = new Socket(server,port);
			System.out.println("Connected");
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public byte[] decryptAES(byte[] ciphertext) {
		try {
			Cipher cipher = Cipher.getInstance("aes");
			cipher.init(Cipher.DECRYPT_MODE, this.skeySpec);
			
			byte[] plaintext = cipher.doFinal(ciphertext);
			return plaintext;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public byte[] encryptAES(byte[] plaintext) {
		try {
			Cipher cipher = Cipher.getInstance("aes");
			cipher.init(Cipher.ENCRYPT_MODE, this.skeySpec);
			
			byte[] ciphertext = cipher.doFinal(plaintext);
			return ciphertext;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public byte[] decryptRSA(byte[] ciphertext) {
		
		//TODO use JavaCard
		FileReader fr;
		try {
			fr = new FileReader("prvkey.txt");
			BufferedReader in = new BufferedReader(fr);
			String prvKeyExp = in.readLine(); 
			String mod = in.readLine();
			rsa.setPrivateKey(prvKeyExp, mod);
			
			byte plaintext[] = rsa.decrypt(ciphertext, ciphertext.length);
			return plaintext;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;	
	}
	
	public static final byte TYPE_SELECT = 0;
	public static final byte TYPE_DELECT = 1;
	public static final byte TYPE_UPDATE = 2;
	public static final byte TYPE_INSERT = 3;
	
	public ResultSet sendQuery(String type, String[] table, String[] field, String whereClause) {
		//TODO make it more generic
		//TODO separate into class
		
		
		type = type.toLowerCase();
		byte TYPE = -1;
		if (type.equals("select")) {
			TYPE = TYPE_SELECT;
		} else if (type.equals("delete")) {
			TYPE = TYPE_DELECT;
		} else if (type.equals("update")) {
			TYPE = TYPE_UPDATE;
		} else if (type.equals("insert")) {
			TYPE = TYPE_INSERT;
		}
		
		String query;
		switch (TYPE) {
			case TYPE_SELECT:
				query = "select ";
				break;
			case TYPE_DELECT:
				query = "delect";
			break;
			case TYPE_UPDATE:
				query = "update";
				break;
			case TYPE_INSERT:
				query = "insert";
				break;
			default:
				return null;	
		}
		for (int i = 0; i < field.length; i++) {
			query = query + field[i] + ",";
		}
				
		query = query + " from ";
				
		for(int i = 0; i < table.length; i++) {
			query = query + table[i] + ",";
		}
				
		query = query + " where " + whereClause;
			
		if(connect()) {
			QueryMessage qmsg = new QueryMessage();
			qmsg.query = encryptAES(query.getBytes());
			qmsg.username = this.name;
			try {
				out.writeObject(qmsg);
				ResponseQueryMessage reqmsg = (ResponseQueryMessage)in.readObject();
				byte[] rawResultSet = decryptAES(reqmsg.ResultSet);
				ResultSet rs = (ResultSet)new ObjectInputStream(new 
						ByteArrayInputStream(rawResultSet)).readObject();
				return rs;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public boolean authenicate() {
		if (connect()) {
			//TODO separate into class
			RequestMessage reqMsg = new RequestMessage();
			reqMsg.username = this.name;
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("md5");
				reqMsg.password = md.digest(this.password.getBytes());
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		    
		    try {
				out.writeObject(reqMsg);
			
			    System.out.println("Sent");
			    
			    ResponseMessage reMsg = (ResponseMessage)in.readObject();
			    if (reMsg.isAuth) {
			    	System.out.println("Success!");
			    	reMsg = (ResponseMessage)in.readObject();
			    	
			    	byte[] sKey = decryptRSA(reMsg.sessionKey);
			    	skeySpec = new SecretKeySpec(sKey, "AES");
			    	return true;
			    } else {
			    	System.out.println("Fail!");
			    	return false;
			    }
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public void disconnect() {
		    try {
				out.close();
				in.close();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	}
	
	
	
}
