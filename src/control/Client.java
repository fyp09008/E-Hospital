package control;


import java.util.Timer;

import UI.LoginDialog;
import UI.MainFrame;

public class Client {
	
	private Client() {
		
	}
	
	private static class ClientHolder { 
	     private static final Client INSTANCE = new Client();
	 }
	 
	public static Client getInstance() {
	     return ClientHolder.INSTANCE;
	}
	
	private MainFrame mf;
	
	public boolean authenticate() {return false;}
	
	public void reset(){
		
		Connector.getInstance().setConnected(false);
		
		this.mf.logoutPanel(true);
		System.out.println("Reseted all");
	}
	
	public void setMf(MainFrame mf) {
		this.mf = mf;
	}
	
	public void re_login()
	{	
		new LoginDialog(mf,true,this);
	}
	
	public void reload(){
		t.cancel();
		mf.restorePanel();
		t = new Timer();
		t.schedule(new Task(t, mf, Task.AFTER_AUTH), new Date(), Task.PERIOD);
	}

	public boolean authenicate() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPassword(String string) {
		// TODO Auto-generated method stub
		
	}


	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getStringPrivileges() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	public String[][] sendQuery(String type, String[] table, String[] field,
			String whereClause, String[] values) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	public Timer getT() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getPrivileges() {
		// TODO Auto-generated method stub
		return null;
	}

	public void card_unplug() {
		// TODO Auto-generated method stub
		
	}

	public void setT(Timer timer) {
		// TODO Auto-generated method stub
		
	}

	public boolean logout() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	

	
		
}

