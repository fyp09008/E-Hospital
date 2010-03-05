package UI;
import control.Client;
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
		this.addText(mf.getName());
	}
	private void addText(String name){
		getMsg().setText("Welcome\n");
		getMsg().append("ID: " + mf.getID() + 
				"\nName: " + mf.getName()+ " \n" );
		getMsg().append("You have the following privileges\n");
		String privil = "";
		privil += Client.getInstance().isRead()?"Read\n":"";
		privil += Client.getInstance().isWrite()?"Write\n":"";
		privil += Client.getInstance().isAdd()?"Add\n":"";
		getMsg().append(privil);
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
