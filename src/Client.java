import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;

import javax.crypto.Cipher;

import message.RequestMessage;

import cipher.RSASoftware;

import com.ibm.jc.JCard;

import message.*;

public class Client {
	
	private String name = "qwer";
	private String password = "1234";
	private Socket s;
	private int port = 8899;
	private String server = "localhost";
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private RSASoftware rsa;
	JCard card;
	
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
			
	public String decrypt(byte[] ciphertext) {
		
		//TODO use JavaCard
		FileReader fr;
		try {
			fr = new FileReader("prvkey.txt");
			BufferedReader in = new BufferedReader(fr);
			String prvKeyExp = in.readLine(); 
			String mod = in.readLine();
			rsa.setPrivateKey(prvKeyExp, mod);
			
			byte plaintext[] = rsa.decrypt(ciphertext, ciphertext.length);
			System.out.println(plaintext.length);
			return new String(plaintext);
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
		    	
		    	System.out.println(decrypt(reMsg.sessionKey));
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
