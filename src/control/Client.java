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

import com.ibm.jc.JCard;

public class Client {

		   // Private constructor prevents instantiation from other classes
		   private Client() {
			   rsa = new RSASoftware();
			   rsaHard = new RSAHardware();
			   pHandler = new PrivilegeHandler();
			   aHandler = new AuthHandler();
			   qHandler = new QueryHandler();
			  // sc = new SoftCard();
			  
			   //logout handler initialized by aHandler later
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

	//private byte[] signedLogoutMsg = null;
		   
	//public SoftCard  sc = null;
	private Timer t = null;
	private MainFrame mf;
	private String id = null;
	private String name = null;
	private String password = null;
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
		rsa = new RSASoftware();
		rsaHard = new RSAHardware();
		pHandler = new PrivilegeHandler();
		aHandler = new AuthHandler();
		qHandler = new QueryHandler();
				
		Connector.getInstance().setConnected(false);
		
		this.card = null;
		this.skeySpec = null;
		this.mf.logoutPanel(true);
		Logger.println("Logged out and Reseted all");
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
	
	public void setMf(MainFrame mf) {
		this.mf = mf;
	}
	
	public void re_login()
	{	
		
		new LoginDialog();
	}
	public void reload(){
		Logger.println("in reload, going to resetTimer to after_auth");
		//this.t.cancel();
		mf.restorePanel();
		//Logger.println("######After resotre, going to reinvoke timer");
		//this.t = new Timer();
		Logger.println("6");
		//this.t.schedule(new Task( Task.AFTER_AUTH), new Date(), Task.PERIOD);
		resetTimer(Task.AFTER_AUTH);
	}


	public String[][] sendQuery(String type, String[] table, String[] field,
			String whereClause, String[] values) {
		// TODO Auto-generated method stub
		return qHandler.sendQuery(type, table, field, whereClause, values);
	}

	public boolean logout() {
		// TODO Auto-generated method stub
		return lHandler.logout();
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
		Logger.println("8 " + Thread.currentThread().getName() );
		//t = Client.getInstance().getT();
		t.cancel();
		//t.purge();
		//t = null;
		t = new Timer();
		t.schedule(new Task(Task.WAIT_REAUTH), new Date(),Task.PERIOD);
		//Logger.println("@@@@");
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
	public void resetTimer(int mode){
		if ( t != null)
			t.cancel();
		t = new Timer();
		t.schedule(new Task(mode),new Date(),Task.PERIOD);
	}
}

