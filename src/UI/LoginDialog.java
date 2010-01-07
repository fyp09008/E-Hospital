package UI;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import control.Client;

public class LoginDialog extends JDialog {
	//private JTextField nameFld = null;
	private JTextField pwFld = null;
	private Client client = null;
	public LoginDialog(MainFrame parent, boolean modal,Client c){
		
		super(parent,modal);
		this.client = c;
		initialize();
	}
	public void initialize(){
		this.setSize(new Dimension(300,300));
		this.setLayout(new GridLayout(2,2));
		//this.add(new JLabel("Name"));
		//this.add(getNameFld());
		this.add(new JLabel("Password"));
		this.add(getPwFld());
		this.add(new JButton("Login"));
		this.setVisible(true);
		
	}
	public JTextField getPwFld() {
		if ( pwFld == null)
			pwFld = new JTextField();
		return pwFld;
	}


}
