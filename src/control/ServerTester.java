package control;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import cipher.RSAHardware;

import remote.obj.AuthHandler;
import remote.obj.ClientCallback;
//import remote.obj.DisconnHandler;
import remote.obj.DataHandler;

public class ServerTester {
	private static String username = "mcfung";
	private static String pwd = "859sLAcI";
	private static SecretKeySpec sessionKey;
	private static byte[] lomsg;
	
	public static ResultSet query(String query, String[] param) throws RemoteException, NotBoundException
	{
		Registry r = LocateRegistry.getRegistry("localhost", 1099);
		DataHandler dh = (DataHandler) r.lookup("DataHandler");
		try {
			Cipher c = Cipher.getInstance("aes");
			c.init(Cipher.ENCRYPT_MODE, sessionKey);
			byte[] q = c.doFinal(query.getBytes());
			byte[] p = c.doFinal(Utility.objToBytes(param));
			c = Cipher.getInstance("aes");
			c.init(Cipher.DECRYPT_MODE, sessionKey);
			return (ResultSet) Utility.BytesToObj(c.doFinal(dh.query(username, q, p)));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTester", e.getMessage());
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTester", e.getMessage());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTester", e.getMessage());
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTest", e.getMessage());
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTest", e.getMessage());
		}
		return null;
	}
	
	public static void update(String query, String[] param) throws RemoteException, NotBoundException
	{
		Registry r = LocateRegistry.getRegistry("localhost", 1099);
		DataHandler dh = (DataHandler) r.lookup("DataHandler");
		try {
			Cipher c = Cipher.getInstance("aes");
			c.init(Cipher.ENCRYPT_MODE, sessionKey);
			byte[] q = c.doFinal(query.getBytes());
			byte[] p = c.doFinal(Utility.objToBytes(param));
			dh.update(username, q, p);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTest", e.getMessage());
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTest", e.getMessage());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTest", e.getMessage());
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTest", e.getMessage());
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTest", e.getMessage());
		}
	}
	
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

		return 0;
		
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

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTester", e.getMessage());
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTester", e.getMessage());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTester", e.getMessage());
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTester", e.getMessage());
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("ServerTester", e.getMessage());
		}
		
		return 4;
	}
	
	
	public static void main(String args[]) throws Exception {
		Registry reg = LocateRegistry.createRegistry(11111);
		ClientCallback ccb = new ClientCallbackImpl();
		reg.bind("ClientCallback", ccb);
		login(username, pwd);


		System.exit(0);
	}
	
}
