
package ehospital.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import ehospital.client.control.Client;

/**
 * Dialog class for logging in.
 * @author   Chun
 */
public class LoginDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPasswordField pwFld = null;
	/**
	 * @uml.property  name="loginBtn"
	 */
	private JButton loginBtn = null;
	/**
	 * @uml.property  name="btnPanel"
	 */
	private JPanel btnPanel = null;
	/**
	 * @uml.property  name="fldPanel"
	 */
	private JPanel fldPanel = null;

	/**
	 * @return
	 * @uml.property  name="fldPanel"
	 */
	public JPanel getFldPanel() {
		if ( fldPanel == null){
			fldPanel = new JPanel(new GridLayout(1,2));
			JLabel label = new JLabel("Password");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			fldPanel.add(new JLabel("Password"));
			fldPanel.add(getPwFd());
		}
		return fldPanel;
	}
	public LoginDialog(){
		super(Client.getInstance().getMf(),true);
		initialize();
	}
	/**
	 * @return
	 * @uml.property  name="loginBtn"
	 */
	public JButton getLoginBtn() {
		if ( loginBtn == null){
			loginBtn = new JButton("Login");
			loginBtn.addActionListener(new ButtonAction(this));
		}
		return loginBtn;
	}

	/**
	 * @return
	 * @uml.property  name="btnPanel"
	 */
	public JPanel getBtnPanel() {
		if ( btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.add(getLoginBtn());
		}
		return btnPanel;
	}
	public void initialize(){
		this.setLocationRelativeTo(Client.getInstance().getMf());
		this.setTitle("CLOSE it AFTER unplugging card");
		this.setSize(new Dimension(600,180));
		this.setLayout(new BorderLayout());
		
		this.add(new KeyboardPanel(this),BorderLayout.SOUTH);
		this.add(getFldPanel(),BorderLayout.CENTER);
		this.setVisible(true);
		
	}
	public JPasswordField getPwFd() {
		if ( pwFld == null){
			pwFld = new JPasswordField();
			pwFld.setEnabled(false);
		}
		return pwFld;
	}
	
	/**
	 * @author   Gilbert
	 */
	class ButtonAction implements ActionListener {
		/**
		 * @uml.property  name="ld"
		 * @uml.associationEnd  
		 */
		LoginDialog ld = null;
		public ButtonAction(LoginDialog ld){
			this.ld = ld;
		}
		public void actionPerformed(ActionEvent arg0) {

		}
		
	}


}
