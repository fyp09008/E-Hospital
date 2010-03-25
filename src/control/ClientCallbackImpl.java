package control;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName()+" Timeout Success");
	}

}
