
package ehospital.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import ehospital.client.control.Client;
/**
 * Panel class show information of the 
 * @author   Chun
 */
public class ShowInfoPanel extends Panels {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static SimpleAttributeSet BOLD_BLACK = new SimpleAttributeSet();
	static SimpleAttributeSet ITALIC_GRAY = new SimpleAttributeSet();
	static SimpleAttributeSet BLACK = new SimpleAttributeSet();
	static {
	    StyleConstants.setForeground(BOLD_BLACK, Color.black);
	    StyleConstants.setBold(BOLD_BLACK, true);
	    StyleConstants.setFontFamily(BOLD_BLACK, "Helvetica");
	    StyleConstants.setFontSize(BOLD_BLACK, 14);
	    
	    StyleConstants.setForeground(ITALIC_GRAY, Color.gray);
	    StyleConstants.setItalic(ITALIC_GRAY, true);
	    StyleConstants.setFontFamily(ITALIC_GRAY, "Helvetica");
	    StyleConstants.setFontSize(ITALIC_GRAY, 14);
	    
	    StyleConstants.setForeground(BLACK, Color.black);
	    StyleConstants.setFontFamily(BLACK, "Helvetica");
	    StyleConstants.setFontSize(BLACK, 14);
	}
	private static final String br = "\n";
	private static final String star = "***************";
	/**
	 * @uml.property  name="jScrollPane"
	 */
	private JScrollPane jScrollPane = null;
	/**
	 * @uml.property  name="info"
	 */
	private JTextPane info = null;
	private String pid = null;
	/**
	 * @uml.property  name="btnPanel"
	 */
	private JPanel btnPanel = null;
	private JButton editBtn = null;
	private JButton allgeryBtn = null;
	private JButton treatmentBtn = null;
	private String[][] result = null; 
	/**
	 * @uml.property  name="bc"
	 * @uml.associationEnd  
	 */
	private ButtonAction bc;
	private int panelToRefresh;
	/**
	 * This method initializes 
	 * 
	 */
	public ShowInfoPanel(int panel) {
		super();
		bc = new ButtonAction(this);
		initialize();
		panelToRefresh = panel;
		this.checkPrivilege();
	}
	protected void insertText(String text, AttributeSet set) {
		try {
			info.getDocument().insertString(
			info.getDocument().getLength(), text, set);
		} catch (BadLocationException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
	}
	public void fetchInfo(){
		this.getInfo().setEditable(false);

		this.getInfo().setText("");
		insertText(star+" Read-only "+star+br+br,BOLD_BLACK);
		//deal with patient personal info
		//String[] tables = {"Patient_personal"};
		//String[] fields = {"name","gender","address","contact_no",
		//		"birthday","pic","description"};
		String[] param = {pid};
		try {
			result = Client.getInstance().sendQuery("SELECT name, gender, address, contact_no, birthday, pic, " +
					"description FROM Patient_personal WHERE pid=?;", param);
		} catch (RemoteException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (NotBoundException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (SQLException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		if ( result != null){
			insertText(star+" Personal "+star+br+br,BOLD_BLACK);
			insertText("Name: ",ITALIC_GRAY);
			insertText(result[0][0]+br,BLACK);
			insertText("Gender: ",ITALIC_GRAY);
			insertText(result[0][1]+br,BLACK);
			insertText("Address: ",ITALIC_GRAY);
			insertText(result[0][2]+br,BLACK);
			insertText("Contact No.: ",ITALIC_GRAY);
			insertText(result[0][3]+br,BLACK);
			insertText("Date Of Birth: ",ITALIC_GRAY);
			insertText(result[0][4]+br,BLACK);
			insertText("Person In Charge: ",ITALIC_GRAY);
			insertText(result[0][5]+br,BLACK);
			insertText("Remarks: ",ITALIC_GRAY);
			insertText(br + result[0][6]+br,BLACK);			
		}
		
		//deal with allergy
		//String[] allergyTable = {"allergy","`dia-allergy_rec`"};
		//String[] allergyFields = {"name,description"};
		//String[] p = {pid};
		String[][] allergyResult = null;
		try {
			allergyResult = Client.getInstance().sendQuery("SELECT name, description FROM allergy, `dia-allergy_rec` WHERE " +
					"allergy_id=`allergy`.id AND valid=1 AND pat_id=?;", param);
		} catch (RemoteException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (NotBoundException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (SQLException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		if ( ! (allergyResult.length == 0)){
			insertText(br+star+" Allergies "+star+br+br,BOLD_BLACK);
			for ( int i = 0; i < allergyResult.length; i++){
				insertText("Allergy: ",ITALIC_GRAY);
				insertText(allergyResult[i][0]+br,BLACK);//allergy name
				insertText("Descriptions: ",ITALIC_GRAY);
				insertText(allergyResult[i][1]+br,BLACK);//allergy descriptions
			}
		}
		
		
		//String[] treatmentTable = {"treatment"};
		//String[] treatmentFields = {"pic","date_of_issue","description"};
		String[] p2 = {pid};
		String[][] treatmentResult = null;
		try {
			treatmentResult = Client.getInstance().sendQuery("SELECT pic, date_of_issue, description FROM treatment WHERE pid=?;", p2);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		if ( ! (treatmentResult.length == 0)){
			insertText(br+star+" Treatments "+star+br+br,BOLD_BLACK);
			for ( int i = 0;  i < treatmentResult.length; i++){
				insertText("PIC: ",ITALIC_GRAY);
				insertText(treatmentResult[i][0]+br,BLACK);
				insertText("Date of Issue: ",ITALIC_GRAY);
				insertText(treatmentResult[i][1]+br,BLACK);
				insertText("Description: ",ITALIC_GRAY);
				insertText(treatmentResult[i][2]+br,BLACK);
			}
			
		}
		
		insertText(br+star+star+br,BLACK);
	}
	public void checkPrivilege(){
		//write
		if ( Client.getInstance().isWrite()){
			editBtn.setEnabled(true);
			allgeryBtn.setEnabled(true);
		}
		//add
		if (Client.getInstance().isAdd()){
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
        this.add(getJScrollPane(), BorderLayout.CENTER);
        getInfo().setText("");
        this.add(getBtnPanel(),BorderLayout.SOUTH);
        
	}
	/**
	 * @return
	 * @uml.property  name="btnPanel"
	 */
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
		ImageIcon icon = createImageIcon("icons/edit.png",
        "a pretty but meaningless splat");
		editBtn = new JButton("Personal Info",icon);
		editBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
	    editBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		editBtn.setPreferredSize(new Dimension(59, 80));
		editBtn.setActionCommand("edit");
		editBtn.addActionListener(bc);
		editBtn.setEnabled(false);
		return editBtn;
	}
	private JButton getAllgeryButton(){
		ImageIcon icon = createImageIcon("icons/edit.png",
        "a pretty but meaningless splat");
		allgeryBtn = new JButton("Edit allergy",icon);
		allgeryBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
		allgeryBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		allgeryBtn.setPreferredSize(new Dimension(59, 80));
		allgeryBtn.setActionCommand("allergy");
		allgeryBtn.addActionListener(bc);
		allgeryBtn.setEnabled(false);
		return allgeryBtn;
	}
	private JButton getTreatmentButton(){
		ImageIcon icon = createImageIcon("icons/edit.png",
        "a pretty but meaningless splat");
		treatmentBtn = new JButton("Add Treatment",icon);
		treatmentBtn.setActionCommand("treatment");
		treatmentBtn.addActionListener(bc);
		treatmentBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
		treatmentBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		treatmentBtn.setPreferredSize(new Dimension(59, 80));

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
	 * @return   javax.swing.JScrollPane
	 * @uml.property  name="jScrollPane"
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane(getInfo());
		}
		return jScrollPane;
	}


	public void setPID(String s){
		this.pid = s;
	}

	/**
	 * This method initializes info	
	 * @return   javax.swing.JTextPane
	 * @uml.property  name="info"
	 */
	private JTextPane getInfo() {
		if (info == null) {
			info = new JTextPane();
			insertText("***********************************\n" 
					+ "Click to see info"
					+ "\n***********************************\n",BLACK);
		}
		return info;
	}
	/**
	 * @author   Chun
	 */
	class ButtonAction implements ActionListener  {
		/**
		 * @uml.property  name="info"
		 * @uml.associationEnd  
		 */
		private ShowInfoPanel info = null;
		public ButtonAction(ShowInfoPanel f){
			super();
			info = f;
		}
		@SuppressWarnings("static-access")
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if ( info.getInfo().getText().equals("")){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null, "Record not selected");
			}	
			else{
				if ( e.getActionCommand().equals("edit")){
					@SuppressWarnings("unused")
					EditInfoDialog eif = new EditInfoDialog(result,pid,panelToRefresh);
				}
				else if ( e.getActionCommand().equals("treatment")){
					@SuppressWarnings("unused")
					AddTreatmentDialog atd = new AddTreatmentDialog(pid);
				}
				else{
					@SuppressWarnings("unused")
					EditAllergyDialog ead = new EditAllergyDialog(pid,panelToRefresh);
				}
			}
		}
	};

}  //  @jve:decl-index=0:visual-constraint="10,10"
