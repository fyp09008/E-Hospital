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
		this.setLayout(new BorderLayout());
		this.add(getMsg(),BorderLayout.CENTER);
		this.addText(mf.getName(),mf.getStringPrivileges());
	}
	private void addText(String name, String[] pri){
		//System.out.println(pri[0]);
		getMsg().setText("Welcome\n");
		getMsg().append("ID: " + mf.getID() + 
				"\nName: " + mf.getName()+ " \n" );
		getMsg().append("You have the following privileges\n");
		/*for(int i = 0; i < pri.length; i++){
			if ( pri[i] != null)
				getMsg().append(pri[i]+"\n");
		}*/
		getMsg().invalidate();
		getMsg().validate();
		
		
	}
	private JTextArea getMsg(){
		if ( msg == null){
			msg = new JTextArea();
		}
		return msg;
	}

}
