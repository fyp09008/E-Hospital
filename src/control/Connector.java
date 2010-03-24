package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

//singleton class
public class Connector {
	
	private int port = 8899;
	private String server = "localhost";
	
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean isConnected = false;
	
	private Connector() {}
	
	private static class ConnectorHolder { 
	     private static final Connector INSTANCE = new Connector();
	 }
	 
	public static Connector getInstance() {
	     return ConnectorHolder.INSTANCE;
	}
	   
	public boolean write(Object m) {
		try {
			out.writeObject(m);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Object read() {
		try {
			return in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean connect() {
		try {
			Logger.println(this.getClass().getName(),"Connecting...");
			s = new Socket(server,port);
			Logger.println(this.getClass().getName(),"Connected to server");
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			isConnected = true;
			Logger.println(this.getClass().getName(),"Server/Client I/O initialized");
			return true;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Logger.println(this.getClass().getName(),"Unknown host");
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Logger.println(this.getClass().getName(),"IO Exception");
			return false;
		}
	}
	
	
	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public void disconnect() {
	    try {
			out.close();
			in.close();
			s.close();
			isConnected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

