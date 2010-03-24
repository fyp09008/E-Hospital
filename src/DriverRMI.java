import java.rmi.*;
import java.rmi.registry.*;
public class DriverRMI {

	//private serverLookUp(String server, int port) {
		//server
	//}
	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws NotBoundException 
	 */
	public static void main(String[] args) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		Registry re = LocateRegistry.getRegistry(args[0], 1099);
		remote.obj.AuthHandler ah = (remote.obj.AuthHandler)re.lookup("AuthHandler");
		System.out.println("auth handler bounds");
		remote.obj.DataHandler dh = (remote.obj.DataHandler)re.lookup("DataHandler");
	}

}
