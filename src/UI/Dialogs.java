package UI;

import java.awt.Frame;

import javax.swing.JDialog;

import control.Client;

public class Dialogs extends JDialog {
	private String id = null;

	public String getID() {
		return id;
	}

	public void setID(String iD) {
		id = iD;
	}

	public Dialogs() {
		super(Client.getInstance().getMf(),true);
		// TODO Auto-generated constructor stub
	}


}
