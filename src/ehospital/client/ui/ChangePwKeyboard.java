//author chris
package ehospital.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ehospital.client.control.Client;

/**
 * Virtual Keyboard Class for changing password. This class is similar to AuthOtherKeyboardPanel. Suggested to merge both classes.
 * @author   Chun
 */
public class ChangePwKeyboard extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * @uml.property  name="relogin"
	 * @uml.associationEnd  
	 */
	ChangePwPanel relogin;
	
	JButton loginButton;
	
	public ChangePwKeyboard(ChangePwPanel lp){
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
		loginButton = new JButton("Change Password");
		loginButton.setPreferredSize(new Dimension(40,20));
		loginButton.addActionListener(new ActionListener(){

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				if (new String(relogin.getPwFd(0).getPassword()).equals("") || 
						new String(relogin.getPwFd(1).getPassword()).equals("") ||
						new String(relogin.getPwFd(2).getPassword()).equals("")){
					JOptionPane m = new JOptionPane();
					Client.getInstance().getMf().addPopUP(m);
					m.showMessageDialog(null, "Field(s) cannot be empty");
					return;
				}
				if(!(new String(relogin.getPwFd(1).getPassword()).equals(
						new String(relogin.getPwFd(2).getPassword())))){
					JOptionPane m = new JOptionPane();
					Client.getInstance().getMf().addPopUP(m);
					m.showMessageDialog(null, "New Passwords Not Match!");
					return;
				}else{
					boolean result = Client.getInstance().changePassword(new String(relogin.getPwFd(0).getPassword()),
							new String(relogin.getPwFd(1).getPassword()));
					if ( result){
						JOptionPane m = new JOptionPane();
						Client.getInstance().getMf().addPopUP(m);
						m.showMessageDialog(null, "Password Changed!");
					}
					else{
						JOptionPane m = new JOptionPane();
						Client.getInstance().getMf().addPopUP(m);
						m.showMessageDialog(null, "Password NOT Changed!");
					}
				}
			}
		});
		
		this.add(loginButton,BorderLayout.SOUTH);
		this.setVisible(true);
	
	}

	
	/**
	 * @author   Chun
	 */
	class NumberPad extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private static final int NUMBERS = 10;
		private ArrayList<Integer> numbers;
		/**
		 * @uml.property  name="oneToNine"
		 */
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
		/**
		 * @return
		 * @uml.property  name="oneToNine"
		 */
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
					oneToNine.add(new VirtualButton(numbers.get(i)));
				}	
			}
			
			return oneToNine;
		}
	}
/**
 * Alphabet panel.
 * @author   Chun
 */
class AlphaPad extends JPanel {
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private ArrayList<VirtualButton> buttonList;
		/**
		 * @uml.property  name="firstCol"
		 */
		private JPanel firstCol;
		/**
		 * @uml.property  name="secondCol"
		 */
		private JPanel secondCol;
		/**
		 * @uml.property  name="thirdCol"
		 */
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
		/**
		 * @return
		 * @uml.property  name="thirdCol"
		 */
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
						relogin.getPwFd(relogin.selected).setText("");
					}	
				});
				thirdCol.add(clear);
			}
			return thirdCol;
		}
		
		/**
		 * @return
		 * @uml.property  name="firstCol"
		 */
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
		/**
		 * @return
		 * @uml.property  name="secondCol"
		 */
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
						String pw = new String(relogin.getPwFd(relogin.selected).getPassword());
						relogin.getPwFd(relogin.selected).setText(pw.substring(0,pw.length()-1));
					}	
				});
				secondCol.add(backspace);
			}
			return secondCol;
		}
	}
	/**
	 * Button class on virtual keyboard
	 * @author Chun
	 *
	 */

	class VirtualButton extends JButton {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
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
				relogin.getPwFd(relogin.selected).setText(
						new String(relogin.getPwFd(relogin.selected).getPassword())+label);
			}
			
		}
	}


}

