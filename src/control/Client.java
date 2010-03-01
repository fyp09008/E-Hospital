package control;

import java.awt.Frame;
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
import UI.*;
import cipher.RSAHardware;
import cipher.RSASoftware;
import message.*;

import com.ibm.jc.JCard;

public class Client {

		   // Private constructor prevents instantiation from other classes
		   private Client() {
			   System.out.println("inited client");
			   rsa = new RSASoftware();
			   rsaHard = new RSAHardware();
			   pHandler = new PrivilegeHandler();
			   aHandler = new AuthHandler();
			   qHandler = new QueryHandler();
		   }
		 
		   /**
		    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
		    * or the first access to SingletonHolder.INSTANCE, not before.
		    */
		   private static class ClientHolder { 
		     private static final Client INSTANCE = new Client();
		   }
		 
		   public static Client getInstance() {
		     return ClientHolder.INSTANCE;
		   }

	private byte[] signedLogoutMsg = null;
	private Timer t;
	private MainFrame mf;
	
	private String id = null;
	private String name = null;
	private String password = null;
	//private SecretKeySpec skeySpec = null;
	//private int port = 8899;
	//private String server = "localhost";
	//private boolean isConnected = false;
	//private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private RSASoftware rsa = null;
	private RSAHardware rsaHard = null;
	private JCard card;
	private SecretKeySpec skeySpec;
	private AuthHandler aHandler = null;
	private LogoutHandler lHandler = null;
	private PrivilegeHandler pHandler = null;
	private QueryHandler qHandler = null;	
	
	public PrivilegeHandler getPrivilegeHandler(){
		return pHandler;
	}
	public boolean authenticate(){
		return aHandler.authenicate();
	}
	public void initLogoutHandler(byte[] a){
		lHandler = new LogoutHandler(a);
	}
	public void reset(){
		
		this.id = null;
		this.name = null;
		this.password = null;
		
		this.pHandler = null;
		
		signedLogoutMsg = null;
		//LogoutHandler = null;
		
		Connector.getInstance().setConnected(false);
		
		this.card = null;
		this.skeySpec = null;
		this.mf.logoutPanel(true);
		System.out.println("Reseted all");
	}
	
	public boolean isConnected(){
		return Connector.getInstance().isConnected();
	}
	
	public String getID(){
		return id;
	}
	public void setID(String s){
		id = s;
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
	
	/*public boolean connect() {
		System.out.println("try to connect");
		try {
			rsa = new RSASoftware();
			rsaHard = new RSAHardware();
			System.out.println("Connecting...");
			s = new Socket(server,port);
			System.out.println("Connected");
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			System.out.println("in out ok");
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
	}*/
	
	/*public void disconnect() {
		    try {
				out.close();
				in.close();
				s.close();
				isConnected = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	}*/

	public void setMf(MainFrame mf) {
		this.mf = mf;
	}
	
	public void re_login()
	{	
		new LoginDialog();
	}
	public void reload(){
		t.cancel();
		mf.restorePanel();
		t = new Timer();
		t.schedule(new Task(t, Task.AFTER_AUTH), new Date(), Task.PERIOD);
	}


	public String[][] sendQuery(String type, String[] table, String[] field,
			String whereClause, String[] values) {
		// TODO Auto-generated method stub
		return qHandler.sendQuery(type, table, field, whereClause, values);
	}

	public boolean logout() {
		// TODO Auto-generated method stub
		return false;
	}
	public MainFrame getMf() {
		return mf;
	}
	public Timer getT() {
		return t;
	}

	public void setT(Timer t) {
		this.t = t;
	}
	
	public void card_unplug()
	{
		t.cancel();
		t = new Timer();
		t.schedule(new Task(t, Task.WAIT_REAUTH), new Date(),Task.PERIOD);
	}

	public RSAHardware getRSAHard(){
		return rsaHard;
	}
	public RSASoftware getRSASoft(){
		return rsa;
	}
	public String getPassword(){
		return password;
	}
	public SecretKeySpec getSkeySpec() {
		return skeySpec;
	}
	public void setSkeySpec(SecretKeySpec skeySpec) {
		this.skeySpec = skeySpec;
	}

		
}

