package ehospital.client.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import message.QueryRequestMessage;
import message.QueryResponseMessage;
import message.UpdateRequestMessage;
import message.UpdateResponseMessage;
import remote.obj.DataHandler;

/**
 * This class is handling the implementation of the client for database manipulation.
 * @author Pizza
 *
 */

public class QueryHandler extends Handler {
	
	public static final byte TYPE_SELECT = 0;
	public static final byte TYPE_UPDATE = 1;
	public static final byte TYPE_INSERT = 2;

	/**
	 * Send query to the database and retrieve result set.
	 * @param type
	 * @param table
	 * @param field
	 * @param whereClause
	 * @param value
	 * @return a set of data in String[][]
	 */
	@SuppressWarnings("deprecation")
	public String[][] sendQuery(String type, String[] table, String[] field, 
			String whereClause, String[] value) {
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
				query = "select ";
				for (int i = 0; i < field.length; i++) {
					query = i == field.length -1 ? query +field[i] : query + field[i] + ",";
				}
						
				query = query + " from ";
						
				for(int i = 0; i < table.length; i++) {
					query = i == table.length -1 ? query +table[i] : query + table[i] + ",";
				}
				if ( whereClause !=null)
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
				Client.getInstance().getLogger().debug(this.getClass().getName(),query);
				break;
			}
			case TYPE_INSERT:{
				query = "insert into ";
				for(int i = 0; i < table.length; i++) {
					query = i == table.length -1 ? query +table[i] : query + table[i] + ",";
				}
				query += "(";
				if ( field != null){
					for (int i = 0; i < field.length; i++) {
						query = i == field.length -1 ? query +field[i] : query + field[i] + ",";
					}
				} 
				query += ") VALUES (";
				if ( value != null){
					for (int i = 0; i < value.length; i++) {
						query = i == field.length -1 ? query +value[i] : query + value[i] + ",";
					}
				}
				query += ")";
				break;
			}
			default:
				return null;	
		}

		Client.getInstance().getLogger().debug(this.getClass().getName(),query);
		if(Client.getInstance().isConnected()) {
			QueryRequestMessage qmsg = null;
			switch(TYPE){
				case TYPE_SELECT: qmsg = new QueryRequestMessage(); break;
				case TYPE_UPDATE: {qmsg = new UpdateRequestMessage();
					((UpdateRequestMessage)qmsg).type = encryptAES(type.getBytes());
					break;}
				
				case TYPE_INSERT: {qmsg = new UpdateRequestMessage();
					((UpdateRequestMessage)qmsg).type = encryptAES(type.getBytes());
					break;
				}
			}
			
			Client.getInstance().getLogger().printPlain(this.getClass().getName(), "Plain Query", 
				query.getBytes(), query);
			qmsg.query = encryptAES(query.getBytes());
			qmsg.username = Client.getInstance().getName();
			String encryptedQueryString = new String(qmsg.query);
			Client.getInstance().getLogger().printCipher(this.getClass().getName(), "Plain Query", 
					qmsg.query, encryptedQueryString);
			//Connector.getInstance().write(((Object) encryptPAES(objToBytes(qmsg))));
			//Object reqmsg = bytesToObj(decryptPAES((byte[])Connector.getInstance().read()));
			
			/**
			 *  This is added to make the program compilable..
			 */
			Object reqmsg = new Object();
			
			
			if ( reqmsg instanceof QueryResponseMessage){
				QueryResponseMessage qrm = (QueryResponseMessage)reqmsg;
				byte[] rawResultSet = decryptAES(qrm.resultSet);
				ResultSet rs = byteToRS(rawResultSet);
				try {
					return RSparse(rs);
				} catch (SQLException e) {
					Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
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

	/**
	 * 
	 * @param rawResultSet
	 * @return result set
	 */
	public ResultSet byteToRS(byte[] rawResultSet){
		ResultSet rs = null;
		try {
			rs = (ResultSet)new ObjectInputStream(new 
				ByteArrayInputStream(rawResultSet)).readObject();
		} catch (IOException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (ClassNotFoundException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		return rs;
	}

	/**
	 * Query the database.
	 * @param username
	 * @param query
	 * @param param
	 * @return a String[][]
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws SQLException
	 */
	@SuppressWarnings("deprecation")
	public String[][] query(String username, String query, String[] param) throws RemoteException, NotBoundException, SQLException
	{
		Registry r = LocateRegistry.getRegistry(Driver.serverPath, 1099);
		DataHandler dh = (DataHandler) r.lookup("DataHandler");
		byte[] q = encryptPAES(encryptAES(query.getBytes()));
		byte[] p = encryptPAES(encryptAES(Utility.objToBytes(param)));
		return RSparse((ResultSet) Utility.BytesToObj(decryptAES(decryptPAES(dh.query(username, q, p)))));
	}

	/**
	 * Update the database.
	 * @param username
	 * @param query
	 * @param param
	 * @return true if successful
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	@SuppressWarnings("deprecation")
	public boolean update(String username, String query, String[] param) throws RemoteException, NotBoundException
	{
		Registry r = LocateRegistry.getRegistry(Driver.serverPath, 1099);
		DataHandler dh = (DataHandler) r.lookup("DataHandler");
		byte[] q = encryptPAES(encryptAES(query.getBytes()));
		byte[] p = encryptPAES(encryptAES(Utility.objToBytes(param)));
		return dh.update(username, q, p);
	}
	
	/**
	 * Parse Result set into String[][].
	 * @param result
	 * @return A 2D String array
	 * @throws SQLException
	 */
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
