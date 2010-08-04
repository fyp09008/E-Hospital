//author chris
package ehospital.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ehospital.client.control.Client;
import ehospital.client.control.Task;


/**
 * Dialog for adding note to a patient.
 * @author   Chun
 */
public class AddNoteDialog extends Dialogs {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="scroll"
	 */
	private JScrollPane scroll;
	private JTextArea text;
	/**
	 * @uml.property  name="btnPanel"
	 */
	private JPanel btnPanel = null;
	/**
	 * @uml.property  name="done"
	 */
	private JButton done = null;
	/**
	 * @uml.property  name="cancel"
	 */
	private JButton cancel = null;
	/**
	 * @uml.property  name="actions"
	 * @uml.associationEnd  
	 */
	private Actions actions;
	private PrintStream ps;
	public AddNoteDialog(String pid){
		super();
		this.setID(pid);
		initialize();
	}
	/**
	 * @return
	 * @uml.property  name="done"
	 */
	public JButton getDone() {
		if ( done == null){
			done = new JButton("Done");
			done.setActionCommand("DONE");
			done.addActionListener(actions);
		}
		return done;
	}
	/**
	 * @return
	 * @uml.property  name="cancel"
	 */
	public JButton getCancel() {
		if ( cancel == null){
			cancel = new JButton("Cancel");
			cancel.setActionCommand("CANCEL");
			cancel.addActionListener(actions);
		}
		return cancel;
	}
	/**
	 * @return
	 * @uml.property  name="btnPanel"
	 */
	public JPanel getBtnPanel() {
		if ( btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.add(getDone());
			btnPanel.add(getCancel());
		}
		return btnPanel;
	}


	/**
	 * initialize UI component.
	 */
	public void initialize(){

		actions = new Actions(this);

		
		this.setTitle("Your private note");
		this.setSize(400,300);
		this.setLayout(new BorderLayout());
		this.add(getScroll(),BorderLayout.CENTER);
		this.add(getBtnPanel(),BorderLayout.SOUTH);
		this.setVisible(true);
	}
	/**
	 * @return
	 * @uml.property  name="scroll"
	 */
	public JScrollPane getScroll() {
		if ( scroll == null){
			scroll = new JScrollPane(getTextArea());
		}
		return scroll;
	}
	@SuppressWarnings("static-access")
	public JTextArea getTextArea() {
		if ( text == null){
			text = new JTextArea();
			try {
				File file = new File("notes");
				if ( !file.exists() || !file.isDirectory())
					file.mkdir();
				file = new File("notes/"+getID()+".dat");
				if ( !file.exists() || !file.isFile())
					file.createNewFile();
				FileInputStream fs = new FileInputStream("notes/"+getID()+".dat");
				File ff = new File("notes/"+getID()+".dat");
				if ( ff.length() != 0){
					byte[] b  = new byte[(int)ff.length()];
					fs.read(b);
					Client.getInstance().getT().cancel();
					Thread.sleep(Task.PERIOD);
					if(Client.getInstance().getRSAHard().initJavaCard("285921800006") != -1){
						byte[] a = Client.getInstance().getRSAHard().decrypt(b, b.length);
				
						String pp = new String(a);
						text.setText(pp);
					}
					else{
						JOptionPane o = new JOptionPane();
						Client.getInstance().getMf().addPopUP(o);
						o.showMessageDialog(null,"Card is dead");
						this.dispose();
					}
					Client.getInstance().resetTimer(Task.AFTER_AUTH);
					fs.close();
				}

			} catch (Exception e) {
				Client.getInstance().resetTimer(Task.AFTER_AUTH);
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null,"New directory created");
			}
		}
		//}
		
		return text;
	}
	/**
	 * @author   Chun
	 */
	class Actions implements ActionListener{
		/**
		 * @uml.property  name="and"
		 * @uml.associationEnd  
		 */
		AddNoteDialog and;
		public Actions(AddNoteDialog atd){
			this.and = atd;
		}
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent ae) {
			if ( ae.getActionCommand().equals("DONE")){
				JOptionPane oo = new JOptionPane();
				Client.getInstance().getMf().addPopUP(oo);
				int ans = oo.showConfirmDialog(null, "Sure?");
				
				if ( ans == 0){
					try {
						Client.getInstance().getT().cancel();
						Thread.sleep(Task.PERIOD);
						if( Client.getInstance().getRSAHard().initJavaCard("285921800006") == -1){
							Client.getInstance().resetTimer(Task.AFTER_AUTH);
							Client.getInstance().getMf().popup = new ArrayList<Component>();
							and.dispose();
							JOptionPane o = new JOptionPane();
							Client.getInstance().getMf().addPopUP(o);
							o.showMessageDialog(null,"Card is dead");
							
						}
						else{
							ps = new PrintStream(new FileOutputStream("notes/"+getID()+".dat"),false);
							byte b[] = Client.getInstance().getRSAHard().encrypt
								(text.getText().getBytes(), text.getText().getBytes().length);
							Client.getInstance().resetTimer(Task.AFTER_AUTH);
							ps.write(b);
							ps.close();
							Client.getInstance().getMf().popup = new ArrayList<Component>();
							and.dispose();
							JOptionPane o = new JOptionPane();
							Client.getInstance().getMf().addPopUP(o);
							o.showMessageDialog(null,"Private note saved");
							
						}
						
					} catch (Exception e) {
						Client.getInstance().resetTimer(Task.AFTER_AUTH);
						Client.getInstance().getMf().popup = new ArrayList<Component>();
						and.dispose();
						
						JOptionPane o = new JOptionPane();
						Client.getInstance().getMf().addPopUP(o);
						o.showMessageDialog(null,"Card is dead");
					
					}
				}

			}else{
				//CANCEL action
				 Client.getInstance().getMf().popup = new ArrayList<Component>();
				 and.dispose();
			}
		}
	}
	}



