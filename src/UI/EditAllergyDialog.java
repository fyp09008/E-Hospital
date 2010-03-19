package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import control.Client;

public class EditAllergyDialog extends Dialogs {
	
	private int panelToRefresh; 
	Vector<String> allergyID;
	Vector<String> description;
	Vector<String> hasAllergyName;
	Vector<String> noDescription;
	Vector<String> noAllergyName;
	Vector<String> noAllergyID;
	AddAllergyPanel aap;
	RemoveAllergyPanel rap;
	JPanel bigPanel;
	JPanel btnPanel;
	JButton done;
	JButton cancel;
	
	public EditAllergyDialog(String pid, int panel){
		super();
		panelToRefresh = panel;
		setID(pid);
		noAllergyID = new Vector<String>();
		noAllergyName = new Vector<String>();
		hasAllergyName = new Vector<String>();
		allergyID = new Vector<String>();
		description = new Vector<String>();
		noDescription = new Vector<String>();
		doQueries();
		initialize();
	}
	private void doQueries(){
		String[] tables = {"allergy","`dia-allergy_rec`"};
		String[] fields = {"allergy_id","name","description"};
		String where = "allergy.id = `dia-allergy_rec`.allergy_id and `dia-allergy_rec`.valid = 1 and" +
				"`dia-allergy_rec`.pat_id = " + getID() + " ORDER BY name";
		String[][] result = Client.getInstance().sendQuery("SELECT", tables, fields, where , null);
		
		for(int i = 0;i < result.length; i++){
			allergyID.add(result[i][0]);
			hasAllergyName.add(result[i][1]);
			description.add(result[i][2]);
		}
		String[] table = {"allergy"};
		String[] field = {"id, name, description"};
		String whereClause = "id not in (select allergy_id from `dia-allergy_rec` " +
				"where pat_id = '"+getID()+"' and valid = '1') ORDER BY name";
		String[][] result2 = Client.getInstance().sendQuery("SELECT", table, field, whereClause, null);
		for(int i =0;i < result2.length;i++){
			noAllergyID.add(result2[i][0]);
			noAllergyName.add(result2[i][1]);
			noDescription.add(result2[i][2]);
		}
	}
	protected void initialize(){
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(Client.getInstance().getMf());
		aap = new AddAllergyPanel(getID(),noAllergyID,noAllergyName,noDescription);
		JScrollPane scroll = new JScrollPane(aap);
		rap = new RemoveAllergyPanel(getID(),allergyID,hasAllergyName,description);
		JScrollPane scroll1 = new JScrollPane(rap);
		getBigPanel().add(scroll,BorderLayout.NORTH);
		getBigPanel().add(scroll1,BorderLayout.CENTER);
		this.add(getBigPanel(),BorderLayout.CENTER);
		this.add(getBtnPanel(),BorderLayout.SOUTH);
		Actions actions = new Actions(this);
		done = new JButton("Done");
		done.setActionCommand("DONE");
		done.addActionListener(actions);
		cancel = new JButton("Cancel");
		cancel.setActionCommand("CANCEL");
		cancel.addActionListener(actions);
		getBtnPanel().add(done);
		getBtnPanel().add(cancel);
		
		this.setSize(300,300);
		this.setVisible(true);
	}
	private JPanel getBigPanel(){
		if( bigPanel == null){
			bigPanel = new JPanel();
			bigPanel.setLayout(new BorderLayout());
		}
		return bigPanel;
	}
	private JPanel getBtnPanel(){
		if ( btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.setLayout(new GridLayout(1,2));
		}
		return btnPanel;
	}
	class Actions implements ActionListener{
		EditAllergyDialog ead;
		public Actions(EditAllergyDialog ead){
			this.ead = ead;
		}
		public void actionPerformed(ActionEvent ae) {
			if ( ae.getActionCommand().equals("DONE")){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				int ans = o.showConfirmDialog(null, "Sure?");
				if ( ans == 0){
					boolean add = aap.submit();
					boolean remove = rap.submit();
					if(add && remove){
						JOptionPane m = new JOptionPane();
						Client.getInstance().getMf().addPopUP(o);
						m.showMessageDialog(null, "Record modification succeed!");
						if ( panelToRefresh == MainFrame.MY_PATIENT_LIST)
							Client.getInstance().getMf().refreshMyPatient(ead.getID());
						if ( panelToRefresh == MainFrame.SEARCH_PATIENTS){
							Client.getInstance().getMf().refreshSearchPanel("ID", ead.getID());
						}
						Client.getInstance().getMf().popup = new ArrayList<Component>();
						ead.dispose();
					}else {
						JOptionPane m = new JOptionPane();
						Client.getInstance().getMf().addPopUP(o);
						Client.getInstance().getMf().popup = new ArrayList<Component>();
						m.showMessageDialog(null, "Record modification failed!");
					}
					
				}
			}else{
				 Client.getInstance().getMf().popup = new ArrayList<Component>();
				 ead.dispose();// TODO Auto-generated Event stub actionPerformed()
			}
		}
	}
}
