//author chris
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ehospital.client.control.Client;

/**
 * Panel class to change password. 
 * @author   Chun
 */
public class ChangePwPanel extends Panels  {
	public int selected = 0;
	
	private static final long serialVersionUID = 1L;
	
	private JLabel welcomeLab = null;
	/**
	 * @uml.property  name="nameFd"
	 */
	private JTextField nameFd = null;
	/**
	 * @uml.property  name="loginBtn"
	 */
	public JButton loginBtn = null;
	private JPasswordField pwFd[] = null;
	private JPanel pwPanel[] = null;
	private JLabel pwLab[] = null;
	private JRadioButton radio[] = null;
	/**
	 * @uml.property  name="ra"
	 * @uml.associationEnd  
	 */
	private RadioActions ra =  null;
	
	public boolean openKeyboard = true;
	
	
	/**
	 * Default constructor.
	 */
	public ChangePwPanel() {
		super();
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		pwFd = new JPasswordField[3];
		pwPanel = new JPanel[3];
		pwLab = new JLabel[3];
		radio = new JRadioButton[3];
		ra = new RadioActions();
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 4;
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 0;
		gridBagConstraints10.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		welcomeLab = new JLabel();
		welcomeLab.setText("Change your password");
		welcomeLab.setFont(new Font("Dialog", Font.BOLD, 36));
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 0;
		gridBagConstraints9.gridy = 2;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.gridy = 1;
		this.setSize(1024, 690);
		this.setLayout(new GridBagLayout());
		this.add(getPwPanel(0), gridBagConstraints8);
		this.add(getPwPanel(1),gridBagConstraints9);
		this.add(welcomeLab, gridBagConstraints);
		this.add(getPwPanel(2), gridBagConstraints10);
		this.add(new ChangePwKeyboard(this),gridBagConstraints11);
	}

	/**
	 * This method initializes namePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JRadioButton getRadio(int i){
		if ( radio[i] == null){
			radio[i] = new JRadioButton();
			if ( i == 0)
				radio[i].setSelected(true);
			else
				radio[i].setSelected(false);
			radio[i].setActionCommand(Integer.toString(i));
			radio[i].addActionListener(ra);
		}
		return radio[i];
	}
	

	/**
	 * Initializes nameFd	
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
			nameFd.setText("test");
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
	 * Initializes pwPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPwPanel(int i) {
		if (pwPanel[i] == null) {
			pwLab[i] = new JLabel();
			pwLab[i].setVerticalAlignment(SwingConstants.CENTER);
			if ( i == 0)
				pwLab[i].setText("Old Password ");
			else if ( i == 1)
				pwLab[i].setText("New Password ");
			else
				pwLab[i].setText("New Password ");
			pwPanel[i] = new JPanel();
			pwPanel[i].setLayout(new BorderLayout());
			pwPanel[i].setPreferredSize(new Dimension(400, 30));
			pwPanel[i].add(pwLab[i], BorderLayout.WEST);
			pwPanel[i].add(getPwFd(i), BorderLayout.CENTER);
			pwPanel[i].add(getRadio(i),BorderLayout.EAST);
		}
		return pwPanel[i];
	}


	/**
	 * Initializes pwFd	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	public JPasswordField getPwFd(int i) {
		if (pwFd[i] == null) {
			pwFd[i] = new JPasswordField();
			pwFd[i].setPreferredSize(new Dimension(100, 20));
			pwFd[i].setEnabled(false);
		}
		return pwFd[i];
	}

	/**
	 * Initializes loginBtn	
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
		for(int i =0;i < 3; i ++)
			pwFd[i].setEnabled(true);
		nameFd.setEnabled(true);
		loginBtn.setEnabled(true);
	}
	public void disableAll(){
		for(int i =0;i < 3; i ++)
			pwFd[i].setEnabled(false);
		nameFd.setEnabled(false);
		loginBtn.setEnabled(false);
	}
	/**
	 * @author   Chun
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

	}

	@Override
	public void focusLost(FocusEvent arg0) {
		getLoginBtn().requestFocus();
	}}
	class RadioActions implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int i = Integer.parseInt(arg0.getActionCommand());
			switch(i){
			case 0:	{ radio[0].setSelected(true); radio[1].setSelected(false); 
						radio[2].setSelected(false); selected = 0; break;}
			case 1:	{ radio[1].setSelected(true); radio[0].setSelected(false); 
					radio[2].setSelected(false); selected = 1; break;}
			case 2:	{ radio[2].setSelected(true); radio[1].setSelected(false); 
					radio[0].setSelected(false); selected = 2; break;}
			default: break;
			}
		}
		
	}
}
