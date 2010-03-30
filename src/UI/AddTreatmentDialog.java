//author chris
package UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import control.Client;
//Add treatment requires Add privilege
public class AddTreatmentDialog extends Dialogs {
	
	//private MainFrame mf = null;
	private JPanel btnPanel = null;
	private JButton done = null;
	private JButton cancel = null;
	private JScrollPane scroll = null;
	private JTextArea text = null;
	int panelToRefresh;
	private Actions actions;
	public AddTreatmentDialog(String pid, int panel){
		this.setID(pid);
		panelToRefresh = panel;
	}
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
	public JButton getDone() {
		if ( done == null){
			done = new JButton("Done");
			done.setActionCommand("DONE");
			done.addActionListener(actions);
			
		}
		return done;
	}
	public JButton getCancel() {
		if ( cancel == null){
			cancel = new JButton("Cancel");
			cancel.setActionCommand("CANCEL");
			cancel.addActionListener(actions);
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
		actions = new Actions(this);
		// TODO Auto-generated method stub
		this.setTitle("Add New Treatment Record for patient "+getID());
		this.setSize(400,300);
		this.setLayout(new BorderLayout());
		this.add(getScroll(),BorderLayout.CENTER);
		this.add(getBtnPanel(),BorderLayout.SOUTH);
		this.setVisible(true);
		
	}
	class Actions implements ActionListener{
		AddTreatmentDialog atd;
		public Actions(AddTreatmentDialog atd){
			this.atd = atd;
		}
		public void actionPerformed(ActionEvent ae) {
			if ( ae.getActionCommand().equals("DONE")){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				int ans = o.showConfirmDialog(null, "Sure?");
				if ( ans == 0){
					String[] table = {"treatment"};
					String[] field = {"id","pid","pic","description","date_of_issue"};
					String[] values = {atd.getID(),Client.getInstance().getID(),"'"+getTextArea().getText()+"'","CURDATE()"};
//					String[][] result = Client.getInstance().sendQuery("INSERT", table, field, null, values);
					String[][] result = null;
					try {
						result = Client.getInstance().sendQuery("INSERT INTO treatment (pid, pic, description, date_of_issue) VALUES (?,?,?,?);", values);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotBoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(result[0][0].equals("true")){
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
				 atd.dispose();// TODO Auto-generated Event stub actionPerformed()
			}
		}
	}
}
	

