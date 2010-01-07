package UI;

import javax.swing.JDialog;
import javax.swing.JPanel;
//Add treatment requires Add privilege
public class AddTreatmentPanel extends JDialog {
	
	private MainFrame mf = null;
	private JPanel btnPanel = null;
	
	public JPanel getBtnPanel() {
		if ( btnPanel == null)
			btnPanel = new JPanel();
		return btnPanel;
	}

	public AddTreatmentPanel() {
		super();
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		this.setSize(400,300);
		this.setVisible(true);
		
	}
	
}
