package UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import UI.MyPatientPanel.SharedListSelectionHandler;

public class SearchPatientPanel extends Panels {
	
	private JScrollPane jScrollPane = null;
	private JTextField searchName = null;
	private JTextField searchID = null;
	private JButton searchBtn = null;
	private JPanel upPanel = null;
	private JPanel showPanel = null;
	private JPanel leftPanel = null;
	private JPanel inputPanel = null;
	private JPanel fieldPanel = null;
	private JRadioButton byName = null;
	private JRadioButton byID = null;
	private JTable table = null;
	
	public JTextField getSearchName() {
		if ( searchName == null){
			searchName = new JTextField();
			searchName.setEnabled(true);
		}
		return searchName;
	}
	public JTextField getSearchID() {
		if ( searchID == null){
			searchID = new JTextField();
			searchID.setEnabled(false);
		}
		return searchID;
	}
	
	public JRadioButton getByName() {
		if ( byName == null){
			byName = new JRadioButton("Name");
			byName.setActionCommand("name");
			byName.setSelected(true);
		}
		return byName;
	}
	public JRadioButton getByID() {
		if ( byID == null){
			byID = new JRadioButton("ID");
			byID.setActionCommand("id");
		}
		return byID;
	}
	public JPanel getFieldPanel() {
		if ( fieldPanel == null){
			fieldPanel = new JPanel();
			fieldPanel.setLayout(new GridLayout(3,2));
			RadioHandler radio = new RadioHandler();
			fieldPanel.add(getByName());
			fieldPanel.add(getSearchName());
			fieldPanel.add(getByID());
			getByName().addActionListener(radio);
			getByID().addActionListener(radio);
			fieldPanel.add(getSearchID());
		}
		return fieldPanel;
	}
	public JButton getSearchBtn() {
		if ( searchBtn == null){
			searchBtn = new JButton("Search");
			//add button action
		}
		return searchBtn;
	}
	public JPanel getInputPanel() {
		if ( inputPanel == null){
			inputPanel = new JPanel();
			inputPanel.setLayout(new BorderLayout());
			inputPanel.add(getFieldPanel(),BorderLayout.CENTER);
			inputPanel.add(getSearchBtn(),BorderLayout.SOUTH);
			
		}
		return inputPanel;
	}	
	private ShowInfoPanel showInfo = null; 
	private ListSelectionModel listSelectionModel;  //  @jve:decl-index=0:
	private MainFrame mf = null;
	
	public SearchPatientPanel(){
		super();
		initialize();
	}
	
	private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(1024, 690);		
        this.add(getUpPanel(), null);
        this.setVisible(true);
	}
	
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane(getTable());
			jScrollPane.setViewportView(getTable());	
		}
		return jScrollPane;
	}
	public JTable getTable() {
		if ( table == null){
			String[] column = {"ID","Name"};
			//String[][] a = {{"hihi","ooo"},{"fdsfds","ppp"}};
			String[][] a = {{"",""}};
			table = new JTable(a,column);
			listSelectionModel = table.getSelectionModel();
			listSelectionModel.addListSelectionListener(new SharedListSelectionHandler(table));
	        table.setSelectionModel(listSelectionModel);
		}
		return table;
	}
	
	private JPanel getShowPanel() {
		if (showPanel == null) {
			showPanel = new JPanel();
			showPanel.setPreferredSize(new Dimension(750, 10));
			showPanel.setLayout(new BorderLayout());
			showInfo = new ShowInfoPanel();
			showPanel.add(showInfo,BorderLayout.CENTER);
		}
		return showPanel;
	}
	private JPanel getUpPanel() {
		//showInfo = new ShowInfo();
		if (upPanel == null) {
			upPanel = new JPanel();
			upPanel.setLayout(new BorderLayout());
			upPanel.add(getShowPanel(),BorderLayout.EAST);
			upPanel.add(getleftPane(),BorderLayout.CENTER);
	
		}
		return upPanel;
	}
	
	private JPanel getleftPane() {
		if ( leftPanel == null){
			leftPanel = new JPanel();
			leftPanel.setLayout(new BorderLayout());
			leftPanel.add(getInputPanel(),BorderLayout.NORTH);
			//leftPanel.add(getJScrollPane(),BorderLayout.CENTER);
		}
		return leftPanel;
	}

	public void setMf(MainFrame mf) {
		this.mf = mf;
	}
	class RadioHandler implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if ( arg0.getActionCommand().equals("name")){
				getSearchName().setEnabled(true);
				getSearchID().setEnabled(false);
				getByID().setSelected(false);
			}
			else{
				getSearchID().setEnabled(true);
				getSearchName().setEnabled(false);
				getByName().setSelected(false);
			}	
		}
		
	}
	class SharedListSelectionHandler implements ListSelectionListener {
		JTable table = null;
		public SharedListSelectionHandler(JTable t){
			table = t;			   
		}
	    public void valueChanged(ListSelectionEvent e) { 
	        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	           // tempLab.setText(lsm.)         
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
               	//patient selected
               if (lsm.isSelectedIndex(i)) {
            	   System.out.println((String)table.getValueAt(i,0));
                	   //for doing once  only , if not,might be doing twice
                   lsm.clearSelection();               	  
                    	showInfo.setPID((String)table.getValueAt(i, 0));
                    	showInfo.fetchInfo();
                }
            }
	    }
	}
	
}
