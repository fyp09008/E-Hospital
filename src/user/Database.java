package user;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Database {
	private Connection conn = null;
	
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        connect();
        if (checkDBExists("share") == 0)
        	createDB("share");
        else
        	useDB("share");
	}
	
	@Override
	protected void finalize() throws Throwable {
		conn.close();
	}
	
	private void connect() {
		try {
		    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql",
		    		"root", "");
		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	private int checkDBExists(String db) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SHOW DATABASES;");
			while (rs.next())
				if (rs.getString("Database").equals("share"))
					return 1;
			return 0;
		} catch (SQLException ex) {
			return -1;
		}
	}
	
	private void createDB(String db) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("CREATE DATABASE " + db + ";");
			stmt.executeUpdate("USE " + db + ";");
			stmt.executeUpdate("CREATE TABLE share (username varchar(15), publickey TEXT, modulus TEXT, share1 BLOB, share2 BLOB, share3 BLOB, share4 BLOB, prime TEXT, date DATETIME, PRIMARY KEY(username));");
			stmt.executeUpdate("CREATE TABLE trust (username varchar(15), party1 varchar(15), party2 varchar(15), party3 varchar(15), party4 varchar(15), PRIMARY KEY(username));");
			stmt.executeUpdate("CREATE TABLE party (name varchar(15), publickey TEXT, modulus TEXT, PRIMARY KEY(name));");
		} catch (SQLException ex) { }
	}
	
	private void useDB(String db) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("USE " + db + ";");
		} catch (SQLException ex) { }
	}
	
	public int checkUserExists(String username) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM share WHERE username = '" + username + "';");
			if (rs.next() && !rs.getString(1).equals("0"))
				return 1;
			return 0;
		} catch (SQLException ex) {
			return -1;
		}
	}
	
	public int checkTPExists(String name) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM party WHERE name = '" + name + "';");
			if (rs.next() && !rs.getString(1).equals("0"))
				return 1;
			return 0;
		} catch (SQLException ex) {
			return -1;
		}
	}
	
	public String[] getTPPublicKey(String name) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT publickey, modulus FROM party WHERE name = '" + name + "';");
			if (rs.next()) {
				String [] result = new String[2];
				result[0] = rs.getString(1);
				result[1] = rs.getString(2);
				return result;
			}
			return null;
		} catch (SQLException ex) {
			return null;
		}
	}
	
	public int insertShare(String username, String publicKey, String modulus, byte[][] shares, String prime) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO share VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW());");
			stmt.setString(1, username);
			stmt.setString(2, publicKey);
			stmt.setString(3, modulus);
			stmt.setBytes(4, shares[0]);
			stmt.setBytes(5, shares[1]);
			stmt.setBytes(6, shares[2]);
			stmt.setBytes(7, shares[3]);
			stmt.setString(8, prime);
			stmt.executeUpdate();
			return 0;
		} catch (SQLException ex) {
			return -1;
		}
	}
	
	public int storePartiesOfUser(String username, String[] parties) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("INSERT INTO trust VALUES ('" + username + "', '" + parties[0] + "', '" + parties[1] + "', '" + parties[2] + "', '" + parties[3]+ "');");
			return 0;
		} catch (SQLException ex) {
			return -1;
		}
	}
	
	public byte[][] getShare(String username) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT share1, share2, share3, share4 FROM share WHERE username = '" + username + "';");
			if (rs.next()) {
				byte[][] result = new byte[4][];
				for (int i = 0; i < 4; ++i)
					result[i] = rs.getBytes(i + 1);
				return result;
			}
			return null;
		} catch (SQLException ex) {
			return null;
		}
	}
	
	public String getPrime(String username) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT prime FROM share WHERE username = '" + username + "';");
			if (rs.next())
				return rs.getString(1);
			return null;
		} catch (SQLException ex) {
			return null;
		}
	}
	
	public String[] getPartiesOfUser(String username) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT party1, party2, party3, party4 FROM trust WHERE username = '" + username + "';");
			if (rs.next()) {
				String[] result = new String[4];
				for (int i = 0; i < 4; ++i)
					result[i] = rs.getString(i + 1);
				return result;
			}
			return null;
		} catch (SQLException ex) {
			return null;
		}
	}
	
	public String[] getPublicKey(String username) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT publickey, modulus FROM share WHERE username = '" + username + "';");
			if (rs.next()) {
				String[] result = new String[2];
				for (int i = 0; i < 2; ++i)
					result[i] = rs.getString(i + 1);
				return result;
			}
			return null;
		} catch (SQLException ex) {
			return null;
		}
	}
	
	public int insertParty(String name, String publicKeyExp, String modulus) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("INSERT INTO party VALUES ('" + name + "', '" + publicKeyExp + "', '" + modulus+ "');");
			return 0;
		} catch (SQLException ex) {
			return -1;
		}
	}
}
