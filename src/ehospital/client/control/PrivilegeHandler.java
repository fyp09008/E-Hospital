package ehospital.client.control;

/**
 * Handler Privilege granted by the server.
 * @author  Chun
 */
public class PrivilegeHandler extends Handler {
	
	/**
	 * @uml.property  name="read"
	 */
	private boolean read = false;
	/**
	 * @uml.property  name="write"
	 */
	private boolean write = false;
	/**
	 * @uml.property  name="add"
	 */
	private boolean add = false;

	/**
	 * @return
	 * @uml.property  name="read"
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @param  read
	 * @uml.property  name="read"
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * @return
	 * @uml.property  name="write"
	 */
	public boolean isWrite() {
		return write;
	}

	/**
	 * @param  write
	 * @uml.property  name="write"
	 */
	public void setWrite(boolean write) {
		this.write = write;
	}

	/**
	 * @return
	 * @uml.property  name="add"
	 */
	public boolean isAdd() {
		return add;
	}

	/**
	 * @param  add
	 * @uml.property  name="add"
	 */
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
