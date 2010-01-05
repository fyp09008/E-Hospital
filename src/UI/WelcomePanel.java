package UI;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class WelcomePanel extends Panels {
	private JPanel contentPane = null;
	private JTextArea msg = null;
	private MainFrame mf = null;
	public WelcomePanel(MainFrame mf){
		super();
		this.mf=mf;
		this.initialize();
	}
	private void initialize(){
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getMsg(),BorderLayout.CENTER);
		this.addText(mf.getName(),mf.getStringPrivileges());
	}
	private void addText(String name, String[] pri){
		getMsg().setText("Welcome "+name+"\n");
		getMsg().setText("You have the following privileges\n");
		for(int i = 0; i < pri.length; i++){
			getMsg().setText(pri[i]+"\n");
		}
		
		
	}
	private JTextArea getMsg(){
		if ( msg == null){
			msg = new JTextArea();
		}
		return msg;
	}
	private JPanel getContentPane(){
		return this.getContentPane();
	}
}
