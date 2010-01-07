/**
 * 
 */
package message;

import java.io.Serializable;

/**
 * @author mc
 *
 */
public class UpdateResponseMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3951827684626883464L;

	private boolean status;
	/**
	 * Currently not applicable
	 */
	private int ErrorCode;
	/**
	 * @param status
	 */
	public UpdateResponseMessage(boolean status) {
		this.status = status;
	}
	public boolean getStatus() {
		return status;
	}
	
}
