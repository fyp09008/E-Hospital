package ehospital.client.control;

import remote.obj.AuthHandler;
import message.DisconnRequestMessage;

/**
 * Handle logout event.
 * Each time the user login, the client program received a byte array with random
 * number. The byte array is stored in the server and client in plaintext. When the
 * client logout, the byte array is encrypted and verified with the server.
 * @author Chun
 *
 */
public class LogoutHandler extends Handler{
	
	private byte[] signedLogoutMsg;
	
	/**
	 * Constructor
	 * @param s random byte array from server
	 */
	public LogoutHandler(byte[] s) {
		this.signedLogoutMsg = s;
	}
	/**
	 * Logout implementation in the client.
	 * @return true if successful
	 */
	@SuppressWarnings("deprecation")
	public boolean logout(){
		if (Connector.getInstance().isConnected()){
			DisconnRequestMessage msg = new DisconnRequestMessage();
			msg.setSignature(signedLogoutMsg);
			try {
				AuthHandler ah = Client.getInstance().getClientAHanlder().getRemoteAuthHandler();
			
				byte[] encryptedName = encryptPAES(Client.getInstance().getName().getBytes());
				byte[] encryptedFlag = ah.logout(encryptedName, encryptPAES(encryptAES((signedLogoutMsg))));
				Boolean b = (Boolean)this.bytesToObj((decryptPAES(encryptedFlag)));
				if (b.booleanValue()){
					//reset the states of Client
					Client.getInstance().reset();
					return true;
				}
				
			} catch (Exception e) {
				Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			}
			
		}
		return false;
	}

}
