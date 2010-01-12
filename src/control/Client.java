package control;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.NotSerializableException;
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
import java.util.Date;
import java.util.Timer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import message.*;
import UI.LoginDialog;
import UI.MainFrame;
import UI.Task;
import cipher.RSAHardware;
import cipher.RSASoftware;
import message.*;

import com.ibm.jc.JCard;




public class Client {
	private static byte[] fingerprint = {(byte)0x16,(byte)0x27,(byte)0xac,(byte)0xa5,
			(byte)0x76,(byte)0x28,(byte)0x2d,(byte)0x36,
			(byte)0x63,(byte)0x1b,(byte)0x56,(byte)0x4d,(byte)0xeb,(byte)0xdf,(byte) 0xa6 , (byte)0x48 };
	
	private final String pub = "10001";
	private final String mod = "a89877a7e5150456b696d40a9a35ac5ce72cf331ed6463bb05a658a98962739a244d770e78f70e0dd1c07404e2e77aaf9dba6ff3ee21a38a5555c1cbd28a2f7fed603b25a9cf8a6ff1a330503c882b300d855a9c315aa7eec4fca5ee3e7ca351b7e086309de90d2ad4183a606352b052b0c990856df7b3a106f76a48ea004a19";

	
	private static final byte[] key = {-19, -11, 122, 111, -37, -13, 16, -47, -65, 78, -126, -128, -88, 54, 101, 86};
	private static final SecretKeySpec ProgramKey = new SecretKeySpec(key, "AES");
	
	private byte[] signedLogoutMsg = null;
	private static int NUM_OF_PRIL = 3;
	//0 = read, 1 = write, 2 = add
	private Timer t;
	private MainFrame mf;
	private String[] privileges = null;
	private String[] stringPrivileges = null;
	private String id = null;
	private String name = "null";
	private String password = "null";
	private int port = 8899;
	private String server = "localhost";
	private RSASoftware rsa = null;
	private boolean isConnected = false;
	private RSAHardware rsaHard = null;
	//perm
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private JCard card;
	private SecretKeySpec skeySpec;
	
	

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
			e.printStackTrace();
			return null;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} return null;
	}
	
	public Object BytesToObj(byte[] b){
	      ByteArrayInputStream bis = new ByteArrayInputStream(b); 
	      ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bis);
			Object obj = ois.readObject();
			ois.close(); 
			bis.close();
			return obj;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    return null;
	}
	 
	
	public void reset(){
		this.rsa = null;
		this.rsaHard = null;
		this.id = null;
		this.name = null;
		this.password = null;
		this.privileges = null;
		this.stringPrivileges = null;
		signedLogoutMsg = null;
		this.isConnected = false;
		this.s = null;
		this.out = null;
		this.in = null;
		this.card = null;
		this.skeySpec = null;
		this.mf.logoutPanel(true);
		System.out.println("Reseted all");
	}
	public boolean isConnected(){
		return isConnected;
	}
	public String[] getStringPrivileges(){
		return stringPrivileges;
	}
	public String getID(){
		return id;
	}
	public void setID(String s){
		id = s;
	}
	public String[] getPrivileges(){
		return privileges;
	}
	public Client() {
		//rsa = new RSASoftware();
		//rsaHard = new RSAHardware();
	}
	public void setName(String name){
		this.name = name;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getName(){
		return name;
	}
	public boolean connect() {
		System.out.println("try to connect");
		try {
			rsa = new RSASoftware();
			rsaHard = new RSAHardware();
			System.out.println("Connecting...");
			s = new Socket(server,port);
			System.out.println("Connected");
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			isConnected = true;
			System.out.println("is Connected = true");
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

	public byte[] encryptPAES(byte[] plaintext) {
		try {
			Cipher cipher = Cipher.getInstance("aes");
			cipher.init(Cipher.ENCRYPT_MODE, ProgramKey);
			
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
	
	public byte[] decryptPAES(byte[] ciphertext) {
		try {
			Cipher cipher = Cipher.getInstance("aes");
			cipher.init(Cipher.DECRYPT_MODE, ProgramKey);
			
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
	
	public byte[] decryptRSA(byte[] ciphertext) {
		
		rsaHard.initJavaCard("285921800006");
			
		byte plaintext[] = rsaHard.decrypt(ciphertext, ciphertext.length);
		return plaintext;
	
	}
	
	public static final byte TYPE_SELECT = 0;
	public static final byte TYPE_UPDATE = 1;
	public static final byte TYPE_INSERT = 2;

	public String[][] sendQuery(String type, String[] table, String[] field, 
			String whereClause, String[] value) {
		//TODO make it more generic
		//TODO separate into class
		type = type.toLowerCase();
		byte TYPE = -1;
		if (type.equals("select")) {
			TYPE = TYPE_SELECT;
		} else if (type.equals("update")) {
			TYPE = TYPE_UPDATE;
		} else if (type.equals("insert")) {
			TYPE = TYPE_INSERT;
		}
		String query;
		switch (TYPE) {
			case TYPE_SELECT:{
				query = "select ";
				for (int i = 0; i < field.length; i++) {
					query = i == field.length -1 ? query +field[i] : query + field[i] + ",";
				}
						
				query = query + " from ";
						
				for(int i = 0; i < table.length; i++) {
					query = i == table.length -1 ? query +table[i] : query + table[i] + ",";
				}
						
				query = query + " where " + whereClause;
				System.out.println(query);
				break;
			}
			case TYPE_UPDATE:{
				query = "update ";
				for(int i = 0; i < table.length; i++) {
					query = i == table.length -1 ? query +table[i] : query + table[i] + ",";
				}
				query += " set ";
				
				for (int i = 0; i < field.length; i++) {
					if ( i != field.length-1)
						if ( ! value[i].equals("NOW()"))
								query+= field[i] + " = '" + value[i] + "' , ";
						else
							query+= field[i] + " = " + value[i] + " , ";
					else
						query += field[i] + " = '" + value[i] + "' ";
				}
			
				query = query + " where " + whereClause;
				System.out.println(query);
				break;
			}
			case TYPE_INSERT:
				query = "insert ";
				break;
			default:
				return null;	
		}

			
		if(isConnected) {
			QueryRequestMessage qmsg = null;
			switch(TYPE){
				case TYPE_SELECT: qmsg = new QueryRequestMessage(); break;
				case TYPE_UPDATE: {qmsg = new UpdateRequestMessage();
					((UpdateRequestMessage)qmsg).type = encryptAES(type.getBytes());
					break;}
				
				case TYPE_INSERT: qmsg = new UpdateRequestMessage();
					((UpdateRequestMessage)qmsg).type = type.getBytes();break;

			}
			qmsg.query = encryptAES(query.getBytes());
			
			qmsg.username = this.name;
			try {
				out.writeObject((Object) encryptPAES(objToBytes(qmsg)));
				Object reqmsg = BytesToObj(decryptPAES((byte[])in.readObject()));
				if ( reqmsg instanceof QueryResponseMessage){
					QueryResponseMessage qrm = (QueryResponseMessage)reqmsg;
					byte[] rawResultSet = decryptAES(qrm.resultSet);
					ResultSet rs = byteToRS(rawResultSet);
					try {
						return RSparse(rs);
					} catch (SQLException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (reqmsg instanceof UpdateResponseMessage)
				{
					UpdateResponseMessage urm = (UpdateResponseMessage)reqmsg;
					String[][] s = new String[1][1];
					s[0][0] = Boolean.toString(urm.getStatus());
					return s;
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
	/*by chun, byte array to ResultSet*/
	public ResultSet byteToRS(byte[] rawResultSet){
		ResultSet rs = null;
		try {
			rs = (ResultSet)new ObjectInputStream(new 
				ByteArrayInputStream(rawResultSet)).readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
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
	public void setStringPrivilege(String[] pri){
		this.stringPrivileges = new String[NUM_OF_PRIL];
		if ( pri[0].equals("true"))
			this.stringPrivileges[0] = "Read";
		if ( pri[1].equals("true"))
			this.stringPrivileges[1] = "Write";
		if ( pri[2].equals("true"))
			this.stringPrivileges[2] = "Add";
		
		for(int i = 0 ; i < stringPrivileges.length; i++)
			System.out.println(stringPrivileges[i]);
	}
	public void setPrivilege(String[] pri){
		this.privileges = new String[NUM_OF_PRIL];
		for( int i = 1; i < pri.length; i++){
			System.out.println(pri[i]);
			this.privileges[i-1] = pri[i];
		}
		
    	for ( int i = 0;i < this.privileges.length; i++)
    		System.out.println(this.privileges[i]);
	}
	public boolean logout(){
		if (isConnected){
			DisconnRequestMessage msg = new DisconnRequestMessage();
			System.out.println("****"+signedLogoutMsg);
			msg.setSignature(signedLogoutMsg);
			try {
				out.writeObject((Object) encryptPAES(objToBytes(msg)));
				DisconnResponseMessage  msg2 = (DisconnResponseMessage)BytesToObj(decryptPAES((byte[])in.readObject()));
				if (msg2.getStatus()){
					this.reset();
					return true;
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}

	public boolean authenicate() {
		if ( ! isConnected)
			this.connect();
		if (isConnected) {
			//TODO separate into class
			
			ServerAuthRequestMessage sarm = new ServerAuthRequestMessage();
			try {
				out.writeObject((Object) encryptPAES(objToBytes(sarm)));
				ServerAuthResponseMessage response = (ServerAuthResponseMessage)BytesToObj(decryptPAES((byte[])in.readObject()));
				RSASoftware rsaServer = new RSASoftware();
				rsaServer.setPublicKey(this.pub, mod);
				byte[] fpReceived = response.getEncryptedFingerprint();
				fpReceived = rsaServer.unsign(fpReceived, fpReceived.length);
				if (!cmpByteArray(fpReceived,fingerprint)) {
					return false;
				}
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				return false;
			}
			
			System.out.println("name:"+name);
			System.out.println("password:"+password);
			AuthRequestMessage reqMsg = new AuthRequestMessage();
			reqMsg.setUsername(this.name);
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("md5");
				System.out.println("before inin jc");
				t.cancel();
				if (rsaHard.initJavaCard("285921800099") == -1){
					JOptionPane.showMessageDialog(null, "init card Fail");
					disconnect();
					isConnected = false;
			    	t = new Timer();
			    	t.schedule(new Task(t, mf, Task.PRE_AUTH), new Date(), Task.PERIOD);
					return false;
				}
				System.out.println("after inin jc");
				System.out.println("before sign"+this.password);
				reqMsg.setPassword(rsaHard.sign(md.digest(this.password.getBytes()), md.digest(this.password.getBytes()).length));
				System.out.println("after sign"+reqMsg.getPassword());
				if ( reqMsg.getPassword() == null){
					JOptionPane.showMessageDialog(null, "Fail");
					disconnect();
					isConnected = false;
			    	t = new Timer();
			    	t.schedule(new Task(t, mf, Task.PRE_AUTH), new Date(), Task.PERIOD);
					return false;
				}
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		    try {
				out.writeObject((Object) encryptPAES(objToBytes(reqMsg)));
			
			    System.out.println("Sent");
			    
			    AuthResponseMessage reMsg = (AuthResponseMessage)BytesToObj(decryptPAES((byte[])in.readObject()));
			    if (reMsg.isAuth) {
			    	System.out.println("Success!");
			    	//get session key first
			    	byte[] sKey = decryptRSA(reMsg.sessionKey);
			    	
			    	skeySpec = new SecretKeySpec(sKey, "AES");
			    	
			    	
			    	//receive privileges and decrypt with session key
			    	byte[] rs = decryptAES(reMsg.resultSet);
			    	ResultSet rs1 = byteToRS(rs);
			    	String[][] rs2 = null;
					try {
						rs2 = RSparse(rs1);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
			    	this.setPrivilege(rs2[0]);
			    	this.setID(rs2[0][0]);
			    	for ( int i = 0;i < this.privileges.length; i++)
			    		System.out.println(this.privileges[i]);
			    	this.setStringPrivilege(this.privileges);
			    	
			    	rsaHard.initJavaCard("285921800099");
			    	//get the logout msg, sign it and store in signedLogoutMsg
			    	signedLogoutMsg = rsaHard.sign(decryptAES(reMsg.logoutmsg), 
			    			decryptAES(reMsg.logoutmsg).length);
			    	//signedLogoutMsg = decryptAES(reMsg.logoutmsg);
			    	
			    	//disconnect();
			    	this.password=null;
			    	t = new Timer();
			    	t.schedule(new Task(t, mf, Task.AFTER_AUTH), new Date(), Task.PERIOD);
			    	return true;
			    } else {
			    	System.out.println(reMsg.isAuth);
			    	JOptionPane.showMessageDialog(null, "auth Fail");
			    	disconnect();
			    	this.password=null;
			    	disconnect();
			    	isConnected = false;
			    	t = new Timer();
			    	t.schedule(new Task(t, mf, Task.PRE_AUTH), new Date(), Task.PERIOD);
			    	return false;
			    }
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				disconnect();
				this.password=null;
				isConnected = false;
		    	t = new Timer();
		    	t.schedule(new Task(t, mf, Task.PRE_AUTH), new Date(), Task.PERIOD);
				return false;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.password=null;
				disconnect();
				isConnected = false;
		    	t = new Timer();
		    	t.schedule(new Task(t, mf, Task.PRE_AUTH), new Date(), Task.PERIOD);
				return false;
			} catch (Exception e){
				e.printStackTrace();
				disconnect();
				this.password=null;
				isConnected = false;
		    	t = new Timer();
		    	t.schedule(new Task(t, mf, Task.PRE_AUTH), new Date(), Task.PERIOD);
				return false;
			}
		}
		this.password=null;
		disconnect();
		isConnected = false;
    	t = new Timer();
    	t.schedule(new Task(t, mf, Task.PRE_AUTH), new Date(), Task.PERIOD);
		return false;
	}
	
	private boolean cmpByteArray(byte[] b1, byte[] b2) {
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

	public void disconnect() {
		    try {
				out.close();
				in.close();
				s.close();
				isConnected = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	}

	public Timer getT() {
		return t;
	}

	public void setT(Timer t) {
		this.t = t;
	}

	public void setMf(MainFrame mf) {
		this.mf = mf;
	}
	
	public void re_login()
	{	
		new LoginDialog(mf,true,this);
	}
	public void reload(){
		t.cancel();
		mf.restorePanel();
		t = new Timer();
		t.schedule(new Task(t, mf, Task.AFTER_AUTH), new Date(), Task.PERIOD);
	}
	
	public void card_unplug()
	{
		t.cancel();
		t = new Timer();
		t.schedule(new Task(t, mf, Task.WAIT_REAUTH), new Date(),Task.PERIOD);
	}

	
}
}
