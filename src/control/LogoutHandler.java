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
			System.out.println("****"+signedLogoutMsg);
			msg.setSignature(signedLogoutMsg);
			try {
				Connector.getInstance().write(((Object) encryptPAES(objToBytes(msg))));
				DisconnResponseMessage  msg2 = (DisconnResponseMessage)BytesToObj(decryptPAES((byte[])Connector.getInstance().read()));
				if (msg2.getStatus()){
					Client.getInstance().reset();
					return true;
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}

}
