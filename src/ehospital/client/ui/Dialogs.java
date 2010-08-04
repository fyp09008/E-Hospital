
package ehospital.client.ui;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JDialog;

import ehospital.client.control.Client;

/**
 * High level encapsulation of dialogs.
 * @author Chun
 *
 */
public class Dialogs extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
		this.setLocationRelativeTo(Client.getInstance().getMf());
	}
	class CloseAction implements WindowListener{

		public CloseAction(){
		}
		public void windowClosing(WindowEvent we){
			Client.getInstance().getMf().popup = new ArrayList<Component>();

	      }
		public void windowActivated(WindowEvent arg0) {
			
		}
		public void windowClosed(WindowEvent arg0) {
		}
		public void windowDeactivated(WindowEvent arg0) {
			
		}
		public void windowDeiconified(WindowEvent arg0) {
			
		}
		public void windowIconified(WindowEvent arg0) {
			
		}
		public void windowOpened(WindowEvent arg0) {
			
		}
	}


}
