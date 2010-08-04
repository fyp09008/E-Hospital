//author chris
package ehospital.client.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Panel class that can be clone.
 * @author Chun
 *
 */
public class Panels extends JPanel implements Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String id = "";

	public Panels() {
		super();
		//share();
	}
	public void share(){
		this.setLayout(new BorderLayout());
	}
	public Object clone(){
		return this;
	}
	public String getID() {
		return id;
	}
	public void setID(String pid) {
		this.id = pid;
	}

}  //  @jve:decl-index=0:visual-constraint="24,57"
