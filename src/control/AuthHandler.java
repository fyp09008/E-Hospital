package control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import UI.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;


import message.AuthRequestMessage;
import message.AuthResponseMessage;
import message.ServerAuthRequestMessage;
import message.ServerAuthResponseMessage;
import cipher.RSAHardware;
import cipher.RSASoftware;

public class AuthHandler extends Handler{
	
	private String name = null;
	private String password = null;
	
	public boolean authenicate() {
		//first time connection
		if (!Connector.getInstance().isConnected()) {
			Connector.getInstance().connect();
		} 
		//check if it is connected to the server
		if (Connector.getInstance().isConnected()){
			
			//do authentication
			name = Client.getInstance().getName();
			password = Client.getInstance().getPassword();
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Authenticating name: "+name);
			Client.getInstance().getLogger().debug(this.getClass().getName(), "Authenticating password: "+password);
			AuthRequestMessage reqMsg = new AuthRequestMessage();
			reqMsg.setUsername(this.name);
			
			//sign and hash the password
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("md5");
				Client.getInstance().getLogger().debug(this.getClass().getName(),"Before initializing jc");
				RSAHardware rsaHard = Client.getInstance().getRSAHard();
				if (rsaHard.initJavaCard("285921800099") == -1){
					JOptionPane.showMessageDialog(null, "Java Card cannot be initialized");
					Connector.getInstance().disconnect();
					Client.getInstance().resetTimer(Task.PRE_AUTH);
					return false;
				}
				Client.getInstance().getLogger().debug(this.getClass().getName(),"After initializing jc");
				Client.getInstance().getLogger().printPlain(this.getClass().getName(), 
						"Unsigned password",this.password.getBytes(),this.password);
			
				byte[] hashedPw = rsaHard.sign(md.digest(this.password.getBytes()), 
						md.digest(this.password.getBytes()).length);
				String hashedPwString = new String(hashedPw);
				Client.getInstance().getLogger().printCipher(this.getClass().getName(), 
						"signed password",hashedPw,hashedPwString);
				reqMsg.setPassword(hashedPw);
				//case: password cannot be set to the request message
				if ( reqMsg.getPassword() == null){
					JOptionPane.showMessageDialog(null, "Internal program failure\n " +
							"Please restart the program");
					Connector.getInstance().disconnect();
			    	Client.getInstance().resetTimer(Task.PRE_AUTH);
					return false;
				}
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			
		    try {
		    	Connector connector = Connector.getInstance();
				connector.write((Object) encryptPAES(objToBytes(reqMsg)));
				
			    Client.getInstance().getLogger().debug(this.getClass().getName(),"Authentication message sent");
			    AuthResponseMessage reMsg = (AuthResponseMessage)bytesToObj(decryptPAES((byte[])connector.read()));
			    
			    //case: successful authentication
			    if (reMsg.isAuth) {
			    	Client.getInstance().getLogger().debug(this.getClass().getName(),"Authentication succeed");
			    	//get session key first
			    	Client.getInstance().getLogger().debug(this.getClass().getName(),"Decrypt session key");
			    	//decrypt the session key given by the server
			    	byte[] sKey = decryptRSA(reMsg.sessionKey);
			    	//add to the Client
			    	Client.getInstance().setSkeySpec( new SecretKeySpec(sKey, "AES"));
			    	Client.getInstance().getLogger().debug(this.getClass().getName(),"Session key added to the Client");
			    	//receive privileges and decrypt with session key
			    	
			    	//no decryption
			    	String cipher = new String(reMsg.resultSet);
			    	Client.getInstance().getLogger().printCipher(this.getClass().getName(), 
			    			"Encrypted ResultSet from, server", reMsg.resultSet, cipher);
			    	
			    	byte[] rs = decryptAES(reMsg.resultSet);
			    	cipher = new String(rs);
			    	Client.getInstance().getLogger().printPlain(this.getClass().getName(), 
			    			"Decrypted ResultSet from, server", rs, cipher);
			    	ResultSet rs1 = (ResultSet)bytesToObj(rs);
			    	String[][] rs2 = null;
					try {
						rs2 = RSparse(rs1);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					PrivilegeHandler pl = Client.getInstance().getPrivilegeHandler();
					//the first row is the ID and privileges
			    	pl.setPrivilege(rs2[0]);
			    	//first column in first row is the ID
			    	Client.getInstance().setID(rs2[0][0]);
			    	
			    	Client.getInstance().getLogger().debug(this.getClass().getName(),"Initialize RSAHard");
			    	Client.getInstance().getRSAHard().initJavaCard("285921800099");
			    	
			    	//get the logout msg, sign it and store in signedLogoutMsg
			    	Client.getInstance().getLogger().debug(this.getClass().getName(),"Sign logout message");
			    	
			        int value = 0;
			        for (int i = 0; i < 4; i++) {
			            int shift = (4 - 1 - i) * 8;
			            value += (reMsg.logoutmsg[i + 0] & 0x000000FF) << shift;
			        }
			    	
			    	cipher = new String(reMsg.logoutmsg);
			    	Client.getInstance().getLogger().printCipher(this.getClass().getName(),
			    			"Encrypted logout message from server", reMsg.logoutmsg,Integer.toString(value));
			    	
			    	byte[] decryptedLogoutMsg = decryptAES(reMsg.logoutmsg);
			    	
			        value = 0;
			        for (int i = 0; i < 4; i++) {
			            int shift = (4 - 1 - i) * 8;
			            value += (decryptedLogoutMsg[i + 0] & 0x000000FF) << shift;
			        }
			    	
			    	
			    	cipher = new String(decryptedLogoutMsg);
			    	Client.getInstance().getLogger().printCipher(this.getClass().getName(),
			    			"Decrypted logout message from server", decryptedLogoutMsg,Integer.toString(value));
			    	byte[] signedLogoutMsg = Client.getInstance().getRSAHard().sign(decryptedLogoutMsg, 
			    		decryptedLogoutMsg.length);
			    	Client.getInstance().getLogger().debug(this.getClass().getName(),"logout message signed and placed");
			    	
			    	//init and LogoutHandler and set the signed logout message 
			    	Client.getInstance().initLogoutHandler(signedLogoutMsg);
			    	
			    	//erase the password in memory as soon as possible
			    	this.password=null;
			    	Client.getInstance().resetTimer(Task.AFTER_AUTH);
			    	return true;
			    
			    //case: authentication failed
			    } else {
			    	JOptionPane.showMessageDialog(null, "Username/Password Incorrect\nPlease try again");
			    	connector.disconnect();
			    	this.password=null;
			    	connector.setConnected(false);
			    	Client.getInstance().resetTimer(Task.PRE_AUTH);
			    	return false;
			    }
		    } catch (Exception e){
		    	Connector connector = Connector.getInstance();
				e.printStackTrace();
				connector.disconnect();
				this.password=null;
				connector.setConnected(false);
				Client.getInstance().resetTimer(Task.PRE_AUTH);
				return false;
			}
		}
		else 
			return false;
	}
	
}
