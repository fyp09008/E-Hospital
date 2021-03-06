
package ehospital.client.ui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import ehospital.client.control.Client;
import ehospital.client.control.Connector;
import ehospital.client.control.Task;

/**
 * Frame class of the main UI interface.
 * @author   Chun
 */
public class MainFrame extends JFrame {
	private JMenuItem viewAll;
	private JMenuItem search;
	private JMenuItem add;
	private JMenuItem exit;
	private JMenuItem logout;
	private JMenuItem note;
	/**
	 * @uml.property  name="bottom"
	 * @uml.associationEnd  
	 */
	public BottomPanel bottom;
	/**
	 * @uml.property  name="quitMenu"
	 */
	public JMenu quitMenu = null;
	public final static int VIEW_ALL = 0;
	public final static int SEARCH = 1;
	public final static int ADD = 2;
	private final static int LOGOUT = 3;
	public final static int NOTE = 4;
	public final static int AUTH_OTHER = 5;
	public final static int CHANGE_PW = 6;
	public JDialog keyboard = null;
	/**
	 * @uml.property  name="ba"
	 * @uml.associationEnd  
	 */
	private ButtonActions ba;
	public ArrayList<Component> popup = null;
	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="jContentPane"
	 */
	private JPanel jContentPane = null;
	//private Vector<JPanel> panels = null;  //  @jve:decl-index=0:
	/**
	 * @uml.property  name="upperPanel"
	 */
	private JPanel upperPanel = null;
	private JMenuBar menuBar = null;
	/**
	 * @uml.property  name="funcMenu"
	 */
	public JMenu funcMenu = null;
	/**
	 * @uml.property  name="mainPanel"
	 * @uml.associationEnd  
	 */
	private Panels mainPanel = null;
	private Vector<JButton> buttons = null;  //  @jve:decl-index=0:
	/**
	 * @uml.property  name="prePanel"
	 * @uml.associationEnd  
	 */
	private Panels prePanel;
	/**
	 * @uml.property  name="loginPanel"
	 * @uml.associationEnd  
	 */
	public LoginPanel loginPanel = null;
	public static int MY_PATIENT_LIST = 0;
	public static int SEARCH_PATIENTS = 1;
	
	/**
	 * This is the constructor
	 */
	public MainFrame() {
		super();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}	
		initialize();		
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
			this.checkPrivilege();
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Client.getInstance().getMf().getBottomPanel().setStatus("Logged at: "+
					dateFormat.format(calendar.getTime()));
			prePanel.revalidate();
			prePanel.getComponent(0).validate();
			mainPanel = new Panels();
			mainPanel.setLayout(new GridLayout());
			mainPanel.setPreferredSize(new Dimension(1024, 590));
			mainPanel.add(prePanel.getComponent(0));
			jContentPane.remove(2);
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
			if (!(mainPanel.getComponent(0).getClass().toString().equals("class UI.SessionLoginPanel")))			
				prePanel = (Panels)mainPanel.clone();
			
			mainPanel = new Panels();
			switch(no){
				case -1: {
					mainPanel.add(new WelcomePanel(this)); 
					Client.getInstance().getMf().getBottomPanel().setPersonal(Client.getInstance().getName()
							+ " (" + Client.getInstance().getID() +")" );
					String privil = "";
					privil += Client.getInstance().isRead()?"Read ":"";
					privil += Client.getInstance().isWrite()?"Write ":"";
					privil += Client.getInstance().isAdd()?"Add ":"";
					Client.getInstance().getMf().getBottomPanel().setPrivilege(privil);
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Client.getInstance().getMf().getBottomPanel().setStatus("Logged at: "+
							dateFormat.format(calendar.getTime()));
					break;
				} //welcome page
				//lock screen
				case -2: {
					funcMenu.setEnabled(false);
					funcMenu.setPopupMenuVisible(false);
					quitMenu.setPopupMenuVisible(false);
					this.disableAllBtns();
					mainPanel.add(new LockPanel()); 
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Client.getInstance().getMf().getBottomPanel().setStatus("Card unplugged at: " +
							dateFormat.format(calendar.getTime()));
					Client.getInstance().card_unplug(); 	
					break;
				}
				case -3:{
					funcMenu.setEnabled(false);
					funcMenu.setPopupMenuVisible(false);
					quitMenu.setPopupMenuVisible(false);
					this.disableAllBtns();
					mainPanel.add(new SessionLoginPanel()); 
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Client.getInstance().getMf().getBottomPanel().setStatus("Session ended at: " +
							dateFormat.format(calendar.getTime()));
					break;
				}
				case VIEW_ALL: { mainPanel.add(new MyPatientPanel()); break;}
				case SEARCH: { mainPanel.add(new SearchPatientPanel()); break;}
				case AUTH_OTHER: {mainPanel.add(new AuthPeoplePanel()); break;}
				case CHANGE_PW: {mainPanel.add(new ChangePwPanel()); break;}
				case ADD: {break;}
				case NOTE: {break;}
				
			}
			jContentPane.remove(2);
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
		jContentPane.remove(2);
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
		jContentPane.remove(2);
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
		return Client.getInstance().sendQuery(type, table, field, whereClause,values);
	}
	public void setPassword(String pw){
		Client.getInstance().setPassword(pw);
	}
	public void checkPrivilege(){
		funcMenu.setEnabled(true);
		logout.setEnabled(true);
		note.setEnabled(true);
		exit.setEnabled(false);
		
		if ( Client.getInstance().isRead()){
			enableButton(0);
			enableButton(1);
			viewAll.setEnabled(true);
			search.setEnabled(true);
		}
		if ( Client.getInstance().isAdd()){
			enableButton(2);
			add.setEnabled(true);
		}
		enableButton(buttons.size()-4);
		enableButton(buttons.size()-3);
		enableButton(buttons.size()-2);
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


	@SuppressWarnings("static-access")
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
		viewAll.setEnabled(false);
		add.setEnabled(false);
		search.setEnabled(false);
		logout.setEnabled(false);
		note.setEnabled(false);
		exit.setEnabled(true);
		mainPanel = new Panels();
		jContentPane.remove(2);
		mainPanel.setLayout(new GridLayout());
		mainPanel.setPreferredSize(new Dimension(1024, 590));
		loginPanel = new LoginPanel();
		if (open){
			Client.getInstance().getMf().getBottomPanel().setStatus("Logged out");
			Client.getInstance().getMf().getBottomPanel().setPersonal("");
			Client.getInstance().getMf().getBottomPanel().setPrivilege("");
			loginPanel.enableAll();
		}
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
		ImageIcon icon = createImageIcon("icons/all.png",
		"view my patients");
		button = new JButton("view my patients", icon);
		button.setActionCommand(Integer.toString(VIEW_ALL));
		button.addActionListener(ba);
		buttons.add(button);
		
		//1-search patient
		icon = createImageIcon("icons/search.png",
        "search");
		button = new JButton("search", icon);
		button.setActionCommand(Integer.toString(SEARCH));
		button.addActionListener(ba);
		buttons.add(button);
		
		//2-add patient
		icon = createImageIcon("icons/add.png",
        "add patient");
		button = new JButton("add", icon);
		button.setActionCommand(Integer.toString(ADD));
		button.addActionListener(ba);
		buttons.add(button);
		
		icon = createImageIcon("icons/note.png",
        "note");
		button = new JButton("note", icon);
		button.setActionCommand(Integer.toString(NOTE));
		button.addActionListener(ba);
		buttons.add(button);
		
		//icon = createImageIcon("icons/logout.png",
        //"logout");
		icon = createImageIcon("icons/auth.png",
        "password");
		button = new JButton("auth other",icon);
		button.setActionCommand(Integer.toString(AUTH_OTHER));
		button.addActionListener(ba);
		buttons.add(button);
		
		icon = createImageIcon("icons/password.png",
        "password");
		button = new JButton("change pw",icon);
		button.setActionCommand(Integer.toString(CHANGE_PW));
		button.addActionListener(ba);
		buttons.add(button);
		
		icon = createImageIcon("icons/logout.png",
        "logout");
		button = new JButton("logout", icon);
		button.setActionCommand(Integer.toString(LOGOUT));
		button.addActionListener(ba);
		buttons.add(button);
		
	}
	class ButtonActions implements ActionListener{

		public ButtonActions(){}
		
		@SuppressWarnings("static-access")
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
			else if (e.getActionCommand().equals(Integer.toString(NOTE))){
				new AddNoteDialog(Client.getInstance().getID());
			}
			else if (e.getActionCommand().equals(Integer.toString(AUTH_OTHER))){
				Client.getInstance().getMf().changePanel(AUTH_OTHER);
			}
			else if (e.getActionCommand().equals(Integer.toString(CHANGE_PW))){
				Client.getInstance().getMf().changePanel(CHANGE_PW);
			}
			else
				new AddPatientDialog();
		}
	}
	
	public void enableButton(int i){
		buttons.get(i).setEnabled(true);
	}

	public void disableAllBtns(){
		for ( int i = 0 ; i < buttons.size(); i++)
			buttons.get(i).setEnabled(false);
	
		this.killPopUp();
	}


	/**
	 * @author   Chun
	 */
	class CloseAction implements WindowListener{
		
		/**
		 * @uml.property  name="mf"
		 * @uml.associationEnd  
		 */
		MainFrame mf = null;
		public CloseAction(MainFrame mf){
			this.mf = mf;	
		}
		@SuppressWarnings("static-access")
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
		this.setTitle("Secure Medical System - FYP09008");
		this.setVisible(true);

		logoutPanel(false);
	}
	public void addPopUP(Component o){
		this.popup.add(o);
	}
	
	
	@SuppressWarnings("static-access")
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
				popup.get(i).setVisible(true);
			}
		}
	}

	
	/**
	 * This method initializes jContentPane
	 * @return   javax.swing.JPanel
	 * @uml.property  name="jContentPane"
	 */
	public JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setPreferredSize(new Dimension(200, 700));
			jContentPane.add(getUpperPanel(), BorderLayout.NORTH,0);
			jContentPane.add(getBottomPanel(),BorderLayout.SOUTH,1);
			jContentPane.add(getMainPanel(), BorderLayout.CENTER,2);
		}
		return jContentPane;
	}
	/**
	 * @return
	 * @uml.property  name="upperPanel"
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
	 * This method initializes upperPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private BottomPanel getBottomPanel() {
		if (bottom == null) {
			bottom = new BottomPanel();
		}
		return bottom;
	}

	private JMenuBar getBar(){
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.setPreferredSize(new Dimension(20, 25));
			menuBar.add(getFuncMenu());
			
		}
		return menuBar;
	}

	private JMenuItem getExitMenu() {
		if ( exit == null){
			exit = new JMenuItem("Exit");
			exit.setEnabled(true);
			exit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}	
			});
		}
		return exit;
	}

	private JMenuItem getLogoutMenu() {
		if ( logout == null){
			logout = new JMenuItem("Logout");
			logout.addActionListener(ba);
			logout.setActionCommand(Integer.toString(LOGOUT));
			logout.setEnabled(false);
		}
		return logout;
	}

	/**
	 * @return
	 * @uml.property  name="funcMenu"
	 */
	private JMenu getFuncMenu() {
		if (funcMenu == null) {
			funcMenu = new JMenu();
			
			funcMenu.setEnabled(true);
			funcMenu.setText("Functions");
			viewAll = new JMenuItem("My Patients");
			viewAll.setEnabled(false);
			viewAll.setActionCommand(Integer.toString(VIEW_ALL));
			viewAll.addActionListener(ba);
			funcMenu.add(viewAll);
			
			search = new JMenuItem("Search Patient");
			search.setEnabled(false);
			search.setActionCommand(Integer.toString(SEARCH));
			search.addActionListener(ba);
			funcMenu.add(search);
			
			add = new JMenuItem("Add Patient");
			add.setEnabled(false);
			add.setActionCommand(Integer.toString(ADD));
			add.addActionListener(ba);
			funcMenu.add(add);

			note = new JMenuItem("Add Note");
			note.setEnabled(false);
			note.setActionCommand(Integer.toString(NOTE));
			note.addActionListener(ba);
			funcMenu.add(note);
			
			funcMenu.add(getQuitMenu());
			

		}
		
		return funcMenu;
	}


	/**
	 * @return
	 * @uml.property  name="quitMenu"
	 */
	private JMenu getQuitMenu() {
		if ( quitMenu == null){
			quitMenu = new JMenu("Quit");
			quitMenu.add(getLogoutMenu());
			quitMenu.add(getExitMenu());
		}
		return quitMenu;
	}

	/**
	 * @return
	 * @uml.property  name="mainPanel"
	 */
	public Panels getMainPanel() {
		if (mainPanel == null) {
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setRows(1);
			mainPanel = new Panels();
			mainPanel.setLayout(gridLayout1);
			mainPanel.setPreferredSize(new Dimension(1024, 590));
		}
		return mainPanel;
	}


}  //  @jve:decl-index=0:visual-constraint="16,7"
