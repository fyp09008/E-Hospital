package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;

public class testing {
	
	
	
	public static void main(String[] argv){
	
		testing t = new testing();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
		
		try {
			String temp = br.readLine();
			temp = t.extract(temp);
			System.out.println(temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private  String extract(String s){
		String result = "";
		for(int i = 0; i < s.length(); i++){
			String c = s.substring(i, i+1);
			//System.out.println(c);
			if (c.equals(":") || c.equals("\\") 
				|| c.equals("/") ||c.equals("*")
				|| c.equals("?") || c.equals("\"")
				|| c.equals("<") || c.equals(">")
				|| c.equals("|")){
				result += "0";
			}
			else
				result += c;
		}
		System.out.println(result);
		String rr = "";
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("md5");
			rr = new String((md.digest(s.getBytes()).toString()));
		}catch (Exception e){e.printStackTrace();}
		return rr;
	}
}
