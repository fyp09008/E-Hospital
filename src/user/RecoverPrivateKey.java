package user;

import java.io.*;
import java.math.BigInteger;

//from our code
import secretsharing.Decryption;
import secretsharing.Share;
import cipher.RSAHardware;

public class RecoverPrivateKey {
	private String username;
	private String modulus;
	private String privateKeyExp;
	private String publicKeyExp;
	private BigInteger[] shares;
	private String prime;
	private byte[][] encryptedShares;
	private Database db;
	private RSAHardware rsaHard;
	private InputStreamReader inr;
	private BufferedReader br;
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java user.RecoverPrivateKey <username>");
			System.exit(1);
		}
		
		RecoverPrivateKey recover = new RecoverPrivateKey(args[0]);
		
		if (recover.getPublicKeyFromDB() < 0) return;
		if (recover.getShareFromDB() < 0) return;
		if (recover.decryptShares() < 0) return;
		if (recover.recoverSecret() < 0) return;
		if (recover.storeKeyToCard() < 0) return;
		System.out.println("Recovery finished");
	}
	
	private RecoverPrivateKey(String username) {
		this.username = username;
		shares = new BigInteger[4];
		db = new Database();
		rsaHard = new RSAHardware();
		inr = new InputStreamReader(System.in);
		br = new BufferedReader(inr);
	}
	
	@Override
	protected void finalize() throws Throwable {
		br.close();
	} 
	
	// post: get public key exponent and modulus from database
	private int getPublicKeyFromDB() {
		System.out.print("Getting public key from database...");
		
		String[] result = db.getPublicKey(username);
		if (result == null) {
			System.out.println("Error");
			return -1;
		}
		
		publicKeyExp = result[0];
		modulus = result[1];
		
		System.out.println("Done");
		return 0;
	}
	
	// post: get encrypted shares from database
	private int getShareFromDB() {
		System.out.print("Getting shares from database...");
		
		// get the encrypted shares from the database
		encryptedShares = db.getShare(username);
		prime = db.getPrime(username);
		if (encryptedShares == null) {
			System.out.println("Error");
			return -1;
		}
		
		System.out.println("Done");
		return 0;
	}
	
	// post: decrypt the shares
	private int decryptShares() {
		// get name of trusted parties associated with the user
		String[] trustedParties = db.getPartiesOfUser(username);
		int noOfShareDecrypted = 0;
		
		for (int i = 0; i < 4; ++i) {
			// Ask the trusted party to insert the java card
			if (noOfShareDecrypted == 3)
				break;
			System.out.print("Please insert java card of " + trustedParties[i] +
					" and press Enter to retrieve share no. " + (i + 1) + ": ");
			try {
				br.readLine();
			} catch (IOException ex) { }
			
			if (rsaHard.initJavaCard() < 0) {
				// check if no. of share will not be enough
				if (3 - i + noOfShareDecrypted < 3) {
					System.out.println("Card not detected. Not enough trusted parties. Process terminated.");
					return -1;
				}
				System.out.println("Card not detected. Trusted party skipped.");
				shares[i] = null;
				continue;
			}
			
			byte[] plaintext = new byte[256];
			byte[] temp1 = new byte[128];
			byte[] temp2;
			int blockno = 0;
			int byteLeft = encryptedShares[i].length;
			while (byteLeft > 0) {
				for (int j = 0; j < 128; ++j)
					temp1[j] = encryptedShares[i][blockno * 128 + j];
				
				temp2 = rsaHard.decrypt(temp1, 128);
				
				for (int j = 0; j < temp2.length; ++j)
					plaintext[blockno * 117 + j] = temp2[j];
				
				byteLeft -= 128;
				++blockno;
			}
			if (plaintext == null) {
				System.out.println("Error");
				return -1;
			}
			
			shares[i] = new BigInteger(new String(plaintext), 16);
			++noOfShareDecrypted;
		}
		
		return 0;
	}
	
	// post: get back the private key from shares
	private int recoverSecret() {
		System.out.print("Recovering private key from shares...");
		
		Share[] temp = new Share[3];
		int count = 0;
		for (int i = 0; i < 4; ++i) {
			if (shares[i] != null)
				temp[count++] = new Share(i + 1, shares[i]);
		}
		Decryption dec = new Decryption(3, new BigInteger(prime, 16), temp);
		byte[] secret = dec.decrypt();
		privateKeyExp = new BigInteger(secret).toString(16);
		
		System.out.println("Done");
		return 0;
	}
	
	// post: store the key pair to the java card
	private int storeKeyToCard() {
		// Ask the trusted party to insert the java card
		do {
			System.out.print("Please insert a java card and press enter: ");
			try {
				br.readLine();
			} catch (IOException ex) {
				return -1;
			}
		} while (rsaHard.initJavaCard() == -1);
		
		System.out.print("Storing key pair into the java card...");
		
		rsaHard.setPrivateKey(privateKeyExp, modulus);
		rsaHard.setPublicKey(publicKeyExp, modulus);
		
		System.out.println("Done");
		
		return 0;
	}
}
