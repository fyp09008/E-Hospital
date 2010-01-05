package UI;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;

public class Panels extends JPanel implements Cloneable{
	public Panels() {
		// TODO Auto-generated constructor stub
		super();
		initialize();
	}
	private void initialize(){
		this.setSize(1024, 590);
	}
	public Object clone(){
		return this;
	}

}  //  @jve:decl-index=0:visual-constraint="24,57"
