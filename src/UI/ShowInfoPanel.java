package UI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

public class ShowInfoPanel extends Panels {

	private JScrollPane jScrollPane = null;
	private JTextPane info = null;
	private String pid = null;
	private JPanel btnPanel = null;
	private JButton editBtn = null;
	private JButton allgeryBtn = null;
	private JButton treatmentBtn = null;
	private MainFrame mf = null;
	/**
	 * This method initializes 
	 * 
	 */
	public ShowInfoPanel(MainFrame mf) {
		super();
		this.mf = mf;
		initialize();
		this.checkPrivilege();
	}
	public void getInfo(){
		
	}
	public void checkPrivilege(){
		//int[] pri = 
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
        getInfo().setText("Click to retrieve information");
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
		return allgeryBtn;
	}
	private JButton getTreatmentButton(){
		//ImageIcon icon = createImageIcon("edit-icon.png",
        //"a pretty but meaningless splat");
		treatmentBtn = new JButton("Add Teatment");
		treatmentBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
		treatmentBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		treatmentBtn.setPreferredSize(new Dimension(59, 80));
		treatmentBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				//new EditInfoFrame(); // TODO Auto-generated Event stub actionPerformed()
			}
		});
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
		getInfo().setText(s);
	}
	public void setInfo(String[] s, char de){
		for(int i =0; i < s.length; i++)
			getInfo().setText(s[i] + de);
	}
	public void setPID(String s){
		this.pid = s;
	}

	/**
	 * This method initializes info	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getInfo() {
		if (info == null) {
			info = new JTextPane();
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
			 new EditInfoFrame();
		}
	};

}  //  @jve:decl-index=0:visual-constraint="10,10"
