package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import control.Client;
import control.Task;

public class LoginDialog extends JDialog {
	//private JTextField nameFld = null;
	private JPasswordField pwFld = null;
	//private Client client = null;
	private JButton loginBtn = null;
	private MainFrame parent = null;
	private JPanel btnPanel = null;
	private JPanel fldPanel = null;

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
		//super();
		super(Client.getInstance().getMf(),true);
		//this.parent = parent;
		//this.client = c;
		initialize();
	}
	public JButton getLoginBtn() {
		if ( loginBtn == null){
			loginBtn = new JButton("Login");
			loginBtn.addActionListener(new ButtonAction(this));
		}
		return loginBtn;
	}

	public JPanel getBtnPanel() {
		if ( btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.add(getLoginBtn());
		}
		return btnPanel;
	}
	public void initialize(){
		//this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(parent);
		this.setTitle("CLOSE it before unplugging card");
		this.setSize(new Dimension(600,180));
		this.setLayout(new BorderLayout());
		//this.add(new JLabel("Name"));
		//this.add(getNameFld());
		//this.add(new JLabel("Password"));
		//this.add(getPwFld());
		
		//this.add(warning,BorderLayout.NORTH);
		
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
	
	class ButtonAction implements ActionListener {
		LoginDialog ld = null;
		public ButtonAction(LoginDialog ld){
			this.ld = ld;
		}
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Client.getInstance().setPassword(new String(ld.getPwFd().getPassword()));
			
			if (Client.getInstance().authenticate()){
				Client.getInstance().getT().cancel();
				Client.getInstance().reload();
			}
			else{
				Client.getInstance().reset();
				Client.getInstance().resetTimer(Task.PRE_AUTH);
			}
			ld.dispose();
			Client.getInstance().getMf().resumePopUp();
			
			
		}
		
	}


}
