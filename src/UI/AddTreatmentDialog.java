package UI;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
//Add treatment requires Add privilege
public class AddTreatmentDialog extends Dialogs {
	
	private MainFrame mf = null;
	private JPanel btnPanel = null;
	private JButton done = null;
	private JButton cancel = null;
	
	
	public JButton getDone() {
		if ( done == null){
			done = new JButton("Done");
		}
		return done;
	}

	public JButton getCancel() {
		if ( cancel == null){
			cancel = new JButton("Done");
		}
		return cancel;
	}

	public void setMf(MainFrame mf) {
		this.mf = mf;
	}

	public JPanel getBtnPanel() {
		if ( btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.add(getDone());
			btnPanel.add(getCancel());
		}
		return btnPanel;
	}

	public AddTreatmentDialog() {
		super();
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		this.setSize(400,300);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		this.add(comp)
		
	}
	
}
