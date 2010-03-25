package control;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import cipher.RSAHardware;

import remote.obj.AuthHandler;
import remote.obj.DisconnHandler;

public class ServerTester {
	private static String username = "1234";
	private static String pwd = "PQ34fyVB";
	
	public static void main(String args[]) throws Exception {
		Registry r = LocateRegistry.getRegistry("localhost", 1099);
		AuthHandler ah = (AuthHandler)r.lookup("AuthHandler");
		MessageDigest md = MessageDigest.getInstance("md5");
		byte a[] = md.digest(pwd.getBytes());
		RSAHardware rsaHard = new RSAHardware();
		if (rsaHard.initJavaCard("285921800099") == -1) {
			JOptionPane.showMessageDialog(null, "Java Card cannot be initialized");
		}
		a = rsaHard.sign(a, a.length);
		byte[] b = ah.authenticate(username, a);
		byte[] lomsg;
		if (b != null) {
			System.out.println("auth succeed");
			lomsg = ah.getLoMsg(username);
		} else {
			System.out.print("shit");
			return;
		}
		
		System.out.println("Waiting for logout");
		Thread.sleep(1000);
		DisconnHandler dch = (DisconnHandler)r.lookup("DisconnHandler"); 
		if (rsaHard.initJavaCard("285921800006") == -1) {
			JOptionPane.showMessageDialog(null, "Java Card cannot be initialized");
		}
		b = rsaHard.decrypt(b, b.length);
		lomsg = rsaHard.decrypt(lomsg, lomsg.length);
		
		dch.logout(username, lomsg);
		
	}
	
}
