import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import message.RequestMessage;

import com.ibm.jc.JCard;

import message.*;

public class Client {
	
	private String name = "qwer";
	private String password = "1234";
	private int port = 8899;
	private String server = "localhost";
	JCard card;
	
	public void connectServer() {
		Socket s;
		try {
			System.out.println("Connecting...");
			s = new Socket(server,port);
			System.out.println("Connected");
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			System.out.println("Connected");
		    
		    RequestMessage reqMsg = new RequestMessage();
		    reqMsg.username = this.name;
		    reqMsg.password = this.password;
		    
		    out.writeObject(reqMsg);
		    System.out.println("Sent");
		    
		    ResponseMessage reMsg = (ResponseMessage)in.readObject();
		    if (reMsg.isAuth) {
		    	System.out.println("Success!");
		    	reMsg = (ResponseMessage)in.readObject();
		    	System.out.println(reMsg.sessionKey);
		    } else {
		    	System.out.println("Fail!");
		    }
		    out.close();
		    in.close();
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
