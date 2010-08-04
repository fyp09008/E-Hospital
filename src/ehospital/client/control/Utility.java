package ehospital.client.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/*
 * This class provide utility for all classes, 
 */
public class Utility {
	private Utility() {}
	
	public static String byteArrayToString(byte[] b) {
		if(b == null)
			return null;
		String HEX_NUM = "0123456789abcdef";
		char[] cStr = new char[b.length*2];
		for(int i = 0, j = 0; i< b.length; i++) {
			byte f = (byte)((b[i] & 0xF0) >> 4);
			byte g = (byte) (b[i] & 0x0F);
			cStr[j] =  HEX_NUM.charAt(f);
			j++;
			cStr[j] = HEX_NUM.charAt(g); 
			j++;
		}
		return new String(cStr);
	}
	
	public static byte[] stringToByteArray(String str) {
		String HEX_NUM = "0123456789abcdef";
		int CHAR_NOT_FOUND = -1;
		byte[] b = new byte[str.length()/2];
		for(int i = 0, j = 0; i < b.length; i++) {
			byte f = (byte)HEX_NUM.indexOf(str.charAt(j));
			if (f != CHAR_NOT_FOUND) {
				b[i] = (byte)(f << 4);
				j++;
				byte g = (byte)HEX_NUM.indexOf(str.charAt(j));
				if (g != CHAR_NOT_FOUND) {
					b[i] = (byte) (b[i] + g);
					j++;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}	
		return b;
	}
	
	public static byte[] objToBytes(Object obj){
	      ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	      ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush(); 
			oos.close(); 
			bos.close();
			byte [] data = bos.toByteArray();
			return data;
		} catch (NotSerializableException e){
			Client.getInstance().getLogger().debug("Utility", e.getMessage());
			return null;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("Utility", e.getMessage());
		} return null;
	}
	
	public static Object BytesToObj(byte[] b){
	      ByteArrayInputStream bis = new ByteArrayInputStream(b); 
	      ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bis);
			Object obj = ois.readObject();
			ois.close(); 
			bis.close();
			return obj;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("Utility", e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug("Utility", e.getMessage());
		} 
	    return null;
	}
	
	public static byte[] intToByteArray (int integer) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeInt(integer);
			dos.flush();
			return bos.toByteArray();
		} catch(IOException ex) {
			Client.getInstance().getLogger().debug("Utility", ex.getMessage());
			return null;
		}
	}
	
	public static boolean compareByte(byte[] b1, byte[] b2)
	{
		if (b1.length != b2.length)
			return false;
		
		for (int i = 0; i < b1.length; ++i)
			if (b1[i] != b2[i])
				return false;
		
		return true;
	}
}
