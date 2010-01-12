package DBPopulator.DBPopulator.src;



import java.sql.*;

/**
 * @author mc
 * 
 * This class is used to manage the database
 *
 */
public class DBManager {
	
	private static final String dbdriver = "com.mysql.jdbc.Driver";
	private static final String dbstr = "jdbc:mysql://localhost/hospital_rec";
	private static final String username = "FYP09";
	private static final String password = "1234qwer";
	
	private Connection conn;
	private ResultSet rs;
	/**
	 * Build a connection to the database using predefined param.
	 */
	public DBManager() {
		
	}
	
	public boolean connect() {
		try {
			Class.forName(dbdriver);
			this.conn = DriverManager.getConnection(dbstr, username, password);
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param query
	 * @return The result from the query
	 * @throws SQLException
	 */
	public ResultSet query(String query) throws SQLException
	{
		this.connect();
		ResultSet rs = conn.createStatement().executeQuery(query);
		return rs;
	}
	
	/**
	 * @param query
	 * @throws SQLException
	 */
	public void update(String query) throws SQLException
	{
		this.connect();
		conn.createStatement().executeUpdate(query);
	}

	/**
	 * @param username
	 * @return String array that store the public key exponent and modulus of user, return null if there is any problem
	 */
	public String [] getPubKeyAndMod(String username)
	{
		String [] buf = new String[2];
		try {
			this.connect();
			ResultSet rs = query("SELECT pub_key, `mod` FROM user WHERE username='"+username+"'");
			rs.next();
			buf[0] = rs.getString(1);
			buf[1] = rs.getString(2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			return null;
		}
		return buf;
	}
	
	public int storeUser(String role, String username, String pwdMDExp, String publicKeyExp, String modulus) {
		try {
			if(this.connect()) {
				Statement stmt = conn.createStatement();
				String q = "INSERT INTO user (Role, pub_key,`mod`, pwd, RegDate, username) VALUES ( '"+role+"', '"+publicKeyExp+"', '"+modulus+"', '"+pwdMDExp+"', CURDATE(),'"+username+"');";
				
				stmt.executeUpdate(q);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} 
		return 0;
	}
	
	public boolean isUserExist(String username) {
		try {
			if (this.connect()) {
				Statement stmt = conn.createStatement();
				String q = "SELECT username,pwd FROM user WHERE username = '"+username+"';";
				setRs(stmt.executeQuery(q));
				if(getRs().first()) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}

	/**
	 * @param rs the rs to set
	 */
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	/**
	 * @return the rs
	 */
	public ResultSet getRs() {
		return rs;
	}
	
	public void disconnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
