package control;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import message.AuthRequestMessage;
import message.AuthResponseMessage;
import message.ServerAuthRequestMessage;
import message.ServerAuthResponseMessage;
import cipher.RSASoftware;

public class AuthHandler extends Handler{
	
	private String id = null;
	private String name = null;
	private String password = null;
	
	public boolean authenicate() {
		if (!Connector.getInstance().isConnected()) {
			
			Connector.getInstance().connect();
			
		} else {
			//TODO separate into class
			
			ServerAuthRequestMessage sarm = new ServerAuthRequestMessage();
			try {
				Connector.getInstance().write((Object) encryptPAES(objToBytes(sarm)));
				ServerAuthResponseMessage response = (ServerAuthResponseMessage)BytesToObj(decryptPAES((byte[])Connector.getInstance().read()));
				RSASoftware rsaServer = new RSASoftware();
				rsaServer.setPublicKey(this.getPub(), getMod());
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
					Connector.getInstance().disconnect();

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
					Connector.getInstance().disconnect();

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

}
