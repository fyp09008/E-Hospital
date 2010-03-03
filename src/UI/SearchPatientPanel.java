package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import control.Client;

public class SearchPatientPanel extends Panels {
	
	public static String[] column = {"ID","Name"};
	private JScrollPane scrollPane = null;
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
	private ShowInfoPanel showInfo = null; 
	private ListSelectionModel listSelectionModel = null;  //  @jve:decl-index=0:
	//private MainFrame mf = null;
	
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
	public JTextField getSearchName() {
		if ( searchName == null){
			searchName = new JTextField("Wildcard search");
			searchName.setEnabled(false);
		}
		return searchName;
	}
	public JTextField getSearchID() {
		if ( searchID == null){
			searchID = new JTextField("Exact search");
			searchID.setEnabled(true);
		}
		return searchID;
	}
	
	public JRadioButton getByName() {
		if ( byName == null){
			byName = new JRadioButton("Name");
			byName.setActionCommand("name");
		}
		return byName;
	}
	public JRadioButton getByID() {
		if ( byID == null){
			byID = new JRadioButton("ID");
			byID.setActionCommand("id");
			byID.setSelected(true);
		}
		return byID;
	}
	public JPanel getFieldPanel() {
		if ( fieldPanel == null){
			fieldPanel = new JPanel();
			fieldPanel.setLayout(new GridLayout(3,2));
			RadioHandler radio = new RadioHandler();
			fieldPanel.add(getByID());
			fieldPanel.add(getSearchID());
			fieldPanel.add(getByName());
			fieldPanel.add(getSearchName());

			getByName().addActionListener(radio);
			getByID().addActionListener(radio);
			
		}
		return fieldPanel;
	}
	public JButton getSearchBtn() {
		if ( searchBtn == null){
			searchBtn = new JButton("Search");
			searchBtn.addActionListener(new ButtonAction());
			searchBtn.setActionCommand("ID");
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

	
	
	public void setScrollPane() {
		//System.out.println("set sroll pane");
		scrollPane = new JScrollPane(table);
		scrollPane.setViewportView(table);
		//System.out.println("last set scroll pane");
	}
	public void setTable(String[][] result) {
		//System.out.println("In set table");
		table = new JTable(result,column);
		listSelectionModel = table.getSelectionModel();
		listSelectionModel.addListSelectionListener(new SharedListSelectionHandler(table));
	    table.setSelectionModel(listSelectionModel);
	   // System.out.println("last set table");
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

	public void showTable(){
		//System.out.println("in show table");
		leftPanel.remove(0);
		leftPanel.add(scrollPane,BorderLayout.CENTER);
		leftPanel.invalidate();
		leftPanel.validate();
	}
	public void showFail(){
		JLabel msg = new JLabel("No Match",JLabel.CENTER);
		msg.setFont(new Font("Serif",Font.BOLD, 20));
		msg.setForeground(Color.red);
		leftPanel.add(msg,BorderLayout.CENTER);
		leftPanel.invalidate();
		leftPanel.validate();
	}
	class RadioHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if ( arg0.getActionCommand().equals("name")){
				getSearchBtn().setActionCommand("name");
				getSearchName().setEnabled(true);
				getSearchID().setEnabled(false);
				getByID().setSelected(false);
			}
			else{
				getSearchBtn().setActionCommand("ID");
				getSearchID().setEnabled(true);
				getSearchName().setEnabled(false);
				getByName().setSelected(false);
			}	
		}
		
	}
	class ButtonAction implements ActionListener{
		//SearchPatientPanel spp = null;
		//public ButtonAction(SearchPatientPanel spp){
			//this.spp = spp;
		//}
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println("gihi");
			String[] tables = {"Patient_personal"};
			String[] fields = {"pid","name"};
			//this.getInfo().append(this.pid);
			String [][]result;
			if ( arg0.getActionCommand().equals("ID")){
				result = Client.getInstance().sendQuery("SELECT", tables, 
						fields, "pid = '" + getSearchID().getText() + "'", null);	
			}
			else{
				result = Client.getInstance().sendQuery("SELECT", tables, 
						fields, "name LIKE '%" + getSearchName().getText() + "%'", null);
			}
			if ( result.length != 0 && result!= null){
				//System.out.println("not null: ");
				setTable(result);
				setScrollPane();
				showTable();
			}else{
				//System.out.println("null");
				showFail();
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
