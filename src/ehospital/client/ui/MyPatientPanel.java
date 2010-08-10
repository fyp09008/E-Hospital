
package ehospital.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ehospital.client.control.Client;
/**
 * Panel class to display the patient
 * @author   Chun
 */
public class MyPatientPanel extends Panels {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="jScrollPane"
	 */
	private JScrollPane[] jScrollPane = null;
	/**
	 * @uml.property  name="tabPanel"
	 */
	private JTabbedPane tabPanel = null;
	/**
	 * @uml.property  name="upPanel"
	 */
	private JPanel upPanel = null;
	/**
	 * @uml.property  name="showPanel"
	 */
	private JPanel showPanel = null;
	private JTable[] tables = null;
	private static int TABLENUM = 7;
	//private JLabel tempLab = null;
	/**
	 * @uml.property  name="showInfo"
	 * @uml.associationEnd  
	 */
	public ShowInfoPanel showInfo = null; 
	ListSelectionModel[] listSelectionModel;  //  @jve:decl-index=0:
	
	public MyPatientPanel(){
		super();
		initTables();
		initialize();	
	}
	public void refresh(String pid){
		showInfo.setPID(pid);
		showInfo.fetchInfo();
	}
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(1024, 680);		
        this.add(getUpPanel(), null);
	}

	/**
	 * This method initializes aPane	
	 * @return   javax.swing.JTabbedPane
	 * @uml.property  name="tabPanel"
	 */
	private JTabbedPane getTabPanel() {
		if (tabPanel == null) {
			tabPanel = new JTabbedPane();
			tabPanel.setPreferredSize(new Dimension(3, 400));
			tabPanel.setFont(new Font("Dialog", Font.BOLD, 22));
			jScrollPane = getJScrollPane();
			tabPanel.addTab("A-D", jScrollPane[0]);
			tabPanel.addTab("E-H", jScrollPane[1]);
			tabPanel.addTab("I-L", jScrollPane[2]);
			tabPanel.addTab("M-P", jScrollPane[3]);
			tabPanel.addTab("Q-T", jScrollPane[4]);
			tabPanel.addTab("U-X", jScrollPane[5]);
			tabPanel.addTab("Y-etc", jScrollPane[6]);
		}
		return tabPanel;
	}
	
	/**
	 * @return
	 * @uml.property  name="jScrollPane"
	 */
	private JScrollPane[] getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane[TABLENUM];
			
			for ( int i = 0 ;i < TABLENUM; i++){
				jScrollPane[i] = new JScrollPane();
				jScrollPane[i].setViewportView(tables[i]);
			}
		}
		return jScrollPane;
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
			upPanel.add(getTabPanel(),BorderLayout.CENTER);
		}
		return upPanel;
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
	 * @return
	 * @uml.property  name="showPanel"
	 */
	private JPanel getShowPanel() {
		if (showPanel == null) {
			showPanel = new JPanel();
			showPanel.setPreferredSize(new Dimension(750, 10));
			showPanel.setLayout(new BorderLayout());
			showInfo = new ShowInfoPanel(MainFrame.MY_PATIENT_LIST);
			showPanel.add(showInfo,BorderLayout.CENTER);
		}
		return showPanel;
	}

	private void initTables(){
		/*
		 * 0 = A-D
		 * 1 = E-H
		 * 2 = I-L
		 * 3 = M-P
		 * 4 = Q-T
		 * 5 = U-X
		 * 6 = Y-etc
		 */
		tables = new JTable[TABLENUM];

		//end of making where clauses
		String[][] param = new String[TABLENUM][2];
		param[0][1] = "^[abcd]";
		param[1][1] = "^[efgh]";
		param[2][1] = "^[ijkl]";
		param[3][1] = "^[mnop]";
		param[4][1] = "^[qrst]";
		param[5][1] = "^[uvwx]";
		param[6][1] = "^[^abcdefghijklmnopqrstuvwx]";
	
		String[] column = {"ID","Name"};
		listSelectionModel = new ListSelectionModel[TABLENUM];
		for ( int i = 0; i < TABLENUM; i++){
			param[i][0] = Client.getInstance().getID();
			String temp[][] = null;
			try {
				temp = Client.getInstance().sendQuery("SELECT patient_personal.pid, name from patient_personal, treatment" +
						" where patient_personal.pic = ?  AND name RLIKE ? GROUP BY name",param[i]);
			} catch (RemoteException e) {
				Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			} catch (NotBoundException e) {
				Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			} catch (SQLException e) {
				Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
			}
			if ( temp != null){
				tables[i] = new JTable(temp,column);
				tables[i].setRowSelectionAllowed(true);
				listSelectionModel[i] = tables[i].getSelectionModel();
				listSelectionModel[i].addListSelectionListener(new SharedListSelectionHandler(tables[i]));
				tables[i].setSelectionModel(listSelectionModel[i]);
			}
		}

		
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
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
                    if (lsm.isSelectedIndex(i)) {
                    	showInfo.setPID((String)table.getValueAt(i, 0));
                    	showInfo.fetchInfo();
                    }
                }
	        }
	    }


}  //  @jve:decl-index=0:visual-constraint="10,10"
