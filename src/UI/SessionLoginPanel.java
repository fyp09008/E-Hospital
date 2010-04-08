//author chris
package UI;

import control.*;

import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JPasswordField;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class SessionLoginPanel extends LoginPanel  {
	//public Keyboard keyboard;
	private static final long serialVersionUID = 1L;
	private JPanel namePanel = null;
	private JPanel pwPanel = null;
	private JLabel nameLab = null;
	private JLabel pwLab = null;
	private JLabel welcomeLab = null;
	private JTextField nameFd = null;
	private JPasswordField pwFd = null;
	public JButton loginBtn = null;
	//private Client client = null;
	//private MainFrame mf = null;
	public boolean openKeyboard = true;
	public SessionLoginPanel() {
		// TODO Auto-generated constructor stub
		super(0);
		//this.mf = mf;
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
		welcomeLab.setText("Reinvoke Session");
		welcomeLab.setFont(new Font("Dialog", Font.BOLD, 36));
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 0;
		gridBagConstraints9.gridy = 2;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.gridy = 1;
		this.setSize(1024, 690);
		this.setLayout(new GridBagLayout());
		//this.add(getNamePanel(), gridBagConstraints8);
		this.add(getPwPanel(), gridBagConstraints8);
		this.add(new SessionKeyboardPanel(this),gridBagConstraints10);
		this.add(welcomeLab, gridBagConstraints);
		//this.add(getLoginBtn(), gridBagConstraints10);
		//this.disableAll();
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
			nameLab.setText("   Name    ");
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
	public JTextField getNameFd() {
		if (nameFd == null) {
			nameFd = new JTextField();
			nameFd.setPreferredSize(new Dimension(100, 20));
			nameFd.setFont(new Font("Dialog", Font.BOLD, 14));
			nameFd.setEditable(true);
			nameFd.setHorizontalAlignment(SwingConstants.CENTER);
			nameFd.setText("test");
			//nameFd.setText("test");
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
	 * 	
	 * @return javax.swing.JPanel	
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
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	public JPasswordField getPwFd() {
		if (pwFd == null) {
			pwFd = new JPasswordField();
			pwFd.setPreferredSize(new Dimension(100, 20));
			pwFd.setEnabled(false);
			pwFd.setText("test");
			pwFd.addFocusListener(new Focus(this));
		}
		return pwFd;
	}

	/**
	 * This method initializes loginBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getLoginBtn() {
		if (loginBtn == null) {
			loginBtn = new JButton();
			loginBtn.setText("Login");
			loginBtn.setEnabled(true);
			loginBtn.addActionListener(new LoginAction(this));
			//loginBtn.addKeyListener(new KeyAction(this));
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
		//nameFd.setText("");
		nameFd.setEnabled(true);
		loginBtn.setEnabled(true);
	}
	public void disableAll(){
		//System.out.println("disable all");
		pwFd.setEnabled(false);
		//nameFd.setText("---------- Insert your card ----------");
		nameFd.setEnabled(false);
		loginBtn.setEnabled(false);
	}
	class LoginAction implements ActionListener{
		SessionLoginPanel p = null;
		public LoginAction(SessionLoginPanel p){
			this.p=p;
		}
		public void actionPerformed(ActionEvent e) {
			String id = Client.getInstance().getName();
			String pw = new String(pwFd.getPassword());
	
				if (p.login(id,pw)){
					Client.getInstance().getMf().checkPrivilege();
					Client.getInstance().getMf().changePanel(-1);
				}
				else {}
			//}
		}
	}
	class Focus implements FocusListener{
		SessionLoginPanel lp;
		public Focus(SessionLoginPanel lp){
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
