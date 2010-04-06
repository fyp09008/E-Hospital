//author chris
package UI;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class CurrentTime extends JPanel implements Runnable{

	
	JLabel time = new JLabel();
	public CurrentTime(){
		this.setBackground(Color.WHITE);
		this.add(time);
	}
	public void run() {
		while(true){
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			time.setText("Current Time: " + dateFormat.format(calendar.getTime()));
		}
	}

}
