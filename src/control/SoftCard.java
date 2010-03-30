package control;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import UI.Panels;

public class SoftCard extends JFrame {
	int mode;
	public static final int PRE_AUTH = 0;
	public static final int AFTER_AUTH = 1;
	public static final int WAIT_REAUTH = 2;
	public boolean isPlug = false;
	JButton in;
	JButton out;
	public SoftCard(){
		initialize();
	}

	private void initialize() {
		ButtonAction ba = new ButtonAction();
		in = new JButton("plug in");
		in.setActionCommand("in");
		in.addActionListener(ba);
		
		out = new JButton("plug out");
		out.setActionCommand("out");
		out.addActionListener(ba);
		
		// TODO Auto-generated method stub
		this.getContentPane().setLayout(new FlowLayout());
		this.getContentPane().add(in);
		this.getContentPane().add(out);
		this.setSize(200, 200);
		this.setVisible(true);
	}
	class ButtonAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getActionCommand().equals("in")){
				//System.out.println("FUCK");
				isPlug = true;
			}
			else{
				//System.out.println("DAMN");
			
				isPlug = false;
			}
		}
		
	}
	
}
