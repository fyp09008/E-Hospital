package control;

public class PrivilegeHandler extends Handler {
	
	private boolean read = false;
	private boolean write = false;
	private boolean add = false;

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}
	
	public void setPrivilege(String[] pri){
		//pri[0] is the ID
		if ( pri[1].equals("true"))
			setRead(true);
		if ( pri[2].equals("true"))
			setWrite(true);
		if ( pri[3].equals("true"))
			setAdd(true);
	}

}
