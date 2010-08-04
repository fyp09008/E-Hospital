
package ehospital.client.ui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ehospital.client.control.Client;

/**
 * Authenication Panel Class
 * @author   Chun
 */
public class AuthPeoplePanel extends Panels  {
	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="namePanel"
	 */
	private JPanel namePanel = null;
	/**
	 * @uml.property  name="pwPanel"
	 */
	private JPanel pwPanel = null;
	private JLabel nameLab = null;
	private JLabel pwLab = null;
	private JLabel welcomeLab = null;
	/**
	 * @uml.property  name="nameFd"
	 */
	private JTextField nameFd = null;
	/**
	 * @uml.property  name="pwFd"
	 */
	private JPasswordField pwFd = null;
	/**
	 * @uml.property  name="loginBtn"
	 */
	public JButton loginBtn = null;
	public JComboBox combo = null;
	public boolean openKeyboard = true;
	public AuthPeoplePanel(int i){super();}
	public AuthPeoplePanel() {
		super();
		initialize();
	}


	/**
	 * Initializes UI component
	 * 
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 0;
		gridBagConstraints10.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		welcomeLab = new JLabel();
		welcomeLab.setText("Assign Temporary Card");
		welcomeLab.setFont(new Font("Dialog", Font.BOLD, 36));
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 0;
		gridBagConstraints9.gridy = 2;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.gridy = 1;
		this.setSize(1024, 690);
		this.setLayout(new GridBagLayout());
		this.add(getNamePanel(), gridBagConstraints8);
		this.add(getPwPanel(), gridBagConstraints9);
		this.add(welcomeLab, gridBagConstraints);
		this.add(new AuthOtherKeyboardPanel(this), gridBagConstraints10);
	}

	/**
	 * This method initializes namePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JComboBox getComboBox(){

		if ( combo == null){
			Vector<String> v = new Vector<String>();
			for(int i = 0;i < 10; i++){
				v.add(Integer.toString(i));
			}
			combo = new JComboBox(v);
		}
		return combo;
		
	}
	/**
	 * @return
	 * @uml.property  name="namePanel"
	 */
	private JPanel getNamePanel() {
		if (namePanel == null) {
			nameLab = new JLabel();
			nameLab.setVerticalAlignment(SwingConstants.CENTER);
			nameLab.setText("Name:Card No.");
			nameLab.setHorizontalTextPosition(SwingConstants.CENTER);
			nameLab.setEnabled(true);
			nameLab.setHorizontalAlignment(SwingConstants.CENTER);
			namePanel = new JPanel();
			namePanel.setLayout(new BorderLayout());
			namePanel.setPreferredSize(new Dimension(400, 30));
			namePanel.add(nameLab, BorderLayout.BEFORE_LINE_BEGINS);
			namePanel.add(getNameFd(), BorderLayout.CENTER);
			namePanel.add(getComboBox(),BorderLayout.EAST);
		}
		return namePanel;
	}

	/**
	 * This method initializes nameFd	
	 * @return   javax.swing.JTextField
	 * @uml.property  name="nameFd"
	 */
	public JTextField getNameFd() {
		if (nameFd == null) {
			nameFd = new JTextField();
			nameFd.setPreferredSize(new Dimension(100, 20));
			nameFd.setFont(new Font("Dialog", Font.BOLD, 14));
			nameFd.setEditable(true);
			nameFd.setHorizontalAlignment(SwingConstants.CENTER);
			nameFd.addFocusListener(new FocusListener(){
				public void focusGained(FocusEvent arg0) {
					nameFd.setText("");
				}
				public void focusLost(FocusEvent arg0) {}
			
			});
		}
		return nameFd;
	}

	/**
	 * This method initializes pwPanel	
	 * @return   javax.swing.JPanel
	 * @uml.property  name="pwPanel"
	 */
	private JPanel getPwPanel() {
		if (pwPanel == null) {
			pwLab = new JLabel();
			pwLab.setVerticalAlignment(SwingConstants.CENTER);
			pwLab.setText("Password ");
			pwPanel = new JPanel();
			pwPanel.setLayout(new BorderLayout());
			pwPanel.setPreferredSize(new Dimension(400, 30));
			pwPanel.add(pwLab, BorderLayout.WEST);
			pwPanel.add(getPwFd(), BorderLayout.CENTER);
		}
		return pwPanel;
	}

	/**
	 * This method initializes pwFd	
	 * @return   javax.swing.JPasswordField
	 * @uml.property  name="pwFd"
	 */
	public JPasswordField getPwFd() {
		if (pwFd == null) {
			pwFd = new JPasswordField();
			pwFd.setPreferredSize(new Dimension(100, 20));
			pwFd.setEnabled(false);
			pwFd.addFocusListener(new Focus(this));
		}
		return pwFd;
	}

	/**
	 * This method initializes loginBtn	
	 * @return   javax.swing.JButton
	 * @uml.property  name="loginBtn"
	 */
	public JButton getLoginBtn() {
		if (loginBtn == null) {
			loginBtn = new JButton();
			loginBtn.setText("Login");
			loginBtn.setEnabled(true);
		}
		return loginBtn;
	}
	public boolean login(String name, String password){
		Client.getInstance().getMf().setName(name);
		Client.getInstance().getMf().setPassword(password);
		boolean logged = Client.getInstance().getMf().authenticate(name,password);
		return logged;
	}
	public void enableAll(){
		pwFd.setEnabled(true);
		nameFd.setEnabled(true);
		loginBtn.setEnabled(true);
	}
	public void disableAll(){
		pwFd.setEnabled(false);
		nameFd.setEnabled(false);
		loginBtn.setEnabled(false);
	}
	/**
	 * @author   Gilbert
	 */
	class LoginAction implements ActionListener{
		/**
		 * @uml.property  name="p"
		 * @uml.associationEnd  
		 */
		LoginPanel p = null;
		public LoginAction(LoginPanel p){
			this.p=p;
		}
		public void actionPerformed(ActionEvent e) {
			String id = nameFd.getText();
			String pw = new String(pwFd.getPassword());
	
				if (p.login(id,pw)){
					Client.getInstance().getMf().checkPrivilege();
					Client.getInstance().getMf().changePanel(-1);
				}
				else {}
		}
	}
	/**
	 * @author   Chun
	 */
	class Focus implements FocusListener{
		/**
		 * @uml.property  name="lp"
		 * @uml.associationEnd  
		 */
		AuthPeoplePanel lp;
		public Focus(AuthPeoplePanel lp){
			this.lp = lp;
		}
		public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent arg0) {
		getLoginBtn().requestFocus();
	}}
}
