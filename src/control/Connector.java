package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Connector {
	
	private int port = 8899;
	private String server = "localhost";
	
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private boolean isConnected = false;
	
	private Connector() {
		
	}
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Object read() {
		try {
			return in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean connect() {
		try {
			Logger.println("Connecting...");
			s = new Socket(server,port);
			Logger.println("Connected to socket");
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			isConnected = true;
			Logger.println("I/O initialized");
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Logger.println("Unknown host");
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Logger.println("IO Exception");
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

