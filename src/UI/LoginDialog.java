package UI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import control.Client;

public class LoginDialog extends JDialog {
	//private JTextField nameFld = null;
	private JPasswordField pwFld = null;
	private Client client = null;
	private JButton loginBtn = null;
	private MainFrame parent = null;


	public LoginDialog(MainFrame parent, boolean modal,Client c){
		
		super(parent,modal);
		this.parent = parent;
		this.client = c;
		initialize();
	}
	public JButton getLoginBtn() {
		if ( loginBtn == null){
			loginBtn = new JButton("Login");
			loginBtn.addActionListener(new ButtonAction(this));
		}
		return loginBtn;
	}

	public void initialize(){
		this.setSize(new Dimension(300,300));
		this.setLayout(new GridLayout(2,2));
		//this.add(new JLabel("Name"));
		//this.add(getNameFld());
		this.add(new JLabel("Password"));
		this.add(getPwFld());
		this.add(getLoginBtn());
		this.setVisible(true);
		
	}
	public JPasswordField getPwFld() {
		if ( pwFld == null)
			pwFld = new JPasswordField();
		return pwFld;
	}
	class ButtonAction implements ActionListener {
		LoginDialog ld = null;
		public ButtonAction(LoginDialog ld){
			this.ld = ld;
		}
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			ld.client.setPassword(new String(ld.getPwFld().getPassword()));
			if (ld.client.authenicate()){
				//System.out.println("ok");
				ld.client.reload();
			}
			else{
				ld.client.getT().cancel();
				ld.client.setT(new Timer());
				ld.client.getT().schedule(new Task(ld.client.getT(), ld.parent, Task.PRE_AUTH), new Date(), Task.PERIOD);
				ld.client.reset();
				System.out.println("not ok");
				
			}
			ld.dispose();
		}
		
	}


}
