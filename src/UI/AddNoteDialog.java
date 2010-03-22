package UI;

import java.awt.BorderLayout;
import java.awt.Component;
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
		//Client. = Client.getInstance().getRSAHard();
		actions = new Actions(this);
		//f = new File("notes/" + getID()+".txt");

		
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
				
				String temp = null;
				Client.getInstance().getT().cancel();
				fr = new BufferedReader(new InputStreamReader(new FileInputStream("notes/"+getID()+".txt")));
				//FileInputStream fs = new FileInputStream("notes/"+getID()+".txt");
				//byte[] b = new byte[10000];
				//fs.read(b);
				if(Client.getInstance().getRSAHard().initJavaCard("285921800006") != -1){
						temp = fr.readLine();
						
						byte[] a = Client.getInstance().getRSAHard().decrypt(temp.getBytes(), temp.getBytes().length);
					
				}
				else
					System.out.println("card dead");
				Client.getInstance().resetTimer(Task.AFTER_AUTH);
				fr.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				int ans = o.showConfirmDialog(null, "Sure?");
				if ( ans == 0){
					//File f = new File("notes/" + getID()+".txt");
					try {
						Client.getInstance().getT().cancel();
						if( Client.getInstance().getRSAHard().initJavaCard("285921800006") == -1)
							System.out.println("fuck");
						else{
						ps = new PrintStream(new FileOutputStream("notes/"+getID()+".txt"),true);
						ps.println(
								Client.getInstance().getRSAHard().encrypt
								(text.getText().getBytes(), text.getText().getBytes().length).toString()
										);
						ps.close();
						}
						Client.getInstance().resetTimer(Task.AFTER_AUTH);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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



