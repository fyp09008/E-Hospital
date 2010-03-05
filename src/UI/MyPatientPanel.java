package UI;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



import java.awt.Font;
import java.sql.ResultSet;
import control.*;
public class MyPatientPanel extends Panels {
	
	private JScrollPane[] jScrollPane = null;
	private JTabbedPane tabPanel = null;
	//private JPanel goPanel = null;
	private JPanel upPanel = null;
	private JPanel showPanel = null;
	//private JButton editBtn = null;
	//private JPanel jPanel = null;
	private JTable[] tables = null;
	private static int TABLENUM = 7;
	private JLabel tempLab = null;
	private ShowInfoPanel showInfo = null; 
	ListSelectionModel[] listSelectionModel;  //  @jve:decl-index=0:
	//private MainFrame mf = null;
	
	public MyPatientPanel(){
		super();
		//this.mf = mf;
		initTables();
		initialize();	
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(1024, 690);		
        this.add(getUpPanel(), null);
        //System.out.println("Finsh");
        //this.add(getGoPanel(),null);
	}

	/**
	 * This method initializes aPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabPanel() {
		if (tabPanel == null) {
			tabPanel = new JTabbedPane();
			tabPanel.setPreferredSize(new Dimension(3, 400));
			tabPanel.setFont(new Font("Dialog", Font.BOLD, 22));
			//initTables();
			jScrollPane = getJScrollPane();
			tabPanel.addTab("A-D", jScrollPane[0]);
			tabPanel.addTab("E-H", jScrollPane[1]);
			tabPanel.addTab("I-L", jScrollPane[2]);
			tabPanel.addTab("M-P", jScrollPane[3]);
			tabPanel.addTab("Q-T", jScrollPane[4]);
			tabPanel.addTab("U-X", jScrollPane[5]);
			tabPanel.addTab("Y-etc", jScrollPane[6]);
			/*for(int i = 0; i < jScrollPane.length; i++)
			if ( jScrollPane[i] == null){
				System.out.println("I know the problem");
			}
			tabPanel.addTab("A-D", new JPanel());
			tabPanel.addTab("E-H", new JPanel());
			tabPanel.addTab("I-L", new JPanel());
			tabPanel.addTab("M-P", new JPanel());
			tabPanel.addTab("Q-T", new JPanel());
			tabPanel.addTab("U-X", new JPanel());
			tabPanel.addTab("Y-etc", new JPanel());*/
			
		}
		return tabPanel;
	}
	
	private JScrollPane[] getJScrollPane() {
		//System.out.println("In jscrollpane");
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane[TABLENUM];
			
			for ( int i = 0 ;i < TABLENUM; i++){
				jScrollPane[i] = new JScrollPane();
				jScrollPane[i].setViewportView(tables[i]);
				//System.out.println(i + "is done");
			}
		}
		return jScrollPane;
	}
	private JPanel getUpPanel() {
		//showInfo = new ShowInfo();
		if (upPanel == null) {
			upPanel = new JPanel();
			upPanel.setLayout(new BorderLayout());
			upPanel.add(getShowPanel(),BorderLayout.EAST);
			upPanel.add(getTabPanel(),BorderLayout.CENTER);
			
			//upPanel.setPreferredSize(new Dimension(5, 400));
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
	
	private JPanel getShowPanel() {
		if (showPanel == null) {
			showPanel = new JPanel();
			showPanel.setPreferredSize(new Dimension(750, 10));
			showPanel.setLayout(new BorderLayout());
			showInfo = new ShowInfoPanel(MainFrame.MY_PATIENT_LIST);
			showPanel.add(showInfo,BorderLayout.CENTER);
		}
		//System.out.println("OK");
		return showPanel;
	}

	private void initTables(){
		//System.out.println("firstline of initTable()");
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
		String[] field = {"pid","name"};
		String[] table = {"Patient_personal"};
		String[] where = new String[TABLENUM];
		//System.out.println("before where");
		//where clauses
		String allWhere = "(pid in ( SELECT pid FROM treatment where pic = " + Client.getInstance().getID()
					+ ") or pid in ( SELECT pid from patient_personal where pic = " + Client.getInstance().getID() + " )) ";
		where[0] = allWhere + " AND " + "name rlike '^[abcd]' ORDER BY name";
		where[1] = allWhere + " AND " + "name rlike '^[efgh]' ORDER BY name";
		where[2] = allWhere + " AND " + "name rlike '^[ijkl]' ORDER BY name";
		where[3] = allWhere + " AND " + "name rlike '^[mnop]' ORDER BY name";
		where[4] = allWhere + " AND " + "name rlike '^[qrst]' ORDER BY name";
		where[5] = allWhere + " AND " + "name rlike '^[uvwx]' ORDER BY name";
		where[6] = allWhere + " AND " + "name rlike '^[^abcdefghijklmnopqrstuvwx]' ORDER BY name";
		
		//end of making where clauses
		
		String[] column = {"ID","Name"};
		listSelectionModel = new ListSelectionModel[TABLENUM];
		//System.out.println("before send query");
		for ( int i = 0; i < TABLENUM; i++){
			String temp[][] = Client.getInstance().getMf().sendQuery("SELECT", table, field, where[i],null);
			//if ( temp != null)
			tables[i] = new JTable(temp,column);
			//else{
				//String[][] temp2 = {{"temp","temp"}};
				//tables[i] = new JTable(temp2,column);
			//}
			//System.out.println("after sql");
			tables[i].setRowSelectionAllowed(true);
			listSelectionModel[i] = tables[i].getSelectionModel();
			listSelectionModel[i].addListSelectionListener(new SharedListSelectionHandler(tables[i]));
	        tables[i].setSelectionModel(listSelectionModel[i]);
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
	           // tempLab.setText(lsm.)         
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
