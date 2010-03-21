package control;

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
				Connector.getInstance().write(((Object) encryptPAES(objToBytes(msg))));
				DisconnResponseMessage  msg2 = (DisconnResponseMessage)bytesToObj(decryptPAES((byte[])Connector.getInstance().read()));
				if (msg2.getStatus()){
					//reset the states of Client
					Client.getInstance().reset();
					return true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return false;
	}

}
