package cipher;

import java.io.*;

public class RSADriver {
	private String inFile, outFile, keyFile;
	private RSAHardware hardCipher;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String usage = "Usage: java cipher.RSADriver <encrypt | decrypt | genkey> [<key file> | <input file> <output file>]";
		
		if (args.length == 0)
			System.out.println(usage);
		else if (args[0].equals("encrypt"))
			if (args.length != 3)
				System.out.println(usage);
			else {
				RSADriver rsaHardCipher = new RSADriver(args[1], args[2]);
				rsaHardCipher.encrypt();
			}
		else if (args[0].equals("decrypt"))
			if (args.length != 3)
				System.out.println(usage);
			else {
				RSADriver rsaHardCipher = new RSADriver(args[1], args[2]);
				rsaHardCipher.decrypt();
			}
		else if (args[0].equals("genkey")) {
			if (args.length != 2)
				System.out.println(usage);
			else {
				RSADriver rsaHardCipher = new RSADriver(args[1]);
				rsaHardCipher.genKey();
			}
		}
		else
		{
			RSAHardware a = new RSAHardware();
			a.initJavaCard();
			a.setPrivateKey("c2fe569548ec279571945cc108090b012f1b8b4ba1c15bcd87ffc04396b8426bfcbddd65c254cc94a4bacf78fd1f7bd2a5b3a615ff1cde7d844e1f1af194c9b4fcd536092e2101a52095be55a0deadc907f8c872a2248d07dc2a3a7d42eb8fa384e45179cab5ebd5e270069b5016422f7d0e64f747ed4477c378cd1c4a70e541", "eb7c1c6583bbc55c98e3411166b728f024100cdd6fb60550df79b0801e2bdb908421b5efa86bb36f6449af857bfe146d3908adcb4dbc796a9b1f78a2f569bd853935a985281b7aff9b49549cfa24f49c9989ca664697db2604fb0673ea74b11695f47d8e7b1096d6d408b646afcdf0a517f1ed2bd64b6eda0236763ce30cd5ef");
			System.out.println("Action " + args[0] + " not supported.");
		}
	}
	
	public RSADriver(String inFile, String outFile) {
		this.keyFile = null;
		this.inFile = inFile;
		this.outFile = outFile;
		hardCipher = new RSAHardware();
		hardCipher.initJavaCard();
	}
	
	public RSADriver(String keyFile) {
		this.keyFile = keyFile;
		this.inFile = null;
		this.outFile = null;
		hardCipher = new RSAHardware();
		hardCipher.initJavaCard();
	}

	public int encrypt() {
		FileInputStream fIStream = null;
		FileOutputStream fOStream = null;
		
		try {
			System.out.print("Encrypting...");
			
			fIStream = new FileInputStream(inFile);
			fOStream = new FileOutputStream(outFile);
			
			byte[] plaintext = new byte[117];
			byte[] ciphertext;
			
			int byteRead;
			while ((byteRead = fIStream.read(plaintext, 0, 117)) != -1) {
				ciphertext = hardCipher.encrypt(plaintext, byteRead);
				if (ciphertext == null) {
					System.out.println("Error");
					return -1;
				}
				fOStream.write(ciphertext);
			}
			fOStream.flush();
			
			fIStream.close();
			fOStream.close();
			
			System.out.println("Done");
			return 0;
		} catch (FileNotFoundException ex) {
			System.out.println(inFile + " does not exist");
			return -1;
		} catch (IOException ex) {
			System.out.println("File Error");
			return -1;
		}
	}
	
	public int decrypt() {
		FileInputStream fIStream = null;
		FileOutputStream fOStream = null;
		
		try {
			System.out.print("Decrypting...");
			
			fIStream = new FileInputStream(inFile);
			fOStream = new FileOutputStream(outFile);
			
			byte[] ciphertext = new byte[128];
			byte[] plaintext;
			
			int byteRead;
			while ((byteRead = fIStream.read(ciphertext, 0, 128)) != -1) {
				plaintext = hardCipher.decrypt(ciphertext, byteRead);
				if (plaintext == null) {
					System.out.println("Error");
					return -1;
				}
				fOStream.write(plaintext, 0, plaintext.length);
			}
			fOStream.flush();
			
			fIStream.close();
			fOStream.close();
			
			System.out.println("Done");
			return 0;
		} catch (FileNotFoundException ex) {
			System.out.println(inFile + " does not exist");
			return -1;
		} catch (IOException ex) {
			System.out.println("File Error");
			return -1;
		}
	}
	
	public int genKey() {
		System.out.print("Generating key pair...");
		
		String outPublicKeyExp, outModulus;
		if (hardCipher.genKey() < 0) {
			System.out.println("Done");
			return -1;
		}
		outPublicKeyExp = hardCipher.getGeneratedPublicKeyExp();
		outModulus = hardCipher.getGeneratedModulus();
		
		System.out.println("Done");
		System.out.print("Writing key pair to output file...");
		
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileWriter(keyFile));
			printWriter.println(outPublicKeyExp);
			printWriter.println(outModulus);
			printWriter.flush();
			printWriter.close();
			System.out.println("Done");
			return 0;
		} catch (IOException ex) {
			System.out.println("Error");
			return -1;
		}
	}
}
