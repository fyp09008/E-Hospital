package secretsharing;

import java.io.*;
import java.math.BigInteger;
import java.util.Random;

public class Tester {
	BigInteger[] shares;
	BigInteger prime;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String usage = "java Tester.java <result file>";
		if (args.length != 1) {
			System.out.println(usage);
			return;
		}
		
		Tester tester = new Tester();
		tester.start(args[0]);
	}

	private int[] randChoice(int k, int n) {
		int[] choice = new int[k];
		
		boolean[] marks = new boolean[n]; // to record which candidate has been chosen
		for (int i = 0; i < n; ++i)
			marks[i] = false;
		
		Random rand = new Random();
		for (int i = 0; i < k; ++i) {
			int candidate;
			do {
				candidate = rand.nextInt(n);
			} while (marks[candidate]);
			choice[i] = candidate;
			marks[candidate] = true;
		}
		
		return choice;
	}
	
	public void start(String resultFile) {
		PrintWriter printWriter = null;
		Random rand = new Random();
		int noOfTest = 0;
		int noOfCorrect = 0;
		
		try {
			printWriter = new PrintWriter(new FileWriter(resultFile));
		} catch (IOException ex) {
			System.out.println("Writing file error");
			return;
		}
		
		// for secrets of different length
		for (int i = 128; i <= 1024; ++i) {
			printWriter.println("Secret of length " + i + " bits:");
			printWriter.println();
			
			BigInteger secret;
			
			// for different k out of n scheme
			for (int n = 1; n <= 10; ++n) {
				for (int k = 1; k <= n; ++k) {
					secret = new BigInteger(i, rand);
					
					printWriter.println(k + " out of " + n + " :");
					
					// print out secret
					printWriter.println("Secret = " + secret.toString());
					
					// generate shares
					Encryption enc = new Encryption(k, n, i, secret.toByteArray());
					String enc_result = enc.encrypt();
					
					if (!enc_result.equals("Encryption done"))
					{
						System.out.println("Error");
						printWriter.println("Error");
						continue;
					}
					
					// get shares
					BigInteger[] shares = enc.export_shares();
					BigInteger prime = enc.export_p();
					
					// print out shares
					for (int j = 0; j < shares.length; ++j)
						printWriter.println("Share no. " + (j + 1) + " = " + shares[j].toString(16));
					printWriter.println("Prime = " + prime.toString(16));
					
					// randomly choose k shares
					int choice[] = randChoice(k, n);
					Share[] temp = new Share[k];
					printWriter.print("Shares used: ");
					for (int j = 0; j < k; ++j) {
						temp[j] = new Share(choice[j] + 1, shares[choice[j]]);
						printWriter.print(choice[j] + 1);
						if (j != k - 1)
							printWriter.print(", ");
					}
					printWriter.println();
					
					// recover secret
					Decryption dec = new Decryption(k, prime, temp);
					BigInteger recovered_secret = new BigInteger(dec.decrypt());
					
					// print out recovered secret
					printWriter.println("Recovered secret = " + recovered_secret);
					
					//print result
					if (secret.equals(recovered_secret)) {
						++noOfCorrect;
						printWriter.println("Secret = Recovered Secret");
					} else
						printWriter.println("Secret != Recovered Secret");
					printWriter.println();
					
					printWriter.flush();
					
					++noOfTest;
				}
			}
		}
		
		// print out statistic
		double correctnessPercentage = (noOfCorrect + 0.0) / noOfTest * 100;
		printWriter.println("No of test done: " + noOfTest);
		printWriter.println("No of correct test: " + noOfCorrect);
		printWriter.println("Percentage of correctness: " + correctnessPercentage);
		
		printWriter.close();
	}
}
