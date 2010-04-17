//author chris
package UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;


import control.Client;
import control.Task;

public class AuthOtherKeyboardPanel extends JPanel {
	private int mode;
	
	AuthPeoplePanel relogin;
	
	JButton loginButton;
	
	public AuthOtherKeyboardPanel(AuthPeoplePanel lp){
		super();
		relogin = lp;
		initialize();	
	}


	private void initialize() {
		BorderLayout bl = new BorderLayout();
		bl.setHgap(1);
		bl.setVgap(0);
		this.setSize(600,150);
		this.setLayout(bl);
		this.add(new AlphaPad(),BorderLayout.CENTER);
		this.add(new NumberPad(),BorderLayout.EAST);
		loginButton = new JButton("AuthOther");
		loginButton.setPreferredSize(new Dimension(40,20));
		loginButton.addActionListener(new AuthAction());

		
		this.add(loginButton,BorderLayout.SOUTH);
		this.setVisible(true);
	
	}

	
	class NumberPad extends JPanel {
		
		private static final int NUMBERS = 10;
		private ArrayList<Integer> numbers;
		private JPanel oneToNine = null;
		
		public NumberPad(){
			super();
			initialize();
		}
		private void initialize() {
			this.setSize(130,100);
			this.setLayout(new BorderLayout());
			this.add(getOneToNine(),BorderLayout.CENTER);
			VirtualButton last = new VirtualButton(numbers.get(numbers.size()-1));
			last.setMargin(new Insets(0,0,0,0));
			last.setPreferredSize(new Dimension(170,20));
			this.add(last,BorderLayout.SOUTH);
			this.setVisible(true);
		}
		public JPanel getOneToNine() {

			
			if ( oneToNine == null){
				numbers = new ArrayList<Integer>();
				for(int i = 0; i < NUMBERS; i++){
					numbers.add(new Integer(i));
				}
				Collections.shuffle(numbers);
				oneToNine = new JPanel();
				oneToNine.setPreferredSize(new Dimension(120,60));
				oneToNine.setLayout(new GridLayout(3,3));
				for(int i = 0; i < NUMBERS-1; i++){
					VirtualButton vb = new VirtualButton(numbers.get(i));
					oneToNine.add(new VirtualButton(numbers.get(i)));
				}	
			}
			
			return oneToNine;
		}
	}
class AlphaPad extends JPanel {
		
		private ArrayList<VirtualButton> buttonList;
		private JPanel firstCol;
		private JPanel secondCol;
		private JPanel thirdCol;
		private boolean isLower = true;
		
		public AlphaPad(){
			super();
			initialize();
		}
		private void initialize() {
			buttonList = new ArrayList<VirtualButton>();
			this.setSize(440,120);
			GridLayout dl = new GridLayout(3,1);
			dl.setVgap(0);
			dl.setHgap(0);
			this.setLayout(dl);
			
			this.add(getFirstCol());
			this.add(getSecondCol());
			this.add(getThirdCol());
			
			this.setVisible(true);
		}
		private Component getThirdCol() {
			String[] list = {"z","x","c","v","b","n","m"};
			if ( thirdCol == null){
				thirdCol = new JPanel();
				thirdCol.setLayout(new FlowLayout(0,0,0));
				
				JButton cap = new JButton("Caps");
				cap.setPreferredSize(new Dimension(60,30));
				cap.setMargin(new Insets(0,0,0,0));
				cap.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						if(isLower){
							for(int i = 0; i < buttonList.size(); i++){
								buttonList.get(i).toUpper();
							}
							isLower = false;
						}
						else{
							for(int i = 0; i < buttonList.size(); i++){
								buttonList.get(i).toLower();
							}
							isLower = true;
						}
					}	
				});
				thirdCol.add(cap);
				
				VirtualButton button;
				for( int i = 0;i< list.length; i++){
					button = new VirtualButton(list[i]);
					buttonList.add(button);
					thirdCol.add(button);
				}
				JButton clear = new JButton("Clr");
				clear.setPreferredSize(new Dimension(60,30));
				clear.setMargin(new Insets(0,0,0,0));
				clear.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						relogin.getPwFd().setText("");
					}	
				});
				thirdCol.add(clear);
			}
			return thirdCol;
		}
		
		private JPanel getFirstCol(){
			String[] list = {"q","w","e","r","t","y","u","i","o","p"};
			if ( firstCol == null){
				firstCol = new JPanel();
				firstCol.setLayout(new FlowLayout(0,0,0));
				
				VirtualButton button;
				for( int i = 0;i< list.length; i++){
					button = new VirtualButton(list[i]);
					buttonList.add(button);
					firstCol.add(button);
				}

			}
			return firstCol;
		}
		private JPanel getSecondCol(){
			String[] list = {"a","s","d","f","g","h","j","k","l"};
			if ( secondCol == null){
				secondCol = new JPanel();
				secondCol.setLayout(new FlowLayout(0,0,0));
				
				VirtualButton button;
				for( int i = 0;i< list.length; i++){
					button = new VirtualButton(list[i]);
					buttonList.add(button);
					secondCol.add(button);
				}
				JButton backspace = new JButton("BS");
				backspace.setPreferredSize(new Dimension(40,30));
				backspace.setMargin(new Insets(0,0,0,0));
				backspace.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						String pw = new String(relogin.getPwFd().getPassword());
						relogin.getPwFd().setText(pw.substring(0,pw.length()-1));
					}	
				});
				secondCol.add(backspace);
			}
			return secondCol;
		}
	}
	class AuthAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			if ( relogin.getNameFd().getText().equals("")){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null,"User Name cannot be empty");
				return;
			}
			if ( new String(relogin.getPwFd().getPassword()).equals("")){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null,"Password cannot be empty");
				return;
			}
			int cardNo = Integer.parseInt(((String)relogin.combo.getSelectedItem()));
			boolean result = Client.getInstance().authOther(relogin.getNameFd().getText(),
					new String(relogin.getPwFd().getPassword()), cardNo);
			if ( result){
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null,"User: " + relogin.getNameFd().getText() 
						+ "\n Temp Card: " + (String)relogin.combo.getSelectedItem() + "\n Valid for 24 Hours");
			}
			else{
				JOptionPane o = new JOptionPane();
				Client.getInstance().getMf().addPopUP(o);
				o.showMessageDialog(null,"Unable to authorize " + relogin.getNameFd().getText());
			}
		}
		
	}
	class VirtualButton extends JButton {
		String label;
		public VirtualButton(int i){
			super();
			this.setSize(40,30);
			this.setPreferredSize(new Dimension(40,30));
			this.setMargin(new Insets(0,0,0,0));
			label = Integer.toString(i);
			this.setText(label);
			this.addActionListener(new ButtonAction());
		}
		public VirtualButton(String s){
			super();
			this.setSize(40,30);
			this.setPreferredSize(new Dimension(40,30));
			label = s;
			this.setText(label);
			this.setMargin(new Insets(0,0,0,0));
			this.addActionListener(new ButtonAction());
		}
		public void toLower(){
			label = label.toLowerCase();
			this.setText(label);
		}
		public void toUpper(){
			label = label.toUpperCase();
			this.setText(label);
		}

		class ButtonAction implements ActionListener{
			public void actionPerformed(ActionEvent arg0) {
				relogin.getPwFd().setText(new String(relogin.getPwFd().getPassword())+label);
			}
			
		}
	}


}

