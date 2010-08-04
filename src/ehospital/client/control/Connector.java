package ehospital.client.control;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

import javax.swing.JOptionPane;


/**
 * Singleton class to connect to the server and check the connection status.
 * @author   Gilbert
 */
public class Connector {
	
	//code for socket programming
	//private int port;
	//private String hostname;
	public static Registry reg;

	/**
	 * @uml.property  name="isConnected"
	 */
	private boolean isConnected = false;
	private Registry r;
	
	private Connector() {}
	
	/**
	 * Singleton
	 * @author   Gilbert
	 */
	private static class ConnectorHolder { 
	     /**
		 * @uml.property  name="iNSTANCE"
		 * @uml.associationEnd  
		 */
	    private static final Connector INSTANCE = new Connector();
	 }
	 
	public static Connector getInstance() {
	     return ConnectorHolder.INSTANCE;
	}


	/**
	 * Initialize Callback functionality by creating a remote object server 
	 * in the client.
	 */
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
	/**
	 * connect to the server
	 * @return true if successful
	 */
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
	
	
	/**
	 * @return
	 * @uml.property  name="isConnected"
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * @param  isConnected
	 * @uml.property  name="isConnected"
	 */
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	/**
	 * @return registry object for debugging
	 */
	public Registry getRegistry(){
		return r;
	}
	
	/**
	 * Disconnect from the server
	 */
	public void disconnect() {
	    try {
			r = null;

			isConnected = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

