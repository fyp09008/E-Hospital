package ehospital.client.control;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;

import javax.crypto.spec.SecretKeySpec;

import cipher.RSAHardware;
import cipher.RSASoftware;
import ehospital.client.ui.LoginDialog;
import ehospital.client.ui.MainFrame;

/**
 * A Singleton class Provide service to handle all input from user and output to user.
 * @author   Chun, Gilbert
 */
public class Client {
	
	// Private constructor prevents instantiation from other classes
	private Client() {
		log = new Logger();
		rsa = new RSASoftware();
		rsaHard = new RSAHardware();
		pHandler = new PrivilegeHandler();
		aHandler = new ClientAuthHandler();
		qHandler = new QueryHandler();
		//logout handler initialized by aHandler later
	}
	 
	
	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()   or the first access to SingletonHolder.INSTANCE, not before.
	 * @author  Chun
	 */
	private static class ClientHolder { 
		/**
		 * @uml.property  name="iNSTANCE"
		 * @uml.associationEnd  
		 */
		private static final Client INSTANCE = new Client();
	}
	/**
	 * Get the logger
	 * @return Logger object
	 */
	public Logger getLogger(){
		return log;
	}
	
	/**
	 * Invoke handler to authenticate others to use the system
	 * 
	 * @param hisName 
	 * @param pw
	 * @param cardNo
	 * @return true if successful
	 * @see ehospital.client.control.ClientAuthHandler
	 */
	public boolean authOther(String hisName, String pw, int cardNo){
		return this.aHandler.authOther(this.getName(), hisName, pw, cardNo);
	}
	
	/**
	 * @return the instance of Client
	 */
	public static Client getInstance() {
		return ClientHolder.INSTANCE;
	}
	/**
	 * @uml.property  name="log"
	 * @uml.associationEnd  
	 */
	private Logger log;
	/**
	 * @uml.property  name="t"
	 */
	private Timer t = null;
	/**
	 * @uml.property  name="mf"
	 * @uml.associationEnd  
	 */
	private MainFrame mf;
	private String id = null;
	/**
	 * @uml.property  name="name"
	 */
	private String name = null;
	/**
	 * @uml.property  name="password"
	 */
	private String password = null;
	/**
	 * @uml.property  name="rsa"
	 * @uml.associationEnd  
	 */
	private RSASoftware rsa = null;
	/**
	 * @uml.property  name="rsaHard"
	 * @uml.associationEnd  
	 */
	private RSAHardware rsaHard = null;
	/**
	 * @uml.property  name="skeySpec"
	 */
	private SecretKeySpec skeySpec;
	/**
	 * @uml.property  name="aHandler"
	 * @uml.associationEnd  
	 */
	private ClientAuthHandler aHandler = null;
	/**
	 * @uml.property  name="lHandler"
	 * @uml.associationEnd  
	 */
	private LogoutHandler lHandler = null;
	/**
	 * @uml.property  name="pHandler"
	 * @uml.associationEnd  
	 */
	private PrivilegeHandler pHandler = null;
	/**
	 * @uml.property  name="qHandler"
	 * @uml.associationEnd  
	 */
	private QueryHandler qHandler = null;	
	
	public PrivilegeHandler getPrivilegeHandler(){
		return pHandler;
	}
	public boolean authenticate(){
		return aHandler.authenicate();
	}
	public ClientAuthHandler getClientAHanlder(){
		return aHandler;
	}
	
	/** 
	 * Initialize logout handler
	 * 
	 * @param a a random byte generated by the server
	 * @see ehospital.client.control.LogoutHandler
	 */
	public void initLogoutHandler(byte[] a){
		lHandler = new LogoutHandler(a);
	}
	/**
	 * Reset the Client instance
	 */
	public void reset(){
		aHandler.unplugCard();
		this.id = null;
		this.name = null;
		this.password = null;
		rsa = new RSASoftware();
		rsaHard = new RSAHardware();
		pHandler = new PrivilegeHandler();
		aHandler = new ClientAuthHandler();
		qHandler = new QueryHandler();
		Connector.getInstance().setConnected(false);
		this.skeySpec = null;
		this.mf.logoutPanel(true);
		log.debug(this.getClass().getName(),"Logged out and reseted");
	}
	
	/**
	 * @return Check if the client is connected to the server
	 */
	public boolean isConnected(){
		return Connector.getInstance().isConnected();
	}
	
	public String getID(){
		return id;
	}
	public void setID(String s){
		id = s;
	}
	
	/**
	 * @param  name
	 * @uml.property  name="name"
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * @param  password
	 * @uml.property  name="password"
	 */
	public void setPassword(String password){
		this.password = password;
	}
	
	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @param  mf
	 * @uml.property  name="mf"
	 */
	public void setMf(MainFrame mf) {
		this.mf = mf;
	}
	
	/**
	 * Prompt the user to login again
	 */
	public void re_login()
	{	
		new LoginDialog();
	}
	
	/**
	 * Reload the client user interface.
	 */
	public void reload(){
		log.debug(this.getClass().getName(),"Relogin finished and reloading");
		mf.restorePanel();
		resetTimer(Task.AFTER_AUTH);
		Client.getInstance().log.debug(this.getClass().getName(),"in reload");
	}

	/**
	 * Invoke query handler to send query to the server
	 * @param type
	 * @param table
	 * @param field
	 * @param whereClause
	 * @param values
	 * @return A set of data in 2D String Array 
	 * @see ehospital.client.control.QueryHandler
	 */
	public String[][] sendQuery(String type, String[] table, String[] field,
			String whereClause, String[] values) {
		return qHandler.sendQuery(type, table, field, whereClause, values);
	}

	/**
	 * Invoke query handler to send query to the server
	 * @param sql
	 * @param param
	 * @return
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws SQLException
	 * @see ehospital.client.control.QueryHandler
	 */
	public String[][] sendQuery(String sql, String[] param) throws RemoteException, NotBoundException, SQLException {
		return qHandler.query(this.getName(), sql, param);
	}
	
	/**
	 * Invoke query handler to send update to the server
	 * @param sql
	 * @param param
	 * @return
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @see ehospital.client.control.QueryHandler
	 */
	public boolean sendUpdate(String sql, String[] param) throws RemoteException, NotBoundException
	{
		return qHandler.update(this.getName(), sql, param);
	}

	/**
	 * Logout from the server.
	 * @return true if successful
	 */
	public boolean logout() {
		return lHandler.logout();
	}
	/**
	 * @return
	 * @uml.property  name="mf"
	 */
	public MainFrame getMf() {
		return mf;
	}
	/**
	 * @return
	 * @uml.property  name="t"
	 */
	public Timer getT() {
		return t;
	}
	/**
	 * @param  t
	 * @uml.property  name="t"
	 */
	public void setT(Timer t) {
		this.t = t;
	}
	/**
	 * Handle state of unplugged card.
	 */
	public void card_unplug()
	{
		this.aHandler.unplugCard();
		t.cancel();
		t = new Timer();
		t.schedule(new Task(Task.WAIT_REAUTH), new Date(),Task.PERIOD);
	}
	
	/**
	 * Handle transition to WAIT_SESSION.
	 */
	public void session_end()
	{
		t.cancel();
		t = new Timer();
		t.schedule(new Task(Task.WAIT_SESSION), new Date(),Task.PERIOD);
	}

	public RSAHardware getRSAHard(){
		return rsaHard;
	}
	public RSASoftware getRSASoft(){
		return rsa;
	}
	/**
	 * @return
	 * @uml.property  name="password"
	 */
	public String getPassword(){
		return password;
	}
	/**
	 * @return
	 * @uml.property  name="skeySpec"
	 */
	public SecretKeySpec getSkeySpec() {
		return skeySpec;
	}
	/**
	 * @param  skeySpec
	 * @uml.property  name="skeySpec"
	 */
	public void setSkeySpec(SecretKeySpec skeySpec) {
		this.skeySpec = skeySpec;
	}		
	/**
	 * Reset the timer for timeout action.
	 *  
	 * @param mode
	 * @see ehospital.client.control.Task
	 */
	public void resetTimer(int mode){
		if ( t != null)
			t.cancel();
		t = new Timer();
		t.schedule(new Task(mode),new Date(),Task.PERIOD);
	}
	public boolean isRead(){
		return pHandler.isRead();
	}
	public boolean isWrite(){
		return pHandler.isWrite();
	}
	public boolean isAdd(){
		return pHandler.isAdd();
	}
	/** 
	 * Handle use case: changing password.
	 * @param string
	 * @param string2
	 * @return true if successful 
	 */
	public boolean changePassword(String string, String string2) {
		return this.aHandler.changePassword(this.getName(),string,string2);
	}


}
