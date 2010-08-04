//author chris
package ehospital.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import ehospital.client.control.Client;

/**
 * Panel Class that lock the screen
 * @author Chun
 *
 */
public class LockPanel extends Panels {
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextPane textpane;
	static SimpleAttributeSet ITALIC_GRAY = new SimpleAttributeSet();
	static SimpleAttributeSet BIG_BOLD_BLACK = new SimpleAttributeSet();
	static {

	    
	    StyleConstants.setForeground(BIG_BOLD_BLACK, Color.black);
	    StyleConstants.setBold(BIG_BOLD_BLACK, true);
	    StyleConstants.setFontFamily(BIG_BOLD_BLACK, "Helvetica");
	    StyleConstants.setFontSize(BIG_BOLD_BLACK, 40);
	    StyleConstants.setAlignment(BIG_BOLD_BLACK, StyleConstants.ALIGN_JUSTIFIED);
	    
	    StyleConstants.setForeground(ITALIC_GRAY, Color.gray);
	    StyleConstants.setItalic(ITALIC_GRAY, true);
	    StyleConstants.setFontFamily(ITALIC_GRAY, "Helvetica");
	    StyleConstants.setFontSize(ITALIC_GRAY, 18);
	    StyleConstants.setAlignment(ITALIC_GRAY, StyleConstants.ALIGN_JUSTIFIED);
	    

	}

	public LockPanel(){
		super();
		this.setLayout(new BorderLayout());
		textpane = new JTextPane();
		textpane.setEditable(false);
		textpane.setEnabled(false);
		insertText("Screen locked\n",BIG_BOLD_BLACK);
		insertText("Unlock by plugging in your card and re-authenticate",ITALIC_GRAY);
		JPanel temp = new JPanel();
		temp.setPreferredSize(new Dimension(100,250));
		JPanel temp1 = new JPanel();
		temp1.setPreferredSize(new Dimension(320,250));
		this.add(temp,BorderLayout.NORTH);
		this.add(temp1,BorderLayout.WEST);
		this.add(textpane,BorderLayout.CENTER);
		this.setVisible(true);

		
	}
	protected void insertText(String text, AttributeSet set) {
		try {
			textpane.getDocument().insertString(
			textpane.getDocument().getLength(), text, set);
		} catch (BadLocationException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
	}
}
