package control;

import java.util.Date;
import java.util.Timer;


public class CardChecker {
	
	Timer t;
	
	public Timer getT() {
		
		return t;
	}

	public void setT(Timer t) {
		this.t = t;
	}
	
	public void card_unplug()
	{
		t.cancel();
		t = new Timer();
		t.schedule(new Task(t, mf, Task.WAIT_REAUTH), new Date(),Task.PERIOD);
	}


	
}
