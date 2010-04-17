package control;

import remote.obj.AuthHandler;
import message.DisconnRequestMessage;
import message.DisconnResponseMessage;

public class LogoutHandler extends Handler{
	
	private byte[] signedLogoutMsg;
	
	public LogoutHandler(byte[] s) {
		this.signedLogoutMsg = s;
	}
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
