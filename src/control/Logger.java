package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Logger {
	File debugMsg;
	File cipherMsg;
	File dir;
	File config;
	boolean debug = false;
	boolean cipher = false;
	PrintStream debugPS;
	PrintStream cipherPS;
    Calendar calendar;
    SimpleDateFormat dateFormat;
	public Logger(){
		calendar = Calendar.getInstance();
		dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		dir = new File("debug");
		if ( !dir.exists() || !dir.isDirectory())
			dir.mkdir();
		config = new File("debug/config.txt");
		if ( !config.exists() || !config.isFile())
			try {
				config.createNewFile();
				PrintStream ps = new PrintStream(new FileOutputStream(config));
				ps.println("debug 1");
				ps.println("cipher 1");
				ps.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		checkMode();
	}
	private void checkMode(){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("debug/config.txt")));
			String temp = null;
			while ( (temp = br.readLine()) != null){
				StringTokenizer st = new StringTokenizer(temp," ");
				String mode = st.nextToken();
				if ( mode.equals("debug")){
					debug = st.nextToken().equals("1")?true:false;
				}
				if ( mode.equals("cipher")){
					cipher = st.nextToken().equals("1")?true:false;
				}
			}


	        String time = dateFormat.format(calendar.getTime());
	        if (debug || cipher){
	        	File dir2 = new File("debug/"+time);
	    		if ( !dir2.exists() || !dir.isDirectory())
	    			dir2.mkdir();
	        }
			if (debug){
				debugMsg = new File("debug/"+time+"/debug.txt");
				debugMsg.createNewFile();
				debugPS = new PrintStream(new FileOutputStream(debugMsg));
			}
			if (cipher){
				cipherMsg = new File("debug/"+time+"/cipher.txt");
				cipherMsg.createNewFile();
				cipherPS = new PrintStream(new FileOutputStream(cipherMsg));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void debug(String className, String msg){
		if (debug){
			String time = dateFormat.format(calendar.getTime());
			//System.out.println(className+": "+msg);
			debugPS.println(time +": " +className+": "+msg);
			debugPS.println();
		}
	}
	/*public void printCipher(String className, String what, String msg){
		if (cipher){
			String time = dateFormat.format(calendar.getTime());
			cipherPS.println(time +": " +className+": " + what +": \r\n" );
			cipherPS.println(msg);
			cipherPS.println();
		}
	}
	public void printPlain(String className, String what, String msg ){
		if (cipher){
			String time = dateFormat.format(calendar.getTime());
			cipherPS.println(time +": " +className+": " + what +": \r\n" );
			cipherPS.println(msg);
			cipherPS.println();
		}
	}*/
	public void printCipher(String className, String what, byte[] msgB, String msg){
		if (cipher){
			String time = dateFormat.format(calendar.getTime());
			cipherPS.println(time +": " +className+": " + what +": \r\n" );
			cipherPS.println("bytes: " + msgB);
			cipherPS.println("String/Integer: " + msg);
			cipherPS.println();
		}
	}
	public void printPlain(String className, String what, byte[] msgB, String msg ){
		if (cipher){
			String time = dateFormat.format(calendar.getTime());
			cipherPS.println(time +": " +className+": " + what +": \r\n" );
			cipherPS.println("bytes: " + msgB);
			cipherPS.println("String/Integer: " + msg);
			cipherPS.println();
		}
	}
	
}
