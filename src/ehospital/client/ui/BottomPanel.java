
package ehospital.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Panel in the botton of UI interface.
 * @author Gilbert
 *
 */
public class BottomPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JLabel status;
	JLabel privilege;
	JLabel personal;
	/**
	 * Default constructor.
	 */
	public BottomPanel(){
		super();
		initialize();
	}
	/**
	 * Initialize UI component 
	 */
	private void initialize(){
		Border blackline = BorderFactory.createLineBorder(Color.black);
		this.setBorder(blackline);
		this.setLayout(new GridLayout(1,4));
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(20,25));
		this.setVisible(true);
		personal = new JLabel("           User: ");
		privilege = new JLabel("Privileges: ");
		status = new JLabel("Status:   Logout");
		this.add(personal);
		this.add(privilege);
		this.add(status);
		CurrentTime ct = new CurrentTime();
		this.add(ct);
		Thread t = new Thread(ct);
		t.start();
	}
	public void setStatus(String s){
		status.setText("Status:  " + s);
	}
	public void setPersonal(String s){
		personal.setText("           User: "+s);
	}
	public void setPrivilege(String s){
		privilege.setText("Privileges: "+s);
	}
}
