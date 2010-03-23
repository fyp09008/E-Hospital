package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BottomPanel extends Panels{
	JLabel label;
	public BottomPanel(){
		super();
		initialize();
	}
	private void initialize(){
		this.setLayout(new FlowLayout());
		this.setBackground(Color.RED);
		this.setSize(20,20);
		this.setPreferredSize(new Dimension(20,20));
		this.setVisible(true);
		label = new JLabel("Status: ");
		this.add(label);
	}
	public void setStatus(String s){
		label.setText("Status: " + s);
		
	}
}
