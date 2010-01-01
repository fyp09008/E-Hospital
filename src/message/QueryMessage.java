package message;

import java.io.Serializable;

public class QueryMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4067528266309901746L;
	public byte[] query;
	public String username;
}
