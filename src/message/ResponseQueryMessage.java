package message;
import java.io.Serializable;

public class ResponseQueryMessage implements Serializable{

		private static final long serialVersionUID = 4067528266309901746L;
		
		public byte[] ResultSet;
		public byte[] nextToken;
	
}
