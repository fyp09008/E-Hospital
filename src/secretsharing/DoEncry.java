package secretsharing;
import java.math.*;
import java.util.Random;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

public class DoEncry {
	static int k;
	static int n;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//reading arguments from user's inputs
			k = Integer.valueOf(args[1]);
			n = Integer.valueOf(args[2]);
		}catch (Exception e){
			k = 2;
			n = 10;
		}
		
		int no_of_bits = 1024;
		int secret_length = 1024;
		byte[] D = "This is an example".getBytes();
		
		String result = "";
		
		String inputFileName  = args[0];
		String outputFileName = "secret_to_share.txt";
		String outputFileName2 = "recombine_input.txt";
		
		long start = System.currentTimeMillis();
        String token = null;
        
		try {
	        // Create FileReader Object
	        FileReader inputFileReader   = new FileReader(inputFileName);
	        FileWriter outputFileReader  = new FileWriter(outputFileName);
	        FileWriter outputFileReader2  = new FileWriter(outputFileName2);

	        // Create Buffered/PrintWriter Objects
	        BufferedReader inputStream   = new BufferedReader(inputFileReader);
	        PrintWriter outputStream  = new PrintWriter(outputFileReader);
	        PrintWriter outputStream2  = new PrintWriter(outputFileReader2);

            token = inputStream.readLine();
            BigInteger p = new BigInteger("0");
            Share[] shares = new Share[k];
			if (n < k) {
				System.out.println("Error: n cannot be smaller than k");
			} else {
			
				Random r = new Random();
				//String token = Long.toString(Math.abs(r.nextLong()), secret_length);
				//System.out.println("Secret: " + token);
				
				D = token.getBytes();
				
				// encryption		
				Encryption enc = new Encryption(k, n, no_of_bits, D);
				System.out.print(enc.encrypt() + ", ");
				p = enc.export_p();
				shares = enc.export_shares2();
				
				/*
				// decryption for verify
				Decryption dec = new Decryption(k, p, shares);
				byte[] secret = dec.decrypt();
				System.out.print("Decryption done, secret: ");
				
				result = new String(secret);
				System.out.println(result);
				*/
			}
		
	        //outputStream.println("+---------- output start ----------+");
	        outputStream.println("p");
	        outputStream.println(p);
	        outputStream2.println(p);
	        int temp = k;
	        for (int j = 0; j < n; j++) {
	        	outputStream.println(shares[j].get_share_no());
	        	outputStream.println(shares[j].get_share());
	        	if (temp > 0) {
		        	outputStream2.println(shares[j].get_share_no());
		        	outputStream2.println(shares[j].get_share());
		        	temp--;
	        	}
	        }
	        //outputStream.println("+---------- secret output start ----------+");
	        //outputStream.println(result);
	        //outputStream.println("+---------- output end ----------+");

	        outputStream.close();
	        outputStream2.close();
	        inputStream.close();
			
        } catch (IOException e) {
            System.out.println("IOException:");
            e.printStackTrace();
        }

		
		long end = System.currentTimeMillis();
		
		System.out.print(k + " out of " + n + " secrest sharing scheme, ");
		System.out.println("time taken = " + (end - start)/1000 + " seconds");

	}
}
