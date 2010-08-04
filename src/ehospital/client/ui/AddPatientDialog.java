
package ehospital.client.ui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ehospital.client.control.Client;


/**
 * Dialog class for adding new patient.
 * @author   Chun
 */

public class AddPatientDialog extends Dialogs {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JComboBox yearCombo;
	private JComboBox monthCombo;
	private JComboBox dayCombo;
	
	private JLabel nameLab = null;
	/**
	 * @uml.property  name="nameFld"
	 */
	private JTextField nameFld = null;
	private JLabel genderLab = null;
	/**
	 * @uml.property  name="genderBox"
	 */
	private JComboBox genderBox = null;
	private JLabel addrLab = null;
	/**
	 * @uml.property  name="addrFld"
	 */
	private JTextField addrFld = null;
	private JLabel contactLab = null;
	/**
	 * @uml.property  name="contactFld"
	 */
	private JTextField contactFld = null;
	private JLabel dobLab = null;
	/**
	 * @uml.property  name="dobFld"
	 */
	private JPanel dobFld = null;
	private JLabel picLab = null;
	/**
	 * @uml.property  name="picFld"
	 */
	private JComboBox picFld = null;
	private JLabel lastLab = null;
	/**
	 * @uml.property  name="lastPanel"
	 */
	private JPanel lastPanel = null;
	/**
	 * @uml.property  name="lastFld"
	 */
	private JTextField lastFld = null;
	/**
	 * @uml.property  name="lastBtn"
	 */
	private JButton lastBtn = null;
	private JLabel reLab = null;
	/**
	 * @uml.property  name="fieldPanel"
	 */
	private JPanel fieldPanel = null;
	/**
	 * @uml.property  name="rePanel"
	 */
	private JPanel rePanel = null;
	/**
	 * @uml.property  name="upPanel"
	 */
	private JPanel upPanel = null;
	/**
	 * @uml.property  name="btnPanel"
	 */
	private JPanel btnPanel = null;
	/**
	 * @uml.property  name="doneBtn"
	 */
	private JButton doneBtn = null;
	
	/**
	 * @uml.property  name="cancelBtn"
	 */
	private JButton cancelBtn = null;
	private JLabel remarkLab = null;
	/**
	 * @uml.property  name="remark"
	 */
	private JTextArea remark  = null;
	private JScrollPane scroll = null;

	/**
	 * 
	 */
	public AddPatientDialog() {
		super();
		initialize();
	}

	/**
	 * @param jp
	 * 
	 * @param x 
	 */


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {

		this.setSize(new Dimension(320, 500));
		this.setTitle("Add new patient");

		this.setLayout(new BorderLayout());
		//add
      	this.add(getUpPanel(),BorderLayout.CENTER);
      	this.add(getBtnPanel(),BorderLayout.SOUTH);
      	this.setVisible(true);
      	
        
	}
	/**
	 * @return
	 * @uml.property  name="btnPanel"
	 */
	private JPanel getBtnPanel(){
		if (btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.setLayout(new GridLayout(1,2));
			btnPanel.add(getDoneBtn());
			btnPanel.add(getCancelBtn());
			btnPanel.setVisible(true);
		}
		return btnPanel;
	}
	/**
	 * @return
	 * @uml.property  name="rePanel"
	 */
	private JPanel getRePanel(){
		if (rePanel == null){
			rePanel = new JPanel();
			GridLayout gl = new GridLayout(1,2);
			rePanel.setLayout(gl);
			rePanel.setPreferredSize(new Dimension(600,200));
			gl.setHgap(0);
			gl.setVgap(0);
			remarkLab = new JLabel("Remark");
			rePanel.add(remarkLab);
			remarkLab.setHorizontalAlignment(SwingConstants.CENTER);

			scroll = new JScrollPane(getRemark());
			rePanel.add(scroll);
			rePanel.setVisible(true);
		}
		return rePanel;
	}
	/**
	 * @return
	 * @uml.property  name="remark"
	 */
	private JTextArea getRemark(){
		if ( remark == null){
			remark = new JTextArea();
			remark.setEnabled(true);
			remark.setLineWrap(true);
		}
		return remark;
		
	}

	/**
	 * @return
	 * @uml.property  name="upPanel"
	 */
	private JPanel getUpPanel(){
		if (upPanel == null){
			upPanel = new JPanel();
			upPanel.setLayout(new BorderLayout());
	        upPanel.add(getFieldPanel(),BorderLayout.CENTER);
	        upPanel.add(getRePanel(),BorderLayout.SOUTH);	
		}
		return upPanel;
	}
	/**
	 * @return
	 * @uml.property  name="doneBtn"
	 */
	private JButton getDoneBtn(){
		if (doneBtn == null){
			doneBtn = new JButton("Done");
			doneBtn.addActionListener(new DoneAction(this));	
		}
		return doneBtn;
	}
	/**
	 * @return
	 * @uml.property  name="cancelBtn"
	 */
	private JButton getCancelBtn(){
		if (cancelBtn == null){
			cancelBtn = new JButton("Cancel");
			cancelBtn.addActionListener(new CancelAction(this));
		}
		return cancelBtn;
	}
	/**
	 * @return
	 * @uml.property  name="lastBtn"
	 */
	private JButton getLastBtn(){
		if ( lastBtn == null){
			lastBtn = new JButton("TODAY");
			lastBtn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
			        Calendar calendar = Calendar.getInstance();
			        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					lastFld.setText(dateFormat.format(calendar.getTime())); 
				}
			});
		}
		return lastBtn;
	}

	/**
	 * @return
	 * @uml.property  name="lastPanel"
	 */
	@SuppressWarnings("unused")
	private JPanel getLastPanel() {
		if (lastPanel == null) {
			lastPanel = new JPanel();
			lastPanel.setLayout(new BorderLayout());
			lastPanel.add(getLastFld(),BorderLayout.CENTER);
			lastPanel.add(getLastBtn(),BorderLayout.EAST);
		}
		return lastPanel;
	}
	/**
	 * @return
	 * @uml.property  name="dobFld"
	 */
	private JPanel getDobFld() {
		if (dobFld == null) {
			dobFld = new JPanel();
			dobFld.setLayout(new GridLayout(1,3));
			Vector<String> years = new Vector<String>();
			Vector<String> months = new Vector<String>();
			Vector<String> days = new Vector<String>();
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			int j = calendar.get(java.util.Calendar.YEAR);
			for(int i = 1900; i < j; i++)
				years.add(Integer.toString(i));
			for(int i = 1; i < 13; i++)
				months.add(Integer.toString(i));
			for(int i = 1; i < 32; i++)
				days.add(Integer.toString(i));
			yearCombo = new JComboBox(years);
			monthCombo = new JComboBox(months);
			dayCombo = new JComboBox(days);
			dobFld.add(yearCombo);
			dobFld.add(monthCombo);
			dobFld.add(dayCombo);
		}
		return dobFld;
	}

	/**
	 * This method initializes idFld	
	 * @return   javax.swing.JTextField
	 * @uml.property  name="fieldPanel"
	 */
	private JPanel getFieldPanel(){
		if (fieldPanel == null){
			fieldPanel = new JPanel();
			fieldPanel.setLayout(new GridLayout(6,2));
			nameLab = new JLabel();
	        nameLab.setText("Name:");
	        nameLab.setHorizontalAlignment(SwingConstants.CENTER);

	        genderLab = new JLabel();
	        genderLab.setText("Gender:");
	        genderLab.setHorizontalAlignment(SwingConstants.CENTER);
	        addrLab = new JLabel();
	        addrLab.setText("Address:");
	        addrLab.setHorizontalAlignment(SwingConstants.CENTER);
	        contactLab = new JLabel();
	        contactLab.setText("Contact No.:");
	        contactLab.setHorizontalAlignment(SwingConstants.CENTER);
	        dobLab = new JLabel();
	        dobLab.setText("Birthday:");
	        dobLab.setHorizontalAlignment(SwingConstants.CENTER);
	        picLab = new JLabel();
	        picLab.setText("PIC:");
	        picLab.setHorizontalAlignment(SwingConstants.CENTER);
	        lastLab = new JLabel();
	        lastLab.setText("Last Dignosis:");
	        lastLab.setHorizontalAlignment(SwingConstants.CENTER);
	        reLab = new JLabel();
	        reLab.setText("Remarks:");
	        lastLab.setHorizontalAlignment(SwingConstants.CENTER);
	        
	   
	        fieldPanel.add(nameLab, nameLab.getName());
	        fieldPanel.add(getNameFld(), getNameFld().getName());
	        fieldPanel.add(genderLab, genderLab.getName());
	        fieldPanel.add(getGenderBox(),getGenderBox().getName());
	        fieldPanel.add(addrLab,addrLab.getName());
	        fieldPanel.add(getAddrFld(),getAddrFld().getName());
	        fieldPanel.add(contactLab,contactLab.getName());
	        fieldPanel.add(getContactFld(),getContactFld().getName());
	        fieldPanel.add(dobLab,dobLab.getName());
	        fieldPanel.add(getDobFld(),getDobFld().getName());
	        fieldPanel.add(picLab,picLab.getName());
	        fieldPanel.add(getPicFld(),getPicFld().getName());

		}
		return fieldPanel;
	}
	
	/**
	 * @return
	 * @uml.property  name="contactFld"
	 */
	private JTextField getContactFld() {
		if (contactFld == null) {
			contactFld = new JTextField();
		}
		return contactFld;
	}
	/**
	 * @return
	 * @uml.property  name="lastFld"
	 */
	private JTextField getLastFld(){
		if ( lastFld == null){
			lastFld = new JTextField();
		}
		return lastFld;
	}
	/**
	 * @return
	 * @uml.property  name="picFld"
	 */
	private JComboBox getPicFld() {
		if (picFld == null) {
			//old code
			//String table[] = {"user"};
			//String field[] = {"uid"};
			//String result[][] = Client.getInstance().sendQuery("SELECT", table, field, null, null);
			String[][] result = null;
			try {
				result = Client.getInstance().sendQuery("SELECT uid FROM user;", null);
			} catch (RemoteException e) {
				Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			} catch (NotBoundException e) {
				Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			} catch (SQLException e) {
				Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			}
			Vector<String> ids = new Vector<String>();
			int index = 0;
			for(int i =0;i< result.length; i++){
				ids.add(result[i][0]);
				if(result[i][0].equals(Client.getInstance().getID()))
					index=i;
			}
			picFld = new JComboBox(ids);
			picFld.setSelectedIndex(index);
		}
		return picFld;
	}

	/**
	 * @return
	 * @uml.property  name="addrFld"
	 */
	private JTextField getAddrFld() {
		if (addrFld == null) {
			addrFld = new JTextField();
		}
		return addrFld;
	}

	/**
	 * This method initializes nameFld	
	 * @return   javax.swing.JTextField
	 * @uml.property  name="nameFld"
	 */
	private JTextField getNameFld() {
		if (nameFld == null) {
			nameFld = new JTextField();
		}
		return nameFld;
	}
	public String getDob() {
		String month = (String)monthCombo.getSelectedItem();
		if ( month.length() == 1)
			month = "0"+month;
		String day = (String)dayCombo.getSelectedItem();
		if ( day.length() == 1)
			day = "0"+day;
		return (String)yearCombo.getSelectedItem()+ "-" + month + "-" + day;
	};
	/**
	 * This method initializes genderBox	
	 * @return   javax.swing.JComboBox
	 * @uml.property  name="genderBox"
	 */
	private JComboBox getGenderBox() {
		if (genderBox == null) {
			String[] gender = {"Male","Female"};
			genderBox = new JComboBox(gender);
			//to be edit
			genderBox.setSelectedIndex(0);
		}
		return genderBox;
	}
	@SuppressWarnings("static-access")
	private boolean checkInput(){
		if ( this.getNameFld().getText().equals("")){
			JOptionPane o = new JOptionPane();
			Client.getInstance().getMf().addPopUP(o);
			o.showMessageDialog(null, "Name cannot be null");
			return false;
		}	
		if ( this.getAddrFld().getText().equals("")){
			JOptionPane o = new JOptionPane();
			Client.getInstance().getMf().addPopUP(o);
			o.showMessageDialog(null, "Address cannot be null");
			return false;
		}
		if ( this.getContactFld().getText().equals("")){
			JOptionPane o = new JOptionPane();
			Client.getInstance().getMf().addPopUP(o);
			o.showMessageDialog(null, "Contact number cannot be null");
			return false;
		}
		if ( this.getContactFld().getText().length() != 8){
			JOptionPane o = new JOptionPane();
			Client.getInstance().getMf().addPopUP(o);
			o.showMessageDialog(null, "Contact number must be 8-digits");
			return false;
		}
		try{
			Integer.parseInt(this.getContactFld().getText());
		}catch (NumberFormatException e){
			JOptionPane o = new JOptionPane();
			Client.getInstance().getMf().addPopUP(o);
			o.showMessageDialog(null, "Contact number must be numeric");
			return false;
		}
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		int year = calendar.get(java.util.Calendar.YEAR);
		int month = calendar.get(java.util.Calendar.MONTH)+1;
		int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
		if ( yearCombo.getSelectedItem().equals(Integer.toString(year))){
			if ( Integer.parseInt((String)monthCombo.getSelectedItem()) > month){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null, "Wrong date");
				return false;
			}
			else if ( Integer.parseInt((String)monthCombo.getSelectedItem()) == month
					&& Integer.parseInt((String)dayCombo.getSelectedItem()) > day ){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null, "Wrong date");
				return false;	
			}
		}
		 
		if (	Integer.parseInt((String)monthCombo.getSelectedItem()) == 4 ||
				Integer.parseInt((String)monthCombo.getSelectedItem()) == 6 ||
				Integer.parseInt((String)monthCombo.getSelectedItem()) == 9 ||
				Integer.parseInt((String)monthCombo.getSelectedItem()) == 11){
			if (Integer.parseInt((String)dayCombo.getSelectedItem()) > 30 ){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null, "Wrong date");
				return false;	
			}
		}
		else if ( Integer.parseInt((String)monthCombo.getSelectedItem()) == 2){
			if (Integer.parseInt((String)dayCombo.getSelectedItem()) > 28 ){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null, "Wrong date");
				return false;	
			}
		}
		return true;
	}
	/**
	 * @author   Gilbert
	 */
	class CancelAction implements ActionListener  {
		/**
		 * @uml.property  name="frame"
		 * @uml.associationEnd  
		 */
		private AddPatientDialog frame = null;
		public CancelAction(AddPatientDialog f){
			super();
			frame = f;
		}
		public void actionPerformed(java.awt.event.ActionEvent e) {
			 Client.getInstance().getMf().popup = new ArrayList<Component>();
			 frame.dispose();
		}
	};
	/**
	 * @author   Gilbert
	 */
	class DoneAction implements ActionListener  {
		/**
		 * @uml.property  name="frame"
		 * @uml.associationEnd  
		 */
		private AddPatientDialog frame = null;
		public DoneAction(AddPatientDialog f){
			super();
			frame = f;
		}
		@SuppressWarnings("static-access")
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (!checkInput()){
				return;
			}
			String result[][] = new String[1][8];
			JOptionPane o = new JOptionPane();
			Client.getInstance().getMf().addPopUP(o);
			int ans = o.showConfirmDialog(null, "Sure?");
			
			if ( ans == 0){
				result[0][0] = "DEFAULT";
				result[0][1] = frame.getNameFld().getText();
				
				if ( frame.getGenderBox().getSelectedIndex() == 0)
					result[0][2] = "M";
				else 
					result[0][2] = "F";
				
				result[0][3] = frame.getAddrFld().getText();
				result[0][4] = frame.getContactFld().getText();
				
				result[0][5] = frame.getDob();
				result[0][6] = frame.getPicFld().getSelectedItem().toString();
				result[0][7] = frame.getRemark().getText();
				String[] param = {result[0][1], result[0][2], result[0][3], result[0][4], result[0][5], result[0][6], result[0][7]};
				//String[] tables = {"Patient_personal"};
				//String[] fields = {"pid","name","gender","address","contact_no","birthday",
				//	"pic","description"};
				
				boolean result2 = false;
				try {
					result2 = Client.getInstance().sendUpdate("INSERT INTO Patient_personal (name, gender, address, contact_no, birthday, pic, description) VALUES (?, ?, ?, ?, ?, ?, ?);", param);
				} catch (RemoteException e1) {
					Client.getInstance().getLogger().debug(this.getClass().getName(), e1.getMessage());
				} catch (NotBoundException e1) {
					Client.getInstance().getLogger().debug(this.getClass().getName(), e1.getMessage());
				}
				if ( result2){
					JOptionPane m = new JOptionPane();
					Client.getInstance().getMf().addPopUP(o);
					m.showMessageDialog(null, "New patient added!");
					//refresh to specific panel
					//id??!?!
					Client.getInstance().getMf().changePanel(Client.getInstance().getMf().VIEW_ALL);
					Client.getInstance().getMf().popup = new ArrayList<Component>();
					frame.dispose();
				}else {
					JOptionPane m = new JOptionPane();
					Client.getInstance().getMf().addPopUP(o);
					Client.getInstance().getMf().popup = new ArrayList<Component>();
					m.showMessageDialog(null, "New patient cannot be added");
				}
				
			}
		}
	};
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"

