package control;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import UI.LoginDialog;

import remote.obj.ClientCallback;

public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallback {

	protected ClientCallbackImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6528781973456505908L;

	@Override
	public void timeout() throws RemoteException {

		if ( Client.getInstance().isConnected())
			Client.getInstance().getMf().changePanel(-3);
	}

}
