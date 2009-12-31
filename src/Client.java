import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.crypto.spec.SecretKeySpec;

import message.RequestMessage;
import cipher.RSASoftware;
import message.*;

import com.ibm.jc.JCard;



public class Client {
	//temp
	private String name = "qwer";
	private String password = "1234";
	private int port = 8899;
	private String server = "localhost";
	private RSASoftware rsa;
	//perm
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private JCard card;
	private SecretKeySpec skeySpec;
	
	public Client() {
		rsa = new RSASoftware();
	}
	
	public boolean connect() {
		try {
			System.out.println("Connecting...");
			s = new Socket(server,port);
			System.out.println("Connected");
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public byte[] decrypt(byte[] ciphertext) {
		
		//TODO use JavaCard
		FileReader fr;
		try {
			fr = new FileReader("prvkey.txt");
			BufferedReader in = new BufferedReader(fr);
			String prvKeyExp = in.readLine(); 
			String mod = in.readLine();
			rsa.setPrivateKey(prvKeyExp, mod);
			
			byte plaintext[] = rsa.decrypt(ciphertext, ciphertext.length);
			return plaintext;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
			
	}
	
	
	public void authenicate() {
		RequestMessage reqMsg = new RequestMessage();
		reqMsg.username = this.name;
		//TODO send Hashed Password
	    reqMsg.password = this.password;
	    
	    try {
			out.writeObject(reqMsg);
		
		    System.out.println("Sent");
		    
		    ResponseMessage reMsg = (ResponseMessage)in.readObject();
		    if (reMsg.isAuth) {
		    	System.out.println("Success!");
		    	reMsg = (ResponseMessage)in.readObject();
		    	
		    	byte[] sKey = decrypt(reMsg.sessionKey);
		    	skeySpec = new SecretKeySpec(sKey, "AES");
		    } else {
		    	System.out.println("Fail!");
		    }
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		    try {
				out.close();
				in.close();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	}
	
}
