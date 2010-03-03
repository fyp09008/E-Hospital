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
	
	//private String id = null;
	private String name = null;
	private String password = null;
	
	public boolean authenicate() {
		if (!Connector.getInstance().isConnected()) {
			Connector.getInstance().connect();
		} 
		
		
		if (Connector.getInstance().isConnected()){

				//TODO separate into class

			//do authentication
	    	
			name = Client.getInstance().getName();
			password = Client.getInstance().getPassword();
			Logger.println("Authenticating name:"+name);
			Logger.println("password: " + password);
			AuthRequestMessage reqMsg = new AuthRequestMessage();
			reqMsg.setUsername(this.name);
			//sign and hash the password
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("md5");
				Logger.println("before inin jc");
				//Client.getInstance().getT().cancel();
				
				RSAHardware rsaHard = Client.getInstance().getRSAHard();
				if (rsaHard.initJavaCard("285921800099") == -1){
					JOptionPane.showMessageDialog(null, "init card Fail");
					Connector.getInstance().disconnect();
					Logger.println("1");
					Client.getInstance().resetTimer(Task.PRE_AUTH);
			    	//t = new Timer();
			    	//t.schedule(new Task( Task.PRE_AUTH), new Date(), Task.PERIOD);
					return false;
				}
				Logger.println("after inin jc");
				reqMsg.setPassword(rsaHard.sign(md.digest(this.password.getBytes()), md.digest(this.password.getBytes()).length));
				if ( reqMsg.getPassword() == null){
					JOptionPane.showMessageDialog(null, "Fail");
					Connector.getInstance().disconnect();
					Logger.println("2");
			    	Client.getInstance().resetTimer(Task.PRE_AUTH);
					//t = new Timer();
			    	//t.schedule(new Task(  Task.PRE_AUTH), new Date(), Task.PERIOD);
					return false;
				}
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		    try {
		    	Connector connector = Connector.getInstance();
		    	
				connector.write((Object) encryptPAES(objToBytes(reqMsg)));
			
			    Logger.println("Sent authentication message");
			    
			    AuthResponseMessage reMsg = (AuthResponseMessage)bytesToObj(decryptPAES((byte[])connector.read()));
			    if (reMsg.isAuth) {
			    	Logger.println("Authentication succeed!");
			    	//get session key first
			    	Logger.println("decrypt session key");
			    	byte[] sKey = decryptRSA(reMsg.sessionKey);
			    	Client.getInstance().setSkeySpec( new SecretKeySpec(sKey, "AES"));
			    	Logger.println("placed session key");
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
			    	//first column = ID
			    	Client.getInstance().setID(rs2[0][0]);
			    	//set the privileges
			    	pl.setStringPrivilege(pl.privileges);
			    	
			    	Client.getInstance().getRSAHard().initJavaCard("285921800099");
			    	Logger.println("init rsaHard");
			    	//get the logout msg, sign it and store in signedLogoutMsg
			    	Logger.println("Before sign");
			    	byte[] signedLogoutMsg = Client.getInstance().getRSAHard().sign(decryptAES(reMsg.logoutmsg), 
			    			decryptAES(reMsg.logoutmsg).length);
			    	Logger.println("After sign");
			    	//init and LogoutHandler and set the signed logout message 
			    	Client.getInstance().initLogoutHandler(signedLogoutMsg);
			    	//erase the password in memory as soon as possible
			    	this.password=null;
			    	//Logger.println("HERE");
			    	//Timer t = Client.getInstance().getT();
			    	//t = new Timer();
			    	//Logger.println("3");
			    	//t.schedule(new Task( Task.AFTER_AUTH), new Date(), Task.PERIOD);
			    	System.out.println("Going to set to after_auth right after auth");
			    	Client.getInstance().resetTimer(Task.AFTER_AUTH);
			    	return true;
			    } else {
			    	//authentication failed
			    	JOptionPane.showMessageDialog(null, "Wrong Password");
			    	connector.disconnect();
			    	this.password=null;
			    	connector.setConnected(false);
			    	Logger.println("4");
			    	//Timer t = Client.getInstance().getT();
			    	//t = new Timer();
			    	//t.schedule(new Task(  Task.PRE_AUTH), new Date(), Task.PERIOD);
			    	Client.getInstance().resetTimer(Task.PRE_AUTH);
			    	return false;
			    }
		    } catch (Exception e){
		    	Connector connector = Connector.getInstance();
				e.printStackTrace();
				connector.disconnect();
				this.password=null;
				connector.setConnected(false);
				//Timer t = Client.getInstance().getT();
				Logger.println("5");
		    	//t = new Timer();
		    	//t.schedule(new Task( Task.PRE_AUTH), new Date(), Task.PERIOD);
				Client.getInstance().resetTimer(Task.PRE_AUTH);
				return false;
			}
		}
		else 
			return false;
	}
	
}
