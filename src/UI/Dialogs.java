package UI;

import java.awt.Frame;

import javax.swing.JDialog;

public class Dialogs extends JDialog {
	private String id = null;

	public String getID() {
		return id;
	}

	public void setID(String iD) {
		id = iD;
	}

	public Dialogs() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Dialogs(Frame owner) {
		super(owner, true);
		// TODO Auto-generated constructor stub
	}
}
