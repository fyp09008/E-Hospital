package DBPopulator.DBPopulator.src;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


/**
 * 
 */

/**
 * @author Gilbert
 *
 */
public class mainClass {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBManager dbm = new DBManager();
		System.out.println(dbm.connect());
		try {
			java.sql.ResultSet rsTable = dbm.query("show tables;");
			ArrayList<String> tables = new ArrayList<String>();
			while (rsTable.next()) {
				if (!(rsTable.getString(1).equals("user") || rsTable.getString(1).equals("privilege"))) {
					tables.add(rsTable.getString(1));
				}
			}
			for(int i = 0; i < tables.size();i++) {
				System.out.println(tables.get(i));
				java.sql.ResultSet rs = dbm.query("desc `" + tables.get(i) + "`;");
				String query = "INSERT INTO `"+ tables.get(i) + "` (";
				ArrayList<String> fields = new ArrayList<String>();
				ArrayList<String> types = new ArrayList<String>();
				while (rs.next()) {
					fields.add(rs.getString(1));
					if (!(fields.get(fields.size() - 1).equals("id") || (fields.size() - 1 == 0 && fields.get(fields.size() - 1).equals("pid"))))
						query = query + "`" + fields.get(fields.size() - 1) + "`, ";
					types.add(rs.getString(2));
				}
				
				query = query.substring(0, query.length()-2);
				query += ") VALUES (";
				String subquery;
				
				int RAND_RECORD = 100000;
				for (int j = 0; j < RAND_RECORD; j++) {
					subquery = query;
					String randData = "";
					for(int k = 0; k < types.size(); k++) {
						if (k != 0 && types.get(k).equals("int(11)")) {
							
							//random < 100000
							Random ran = new Random();
							int randno = ran.nextInt(RAND_RECORD) + 1;
							String randnoStr = Integer.toString(randno);
							randData = randData +randnoStr + ", ";

						} else if (types.get(k).equals("tinyint(1)")) {
							//random T/F
							Random ran = new Random();
							boolean randno = ran.nextBoolean();
							String randnoStr = (randno) ? "1" : "0";
							randData = randData+randnoStr + ", ";
						} else if (fields.get(k).equals("gender")) {
							//random T/F
							Random ran = new Random();
							boolean randno = ran.nextBoolean();
							String randnoStr = (randno) ? "M" : "F";
							randData = randData+"'" +randnoStr + "', ";
						} else if (types.get(k).contains("date")) {
							//random date using sql
							randData = randData+ "FROM_UNIXTIME(RAND()*2147483647), ";
						} else if (fields.get(k).equals("pic")) {
							//random < 100000
							Random ran = new Random();
							int randno = ran.nextInt(4) + 1;
							String randnoStr = Integer.toString(randno);
							randData = randData+randnoStr + ", ";
						} else if (k != 0 && fields.get(k).contains("id")) {
							//random < 100000
							Random ran = new Random();
							int randno = ran.nextInt(RAND_RECORD) + 1;
							String randnoStr = Integer.toString(randno);
							randData = randData +randnoStr + ", ";
						} else if (fields.get(k).equals("contact_no")) {
							//random ^[23569]xxxxxxx
							Random ran = new Random();
							int randno;
							do {
								randno= ran.nextInt(99999999) + 1;
							} while (!((randno / 10000000) == 2 || 
									(randno / 10000000) == 3 || 
									(randno / 10000000) == 5 || 
									(randno / 10000000) == 6 || 
									(randno / 10000000) == 9 || randno / 100000 != 999));
							String randnoStr = Integer.toString(randno);
							randData = randData+"'" +randnoStr + "', ";

						} else if (k!=0) {
							Random ran = new Random();
							String STRING_ASCII = "0987654321qweyuioplkjhgfdsazcvbnmQWETYUIOPLKJHGFDSAZXCVBNM";
							randData += "'";
							for (int a = 0; a < 50; a++) {
								int randno = ran.nextInt(STRING_ASCII.length());
								randData += STRING_ASCII.charAt(randno);
							}
							randData += "', ";
							
						}
					}
					subquery = subquery + randData.substring(0, randData.length()-2) + ");";
					System.out.println(subquery);
					dbm.update(subquery);
					
					randData = "";
				}
				System.out.println();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Your SQL is lame!");
			e.printStackTrace();
		}
	}

}
