package control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import message.QueryRequestMessage;
import message.QueryResponseMessage;
import message.UpdateRequestMessage;
import message.UpdateResponseMessage;

public class QueryHandler extends Handler {
	
	public static final byte TYPE_SELECT = 0;
	public static final byte TYPE_UPDATE = 1;
	public static final byte TYPE_INSERT = 2;

	public String[][] sendQuery(String type, String[] table, String[] field, 
			String whereClause, String[] value) {
		//TODO make it more generic
		//TODO separate into class
		type = type.toLowerCase();
		byte TYPE = -1;
		if (type.equals("select")) {
			TYPE = TYPE_SELECT;
		} else if (type.equals("update")) {
			TYPE = TYPE_UPDATE;
		} else if (type.equals("insert")) {
			TYPE = TYPE_INSERT;
		}
		String query;
		switch (TYPE) {
			case TYPE_SELECT:{
				System.out.println("*");
				query = "select ";
				for (int i = 0; i < field.length; i++) {
					query = i == field.length -1 ? query +field[i] : query + field[i] + ",";
				}
						
				query = query + " from ";
						
				for(int i = 0; i < table.length; i++) {
					query = i == table.length -1 ? query +table[i] : query + table[i] + ",";
				}
						
				query = query + " where " + whereClause;

				break;
			}
			case TYPE_UPDATE:{
				query = "update ";
				for(int i = 0; i < table.length; i++) {
					query = i == table.length -1 ? query +table[i] : query + table[i] + ",";
				}
				query += " set ";
				
				for (int i = 0; i < field.length; i++) {
					if ( i != field.length-1)
						if ( ! value[i].equals("NOW()"))
								query+= field[i] + " = '" + value[i] + "' , ";
						else
							query+= field[i] + " = " + value[i] + " , ";
					else
						query += field[i] + " = '" + value[i] + "' ";
				}
			
				query = query + " where " + whereClause;
				System.out.println(query);
				break;
			}
			case TYPE_INSERT:
				query = "insert ";
				break;
			default:
				return null;	
		}

		Logger.println(query);
		if(Client.getInstance().isConnected()) {
			QueryRequestMessage qmsg = null;
			switch(TYPE){
				case TYPE_SELECT: qmsg = new QueryRequestMessage(); break;
				case TYPE_UPDATE: {qmsg = new UpdateRequestMessage();
					((UpdateRequestMessage)qmsg).type = encryptAES(type.getBytes());
					break;}
				
				case TYPE_INSERT: qmsg = new UpdateRequestMessage();
					((UpdateRequestMessage)qmsg).type = type.getBytes();break;

			}
			qmsg.query = encryptAES(query.getBytes());
			
			qmsg.username = Client.getInstance().getName();
			Connector.getInstance().write(((Object) encryptPAES(objToBytes(qmsg))));
			Object reqmsg = bytesToObj(decryptPAES((byte[])Connector.getInstance().read()));
			if ( reqmsg instanceof QueryResponseMessage){
				QueryResponseMessage qrm = (QueryResponseMessage)reqmsg;
				byte[] rawResultSet = decryptAES(qrm.resultSet);
				ResultSet rs = byteToRS(rawResultSet);
				try {
					return RSparse(rs);
				} catch (SQLException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (reqmsg instanceof UpdateResponseMessage)
			{
				UpdateResponseMessage urm = (UpdateResponseMessage)reqmsg;
				String[][] s = new String[1][1];
				s[0][0] = Boolean.toString(urm.getStatus());
				return s;
			}
		}
		
		return null;
	}

	/*by chun, byte array to ResultSet*/
	public ResultSet byteToRS(byte[] rawResultSet){
		ResultSet rs = null;
		try {
			rs = (ResultSet)new ObjectInputStream(new 
				ByteArrayInputStream(rawResultSet)).readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	/*By pizza*/
	public static String[][] RSparse(ResultSet result) throws SQLException
	{
		String[][] s = null;
		ArrayList<String[]> slist = new ArrayList<String[]>();
		while (result.next())
		{
			String[] tmp = new String[result.getMetaData().getColumnCount()];
			for (int i = 0; i < result.getMetaData().getColumnCount(); i++)
			{
				tmp[i] = result.getString(i+1);
			}
			slist.add(tmp);
		}
		s = new String[slist.size()][result.getMetaData().getColumnCount()];
		for (int i = 0; i < slist.size(); i++)
		{
			s[i] = slist.get(i);
		}
		return s;
		
	}
	
}
