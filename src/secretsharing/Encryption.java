package secretsharing;
import java.math.*;
import java.security.*;

public class Encryption {
	private int no_of_bits;
	private int k;
	private int n;
	private byte[] D;
	private BigInteger[] shares;
	private BigInteger p;
	
	public Encryption(int intK, int intN, int intBits, byte[] secret) {
		shares = new BigInteger[intN];
		this.set_scheme(intK, intN);
		this.set_bits(intBits);
		this.set_secret(secret);
	}
	
	private void set_scheme(int intK, int intN) {
		k = intK;
		n = intN;
	}
	
	private void set_bits(int intBits) {
		no_of_bits = intBits;
	}
	
	private void set_secret(byte[] secret) {
		D = secret;
	}
	
	public String encrypt() {
		
		// System.out.println("Encryption starts, " + k + " out of " + n + " scheme");
		
		// s : coeff of the formula
		BigInteger s[] = new BigInteger[k-1];
		
		BigInteger secret = new BigInteger(D);
		if (secret.bitLength() > no_of_bits) {
			return "Error: Secret too long, current = " + secret.bitLength() + " bits, max = " + no_of_bits;
		}

		// generate a random p larger than the secret D
		SecureRandom random = new SecureRandom();
		
		while ((p = new BigInteger(no_of_bits, 10, random)).compareTo(secret) <= 0) {
			// can generate another p
		}
		
		// print for debug
		//System.out.println("p = " + p.toString(16));
		
		// generate random (k-1) coefficients for secret sharing
		for (int i = 0; i < k-1; i++) {
			s[i] = new BigInteger(no_of_bits, random).mod(p);
		}
		
		// generate (n) shares
		for (int i = 0; i < n; i++) {
			shares[i] = secret;
			for (int j = 0; j < k-1; j++) {
				shares[i] = shares[i].add(s[j].multiply(new BigInteger(String.valueOf(i+1)).pow(j+1))).mod(p);
			}
			// print for debug
			//System.out.println(i+1 + " : " + shares[i].toString(16));
		}
		return "Encryption done";
	}
	
	// export the shares	
	public BigInteger[] export_shares() {
		return shares;
	}
	
	// new scheme
	public Share[] export_shares2() {
		Share[] shares = new Share[n];
		for (int i=0; i<n; i++) {
			shares[i] = new Share(i+1, this.shares[i]);
		}
		return shares;
	}
	
	// export the prime
	public BigInteger export_p() {
		return p;
	}
}
