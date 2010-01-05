package UI;

import control.*;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;

import java.sql.ResultSet;
import java.util.Vector;
import java.awt.Font;
import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.ComponentOrientation;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Client client = null;
	private JPanel jContentPane = null;
	private Vector<JPanel> panels = null;  //  @jve:decl-index=0:
	private JPanel upperPanel = null;
	private JMenuBar jJMenuBar = null;
	private JMenu jMenu = null;
	private Panels mainPanel = null;
	//private Module control = null;
	private Vector<JButton> buttons = null;  //  @jve:decl-index=0:
	//store previuos panel
	private Panels prePanel = null;
	//store current panel
	private JPanel currentPanel = null;
	
	/**
	 * This is the constructor
	 */
	public MainFrame(Client c) {
		super();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			e.printStackTrace();
		}	
		client = c;
		initialize();		
	}
	public String[] getStringPrivileges(){
		return client.getStringPrivileges();
	}
	public int[] getPrivileges(){
		return client.getPrivileges();
	}
	public String getName(){
		return client.getName();
	}
	/**
	 * This method restore previous panel
	 * used when card is re-plugin and re-authentication
	 * success
	 */
	public void restorePanel(){
		prePanel.revalidate();
		prePanel.getComponent(0).validate();
		mainPanel = new Panels();
		mainPanel.setLayout(new GridLayout());
		mainPanel.setPreferredSize(new Dimension(1024, 590));
		mainPanel.add(prePanel.getComponent(0));
		jContentPane.remove(1);
		jContentPane.add(mainPanel,BorderLayout.CENTER);
		jContentPane.invalidate();
		jContentPane.validate();
	}
	 /**
	  * This method changes current panel and store
	  * current panel to prePanel
	 * @param no
	 * -1 - welcome panel, open WelcomePanel
	 * 0 - view my patient, open MyPatientList
	 * 1 - search patient, X
	 * 2 - add patient, X
	 */
	public void changePanel(int no){
			prePanel = (Panels)mainPanel.clone();
			mainPanel = new Panels();
			switch(no){
			case -1: {mainPanel.add(new WelcomePanel(this)); break;}
			case 0: { mainPanel.add(new MyPatientList(this)); break;}
			case 1: { mainPanel.add(new ShowInfoPanel()); break;}
			}
			jContentPane.remove(1);
			mainPanel.setLayout(new GridLayout());
			mainPanel.setPreferredSize(new Dimension(1024, 590));
			jContentPane.add(mainPanel,BorderLayout.CENTER);
			jContentPane.invalidate();
			jContentPane.validate();
		}
	public void setName(String name){
		client.setName(name);
	}
	public String[][] sendQuery(String type, String[] table, String[] field, 
			String whereClause) {
		return client.sendQuery(type, table, field, whereClause);
	}
	public void setPassword(String pw){
		client.setPassword(pw);
	}
	public void checkPrivilege(){
		int[] pri = client.getPrivileges();
		/* 0 = READ, 1 = WRITE, 2 = ADD*/
		if ( pri[0] == 1){
			enableButton(0);
			enableButton(1);
		}
		if ( pri[1] == 1){
			enableButton(2);
		}
		
	}
	public boolean authenicate(){
		return client.authenicate();
	}

	 /**
	 * This method add ActionListeners to the buttons
	 */
	public void initButtonAction(){
		for(int i = 0; i < buttons.size(); i++)
			buttons.get(i).addActionListener(new ButtonActions(this,i));
	}
	 /**
	 * @author cctang
	 *	actions for the buttons
	 */
	class ButtonActions implements ActionListener{
		int no;
		MainFrame mf = null;
		public ButtonActions(MainFrame mf,int x){
			this.mf=mf;
			no = x;
		}
		public void actionPerformed(ActionEvent e) {
			mf.changePanel(no);
		}
	}
/*	class BackActions implements ActionListener{
		int no;
		MainFrame mf = null;
		public BackActions(MainFrame mf,int x){
			this.mf=mf;
			no = x;
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("inclass"+no);
			mf.restorePanel();
			//jContentPane.add(new MyPatientList(),BorderLayout.CENTER);
		}
	}
	*/

	 /**
	 * This method change the panel to LoginPanel
	 * and delete prePanel
	 */
	public void logout(){
		prePanel = null;
		mainPanel = new Panels();
		jContentPane.remove(1);
		mainPanel.setLayout(new GridLayout());
		mainPanel.setPreferredSize(new Dimension(1024, 590));
		mainPanel.add(new LoginPanel(this));
		jContentPane.add(mainPanel,BorderLayout.CENTER);
		jContentPane.invalidate();
		jContentPane.validate();
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
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
	 * This method init buttons as follows
	 * 0 = view my patients
	 * 1 = search patient
	 * 2 = add patient
	 * 3
	 * 4 = logout
	 */
	public void initButtons(){
		buttons = new Vector<JButton>();
		//buttons.add(new JButton(""));
		
		//0-view my patient button
		ImageIcon icon = createImageIcon("a.png",
		"view my patients");
		buttons.add(new JButton(icon));
		
		//1-search patient
		icon = createImageIcon("search.png",
        "search");
		buttons.add(new JButton(icon));
		
		//2-add patient
		icon = createImageIcon("add-user.png",
        "add patient");
		
		buttons.add(new JButton(icon));
		buttons.add(new JButton("X"));
		
		icon = createImageIcon("logout.png",
        "add patient");
		buttons.add(new JButton("icon"));
		
		//init buttons' actions
		initButtonAction();
		//disable all buttons before authentication
		disableAllBtns();
	}
	
	public void enableButton(int i){
		buttons.get(i).setEnabled(true);
	}
	public void disableAllBtns(){
		for ( int i = 0 ; i < buttons.size(); i++)
			buttons.get(i).setEnabled(false);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		//init UI
		this.setSize(1024,750);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("Secure Medicial System - fyp09008");
		this.setVisible(true);
		//init buttons
		initButtons();
		//init as logout status
		logout();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setPreferredSize(new Dimension(200, 700));
			jContentPane.add(getUpperPanel(), BorderLayout.NORTH,0);
			jContentPane.add(getMainPanel(), BorderLayout.CENTER,1);
			//jContentPane.add(new LoginPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes upperPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getUpperPanel() {
		if (upperPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			upperPanel = new JPanel();
			upperPanel.setLayout(gridLayout);
			upperPanel.setPreferredSize(new Dimension(50, 50));
			for ( int i=0;i<buttons.size();i++){
				upperPanel.add(buttons.get(i));
			}
		}
		return upperPanel;
	}

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.setPreferredSize(new Dimension(20, 20));
			jJMenuBar.add(getJMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("File");
		}
		return jMenu;
	}


	private Panels getMainPanel() {
		if (mainPanel == null) {
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setRows(1);
			mainPanel = new Panels();
			mainPanel.setLayout(gridLayout1);
			mainPanel.setPreferredSize(new Dimension(1024, 590));
			//mainPanel.add(getJButton5(), null);
		}
		return mainPanel;
	}

}  //  @jve:decl-index=0:visual-constraint="16,7"
