package control;

import java.io.IOException;
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
			Logger.println(this.getClass().getName(),"Authenticating name: "+name);
			Logger.println(this.getClass().getName(), "Authenticating password: "+password);
			AuthRequestMessage reqMsg = new AuthRequestMessage();
			reqMsg.setUsername(this.name);
			
			//sign and hash the password
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("md5");
				Logger.println(this.getClass().getName(),"Before initializing jc");
				RSAHardware rsaHard = Client.getInstance().getRSAHard();
				if (rsaHard.initJavaCard("285921800099") == -1){
					JOptionPane.showMessageDialog(null, "Java Card cannot be initialized");
					Connector.getInstance().disconnect();
					Client.getInstance().resetTimer(Task.PRE_AUTH);
					return false;
				}
				Logger.println(this.getClass().getName(),"After initializing jc");
				reqMsg.setPassword(rsaHard.sign(md.digest(this.password.getBytes()), 
						md.digest(this.password.getBytes()).length));
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
				
			    Logger.println(this.getClass().getName(),"Authentication message sent");
			    AuthResponseMessage reMsg = (AuthResponseMessage)bytesToObj(decryptPAES((byte[])connector.read()));
			    
			    //case: successful authentication
			    if (reMsg.isAuth) {
			    	Logger.println(this.getClass().getName(),"Authentication succeed");
			    	//get session key first
			    	Logger.println(this.getClass().getName(),"Decrypt session key");
			    	//decrypt the session key given by the server
			    	byte[] sKey = decryptRSA(reMsg.sessionKey);
			    	//add to the Client
			    	Client.getInstance().setSkeySpec( new SecretKeySpec(sKey, "AES"));
			    	Logger.println(this.getClass().getName(),"Session key added to the Client");
			    	//receive privileges and decrypt with session key
			    	byte[] rs = decryptAES(reMsg.resultSet);
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
			    	
			    	Logger.println(this.getClass().getName(),"Initialize RSAHard");
			    	Client.getInstance().getRSAHard().initJavaCard("285921800099");
			    	
			    	//get the logout msg, sign it and store in signedLogoutMsg
			    	Logger.println(this.getClass().getName(),"Sign logout message");
			    	byte[] signedLogoutMsg = Client.getInstance().getRSAHard().sign(decryptAES(reMsg.logoutmsg), 
			    			decryptAES(reMsg.logoutmsg).length);
			    	Logger.println(this.getClass().getName(),"logout message signed and placed");
			    	
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
