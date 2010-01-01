package message;

import java.io.Serializable;

public class RequestMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1033966351913762673L;
	public String username;
	public byte[] password;
}
