package control;

import message.DisconnRequestMessage;
import message.DisconnResponseMessage;

public class LogoutHandler {
	
	public boolean logout(){
		if (isConnected){
			DisconnRequestMessage msg = new DisconnRequestMessage();
			System.out.println("****"+signedLogoutMsg);
			msg.setSignature(signedLogoutMsg);
			try {
				out.writeObject((Object) encryptPAES(objToBytes(msg)));
				DisconnResponseMessage  msg2 = (DisconnResponseMessage)BytesToObj(decryptPAES((byte[])in.readObject()));
				if (msg2.getStatus()){
					this.reset();
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
