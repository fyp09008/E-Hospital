package UI;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//Add treatment requires Add privilege
public class AddTreatmentDialog extends Dialogs {
	
	//private MainFrame mf = null;
	private JPanel btnPanel = null;
	private JButton done = null;
	private JButton cancel = null;
	private JScrollPane scroll = null;
	
	public JScrollPane getScroll() {
		if ( scroll == null){
			scroll = new JScrollPane(getText());
		}
		return scroll;
	}

	public JTextArea getText() {
		if ( text == null){
			text = new JTextArea();
		}
		return text;
	}

	private JTextArea text = null;
	
	
	public JButton getDone() {
		if ( done == null){
			done = new JButton("Done");
		}
		return done;
	}

	public JButton getCancel() {
		if ( cancel == null){
			cancel = new JButton("Cancel");
		}
		return cancel;
	}



	public JPanel getBtnPanel() {
		if ( btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.add(getDone());
			btnPanel.add(getCancel());
		}
		return btnPanel;
	}

	public AddTreatmentDialog(String pid) {
		super();
		setID(pid);
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		this.setTitle("Add New Treatment Record for patient "+getID());
		this.setSize(400,300);
		this.setLayout(new BorderLayout());
		this.add(getScroll(),BorderLayout.CENTER);
		this.add(getBtnPanel(),BorderLayout.SOUTH);
		this.setVisible(true);
		//this.add(comp);
		
	}
	
}
