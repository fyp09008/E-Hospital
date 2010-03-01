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
			//TODO separate into class
		/*System.out.println("Start server auth");
		ServerAuthRequestMessage sarm = new ServerAuthRequestMessage();
		try {
			Connector.getInstance().write((Object) encryptPAES(objToBytes(sarm)));
			ServerAuthResponseMessage response = (ServerAuthResponseMessage)bytesToObj(decryptPAES((byte[])Connector.getInstance().read()));
			RSASoftware rsaServer = new RSASoftware();
			rsaServer.setPublicKey(this.getPub(), getMod());
			byte[] fpReceived = response.getEncryptedFingerprint();
			fpReceived = rsaServer.unsign(fpReceived, fpReceived.length);
			if (!cmpByteArray(fpReceived,fingerprint)) {
				return false;
			}
			System.out.println("finished server auth");
		} catch (Exception e2) {
			
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return false;
		}*/
		name = Client.getInstance().getName();
		password = Client.getInstance().getPassword();
		System.out.println("name:"+name);
		System.out.println("password:"+password);
		AuthRequestMessage reqMsg = new AuthRequestMessage();
		reqMsg.setUsername(this.name);
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("md5");
			System.out.println("before inin jc");
			
			Timer t = Client.getInstance().getT();
			t.cancel();
			RSAHardware rsaHard = Client.getInstance().getRSAHard();
			if (rsaHard.initJavaCard("285921800099") == -1){
				JOptionPane.showMessageDialog(null, "init card Fail");
				Connector.getInstance().disconnect();

		    	t = new Timer();
		    	t.schedule(new Task(t, Task.PRE_AUTH), new Date(), Task.PERIOD);
				return false;
			}
			System.out.println("after inin jc");
			/*byte[] a = md.digest(this.password.getBytes());
			for (int i = 0; i<a.length; i++)
				System.out.print(a[i]) ;
			System.out.println();*/
			System.out.println("before sign"+this.password);
			reqMsg.setPassword(rsaHard.sign(md.digest(this.password.getBytes()), md.digest(this.password.getBytes()).length));
			System.out.println("after sign"+reqMsg.getPassword());
			if ( reqMsg.getPassword() == null){
				JOptionPane.showMessageDialog(null, "Fail");
				Connector.getInstance().disconnect();

		    	t = new Timer();
		    	t.schedule(new Task(t,  Task.PRE_AUTH), new Date(), Task.PERIOD);
				return false;
			}
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    try {
	    	Connector connector = Connector.getInstance();
	    	
			connector.write((Object) encryptPAES(objToBytes(reqMsg)));
		
		    System.out.println("Sent");
		    
		    AuthResponseMessage reMsg = (AuthResponseMessage)bytesToObj(decryptPAES((byte[])connector.read()));
		    if (reMsg.isAuth) {
		    	System.out.println("Success!");
		    	//get session key first
		    	byte[] sKey = decryptRSA(reMsg.sessionKey);
		    	
		    	Client.getInstance().setSkeySpec( new SecretKeySpec(sKey, "AES"));
		    	System.out.println("session key is placed");
		    	
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
		    	pl.setPrivilege(rs2[0]);
		    	Client.getInstance().setID(rs2[0][0]);
		    	for ( int i = 0;i < pl.privileges.length; i++)
		    		System.out.println(pl.privileges[i]);
		    	pl.setStringPrivilege(pl.privileges);
		    	
		    	Client.getInstance().getRSAHard().initJavaCard("285921800099");
		    	//get the logout msg, sign it and store in signedLogoutMsg
		    	byte[] signedLogoutMsg = Client.getInstance().getRSAHard().sign(decryptAES(reMsg.logoutmsg), 
		    			decryptAES(reMsg.logoutmsg).length);
		    	//signedLogoutMsg = decryptAES(reMsg.logoutmsg);
		    	Client.getInstance().initLogoutHandler(signedLogoutMsg);
		    	//disconnect();
		    	this.password=null;
		    	Timer t = Client.getInstance().getT();
		    	t = new Timer();
		    	t.schedule(new Task(t, Task.AFTER_AUTH), new Date(), Task.PERIOD);
		    	return true;
		    } else {
		    	System.out.println(reMsg.isAuth);
		    	JOptionPane.showMessageDialog(null, "auth Fail");
		    	connector.disconnect();
		    	this.password=null;
		    	//disconnect();
		    	connector.setConnected(false);
		    	Timer t = Client.getInstance().getT();
		    	t = new Timer();
		    	t.schedule(new Task(t,  Task.PRE_AUTH), new Date(), Task.PERIOD);
		    	return false;
		    }
	    } catch (Exception e){
	    	Connector connector = Connector.getInstance();
			e.printStackTrace();
			connector.disconnect();
			this.password=null;
			connector.setConnected(false);
			Timer t = Client.getInstance().getT();
	    	t = new Timer();
	    	t.schedule(new Task(t, Task.PRE_AUTH), new Date(), Task.PERIOD);
			return false;
		}
		
		/*this.password=null;
		Connector.getInstance().disconnect();
		Connector.getInstance().setConnected(false);
		Timer t = Client.getInstance().getT();
		t = new Timer();
    	t.schedule(new Task(t,  Task.PRE_AUTH), new Date(), Task.PERIOD);
		return false;*/
	}

}
