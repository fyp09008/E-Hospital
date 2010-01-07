package message;

import java.io.Serializable;

public class ServerAuthResponseMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8589529801648916501L;
	byte[] encryptedFingerprint;

	/**
	 * @param encryptedFingerprint
	 */
	public ServerAuthResponseMessage(byte[] encryptedFingerprint) {
		this.encryptedFingerprint = encryptedFingerprint;
	}

	public byte[] getEncryptedFingerprint() {
		return encryptedFingerprint;
	}

	public void setEncryptedFingerprint(byte[] encryptedFingerprint) {
		this.encryptedFingerprint = encryptedFingerprint;
	}
}
