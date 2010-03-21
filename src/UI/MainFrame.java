package UI;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import control.Client;
import control.Connector;
import control.Task;

public class MainFrame extends JFrame {
	private JMenuItem viewAll;
	private JMenuItem search;
	private JMenuItem add;
	private final static int VIEW_ALL = 0;
	private final static int SEARCH = 1;
	private final static int ADD = 2;
	private final static int LOGOUT = 3;
	public Keyboard keyboard = null;
	private ButtonActions ba;
	public ArrayList<Component> popup = null;
	private static final long serialVersionUID = 1L;
	//private Client client = null;
	private JPanel jContentPane = null;
	private Vector<JPanel> panels = null;  //  @jve:decl-index=0:
	private JPanel upperPanel = null;
	private JMenuBar menuBar = null;
	public JMenu funcMenu = null;
	private Panels mainPanel = null;
	//private Module control = null;
	private Vector<JButton> buttons = null;  //  @jve:decl-index=0:
	//store previuos panel
	private Panels prePanel;
	public LoginPanel loginPanel = null;
	public static int MY_PATIENT_LIST = 0;
	public static int SEARCH_PATIENTS = 1;
	//store current panel
	//private JPanel currentPanel = null;
	
	/**
	 * This is the constructor
	 */
	public MainFrame() {
		super();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			e.printStackTrace();
		}	
		initialize();		
	}
	public String[] getStringPrivileges(){
		return Client.getInstance().getPrivilegeHandler().getStringPrivileges();
	}
	public String[] getPrivileges(){
		if (Client.getInstance().getPrivilegeHandler().getPrivileges() == null)
			System.out.println("FUCK");
		return Client.getInstance().getPrivilegeHandler().getPrivileges();
	}

	public String getName(){
		return Client.getInstance().getName();
	}
	/**
	 * This method restore previous panel
	 * used when card is re-plugin and re-authentication
	 * success
	 */
	
	public void restorePanel(){
			//this.enableAllBtns();
			this.checkPrivilege();

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
		//System.out.println("Changepanel");
			prePanel = (Panels)mainPanel.clone();
			mainPanel = new Panels();
			switch(no){
			case -1: {mainPanel.add(new WelcomePanel(this)); break;} //welcome page
			//lock screen
			case -2: {
				//funcMenu.setSelected(false);
				funcMenu.setEnabled(false);
				funcMenu.setPopupMenuVisible(false);
				this.disableAllBtns();
				mainPanel.add(new LockPanel()); 
				Client.getInstance().card_unplug(); 	
				break;
			}
			case 0: { mainPanel.add(new MyPatientPanel()); break;}
			case 1: { mainPanel.add(new SearchPatientPanel()); break;}
			}
			jContentPane.remove(1);
			mainPanel.setLayout(new GridLayout());
			mainPanel.setPreferredSize(new Dimension(1024, 590));
			jContentPane.add(mainPanel,BorderLayout.CENTER);
			jContentPane.invalidate();
			jContentPane.validate();
		}
	public void refreshMyPatient(String param){
		prePanel = (Panels)mainPanel.clone();
		mainPanel = new Panels();
		MyPatientPanel mpp = new MyPatientPanel();
		mpp.refresh(param);
		mainPanel.add(mpp);
		jContentPane.remove(1);
		mainPanel.setLayout(new GridLayout());
		mainPanel.setPreferredSize(new Dimension(1024, 590));
		jContentPane.add(mainPanel,BorderLayout.CENTER);
		jContentPane.invalidate();
		jContentPane.validate();
	}
	public void refreshSearchPanel(String mode, String param){
		prePanel = (Panels)mainPanel.clone();
		mainPanel = new Panels();
		SearchPatientPanel spp = new SearchPatientPanel();
		mainPanel.add(spp);
		jContentPane.remove(1);
		mainPanel.setLayout(new GridLayout());
		mainPanel.setPreferredSize(new Dimension(1024, 590));
		jContentPane.add(mainPanel,BorderLayout.CENTER);
		jContentPane.invalidate();
		jContentPane.validate();
		spp.refresh(mode, param);

	}
	public void setName(String name){
		Client.getInstance().setName(name);
	}
	public String[][] sendQuery(String type, String[] table, String[] field, 
			String whereClause, String[] values) {
		//System.out.println("on99");
		return Client.getInstance().sendQuery(type, table, field, whereClause,values);
	}
	public void setPassword(String pw){
		Client.getInstance().setPassword(pw);
	}
	public void checkPrivilege(){
		//String[] pri = Client.getInstance().getPrivilegeHandler().getPrivileges();
		funcMenu.setEnabled(true);
		if ( Client.getInstance().isRead()){
			enableButton(0);
			enableButton(1);
			viewAll.setEnabled(true);
			search.setEnabled(true);
		}
		if ( Client.getInstance().isWrite()){
			enableButton(2);
			add.setEnabled(true);
		}
		enableButton(buttons.size()-1);
	}
	public boolean authenticate(String name, String password){
		Client.getInstance().setName(name);
		Client.getInstance().setPassword(password);
		return Client.getInstance().authenticate();
	}
	public String getID(){
		return Client.getInstance().getID();
	}


	public void doLogout(){
		
		if (this.logout()){
			JOptionPane o = new JOptionPane();
			this.addPopUP(o);
			o.showMessageDialog(null,"Logged out");

			Client.getInstance().resetTimer(Task.PRE_AUTH);

			logoutPanel(true);
		}
		else{
			JOptionPane o = new JOptionPane();
			this.addPopUP(o);
			o.showMessageDialog(null,"un logout");
		}
	}

	public boolean logout(){
		return Client.getInstance().logout();
	}
	 /**
	 * This method change the panel to LoginPanel
	 * and delete prePanel
	 */
	public void logoutPanel(boolean open){
		prePanel = null;
		//disable all buttons before authentication
		disableAllBtns();
		mainPanel = new Panels();
		jContentPane.remove(1);
		mainPanel.setLayout(new GridLayout());
		mainPanel.setPreferredSize(new Dimension(1024, 590));
		loginPanel = new LoginPanel();
		if (open)
			loginPanel.enableAll();
		mainPanel.add(loginPanel);
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
		JButton button;
		
		//0-view my patient button
		ImageIcon icon = createImageIcon("icons/a.png",
		"view my patients");
		button = new JButton(icon);
		button.setActionCommand(Integer.toString(VIEW_ALL));
		button.addActionListener(ba);
		buttons.add(button);
		
		//1-search patient
		icon = createImageIcon("icons/search.png",
        "search");
		button = new JButton(icon);
		button.setActionCommand(Integer.toString(SEARCH));
		button.addActionListener(ba);
		buttons.add(button);
		
		//2-add patient
		icon = createImageIcon("icons/add-user.png",
        "add patient");
		button = new JButton(icon);
		button.setActionCommand(Integer.toString(ADD));
		button.addActionListener(ba);
		buttons.add(button);
		
		
		icon = createImageIcon("icons/logout.png",
        "logout");
		button = new JButton(icon);
		button.setActionCommand(Integer.toString(LOGOUT));
		button.addActionListener(ba);
		buttons.add(button);
		
	}
	class ButtonActions implements ActionListener{

		public ButtonActions(){}
		
		public void actionPerformed(ActionEvent e) {
			if ( e.getActionCommand().equals(Integer.toString(LOGOUT))){ //last button = logout
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
	    		 int ans = o.showConfirmDialog(null, "Logout?");
	    		 if ( ans == 0 ){
	    			 Client.getInstance().getMf().doLogout();
	    		 }
			}
			else if ( e.getActionCommand().equals(Integer.toString(VIEW_ALL)))
				Client.getInstance().getMf().changePanel(VIEW_ALL);
			else if ( e.getActionCommand().equals(Integer.toString(SEARCH)))
				Client.getInstance().getMf().changePanel(SEARCH);
			else
				Client.getInstance().getMf().changePanel(ADD);
		}
	}
	
	public void enableButton(int i){
		buttons.get(i).setEnabled(true);
	}
	/*public void enableAllBtns(){
		for ( int i = 0 ; i < buttons.size(); i++)
			buttons.get(i).setEnabled(false);
		//funcMenu.setEnabled(true);
	}*/
	public void disableAllBtns(){
		for ( int i = 0 ; i < buttons.size(); i++)
			buttons.get(i).setEnabled(false);
	
		this.killPopUp();
	}


	class CloseAction implements WindowListener{
		
		MainFrame mf = null;
		public CloseAction(MainFrame mf){
			this.mf = mf;	
		}
		public void windowClosing(WindowEvent we){
			mf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	    	 if ( Connector.getInstance().isConnected()){
	 			JOptionPane o = new JOptionPane();
				this.mf.addPopUP(o);

				o.showMessageDialog(null,"Please use logout button to exit");
	    	 }
	    	 else
	    		 System.exit(0);
	      }
		public void windowActivated(WindowEvent arg0) {
		}
		public void windowClosed(WindowEvent arg0) {
		}
		public void windowDeactivated(WindowEvent arg0) {
		}
		public void windowDeiconified(WindowEvent arg0) {
		}
		public void windowIconified(WindowEvent arg0) {
		}
		public void windowOpened(WindowEvent arg0) {
		}
	}
	private void initialize() {
		this.addWindowListener(new CloseAction(this));
		this.popup = new ArrayList<Component>();
		//init buttons
		ba = new ButtonActions();
		this.initButtons();
		//init UI
		this.setSize(1024,750);
		this.setJMenuBar(getBar());
		
		this.setContentPane(getJContentPane());
		this.setTitle("Secure Medicial System - fyp09008");
		this.setVisible(true);

		logoutPanel(false);
	}
	public void addPopUP(Component o){
		this.popup.add(o);
	}
	
	
	public void killPopUp(){
		for ( int i = 0 ; i < popup.size(); i++){
			if( popup.get(i) != null){
				popup.get(i).setVisible(false);
				if ( popup.get(i).getClass().getName().equals("javax.swing.JOptionPane")){
					((JOptionPane)popup.get(i)).getRootFrame().setVisible(false);
				}
			}
		}
	}
	public void resumePopUp(){

		for ( int i = 0 ; i < popup.size(); i++){
			if( popup.get(i) != null){
				System.out.println("resume");
				popup.get(i).setVisible(true);
				if ( popup.get(i).getClass().getName().equals("javax.swing.JOptionPane")){
					((JOptionPane)popup.get(i)).getRootFrame().setVisible(true);
				}
			}
		}
		//popup = new ArrayList<Component>();
	}

	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getJContentPane() {
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

	private JMenuBar getBar(){
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.setPreferredSize(new Dimension(20, 20));
			menuBar.add(getFuncMenu());
		}
		return menuBar;
	}

	private JMenu getFuncMenu() {
		if (funcMenu == null) {
			funcMenu = new JMenu();
			funcMenu.setEnabled(false);
			funcMenu.setText("Functions");
			viewAll = new JMenuItem("My Patients");
			viewAll.setActionCommand(Integer.toString(VIEW_ALL));
			viewAll.addActionListener(ba);
			funcMenu.add(viewAll);
			
			search = new JMenuItem("Search Patient");
			search.setActionCommand(Integer.toString(SEARCH));
			search.addActionListener(ba);
			funcMenu.add(search);
			
			add = new JMenuItem("Add Patient");
			add.setActionCommand(Integer.toString(ADD));
			add.addActionListener(ba);
			funcMenu.add(add);
	
		}
		
		return funcMenu;
	}


	public Panels getMainPanel() {
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
