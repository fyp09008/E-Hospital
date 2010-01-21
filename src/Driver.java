

import java.util.Date;
import java.util.Timer;

import control.*;
import UI.*;

public class Driver {
	public static void main(String[] arg) {
		//Client client = new Client();
		Timer t = new Timer();
		Date now = new Date();
		Client.getInstance().setT(t);
		MainFrame mf = new MainFrame();
		//mf.loginPanel.enableAll();
		Task task = new Task(t,  Task.PRE_AUTH);
		Client.getInstance().setMf(mf);
		t.schedule(task, now, 2000);
		
	}
}
