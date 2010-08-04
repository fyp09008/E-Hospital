package ehospital.client.control;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import remote.obj.ClientCallback;

/**
 * Class to provide service for Callback function.
 * @author Gilbert
 *
 */
public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallback {

	protected ClientCallbackImpl() throws RemoteException {
		super();

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
