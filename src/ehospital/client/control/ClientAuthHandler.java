package ehospital.client.control;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import remote.obj.AuthHandler;
import remote.obj.EmergencyAccessHandler;
import cipher.RSAHardware;


/**
 * A handler class to handle communication to server by client-authentication invoked by client program.
 * @author   Gilbert
 */
public class ClientAuthHandler extends Handler{
	
	private String name = null;
	private String password = null;
	/**
	 * @uml.property  name="ah"
	 * @uml.associationEnd  
	 */
	private AuthHandler ah = null;
	
	
	public AuthHandler getRemoteAuthHandler(){
		return ah;
	}
	@SuppressWarnings("deprecation")
	public void unplugCard(){
		byte[] encryptedPKeyName = encryptPAES(Client.getInstance().getName().getBytes());
		try {
			ah.unplugCard(encryptedPKeyName);
		} catch (RemoteException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
	}
	@SuppressWarnings("deprecation")
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
				Client.getInstance().getLogger().debug(this.getClass().getName(), e2.getMessage());
			} catch (RemoteException e2) {
				JOptionPane.showMessageDialog(null, "Server Unavailable");
				Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Exception");
				Client.getInstance().getLogger().debug(this.getClass().getName(), e2.getMessage());
			} catch (NotBoundException e2) {
				JOptionPane.showMessageDialog(null, "Server Unavailable");
				Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Not Bound Exception");
				Client.getInstance().getLogger().debug(this.getClass().getName(), e2.getMessage());
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
					Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
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

			} catch (NoSuchAlgorithmException e1) {
				Client.getInstance().getLogger().debug(this.getClass().getName(), e1.getMessage());
			}

		    try {

		    	byte[] encryptedPKeyName = encryptPAES(name.getBytes());
		    	byte[] encryptedSKey = ah.authenticate(encryptedPKeyName, encryptPAES(hashedPw));
				
			    logger.debug(this.getClass().getName(),"Authentication message sent");

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
						Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
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
			    	
			    	//callback
			    	

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
		    	Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
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
	@SuppressWarnings("deprecation")
	public boolean changePassword(String name2, String oldPW, String newPW) {
		try {
			ah = (AuthHandler)Connector.getInstance().getRegistry().lookup("AuthHandler");
		} catch (AccessException e2) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Access Exception");
			Client.getInstance().getLogger().debug(this.getClass().getName(), e2.getMessage());
		} catch (RemoteException e2) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Exception");
			Client.getInstance().getLogger().debug(this.getClass().getName(), e2.getMessage());
		} catch (NotBoundException e2) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Not Bound Exception");
			Client.getInstance().getLogger().debug(this.getClass().getName(), e2.getMessage());
		}
		MessageDigest md = null;
		byte[] hashedOldPw = null;
		byte[] hashedNewPw = null;
		try {
			md = MessageDigest.getInstance("md5");
			Client.getInstance().getT().cancel();
			try {
				Thread.sleep(Task.PERIOD);
			} catch (InterruptedException e) {
				Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			}
			RSAHardware rsaHard = Client.getInstance().getRSAHard();
			if (rsaHard.initJavaCard("285921800099") == -1){
				JOptionPane.showMessageDialog(null, "Java Card cannot be initialized");
				Connector.getInstance().disconnect();
				Client.getInstance().resetTimer(Task.AFTER_AUTH);
				return false;
			}
	
			hashedOldPw = rsaHard.sign(md.digest(oldPW.getBytes()), 
					md.digest(oldPW.getBytes()).length);
			hashedNewPw = rsaHard.sign(md.digest(newPW.getBytes()), 
					md.digest(newPW.getBytes()).length);
			Client.getInstance().resetTimer(Task.AFTER_AUTH);
		}catch (NoSuchAlgorithmException e1) {
			Client.getInstance().resetTimer(Task.AFTER_AUTH);
			Client.getInstance().getLogger().debug(this.getClass().getName(), e1.getMessage());
		}
	    try {

	    	byte[] encryptedPKeyName = encryptPAES(name.getBytes());
	    	byte[] result = ah.changePassword(encryptedPKeyName, encryptPAES(hashedOldPw),encryptPAES(hashedNewPw));
	    	Boolean b = (Boolean)this.bytesToObj((decryptPAES(result)));
	    	if (b)
	    		return true;
	    	else 
	    		return false;
		 
	    } catch (Exception e){
	    	Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			return false;
		}
		    	
	}
	@SuppressWarnings("deprecation")
	public boolean authOther(String myName, String hisName, String pw,int cardNo){
		EmergencyAccessHandler eah = null;
		try {
			eah = (EmergencyAccessHandler)Connector.getInstance().getRegistry().lookup("EmergencyAccessHandler");
		} catch (AccessException e2) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Access Exception");
			Client.getInstance().getLogger().debug(this.getClass().getName(), e2.getMessage());
		} catch (RemoteException e2) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Exception");
			Client.getInstance().getLogger().debug(this.getClass().getName(), e2.getMessage());
		} catch (NotBoundException e2) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Remote Not Bound Exception");
			Client.getInstance().getLogger().debug(this.getClass().getName(), e2.getMessage());
		}
		byte[] encryptedMyName = encryptPAES(myName.getBytes());
		byte[] encryptedHisName = encryptPAES(encryptAES(hisName.getBytes()));
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}

		byte[] hashedPw = encryptPAES(encryptAES(md.digest(pw.getBytes())));
		

		int result = 1;
		try {
			result = eah.emergencyAccess(encryptedMyName, encryptedHisName, hashedPw, cardNo);
		} catch (RemoteException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		return (result == 0)?true:false;

	}
	
}
