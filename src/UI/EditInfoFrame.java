package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;

/**
 * @author cctang
 *
 */

public class EditInfoFrame extends JFrame {
	private int id = -1;
	private JLabel idLab = null;
	private JTextField idFld = null;
	private JLabel nameLab = null;
	private JTextField nameFld = null;
	private JLabel genderLab = null;
	private JComboBox genderBox = null;
	private JLabel addrLab = null;
	private JTextField addrFld = null;
	private JLabel contactLab = null;
	private JTextField contactFld = null;
	private JLabel dobLab = null;
	private JTextField dobFld = null;
	private JLabel picLab = null;
	private JTextField picFld = null;
	private JLabel lastLab = null;
	private JPanel lastPanel = null;
	private JTextField lastFld = null;
	private JButton lastBtn = null;
	private JLabel reLab = null;
	private JTextPane rePane= null; 
	private JPanel fieldPanel = null;
	private JPanel rePanel = null;
	private JPanel upPanel = null;
	private JPanel btnPanel = null;
	private JButton doneBtn = null;
	private ShowInfoPanel parentPanel = null;
	private JButton cancelBtn = null;

	/**
	 * 
	 */
	public EditInfoFrame() {
		super();
		initialize();
	}
	/**
	 *  
	 * @param x 
	 */
	public EditInfoFrame(int x){
		super();
		id = x;
		initialize();
	}
	/**
	 * @param jp
	 * 
	 * @param x 
	 */
	public EditInfoFrame(ShowInfoPanel jp,int x){
		super();
		id = x;
		parentPanel = jp;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new Dimension(300, 500));
		this.setTitle(id + "'s Personal Information");
		this.setVisible(true);
		JPanel main = (JPanel) this.getContentPane();
		BorderLayout bl = new BorderLayout();
		bl.setVgap(20);
		main.setLayout(bl);
		//add
        main.add(getUpPanel(),BorderLayout.CENTER);
        main.add(getBtnPanel(),BorderLayout.SOUTH);
	}
	/**
	 * @return 
	 * 
	 */
	private JPanel getRePanel(){
		if (rePanel == null){
			rePanel = new JPanel();
			rePanel.setLayout(new GridLayout(1,2));
			rePanel.setPreferredSize(new Dimension(600,100));
			JLabel temp2 = new JLabel("Remark");
			rePanel.add(temp2);
			temp2.setHorizontalAlignment(SwingConstants.CENTER);
			JTextArea temp = new JTextArea();
			temp.setLineWrap(true);
			JScrollPane temp3 = new JScrollPane(temp);
			rePanel.add(temp3);
		}
		return rePanel;
	}
	/**
	 * @return
	 * 
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
	 */
	private JButton getDoneBtn(){
		if (doneBtn == null){
			doneBtn = new JButton("Done");
			doneBtn.addActionListener(new DoneAction(this));
			//neBtn.setLayout(new GridLayout(1,2));	
		}
		return doneBtn;
	}
	private JButton getCancelBtn(){
		if (cancelBtn == null){
			cancelBtn = new JButton("Cancel");
			cancelBtn.addActionListener(new CancelAction(this));
			//neBtn.setLayout(new GridLayout(1,2));	
		}
		return cancelBtn;
	}
	private JButton getLastBtn(){
		if ( lastBtn == null){
			lastBtn = new JButton("TODAY");
			lastBtn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
			        Calendar calendar = Calendar.getInstance();
			        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					lastFld.setText(dateFormat.format(calendar.getTime())); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return lastBtn;
	}
	private JPanel getBtnPanel(){
		if (btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.setLayout(new GridLayout(1,2));
			btnPanel.add(getDoneBtn());
			btnPanel.add(getCancelBtn());
		}
		return btnPanel;
	}
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
	 * This method initializes idFld	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JPanel getFieldPanel(){
		if (fieldPanel == null){
			fieldPanel = new JPanel();
			fieldPanel.setLayout(new GridLayout(9,2));
			nameLab = new JLabel();
	        nameLab.setText("Name:");
	        nameLab.setHorizontalAlignment(SwingConstants.CENTER);
	        idLab = new JLabel();
	        idLab.setText("Patient ID:");
	        idLab.setHorizontalAlignment(SwingConstants.CENTER);
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
	        
	        fieldPanel.add(idLab, idLab.getName());
	        fieldPanel.add(getIdFld(), getIdFld().getName());
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
	        fieldPanel.add(lastLab,lastLab.getName());
	        fieldPanel.add(getLastPanel(),getLastPanel().getName());
	       // fieldPanel.add(reLab,reLab.getClass());
	        //fieldPanel.add(getRePane(),getRePane().getName());
		}
		return fieldPanel;
	}
	private JTextField getIdFld() {
		if (idFld == null) {
			idFld = new JTextField();
			idFld.setEnabled(false);
			//idFld.setPreferredSize(new Dimension(5, 5));
		}
		return idFld;
	}
	private JTextField getContactFld() {
		if (contactFld == null) {
			contactFld = new JTextField();
		}
		return contactFld;
	}
	private JTextField getLastFld(){
		if ( lastFld == null){
			lastFld = new JTextField();
		}
		return lastFld;
	}
	private JTextField getPicFld() {
		if (picFld == null) {
			picFld = new JTextField();
		}
		return picFld;
	}
	private JTextField getDobFld() {
		if (dobFld == null) {
			dobFld = new JTextField();
		}
		return dobFld;
	}
	private JTextField getAddrFld() {
		if (addrFld == null) {
			addrFld = new JTextField();
			//idFld.setPreferredSize(new Dimension(30, 20));
		}
		return addrFld;
	}

	private JTextPane getRePane(){
		if ( rePane == null){
			rePane = new JTextPane();
		}
		return rePane;
	}




	/**
	 * This method initializes nameFld	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNameFld() {
		if (nameFld == null) {
			nameFld = new JTextField();
		}
		return nameFld;
	}
	/**
	 * This method initializes genderBox	
	 * 	
	 * @return javax.swing.JComboBox	
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
	class CancelAction implements ActionListener  {
		private JFrame frame = null;
		public CancelAction(JFrame f){
			super();
			frame = f;
		}
		public void actionPerformed(java.awt.event.ActionEvent e) {
			 frame.dispose();// TODO Auto-generated Event stub actionPerformed()
		}
	};
	class DoneAction implements ActionListener  {
		private JFrame frame = null;
		public DoneAction(JFrame f){
			super();
			frame = f;
		}
		public void actionPerformed(java.awt.event.ActionEvent e) {
			int ans = JOptionPane.showConfirmDialog(null, "Sure?");
			if ( ans == 0){
				parentPanel.hasEditted();
				frame.dispose();
			}
			// TODO Auto-generated Event stub actionPerformed()
		}
	};


}  //  @jve:decl-index=0:visual-constraint="10,10"
