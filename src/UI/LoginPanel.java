package UI;

import control.*;

import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JPasswordField;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class LoginPanel extends Panels  {
	private static final long serialVersionUID = 1L;
	private JPanel namePanel = null;
	private JPanel pwPanel = null;
	private JLabel nameLab = null;
	private JLabel pwLab = null;
	private JLabel welcomeLab = null;
	private JTextField nameFd = null;
	private JPasswordField pwFd = null;
	private JButton loginBtn = null;
	private Client client = null;
	private MainFrame mf = null;
	public LoginPanel(MainFrame mf) {
		// TODO Auto-generated constructor stub
		super();
		this.mf = mf;
		//client = c;
		initialize();
	}


	/**
	 * This method initializes this
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
		welcomeLab.setText("Secure Medicinal System");
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
		this.add(getLoginBtn(), gridBagConstraints10);
		this.disableAll();
	}

	/**
	 * This method initializes namePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNamePanel() {
		if (namePanel == null) {
			nameLab = new JLabel();
			nameLab.setVerticalAlignment(SwingConstants.CENTER);
			nameLab.setText("Name");
			nameLab.setHorizontalTextPosition(SwingConstants.CENTER);
			nameLab.setEnabled(true);
			nameLab.setHorizontalAlignment(SwingConstants.CENTER);
			namePanel = new JPanel();
			namePanel.setLayout(new BorderLayout());
			namePanel.setPreferredSize(new Dimension(400, 30));
			namePanel.add(nameLab, BorderLayout.BEFORE_LINE_BEGINS);
			namePanel.add(getNameFd(), BorderLayout.CENTER);
		}
		return namePanel;
	}

	/**
	 * This method initializes nameFd	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNameFd() {
		if (nameFd == null) {
			nameFd = new JTextField();
			nameFd.setPreferredSize(new Dimension(100, 20));
			nameFd.setFont(new Font("Dialog", Font.ITALIC, 14));
			nameFd.setEditable(true);
			nameFd.setHorizontalAlignment(SwingConstants.CENTER);
			nameFd.setText("---------- Insert your card ----------");
		}
		return nameFd;
	}

	/**
	 * This method initializes pwPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPwPanel() {
		if (pwPanel == null) {
			pwLab = new JLabel();
			pwLab.setVerticalAlignment(SwingConstants.CENTER);
			pwLab.setText("Password");
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
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getPwFd() {
		if (pwFd == null) {
			pwFd = new JPasswordField();
			pwFd.setPreferredSize(new Dimension(100, 20));
			pwFd.setEnabled(true);
		}
		return pwFd;
	}

	/**
	 * This method initializes loginBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLoginBtn() {
		if (loginBtn == null) {
			loginBtn = new JButton();
			loginBtn.setText("Login");
			loginBtn.setEnabled(true);
			loginBtn.addActionListener(new LoginAction(this));		
		}
		return loginBtn;
	}
	private boolean login(String name, String password){
		mf.setName(name);
		mf.setPassword(password);
		boolean logged = mf.authenicate(name,password);
		return logged;
	}
	public void enableAll(){
		pwFd.setEnabled(true);
		nameFd.setEnabled(true);
		loginBtn.setEnabled(true);
	}
	public void disableAll(){
		System.out.println("disable all");
		pwFd.setEnabled(false);
		nameFd.setEnabled(false);
		loginBtn.setEnabled(false);
	}
	class LoginAction implements ActionListener{
		LoginPanel p = null;
		public LoginAction(LoginPanel p){
			this.p=p;
		}
		public void actionPerformed(ActionEvent e) {
			String id = nameFd.getText();
			String pw = new String(pwFd.getPassword());
			System.out.println(pw);
			if (id.equals("") || pw.equals("")){
				JOptionPane jop = new JOptionPane();
				JOptionPane.showMessageDialog(jop, "Empty Fields","ERROR",JOptionPane.ERROR_MESSAGE);
			}
			else{
				if (p.login(id,pw)){
					
					mf.checkPrivilege();
					mf.changePanel(-1);
				}
				else {}
			}
		}
	}
}  //  @jve:decl-index=0:visual-constraint="24,5"