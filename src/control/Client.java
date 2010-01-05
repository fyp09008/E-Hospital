package control;

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
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import message.*;
import cipher.RSASoftware;
import message.*;

import com.ibm.jc.JCard;



public class Client {
	private static int NUM_OF_PRIL = 3;
	//0 = read, 1 = write, 2 = add
	private int[] privileges = new int[NUM_OF_PRIL];
	private String[] stringPrivileges = new String[NUM_OF_PRIL];
	private String id = null;
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
	public String[] getStringPrivileges(){
		return stringPrivileges;
	}
	public String getID(){
		return id;
	}
	public int[] getPrivileges(){
		return privileges;
	}
	public Client() {
		rsa = new RSASoftware();
	}
	public void setName(String name){
		this.name = name;
	}
	public void setPassword(String name){
		this.password = password;
	}
	public String getName(){
		return name;
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
	
	public String[][] sendQuery(String type, String[] table, String[] field, 
			String whereClause) {
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
			QueryRequestMessage qmsg = new QueryRequestMessage();
			qmsg.query = encryptAES(query.getBytes());
			qmsg.username = this.name;
			try {
				out.writeObject(qmsg);
				QueryResponseMessage reqmsg = (QueryResponseMessage)in.readObject();
				byte[] rawResultSet = decryptAES(reqmsg.ResultSet);
				ResultSet rs = (ResultSet)new ObjectInputStream(new 
						ByteArrayInputStream(rawResultSet)).readObject();
				try {
					return RSparse(rs);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	/*By pizza*/
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
	public boolean authenicate() {
		if (connect()) {
			//TODO separate into class
			AuthRequestMessage reqMsg = new AuthRequestMessage();
			reqMsg.setUsername(this.name);
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("md5");
				reqMsg.setPassword(md.digest(this.password.getBytes()));
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		    
		    try {
				out.writeObject(reqMsg);
			
			    System.out.println("Sent");
			    
			    AuthResponseMessage reMsg = (AuthResponseMessage)in.readObject();
			    if (reMsg.isAuth) {
			    	System.out.println("Success!");
			    	reMsg = (AuthResponseMessage)in.readObject();
			    	
			    	byte[] sKey = decryptRSA(reMsg.sessionKey);
			    	skeySpec = new SecretKeySpec(sKey, "AES");
			    	//disconnect();
			    	this.password=null;
			    	return true;
			    } else {
			    	System.out.println("Fail!");
			    	disconnect();
			    	this.password=null;
			    	return false;
			    }
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				disconnect();
				this.password=null;
				return false;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.password=null;
				return false;
			}
		}
		this.password=null;
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
