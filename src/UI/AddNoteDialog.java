//author chris
package UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cipher.RSAHardware;
import control.Client;
import control.Connector;
import control.Logger;
import control.Task;

import UI.AddTreatmentDialog.Actions;

public class AddNoteDialog extends Dialogs {
	private JScrollPane scroll;
	private JTextArea text;
	private JPanel btnPanel = null;
	private JButton done = null;
	private JButton cancel = null;
	private Actions actions;
	private BufferedReader fr = null;
	//private File f = null;
	private PrintStream ps;
	//private RSAHardware rsaHard;
	public AddNoteDialog(String pid){
		super();
		//System.out.println(pid);
		this.setID(pid);
		initialize();
	}
	public JButton getDone() {
		if ( done == null){
			done = new JButton("Done");
			done.setActionCommand("DONE");
			done.addActionListener(actions);
			
		}
		return done;
	}
	public JButton getCancel() {
		if ( cancel == null){
			cancel = new JButton("Cancel");
			cancel.setActionCommand("CANCEL");
			cancel.addActionListener(actions);
		}
		return cancel;
	}
	public JPanel getBtnPanel() {
		if ( btnPanel == null){
			btnPanel = new JPanel();
			btnPanel.add(getDone());
			btnPanel.add(getCancel());
		}
		return btnPanel;
	}


	public void initialize(){

		actions = new Actions(this);

		// TODO Auto-generated method stub
		
		this.setTitle("Your private note");
		this.setSize(400,300);
		this.setLayout(new BorderLayout());
		this.add(getScroll(),BorderLayout.CENTER);
		this.add(getBtnPanel(),BorderLayout.SOUTH);
		this.setVisible(true);
	}
	public JScrollPane getScroll() {
		if ( scroll == null){
			scroll = new JScrollPane(getTextArea());
		}
		return scroll;
	}
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
				//fr = new BufferedReader(new InputStreamReader(new FileInputStream("notes/"+getID()+".dat")));
				FileInputStream fs = new FileInputStream("notes/"+getID()+".dat");
				File ff = new File("notes/"+getID()+".dat");
				if ( ff.length() != 0){
					byte[] b  = new byte[(int)ff.length()];
					fs.read(b);
					Client.getInstance().getT().cancel();
					Thread.sleep(Task.PERIOD);
					if(Client.getInstance().getRSAHard().initJavaCard("285921800006") != -1){
					
							byte[] a = Client.getInstance().getRSAHard().decrypt(b, b.length);
					
						//Client.getInstance().resetTimer(Task.AFTER_AUTH);
							String pp = new String(a);
							text.setText(pp);
			
						
						//Client.getInstance().resetTimer(Task.AFTER_AUTH);
					}
					else{
						//Client.getInstance().resetTimer(Task.AFTER_AUTH);
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
	class Actions implements ActionListener{
		AddNoteDialog and;
		public Actions(AddNoteDialog atd){
			this.and = atd;
		}
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
						// TODO Auto-generated catch block
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
				 and.dispose();// TODO Auto-generated Event stub actionPerformed()
			}
		}
	}
	}



