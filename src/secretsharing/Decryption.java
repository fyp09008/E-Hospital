package secretsharing;
import java.math.*;

public class Decryption {
	/**
	 * @param args
	 */
	private int k;
	private BigInteger p;
	private Share[] s;
	
	public Decryption(int no_of_shares, BigInteger prime, Share[] shares) {
		k = no_of_shares;
		p = prime;
		s = shares;
	}
	
	public byte[] decrypt() {
		// TODO Auto-generated method stub
		// k out of n is enough
		//System.out.println("Decryption begin, uses " + k + " shares.");
		BigInteger[] x = new BigInteger[k];
		BigInteger[] y = new BigInteger[k];
		for (int i = 0; i < k; i++) {
			x[i] = new BigInteger(String.valueOf(s[i].get_share_no()));
			y[i] = s[i].get_share();
		}
		
		//BigInteger p = new BigInteger("93ad9836d4fb35500ec40d737abe1049", 16);
		byte[] sum = new byte[1024 * 4];

		BigInteger lj = new BigInteger("1");
		BigInteger secret = new BigInteger("0");
		for (int j = 0; j < k; j++) {
			for (int i = 0; i < k; i++) {
				if (i != j) lj = (lj.multiply(x[i].negate()).multiply((x[j].subtract(x[i])).modInverse(p))).mod(p);
				//System.out.println(lj);
			}
			secret = secret.add(lj.multiply(y[j]));
			lj = new BigInteger("1");
		}

		sum = secret.mod(p).toByteArray();
		
		//byte[] abc = secret.setScale(0, RoundingMode.HALF_UP).toBigInteger().toByteArray();
		//System.out.println("P: " + p);
		//System.out.println(new String(abc));

		return sum;
	}
}
