package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

//singleton class
public class Connector {
	
	private int port = 8899;
	private String server = "localhost";
	
	//private Socket s;
	//private ObjectOutputStream out;
	//private ObjectInputStream in;
	private boolean isConnected = false;
	private Registry r;
	
	private Connector() {}
	
	private static class ConnectorHolder { 
	     private static final Connector INSTANCE = new Connector();
	 }
	 
	public static Connector getInstance() {
	     return ConnectorHolder.INSTANCE;
	}
	/*   
	public boolean write(Object m) {
		try {
			out.writeObject(m);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	*/
	/*
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
	*/
	public boolean connect() {
		try {
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Connecting...");
			//s = new Socket(server,port);
			r = LocateRegistry.getRegistry("localhost", 1099);
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Connected to server");
			//out = new ObjectOutputStream(s.getOutputStream());
			//in = new ObjectInputStream(s.getInputStream());
			isConnected = true;
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Server/Client I/O initialized");
			return true;
			/*
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Unknown host");
			return false;
			*/
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Client.getInstance().getLogger().debug(this.getClass().getName(),"IO Exception");
			return false;
		}
	}
	
	
	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	public Registry getRegistry(){
		return r;
	}
	public void disconnect() {
	    try {
			r = null;
	    	//out.close();
			//in.close();
			//s.close();
			isConnected = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

