package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

import javax.swing.JOptionPane;

//singleton class
public class Connector {
	
	private int port = 8899;
	private String server = "localhost";
	public static Registry reg;

	private boolean isConnected = false;
	private Registry r;
	
	private Connector() {}
	
	private static class ConnectorHolder { 
	     private static final Connector INSTANCE = new Connector();
	 }
	 
	public static Connector getInstance() {
	     return ConnectorHolder.INSTANCE;
	}


	public void initCallback(){
		try{
			try {
				reg = LocateRegistry.createRegistry(7788);
			} catch (ExportException e) {
				reg = LocateRegistry.getRegistry(7788);
				Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			}
			
	            String name = "ClientCallback";
	            remote.obj.ClientCallback engine = new ClientCallbackImpl();
	            reg.rebind(name, engine);			            	
	            //log
	            Client.getInstance().getLogger().debug(this.getClass().getName(),"ClientCallbackImpl bound");
	            
	        } catch (Exception e) {
            Client.getInstance().getLogger().debug(this.getClass().getName(),"ClientCallbackImpl exception:");
            Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
        }
	}
	public boolean connect() {
		try {
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Connecting...");
			r = LocateRegistry.getRegistry(Driver.serverPath, 1099);
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Connected to server");

			isConnected = true;
			Client.getInstance().getLogger().debug(this.getClass().getName(),"Server/Client I/O initialized");
			initCallback();
			return true;

		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, "Server Unavailable");
			Client.getInstance().getLogger().debug(this.getClass().getName(),"IO Exception");
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
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

			isConnected = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

