package message;

import java.io.Serializable;
/**
 * 
 * @author Gilbert
 *
 */
public class AuthRequestMessage implements Serializable{

	/**
	 * generated serialVersionUID
	 * serialVersionUID = -1033966351913762673L;
	 */
	private static final long serialVersionUID = -1033966351913762673L;
	private String username;
	private byte[] password;
	/**
	 * @param username the user name to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the user name
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(byte[] password) {
		this.password = password;
	}
	/**
	 * @return the password
	 */
	public byte[] getPassword() {
		return password;
	}
}
