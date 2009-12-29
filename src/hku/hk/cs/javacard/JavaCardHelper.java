/**
 * 
 */
package hku.hk.cs.javacard;

/**
 * @author Dan
 * 
 */
public class JavaCardHelper {
	final static String HEX_NUMBER = "0123456789abcdef";

	/**
	 * Verify the status word to be 0900
	 * 
	 * @param sw
	 * @return
	 */
	public static boolean checkStatusWord(byte[] sw) {
		return (sw.length >= 2) && (sw[sw.length - 2] == (byte) 0x90)
				&& (sw[sw.length - 1] == 0x00);
	}

	/**
	 * Discard last 2 bytes from the response APDU to obtain data byte array
	 * 
	 * @param responseAPDU
	 * @return
	 */
	public static byte[] extractDateFromAPDU(byte[] responseAPDU) {
		int size = 0;
		if (responseAPDU.length > 2) {
			size = responseAPDU.length - 2;
		}

		byte[] data = new byte[size];
		for (int i = 0; i < size; i++) {
			data[i] = responseAPDU[i];
		}

		return data;
	}

	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}

	/**
	 * Convert string to byte array
	 * 
	 * @param s
	 *            String to be converted into byte array
	 * @return
	 */
	public static byte[] string2bytes(String s) {
		if (s == null)
			return null;
		if (s.length() % 2 != 0)
			throw new RuntimeException("invalid length");
		byte[] result = new byte[s.length() / 2];
		for (int i = 0; i < s.length(); i += 2) {
			int i1 = HEX_NUMBER.indexOf(s.charAt(i));
			if (i1 == -1)
				throw new RuntimeException("invalid number");
			int i2 = HEX_NUMBER.indexOf(s.charAt(i + 1));
			if (i2 == -1)
				throw new RuntimeException("invalid number");
			result[i / 2] = (byte) ((i1 << 4) | i2);
		}

		return result;
	}

}
