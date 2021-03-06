package ehospital.client.control;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;



/**
 * Class to simulate a card with different state. It also
 * contains a UI component to allow user to change state of
 * the simulated card.
 * 
 * One must modify the client program to use this class.
 * @author Chun
 *
 */
public class SoftCard extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int mode;
	public static final int PRE_AUTH = 0;
	public static final int AFTER_AUTH = 1;
	public static final int WAIT_REAUTH = 2;
	public boolean isPlug = false;
	JButton in;
	JButton out;
	/**
	 * Default Constructor. Initialize the simulation.
	 */
	public SoftCard() {
		initialize();
	}

	/**
	 * Initialize the simulation.
	 */
	private void initialize() {
		ButtonAction ba = new ButtonAction();
		in = new JButton("plug in");
		in.setActionCommand("in");
		in.addActionListener(ba);
		
		out = new JButton("plug out");
		out.setActionCommand("out");
		out.addActionListener(ba);
		
		
		this.getContentPane().setLayout(new FlowLayout());
		this.getContentPane().add(in);
		this.getContentPane().add(out);
		this.setSize(200, 200);
		this.setVisible(true);
	}
	class ButtonAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getActionCommand().equals("in")){
				isPlug = true;
			}
			else{
				isPlug = false;
			}
		}
		
	}
	
}
