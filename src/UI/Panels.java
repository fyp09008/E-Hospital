package UI;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;

public class Panels extends JPanel implements Cloneable{
	String id = "";

	public Panels() {
		// TODO Auto-generated constructor stub
		super();
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
