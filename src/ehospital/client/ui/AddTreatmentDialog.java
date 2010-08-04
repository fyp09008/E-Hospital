 
package ehospital.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ehospital.client.control.Client;

/**
 * Dialog Class for adding treatment Add treatment requires Add privilege
 * @author   Chun
 */
public class AddTreatmentDialog extends Dialogs {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="btnPanel"
	 */
	private JPanel btnPanel = null;
	/**
	 * @uml.property  name="done"
	 */
	private JButton done = null;
	/**
	 * @uml.property  name="cancel"
	 */
	private JButton cancel = null;
	/**
	 * @uml.property  name="scroll"
	 */
	private JScrollPane scroll = null;
	private JTextArea text = null;
	int panelToRefresh;
	/**
	 * @uml.property  name="actions"
	 * @uml.associationEnd  
	 */
	private Actions actions;
	public AddTreatmentDialog(String pid, int panel){
		this.setID(pid);
		panelToRefresh = panel;
	}
	/**
	 * @return
	 * @uml.property  name="scroll"
	 */
	public JScrollPane getScroll() {
		if ( scroll == null){
			scroll = new JScrollPane(getTextArea());
		}
		return scroll;
	}

	public JTextArea getTextArea() {
		if ( text == null){
			text = new JTextArea();
		}
		return text;
	}
	/**
	 * @return
	 * @uml.property  name="done"
	 */
	public JButton getDone() {
		if ( done == null){
			done = new JButton("Done");
			done.setActionCommand("DONE");
			done.addActionListener(actions);
			
		}
		return done;
	}
	/**
	 * @return
	 * @uml.property  name="cancel"
	 */
	public JButton getCancel() {
		if ( cancel == null){
			cancel = new JButton("Cancel");
			cancel.setActionCommand("CANCEL");
			cancel.addActionListener(actions);
		}
		return cancel;
	}

	/**
	 * @return
	 * @uml.property  name="btnPanel"
	 */
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
		actions = new Actions(this);
		this.setTitle("Add New Treatment Record for patient "+getID());
		this.setSize(400,300);
		this.setLayout(new BorderLayout());
		this.add(getScroll(),BorderLayout.CENTER);
		this.add(getBtnPanel(),BorderLayout.SOUTH);
		this.setVisible(true);
		
	}
	/**
	 * @author  Chun
	 */
	class Actions implements ActionListener{
		/**
		 * @uml.property  name="atd"
		 * @uml.associationEnd  
		 */
		AddTreatmentDialog atd;
		public Actions(AddTreatmentDialog atd){
			this.atd = atd;
		}
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent ae) {
			if ( ae.getActionCommand().equals("DONE")){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				int ans = o.showConfirmDialog(null, "Sure?");
				if ( ans == 0){
					//String[] table = {"treatment"};
					//String[] field = {"id","pid","pic","description","date_of_issue"};
					String[] values = {atd.getID(),Client.getInstance().getID(),getTextArea().getText()};
					boolean result = false;
					try {
						result = Client.getInstance().sendUpdate("INSERT INTO treatment (pid, pic, description, date_of_issue) VALUES (?,?,?,CURDATE());", values);
					} catch (RemoteException e) {
						Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
					} catch (NotBoundException e) {
						Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
					}
					if(result){
						Client.getInstance().getMf().popup = new ArrayList<Component>();
						atd.dispose();
						JOptionPane m = new JOptionPane();
						Client.getInstance().getMf().addPopUP(o);
						int ans1 = 1;
						ans1 = m.showConfirmDialog(null, "Record modification succeed! Refresh?");
						if ( ans1 == 0){
							if ( panelToRefresh == MainFrame.MY_PATIENT_LIST)
								Client.getInstance().getMf().refreshMyPatient(atd.getID());
							if ( panelToRefresh == MainFrame.SEARCH_PATIENTS){
								Client.getInstance().getMf().refreshSearchPanel("ID", atd.getID());
							}
						}

					}else {
						JOptionPane m = new JOptionPane();
						Client.getInstance().getMf().addPopUP(o);
						Client.getInstance().getMf().popup = new ArrayList<Component>();
						m.showMessageDialog(null, "Record modification failed!");
					}
				}

			}else{
				//CANCEL action
				 Client.getInstance().getMf().popup = new ArrayList<Component>();
				 atd.dispose();
			}
		}
	}
}
	

