package message;
import java.io.Serializable;

public class QueryResponseMessage implements Serializable{

		private static final long serialVersionUID = 4067528266309901746L;
		
		public byte[] resultSet;
		public byte[] nextToken;
	
}
