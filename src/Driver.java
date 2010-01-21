

import java.util.Date;
import java.util.Timer;

import control.*;
import UI.*;

public class Driver {
	public static void main(String[] arg) {
		Client client = Client.getInstance();
		Timer t = new Timer();
		Date now = new Date();
		//client.setT(t);
		MainFrame mf = new MainFrame();
		//mf.loginPanel.enableAll();
		Task task = new Task(t, mf, Task.PRE_AUTH);
		client.setMf(mf);
		t.schedule(task, now, 2000);
		
	}
}
