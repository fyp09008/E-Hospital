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

import UI.TableListSelectionDemo.SharedListSelectionHandler;

import java.awt.Font;
import java.sql.ResultSet;

public class MyPatientList extends Panels {
	
	private JScrollPane jScrollPane = null;
	private JTabbedPane tabPanel = null;
	private JPanel goPanel = null;
	private JPanel upPanel = null;
	private JPanel showPanel = null;
	private JButton editBtn = null;
	private JPanel jPanel = null;
	private JTable[] tables = null;
	private static int TABLENUM = 7;
	private JLabel tempLab = null;
	private ShowInfoPanel showInfo = null; 
	ListSelectionModel[] listSelectionModel;  //  @jve:decl-index=0:
	private MainFrame mf = null;
	
	public MyPatientList(MainFrame mf){
		super();
		this.mf = mf;
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
        this.add(getGoPanel(),null);
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
			tabPanel.addTab("A-D", getJScrollPane());
			tabPanel.addTab("E-H", tables[1]);
			tabPanel.addTab("I-L", null);
			tabPanel.addTab("M-P", null);
			tabPanel.addTab("Q-T", null);
			tabPanel.addTab("U-X", null);
			tabPanel.addTab("Y-etc", null);
		}
		return tabPanel;
	}
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(tables[0]);
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
	private JButton getEditButton(){
		ImageIcon icon = createImageIcon("edit-icon.png",
        "a pretty but meaningless splat");
		editBtn = new JButton("EDIT",icon);
		editBtn.setPreferredSize(new Dimension(59, 80));
		editBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				new EditInfoFrame(); // TODO Auto-generated Event stub actionPerformed()
			}
		});
		return editBtn;
	}
	private JPanel getShowPanel() {
		if (showPanel == null) {
			showPanel = new JPanel();
			showPanel.setPreferredSize(new Dimension(500, 10));
			showPanel.setLayout(new BorderLayout());
			showPanel = new ShowInfoPanel(this.mf);
			showPanel.add(showInfo,BorderLayout.CENTER);
		}
		return showPanel;
	}
	private JPanel getGoPanel() {
		if (goPanel == null) {
			goPanel = new JPanel();
		}
		return goPanel;
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
		String[] field = {"pid","name"};
		String[] table = {"Patient_personal"};
		String[] where = new String[TABLENUM];
		//where clauses
		where[0] = "upper(name) LIKE 'A%' OR upper(name) LIKE 'B%' OR " +
				"upper(name) LIKE 'C%' OR upper(name) LIKE 'D%' ORDER BY name";
		where[1] = "upper(name) LIKE 'E%' OR upper(name) LIKE 'F%' OR " +
		"upper(name) LIKE 'G%' OR upper(name) LIKE 'H%' ORDER BY name";
		where[2] = "upper(name) LIKE 'I%' OR upper(name) LIKE 'J%' OR " +
		"upper(name) LIKE 'K%' OR upper(name) LIKE 'L%' ORDER BY name";
		where[3] = "upper(name) LIKE 'M%' OR upper(name) LIKE 'N%' OR " +
		"upper(name) LIKE 'O%' OR upper(name) LIKE 'P%' ORDER BY name";
		where[4] = "upper(name) LIKE 'Q%' OR upper(name) LIKE 'R%' OR " +
		"upper(name) LIKE 'S%' OR upper(name) LIKE 'T%' ORDER BY name";
		where[5] = "upper(name) LIKE 'U%' OR upper(name) LIKE 'V%' OR " +
		"upper(name) LIKE 'W%' OR upper(name) LIKE 'X%' ORDER BY name";
		where[5] = "upper(name) LIKE 'Y%' OR upper(name) LIKE 'Z%' OR " +
		"upper(name) LIKE 'W%' OR upper(name) LIKE 'X%' ORDER BY name";
		
		char[] letter = {'B','C','D','E','F','G','H','I','J','K',
				'L','M','N','O','P','Q','R','S','T','U','V','W','X'};
		where[6] = "upper(name) NOT LIKE 'A%'";
		for ( int i = 0; i < letter.length; i++){
			where[6] = where[6] + " AND upper(name) NOT LIKE '"+letter[i]+"%'";
		}
		where[6] = where[6] + " ORDER BY name";
		//end of making where clauses
		
		String[] column = {"ID","Name"};
		for ( int i = 0; i < TABLENUM; i++){
			tables[i] = new JTable(mf.sendQuery("select", table, field, where[i])
					,column);
			tables[i].setRowSelectionAllowed(true);
			listSelectionModel[i] = tables[0].getSelectionModel();
			listSelectionModel[i].addListSelectionListener(new SharedListSelectionHandler(tables[0]));
	        tables[i].setSelectionModel(listSelectionModel[i]);
		}
		
		//require SQL result
		//tables[0] = new JTable(temp,col);
		//tables[0].setRowSelectionAllowed(true);
		//listSelectionModel = tables[0].getSelectionModel();
        //listSelectionModel.addListSelectionListener(new SharedListSelectionHandler(tables[0]));
        //tables[0].setSelectionModel(listSelectionModel);
		//tables[1] = new JTable(temp1,col);
		//tables[1].setRowSelectionAllowed(true);
		//listSelectionModel = tables[1].getSelectionModel();
        //listSelectionModel.addListSelectionListener(new SharedListSelectionHandler(tables[1]));
        //tables[1].setSelectionModel(listSelectionModel);
		//tables[0].
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
                    	showInfo.getInfo();
                    }
                }
	        }
	    }


}  //  @jve:decl-index=0:visual-constraint="10,10"
