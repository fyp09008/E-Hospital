package secretsharing;
import java.math.*;

public class Share {
	private int share_no;
	private BigInteger share;
	
	public Share() {
		share_no = 0;
		share = new BigInteger("0");
	}
	
	public Share(int input_share_no, BigInteger input_share) {
		share_no = input_share_no;
		share = input_share;
	}
	
	public void set_share_no(int input_share_no) {
		share_no = input_share_no;
	}
	
	public void set_share(BigInteger input_share) {
		share = input_share;
	}
	
	public int get_share_no() {
		return share_no;
	}
	
	public BigInteger get_share() {
		return share;
	}
	
	public String to_String() {
		return (share_no + ": " + share.toString());
	}
}
