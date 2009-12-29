package user;

// from our code
import user.Database;
import cipher.RSAHardware;

// from java
import java.io.*;


public class RegisterTrustedParty {
	private String name;
	private String publicKeyExp;
	private String modulus;
	private Database db;
	private RSAHardware rsaHard;
	private InputStreamReader inr;
	private BufferedReader br;
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java user.RegisterTrustedParty <name>");
			System.exit(1);
		}
		
		RegisterTrustedParty registerTP = new RegisterTrustedParty(args[0]);
		
		if (!registerTP.checkTPExists()) {
			if (registerTP.genKeyPair() < 0) return;
			if (registerTP.storeToDB() < 0)return;
			System.out.println("Trusted party " + args[0] + " created");
		} else
			System.out.println("Trusted party " + args[0] + " already exists");
	}
	
	private RegisterTrustedParty(String name) {
		this.name = name;
		db = new Database();
		rsaHard = new RSAHardware();
		inr = new InputStreamReader(System.in);
		br = new BufferedReader(inr);
	}
	
	@Override
	protected void finalize() throws Throwable
	{
	  br.close();
	}
	
	// post: return true if trusted party is found in database. Otherwise, return false.
	private boolean checkTPExists() {
		return (db.checkTPExists(name) == 1);
	}
	
	// post: generate a RSA key pair and store the key pair to java card
	private int genKeyPair() {
		do {
			System.out.print("Please insert a java card and press enter: ");
			try {
				br.readLine();
			} catch (IOException ex) { }
		} while (rsaHard.initJavaCard() == -1);
		
		// generate a RSA key pair
		System.out.print("Generating RSA key pair...");
		
		if (rsaHard.genKey() == -1) {
			System.out.println("Error");
			return -1;
		}
		
		modulus = rsaHard.getGeneratedModulus();
		publicKeyExp = rsaHard.getGeneratedPublicKeyExp();
		
		System.out.println("Done");
		
		return 0;
	}
	
	// post: store the public key to the database
	private int storeToDB() {
		System.out.print("Storing public key into the database...");
		int result =  db.insertParty(name, publicKeyExp, modulus);
		if (!(result < 0)) {
			System.out.println("Done");
			return 0;
		}
		else {
			System.out.println("Error");
			return -1;
		}
	}
}
