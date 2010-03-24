package control;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;

import javax.swing.JOptionPane;

import cipher.RSAHardware;

import remote.obj.AuthHandler;

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
		if (b != null) {
			System.out.println("auth succeed");
		} else {
			System.out.print("shit");
		}
		
		
	}
	
}
