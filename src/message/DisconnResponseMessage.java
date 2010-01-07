/**
 * 
 */
package message;

import java.io.Serializable;

/**
 * @author mc
 *
 */
public class DisconnResponseMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8071925500966803931L;
	private boolean status;

	/**
	 * @param status
	 */
	public DisconnResponseMessage(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

}
