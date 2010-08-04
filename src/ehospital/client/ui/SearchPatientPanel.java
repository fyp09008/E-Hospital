
package ehospital.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ehospital.client.control.Client;

/**
 * Panel class to search patient
 * @author   Chun
 */
public class SearchPatientPanel extends Panels {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static String[] column = {"ID","Name"};
	private JScrollPane scrollPane = null;
	/**
	 * @uml.property  name="searchName"
	 */
	private JTextField searchName = null;
	/**
	 * @uml.property  name="searchID"
	 */
	private JTextField searchID = null;
	/**
	 * @uml.property  name="searchBtn"
	 */
	private JButton searchBtn = null;
	/**
	 * @uml.property  name="upPanel"
	 */
	private JPanel upPanel = null;
	/**
	 * @uml.property  name="showPanel"
	 */
	private JPanel showPanel = null;
	private JPanel leftPanel = null;
	/**
	 * @uml.property  name="inputPanel"
	 */
	private JPanel inputPanel = null;
	/**
	 * @uml.property  name="fieldPanel"
	 */
	private JPanel fieldPanel = null;
	/**
	 * @uml.property  name="byName"
	 */
	private JRadioButton byName = null;
	/**
	 * @uml.property  name="byID"
	 */
	private JRadioButton byID = null;
	/**
	 * @uml.property  name="table"
	 */
	private JTable table = null;
	/**
	 * @uml.property  name="showInfo"
	 * @uml.associationEnd  
	 */
	private ShowInfoPanel showInfo = null; 
	@SuppressWarnings("unused")
	private String mode;
	@SuppressWarnings("unused")
	private String param;
	private ListSelectionModel listSelectionModel = null;  //  @jve:decl-index=0:
	
	public SearchPatientPanel(){
		super();
		initialize();
	}
	/**
	 * @return
	 * @uml.property  name="table"
	 */
	public JTable getTable(){
		return table;
	}
	private void initialize() {
		mode = "ID";
		param = "";
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(1024, 690);		
        this.add(getUpPanel(), null);
        this.setVisible(true);
	}
	/**
	 * @return
	 * @uml.property  name="showInfo"
	 */
	public ShowInfoPanel getShowInfo(){
		return showInfo;
	}
	/**
	 * @return
	 * @uml.property  name="searchName"
	 */
	public JTextField getSearchName() {
		if ( searchName == null){
			searchName = new JTextField("Wildcard search");
			searchName.setEnabled(false);
		}
		return searchName;
	}
	/**
	 * @return
	 * @uml.property  name="searchID"
	 */
	public JTextField getSearchID() {
		if ( searchID == null){
			searchID = new JTextField("Exact search");
			searchID.setEnabled(true);
		}
		return searchID;
	}
	
	/**
	 * @return
	 * @uml.property  name="byName"
	 */
	public JRadioButton getByName() {
		if ( byName == null){
			byName = new JRadioButton("Name");
			byName.setActionCommand("NAME");
		}
		return byName;
	}
	/**
	 * @return
	 * @uml.property  name="byID"
	 */
	public JRadioButton getByID() {
		if ( byID == null){
			byID = new JRadioButton("ID");
			byID.setActionCommand("ID");
			byID.setSelected(true);
		}
		return byID;
	}
	/**
	 * @return
	 * @uml.property  name="fieldPanel"
	 */
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
	/**
	 * @return
	 * @uml.property  name="searchBtn"
	 */
	public JButton getSearchBtn() {
		if ( searchBtn == null){
			searchBtn = new JButton("Search");
			searchBtn.addActionListener(new ButtonAction());
			searchBtn.setActionCommand("ID");
			//add button action
		}
		return searchBtn;
	}
	/**
	 * @return
	 * @uml.property  name="inputPanel"
	 */
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
		scrollPane = new JScrollPane(table);
		scrollPane.setViewportView(table);
	}
	public void setTable(String[][] result) {
		table = new JTable(result,column);
		listSelectionModel = table.getSelectionModel();
		listSelectionModel.addListSelectionListener(new SharedListSelectionHandler(table));
	    table.setSelectionModel(listSelectionModel);
	}
	
	/**
	 * @return
	 * @uml.property  name="showPanel"
	 */
	private JPanel getShowPanel() {
		if (showPanel == null) {
			showPanel = new JPanel();
			showPanel.setPreferredSize(new Dimension(750, 10));
			showPanel.setLayout(new BorderLayout());
			showInfo = new ShowInfoPanel(MainFrame.SEARCH_PATIENTS);
			showPanel.add(showInfo,BorderLayout.CENTER);
		}
		return showPanel;
	}
	/**
	 * @return
	 * @uml.property  name="upPanel"
	 */
	private JPanel getUpPanel() {
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
		}
		return leftPanel;
	}
	public void refresh(String mode, String param){
		setTable(doSearch(mode,param));
		setScrollPane();
		showTable();
		getShowInfo().setPID((String)getTable().getValueAt(0, 0));
		getShowInfo().fetchInfo();
	}
	public String[][] doSearch(String mode, String param){

		try {
		//String[] tables = {"Patient_personal"};
		//String[] fields = {"pid","name"};
		String[] p = {param};
		if ( mode.equals("ID"))
			return Client.getInstance().sendQuery("SELECT pid, name FROM Patient_personal" +
					" WHERE pid=?", p);
		else
		{
			String[] p2 = {"%"+param+"%"};
			return Client.getInstance().sendQuery("SELECT pid, name FROM Patient_personal" +
					" WHERE name LIKE ?", p2);
		}
		} catch (RemoteException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (NotBoundException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (SQLException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		return null;
	}

	public void showTable(){
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
			if ( arg0.getActionCommand().equals("NAME")){
				getSearchBtn().setActionCommand("NAME");
				getSearchName().setEnabled(true);
				getSearchID().setEnabled(false);
				mode = "NAME";
				param = searchName.getText();
				getByID().setSelected(false);
				getByName().setSelected(true);
			}
			else{
				getSearchBtn().setActionCommand("ID");
				getSearchID().setEnabled(true);
				getSearchName().setEnabled(false);
				getByName().setSelected(false);
				getByID().setSelected(true);
				param = searchID.getText();
				mode = "ID";
			}	
		}
		
	}

	class ButtonAction implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			String [][]result;
			if ( arg0.getActionCommand().equals("ID")){
				result = doSearch("ID",getSearchID().getText());
			}
			else{
				result = doSearch("NAME",getSearchName().getText());
			}
	
			if ( result.length != 0 && result!= null){
				setTable(result);
				setScrollPane();
				showTable();
			}else{
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
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
               	//patient selected
               if (lsm.isSelectedIndex(i)) {
                	   //for doing once  only , if not,might be doing twice
                   lsm.clearSelection();               	  
                    	showInfo.setPID((String)table.getValueAt(i, 0));
                    	showInfo.fetchInfo();
                }
            }
	    }
	}
	
}
