package control;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import cipher.RSAHardware;

import remote.obj.AuthHandler;
import remote.obj.ClientCallback;
import remote.obj.DisconnHandler;

public class ServerTester {
	private static String username = "1234";
	private static String pwd = "PQ34fyVB";
	private static SecretKeySpec sessionKey;
	private static byte[] lomsg;
	
	public static int login(String username, String pwd) throws RemoteException, NotBoundException, NoSuchAlgorithmException {
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
			if (rsaHard.initJavaCard("285921800006") == -1) {
				JOptionPane.showMessageDialog(null, "Java Card cannot be initialized");
				return 2;
			}
			b = rsaHard.decrypt(b, b.length);
			sessionKey = new SecretKeySpec(b, "aes");
			lomsg = ah.getLoMsg(username);
			return 0;
		} else {
			System.out.print("shit");
			return 4;
		}
		
	}
	
	public static int logout(String username, byte[] lomsg) throws RemoteException, NotBoundException {
		Registry r = LocateRegistry.getRegistry("localhost", 1099);
		AuthHandler ah = (AuthHandler)r.lookup("AuthHandler");
		RSAHardware rsaHard = new RSAHardware();
		
		Cipher c;
		try {
			c = Cipher.getInstance("aes");
			c.init(Cipher.DECRYPT_MODE, sessionKey);
			lomsg = c.doFinal(lomsg);
			if (rsaHard.initJavaCard("285921800099") == -1) {
				JOptionPane.showMessageDialog(null, "Java Card cannot be initialized");
				return 2;
			}
			lomsg = rsaHard.sign(lomsg, lomsg.length);
			if (ah.logout(username, lomsg)) {
				System.out.println("DIU OK!");
				return 0;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 4;
	}
	
	
	public static void main(String args[]) throws Exception {
		Registry reg = LocateRegistry.createRegistry(11111);
		ClientCallback ccb = new ClientCallbackImpl();
		reg.bind("ClientCallback", ccb);
		login(username, pwd);
		System.out.println(Thread.currentThread().getName()+"Waiting for logout");
		Thread.sleep(10000);
		System.out.println(Thread.currentThread().getName()+"Waiting for logout");
		if (logout(username, lomsg) != 0) {
			System.out.println("logout failed");
		} else {
			System.out.println("logout successful");
		}
		if (UnicastRemoteObject.unexportObject(reg, true)) {
			System.out.println();
		}
		System.out.println(Thread.activeCount());
		System.exit(0);
	}
	
}
