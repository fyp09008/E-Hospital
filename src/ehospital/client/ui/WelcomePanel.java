package ehospital.client.ui;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import ehospital.client.control.Client;

/**
 * Panel Class of welcoming screen
 * @author   Chun
 */
public class WelcomePanel extends Panels {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static SimpleAttributeSet BOLD_BLACK = new SimpleAttributeSet();
	static SimpleAttributeSet ITALIC_GRAY = new SimpleAttributeSet();
	static SimpleAttributeSet BLACK = new SimpleAttributeSet();
	static SimpleAttributeSet BIG_BOLD_BLACK = new SimpleAttributeSet();
	static {
	    StyleConstants.setForeground(BOLD_BLACK, Color.black);
	    StyleConstants.setBold(BOLD_BLACK, true);
	    StyleConstants.setFontFamily(BOLD_BLACK, "Helvetica");
	    StyleConstants.setFontSize(BOLD_BLACK, 20);
	    
	    StyleConstants.setForeground(BIG_BOLD_BLACK, Color.black);
	    StyleConstants.setBold(BIG_BOLD_BLACK, true);
	    StyleConstants.setFontFamily(BIG_BOLD_BLACK, "Helvetica");
	    StyleConstants.setFontSize(BIG_BOLD_BLACK, 30);
	    
	    StyleConstants.setForeground(ITALIC_GRAY, Color.gray);
	    StyleConstants.setItalic(ITALIC_GRAY, true);
	    StyleConstants.setFontFamily(ITALIC_GRAY, "Helvetica");
	    StyleConstants.setFontSize(ITALIC_GRAY, 20);
	    
	    StyleConstants.setForeground(BLACK, Color.black);
	    StyleConstants.setFontFamily(BLACK, "Helvetica");
	    StyleConstants.setFontSize(BLACK, 20);
	}
	//private JPanel contentPane = null;
	/**
	 * @uml.property  name="msg"
	 */
	private JTextPane msg = null;
	public WelcomePanel(MainFrame mf){
		super();
		this.initialize();
	}
	protected void insertText(String text, AttributeSet set) {
		try {
			getMsg().getDocument().insertString(
			getMsg().getDocument().getLength(), text, set);
		} catch (BadLocationException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
	}
	private void initialize(){
		this.setLayout(new BorderLayout());
		this.add(getMsg(),BorderLayout.CENTER);
		this.addText(Client.getInstance().getName());
	}
	private void addText(String name){
		insertText("Successfully Logged on as: \n\n",BIG_BOLD_BLACK);
		insertText("ID: ",ITALIC_GRAY);
		insertText( Client.getInstance().getID() + "\n",BLACK);
		insertText("Name: ",ITALIC_GRAY);
		insertText( Client.getInstance().getName()+ " \n\n",BLACK );
		insertText("You have the following privileges\n\n",ITALIC_GRAY);
		String privil = "";
		privil += Client.getInstance().isRead()?"Read\n\n":"";
		privil += Client.getInstance().isWrite()?"Write\n\n":"";
		privil += Client.getInstance().isAdd()?"Add\n\n":"";
		insertText(privil,BOLD_BLACK);
		getMsg().invalidate();
		getMsg().validate();
		
		
	}
	/**
	 * @return
	 * @uml.property  name="msg"
	 */
	private JTextPane getMsg(){
		if ( msg == null){
			msg = new JTextPane();
		}
		return msg;
	}

}
