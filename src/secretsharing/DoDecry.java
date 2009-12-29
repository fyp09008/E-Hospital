package secretsharing;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.*;

public class DoDecry {
	private static int k = 2;
	private static int n = 10;
	
	public static void main(String[] args) {
		try {
			k = Integer.valueOf(args[0]);
		} catch (Exception e) {
			k = 2;
		}
		try {
			n = Integer.valueOf(args[1]);
		} catch (Exception e) {
			n = 10;
		}
		
	    try {
	        // input/output file names
	        String inputFileName = "recombine_input.txt"; // p 1 2 3 4 5 ...
	        //String outputFileName = "OutputFile.txt";
	
	        // Create FileReader Object
	        FileReader inputFileReader = new FileReader(inputFileName);
	        //FileWriter outputFileReader = new FileWriter(outputFileName);
	
	        // Create Buffered/PrintWriter Objects
	        BufferedReader inputStream = new BufferedReader(inputFileReader);
	        //PrintWriter outputStream = new PrintWriter(outputFileReader);
	
	        // Keep in mind that all of the above statements can be combined
	        // into the following:
	        //BufferedReader inputStream = new BufferedReader(new FileReader("README_InputFile.txt"));
	        //PrintWriter outputStream   = new PrintWriter(new FileWriter("ReadWriteTextFile.out"));
	
	        //String inLine = null;
	        Share[] shares = new Share[n];
	        // p
	        BigInteger p = new BigInteger(inputStream.readLine());
	        System.out.println(p);
	        // read shares
	        for (int j = 0; j < k; j++) {
	        	shares[j] = new Share();
	        	shares[j].set_share_no(Integer.valueOf(inputStream.readLine()));
	        	shares[j].set_share(new BigInteger(inputStream.readLine()));
	        	System.out.println(shares[j].to_String());
	        }

	        // decryption
			Decryption dec = new Decryption(k, p, shares);
			byte[] secret = dec.decrypt();
			
			System.out.print("Decryption done, key: ");
			String result = new String(secret);
			System.out.println(result);
			WriteTextFile.write(result);
	        
	        //outputStream.close();
	        inputStream.close();
	
	    } catch (IOException e) {
	        System.out.println("IOException:");
	        e.printStackTrace();
	    }
	    
	}
}
