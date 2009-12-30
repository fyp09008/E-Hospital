


public class InsignificantMain {
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client c = new Client();
		if (c.connect()) {
			c.authenicate();
			c.disconnect();
		}
		
		
	}

}
