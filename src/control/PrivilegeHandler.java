package control;

public class PrivilegeHandler extends Handler {
	
	private static int NUM_OF_PRIL = 3;
	//0 = read, 1 = write, 2 = add
	
	String[] stringPrivileges;
	String[] privileges;
	
	public String[] getStringPrivileges(){
		return stringPrivileges;
	}
	
	public String[] getPrivileges(){
		return privileges;
	}
	
	public void setStringPrivilege(String[] pri){
		this.stringPrivileges = new String[NUM_OF_PRIL];
		if ( pri[0].equals("true"))
			this.stringPrivileges[0] = "Read";
		if ( pri[1].equals("true"))
			this.stringPrivileges[1] = "Write";
		if ( pri[2].equals("true"))
			this.stringPrivileges[2] = "Add";
		
		for(int i = 0 ; i < stringPrivileges.length; i++)
			Logger.println("Privilege: "+stringPrivileges[i]);
	}
	public void setPrivilege(String[] pri){
		this.privileges = new String[NUM_OF_PRIL];
		for( int i = 1; i < pri.length; i++){
			//Logger.println(pri[i]);
			this.privileges[i-1] = pri[i];
		}

	}


	
}
