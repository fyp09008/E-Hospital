package UI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
public class ShowInfoPanel extends Panels {

	private JScrollPane jScrollPane = null;
	private JTextArea info = null;
	private String pid = null;
	private JPanel btnPanel = null;
	private JButton editBtn = null;
	private JButton allgeryBtn = null;
	private JButton treatmentBtn = null;
	private String[][] result = null; 
	private MainFrame mf = null;
	/**
	 * This method initializes 
	 * 
	 */
	public ShowInfoPanel() {
		super();
		//this.mf = mf;
		initialize();
		this.checkPrivilege();
	}
	public void fetchInfo(){
		this.getInfo().setEditable(false);
		String br = "\n";
		this.getInfo().setText("");
		this.getInfo().append("********** Read-only **********"+br+br);
		//deal with patient personal info
		String[] tables = {"Patient_personal"};
		String[] fields = {"name","gender","address","contact_no",
				"birthday","pic","description"};
		result = mf.sendQuery("SELECT", tables, 
				fields, "pid = '"+pid+"'",null);
		if ( result != null){
			this.getInfo().append("********** Personal **********"+br+br);
			this.getInfo().append("Name: "+result[0][0]+br);
			this.getInfo().append("Gender: " + result[0][1]+br);
			this.getInfo().append("Address: " + result[0][2]+br);
			this.getInfo().append("Contact No.: " + result[0][3]+br);
			this.getInfo().append("Date Of Birth: " + result[0][4]+br);
			this.getInfo().append("Person In Charge: " + result[0][5]+br);
			this.getInfo().append("Remarks: " + br + result[0][6]+br);			
		}
		//deal with allergy
		String[] allergyTable = {"allergy","`dia-allergy_rec`"};
		String[] allergyFields = {"name"};
		String[][] allergyResult = mf.sendQuery("SELECT", allergyTable, 
				allergyFields, "`dia-allergy_rec`.allergy_id = allergy.id"
				+ " AND `dia-allergy_rec`.`pat_id` = "+pid, null);
		System.out.println(allergyResult.length);
		if ( ! (allergyResult.length == 0)){
			this.getInfo().append(br+"********** Allergies **********"+br+br);
			for ( int i = 0; i < allergyResult.length; i++){
				this.getInfo().append(allergyResult[i][0]+br);
			}
		}
		
		
		String[] treatmentTable = {"treatment"};
		String[] treatmentFields = {"pic","date_of_issue","description"};
		String[][] treatmentResult = mf.sendQuery("SELECT", treatmentTable, 
				treatmentFields, "pid = "+pid, null);
		if ( ! (treatmentResult.length == 0)){
			this.getInfo().append(br+"********** Treatments **********"+br+br);
			for ( int i = 0;  i < treatmentResult.length; i++){
				this.getInfo().append("PIC: ");
				this.getInfo().append(treatmentResult[i][0]+br);
				this.getInfo().append("Date of Issue: ");
				this.getInfo().append(treatmentResult[i][1]+br);
				this.getInfo().append("Description: ");
				this.getInfo().append(treatmentResult[i][2]+br);
				this.getInfo().append(br+"+++++++++++++++++++++++++++++++"+br);
			}
			
		}
		
		this.getInfo().append(br+"*******************************"+br);
	}
	public void checkPrivilege(){
		String[] pri = mf.getPrivileges();
		//write
		if ( pri[1].equals("true")){
			editBtn.setEnabled(true);
			allgeryBtn.setEnabled(true);
		}
		//read
		if ( pri[2].equals("true")){
			treatmentBtn.setEnabled(true);
		}
	}
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(1024, 590));
        //this.add(getInfo(), BorderLayout.CENTER);
        this.add(getJScrollPane(), BorderLayout.CENTER);
        getInfo().setText("");
        this.add(getBtnPanel(),BorderLayout.SOUTH);
	}
	private JPanel getBtnPanel(){
		if ( btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.setLayout(new GridLayout(1,3));
			btnPanel.add(getEditButton());
			btnPanel.add(getAllgeryButton());
			btnPanel.add(getTreatmentButton());
		}
		return btnPanel;
	}
	public void hasEditted(){
		this.getInfo().setText("Modification is made, Please select again");
	}
	private JButton getEditButton(){
		ImageIcon icon = createImageIcon("edit-icon.png",
        "a pretty but meaningless splat");
		editBtn = new JButton("Personal Info",icon);
		editBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
	    editBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		editBtn.setPreferredSize(new Dimension(59, 80));
		editBtn.addActionListener(new EditAction(this));
		editBtn.setEnabled(false);
		return editBtn;
	}
	private JButton getAllgeryButton(){
		//ImageIcon icon = createImageIcon("edit-icon.png",
        //"a pretty but meaningless splat");
		allgeryBtn = new JButton("Edit Allgery");
		allgeryBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
		allgeryBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		allgeryBtn.setPreferredSize(new Dimension(59, 80));
		allgeryBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				//new EditInfoFrame(); // TODO Auto-generated Event stub actionPerformed()
			}
		});
		allgeryBtn.setEnabled(false);
		return allgeryBtn;
	}
	private JButton getTreatmentButton(){
		//ImageIcon icon = createImageIcon("edit-icon.png",
        //"a pretty but meaningless splat");
		treatmentBtn = new JButton("Add Treatment");
		treatmentBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
		treatmentBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		treatmentBtn.setPreferredSize(new Dimension(59, 80));
		treatmentBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				//new EditInfoFrame(); // TODO Auto-generated Event stub actionPerformed()
			}
		});
		treatmentBtn.setEnabled(false);
		return treatmentBtn;
	}
	protected ImageIcon createImageIcon(String path,
            String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane(getInfo());
		}
		return jScrollPane;
	}
	public void setInfo(String s){
		getInfo().append(s);
	}
	public void setInfo(String s, char de){
		//for(int i =0; i < s.length; i++)
		getInfo().append(s);
		//getInfo().setText("\n");
	}
	public void setPID(String s){
		this.pid = s;
		System.out.println("IN GETID: "+s);
	}

	/**
	 * This method initializes info	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextArea getInfo() {
		if (info == null) {
			info = new JTextArea();
			info.setText("***********************************\n" 
					+ "Click to see info"
					+ "\n***********************************\n");
		}
		return info;
	}
	class EditAction implements ActionListener  {
		private ShowInfoPanel info = null;
		public EditAction(ShowInfoPanel f){
			super();
			info = f;
		}
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if ( info.getInfo().getText().equals("")){
				JOptionPane o = new JOptionPane();
				this.info.mf.addPopUP(o);
				o.showMessageDialog(null, "Record not selected");
			}	
			else{
				EditInfoDialog eif = new EditInfoDialog(result,pid);
			}
			 //info.mf.addPopUP(eif);
		}
	};

}  //  @jve:decl-index=0:visual-constraint="10,10"
