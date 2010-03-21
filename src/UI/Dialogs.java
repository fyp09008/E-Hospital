package UI;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

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
		Client.getInstance().getMf().addPopUP(this);
		this.addWindowListener(new CloseAction());
	}
	class CloseAction implements WindowListener{

		public CloseAction(){
		}
		public void windowClosing(WindowEvent we){
			Client.getInstance().getMf().popup = new ArrayList<Component>();

	      }
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
		}
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}


}
