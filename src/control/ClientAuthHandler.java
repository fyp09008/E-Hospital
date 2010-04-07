package control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import UI.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import remote.obj.AuthHandler;


import message.AuthRequestMessage;
import message.AuthResponseMessage;
import message.ServerAuthRequestMessage;
import message.ServerAuthResponseMessage;
import cipher.RSAHardware;
import cipher.RSASoftware;

public class ClientAuthHandler extends Handler{
	
	private String name = null;
	private String password = null;
	private AuthHandler ah = null;
	
	public AuthHandler getRemoteAuthHandler(){
		return ah;
	}
	public boolean authenicate() {
		//first time connection
		Connector connector = Connector.getInstance();
		Client client = Client.getInstance();
		Logger logger = client.getLogger();
		
		if (!connector.isConnected()) {
			connector.connect();
		} 
		//check if it is connected to the server
		if (connector.isConnected()){
			//get remote obj AuthHandler (not client's one)
			
			try {
				ah = (AuthHandler)connector.getRegistry().lookup("AuthHandler");
			} catch (AccessException e2) {
				JOptionPane.showMessageDialog(null, "Server Unavailable");
				Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Access Exception");
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (RemoteException e2) {
				JOptionPane.showMessageDialog(null, "Server Unavailable");
				Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Exception");
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (NotBoundException e2) {
				JOptionPane.showMessageDialog(null, "Server Unavailable");
				Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Not Bound Exception");
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			//do authentication
			name = client.getName();
			password = client.getPassword();
			logger.debug(this.getClass().getName(),"Authenticating name: "+name);
			logger.debug(this.getClass().getName(), "Authenticating password: "+password);
			//AuthRequestMessage reqMsg = new AuthRequestMessage();
			//reqMsg.setUsername(this.name);
			
			//sign and hash the password
			MessageDigest md = null;
			byte[] hashedPw = null;
			try {
				md = MessageDigest.getInstance("md5");
				//****
				client.getT().cancel();
				try {
					Thread.sleep(Task.PERIOD);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.debug(this.getClass().getName(),"Before initializing jc");
				RSAHardware rsaHard = client.getRSAHard();
				if (rsaHard.initJavaCard("285921800099") == -1){
					JOptionPane.showMessageDialog(null, "Java Card cannot be initialized");
					Connector.getInstance().disconnect();
					Client.getInstance().resetTimer(Task.PRE_AUTH);
					return false;
				}
				logger.debug(this.getClass().getName(),"After initializing jc");
				logger.printPlain(this.getClass().getName(), 
						"Unsigned password",this.password.getBytes(),this.password);
			
				hashedPw = rsaHard.sign(md.digest(this.password.getBytes()), 
						md.digest(this.password.getBytes()).length);
				String hashedPwString = new String(hashedPw);
				Client.getInstance().getLogger().printCipher(this.getClass().getName(), 
						"signed password",hashedPw,hashedPwString);
				//reqMsg.setPassword(hashedPw);
				
				//case: password cannot be set to the request message
				//no longer valid
				/*if ( reqMsg.getPassword() == null){
					JOptionPane.showMessageDialog(null, "Internal program failure\n " +
							"Please restart the program");
					Connector.getInstance().disconnect();
			    	Client.getInstance().resetTimer(Task.PRE_AUTH);
					return false;
				}*/
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			
		    try {
		    	//Connector connector = Connector.getInstance();
				//connector.write((Object) encryptPAES(objToBytes(reqMsg)));
		    	byte[] encryptedPKeyName = encryptPAES(name.getBytes());
		    	byte[] encryptedSKey = ah.authenticate(encryptedPKeyName, encryptPAES(hashedPw));
				
			    logger.debug(this.getClass().getName(),"Authentication message sent");
			   
			    //AuthResponseMessage reMsg = (AuthResponseMessage)bytesToObj(decryptPAES((byte[])connector.read()));
			    
			    //case: successful authentication
			    if (encryptedSKey != null) {
			    	logger.debug(this.getClass().getName(),"Authentication succeed");
			    	//get session key first
			    	logger.debug(this.getClass().getName(),"Decrypt session key");
			    	//decrypt the session key given by the server
			    	//byte[] sKey = decryptRSA(reMsg.sessionKey);
			    	byte[] sKey = decryptRSA(decryptPAES(encryptedSKey));
			    	//add to the Client
			    	client.setSkeySpec( new SecretKeySpec(sKey, "AES"));
			    	logger.debug(this.getClass().getName(),"Session key added to the Client");
			    	
			    	//receive privileges 
			    	
			    	byte[] encryptedPrivil = ah.getPrivilege(encryptedPKeyName);
			    	//no decryption
			    	String cipher = new String(encryptedPrivil);
			    	logger.printCipher(this.getClass().getName(), 
			    			"Encrypted ResultSet from, server",encryptedPrivil, cipher);
			    	
			    	byte[] rs = decryptAES(decryptPAES(encryptedPrivil));
			    	cipher = new String(rs);
			    	logger.printPlain(this.getClass().getName(), 
			    			"Decrypted ResultSet from, server", rs, cipher);
			    	ResultSet rs1 = (ResultSet)bytesToObj(rs);
			    	String[][] rs2 = null;
					try {
						rs2 = RSparse(rs1);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					PrivilegeHandler pl = client.getPrivilegeHandler();
					//the first row is the ID and privileges
			    	pl.setPrivilege(rs2[0]);
			    	//first column in first row is the ID
			    	client.setID(rs2[0][0]);
			    	
			    	logger.debug(this.getClass().getName(),"Initialize RSAHard");
			    	client.getRSAHard().initJavaCard("285921800099");
			    	
			    	//get the logout msg, sign it and store in signedLogoutMsg
			    	logger.debug(this.getClass().getName(),"Sign logout message");
			    	
			    	byte[] encryptedLogoutMsg = ah.getLoMsg(encryptedPKeyName);
			        int value = 0;
			        for (int i = 0; i < 4; i++) {
			            int shift = (4 - 1 - i) * 8;
			            value += (encryptedLogoutMsg[i + 0] & 0x000000FF) << shift;
			        }
			    	
			    	cipher = new String(encryptedLogoutMsg);
			    	Client.getInstance().getLogger().printCipher(this.getClass().getName(),
			    			"Encrypted logout message from server", encryptedLogoutMsg,Integer.toString(value));
			    	
			    	byte[] decryptedLogoutMsg = decryptAES(decryptPAES(encryptedLogoutMsg));
			    	
			        value = 0;
			        for (int i = 0; i < 4; i++) {
			            int shift = (4 - 1 - i) * 8;
			            value += (decryptedLogoutMsg[i + 0] & 0x000000FF) << shift;
			        }
			    	
			    	
			    	cipher = new String(decryptedLogoutMsg);
			    	logger.printCipher(this.getClass().getName(),
			    			"Decrypted logout message from server", decryptedLogoutMsg,Integer.toString(value));
			    	byte[] signedLogoutMsg = client.getRSAHard().sign(decryptedLogoutMsg, 
			    		decryptedLogoutMsg.length);
			    	logger.debug(this.getClass().getName(),"logout message signed and placed");
			    	
			    	//init and LogoutHandler and set the signed logout message 
			    	client.initLogoutHandler(signedLogoutMsg);
			    	
			    	//erase the password in memory as soon as possible
			    	this.password=null;
			    	client.resetTimer(Task.AFTER_AUTH);
			    	return true;
			    
			    //case: authentication failed
			    } else {
			    	JOptionPane.showMessageDialog(null, "Username/Password Incorrect\nPlease try again");
			    	connector.disconnect();
			    	this.password=null;
			    	connector.setConnected(false);
			    	client.resetTimer(Task.PRE_AUTH);
			    	return false;
			    }
		    } catch (Exception e){
		    	//Connector connector = Connector.getInstance();
				e.printStackTrace();
				connector.disconnect();
				this.password=null;
				connector.setConnected(false);
				client.resetTimer(Task.PRE_AUTH);
				return false;
			}
		}
		else 
			return false;
	}
	
}
