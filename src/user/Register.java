package user;

// from our code
import user.Database;
import secretsharing.Encryption;
import cipher.RSASoftware;
import cipher.RSAHardware;

// from java
import java.math.*;
import java.io.*;import java.net.URL;import java.net.URLConnection;
public class Register {
	private static String setPublicKeyURL = "http://localhost/plugin/setPublicKey.php";
	
	private String username;
	private String privateKeyExp;
	private String publicKeyExp;
	private String modulus;
	private BigInteger[] shares;
	private String prime;
	private byte[][] encryptedShares;
	private Database db;
	private String[] trustedParties;
	private RSASoftware rsaSoft;
	private RSAHardware rsaHard;
	private InputStreamReader inr;
	private BufferedReader br;
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java user.Register <username>");
			System.exit(1);
		}
		
		Register register = new Register(args[0]);
		
		if (!register.checkUserExists()) {
			if (register.genKeyPair() < 0) return;
			if (register.genShares() < 0) return;
			if (register.encryptShares() < 0) return;
			if (register.sendPubKeyToFileServer() < 0) return;
			if (register.storeToDB() < 0) return;
			System.out.println("User " + args[0] + " registered");
		} else
			System.out.println("User " + args[0] + " already exists");
	}
	
	private Register(String username) {
		this.username = username;
		db = new Database();
		trustedParties = new String[4];
		encryptedShares = new byte[4][];
		rsaSoft = new RSASoftware();		
		rsaHard = new RSAHardware();
		inr = new InputStreamReader(System.in);
		br = new BufferedReader(inr);
	}
	
	@Override
	protected void finalize() throws Throwable {
		br.close();
	}
	
	// post: return true if user is found in database. Otherwise, return false.
	private boolean checkUserExists() {
		return (db.checkUserExists(username) == 1);
	}
	
	// post: generate a RSA key pair and store the key pair to the java card
	private int genKeyPair() {
		do {
			System.out.print("Please insert a java card and press enter: ");
			try {
				br.readLine();
			} catch (IOException ex) { }
		} while (rsaHard.initJavaCard() < 0);
		
		System.out.print("Generating RSA key pair...");
		
		// generate a RSA key pair
		if (rsaHard.genKey() == -1) {
			System.out.println("Error");
			return -1;
		}
		
		modulus = rsaHard.getGeneratedModulus();
		privateKeyExp = rsaHard.getGeneratedPrivateKeyExp();
		publicKeyExp = rsaHard.getGeneratedPublicKeyExp();
		
		System.out.println("Done");
		
		return 0;
	}
	
	// post: create shares for the private key
	private int genShares() {
		System.out.print("Generating shares for private key...");
		
		Encryption enc = new Encryption(3, 4, 1024, new BigInteger(privateKeyExp, 16).toByteArray());
		String enc_result = enc.encrypt();
		prime = enc.export_p().toString(16);
		shares = enc.export_shares();
		if (enc_result.equals("Encryption done"))
		{
			System.out.println("Done");
			return 0;
		}
		System.out.println(enc_result);
		return -1;
	}
	
	// post: encrypt the shares
	private int encryptShares() {
		// prompt which 4 trusted parties
		String[] rank = {"First", "Second", "Third", "Forth"};
		boolean retry = false;
		for (int i = 0; i < 4; ++i) {
			System.out.print("Name of " + rank[i] + " trusted party: ");
			String nameOfTrustedParty;
			try {
				nameOfTrustedParty = br.readLine();
			} catch (IOException ex) {
				System.out.println("IOException occurred");
				return -1;
			}
			int result = db.checkTPExists(nameOfTrustedParty);
			if (result == 0) {
				if (retry == true) {
					System.out.println("You have entered a wrong trusted party twice. Registration terminated.");
					return -1;
				}
				System.out.println(nameOfTrustedParty + " is not a trusted party.");
				retry = true;
				--i;
			} else if (result == -1) {
				System.out.println("Database error occurred");
				return -1;
			} else {
				retry = false;
				trustedParties[i] = nameOfTrustedParty;
			}
		}
		
		System.out.print("Encrypting shares by trusted parties' public key...");
		
		// encrypt the shares
		for (int i = 0; i < 4; ++i) {
			String[] publicKey = db.getTPPublicKey(trustedParties[i]);
			rsaSoft.setPublicKey(publicKey[0], publicKey[1]);
			
			byte[] plaintext = shares[i].toString(16).getBytes();
			byte[] ciphertext = new byte[(int)Math.ceil(plaintext.length / 117.0) * 128];
			byte[] temp1 = new byte[117];
			byte[] temp2;
			int byteRead = 0;
			int blocksize = 0;
			int blockno = 0;
			while (byteRead < plaintext.length) {
				temp1[blocksize++] = plaintext[byteRead++];
				if (blocksize == 117 || byteRead == plaintext.length) {
					temp2 = rsaSoft.encrypt(temp1, blocksize);
					
					for (int j = 0; j < 128; ++j)
						ciphertext[blockno * 128 + j] = temp2[j];
					++blockno;
					blocksize = 0;
				}
			}
			if (ciphertext == null) {
				System.out.println("Error");
				return -1;
			}
			encryptedShares[i] = ciphertext;
		}
		
		System.out.println("Done");
		
		return 0;
	}
	
	// post: send the public key to the file server for storage
	private int sendPubKeyToFileServer() {
		try {
			System.out.print("Sending public key to file server...");			
        	// create a connection to send the public key
        	URL servletURL = new URL(setPublicKeyURL + "?username=" + username + "&publicKeyExp=" + publicKeyExp + "&modulus=" + modulus);
        	//System.out.print(servletURL.toString());
        	URLConnection servletConnection = servletURL.openConnection();
        	servletConnection.setDoOutput(true);
        	servletConnection.setUseCaches(false);

        	// read response from server
        	final InputStream in = servletConnection.getInputStream();
        	final StringBuffer response = new StringBuffer();
        	int chr;
        	while ((chr=in.read())!=-1) {
        		response.append((char) chr);
        	}
        	in.close();
        	
        	if (response.substring(0).equals("OK")){
        		System.out.println("Done");
        		return 0;
        	}else{
        		System.out.println("Error");
        		return -1;
        	}
        } catch (final IOException e) {
        	System.out.println("Error");
        	return -1;
        }
	}
	
	// post: store the private key shares and the public key to the database
	private int storeToDB() {
		System.out.print("Storing shares, public key into the database...");
		int result1 =  db.insertShare(username, publicKeyExp, modulus, encryptedShares, prime);
		int result2 = db.storePartiesOfUser(username, trustedParties);
		if (!(result1 < 0)  && !(result2 < 0)) {
			System.out.println("Done");
			return 0;
		}
		else {
			System.out.println("Error");
			return -1;
		}
	}
}
