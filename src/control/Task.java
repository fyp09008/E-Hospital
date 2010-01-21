package control;

import UI.MainFrame;

import com.ibm.jc.JCTerminal;
import com.ibm.jc.JCard;

import java.io.IOException;
import java.util.*;

import org.omg.CORBA.portable.IndirectionException;

import hku.hk.cs.javacard.*;

public class Task extends TimerTask{

	public static final int PRE_AUTH = 0;
	public static final int AFTER_AUTH = 1;
	public static final int WAIT_REAUTH = 2;
	private Timer t;
	private MainFrame mf;
	private int mode;
	public static final int PERIOD = 1000;
	
	public Task(Timer t, MainFrame mf, int mode) {
		super();
		this.t = t;
		this.mf = mf;
		this.mode = mode;
	}

	public Task(Timer t, MainFrame mf) {
		super();
		this.t = t;
		this.mf = mf;
	}

	/**
	 * @param t
	 */
	public Task(Timer t) {
		this.t = t;
	}

	public void run() {
		Date now = new Date(); 
		//byte [] a = {0x00, 0x02};
		//System.out.println("Generating report:"+now.getHours()+":"+now.getMinutes()+":"+ now.getSeconds());
        boolean haveCard = true;
        JavaCardManager jcm = null;
        
      //  terminal.PCSCJCTerminal.send(0,a,0,0);
        
		try{
		jcm = new JavaCardManager(JavaCardManager.JCM_PCSC, "ACS ACR38U 0", "285921800099");
	    }
		catch(Exception e) {
      			//System.err.println("Erorr!");
      			//cardOn(false);
      			haveCard = false;
	            switch (mode)
	            {
	            	case PRE_AUTH:
	    	            mf.loginPanel.disableAll();
	            		break;
	            	case AFTER_AUTH:
	    	            mf.changePanel(-2);
	            		break;
	            	case WAIT_REAUTH:
	            		break;
	            }
      	};
      	
      	
      	if (haveCard == true){
	      	//JavaCardManager jcm = new JavaCardManager(JavaCardManager.JCM_PCSC, "ACS ACR38U 0", "285921800099");
		    
	      	JCard c = jcm.getCard(); 
	      	
			byte [] h= {0x00, 0x01};
			
		//third changed from 0x01 to 0x00
			byte[] cardresponse = c.send(0, 0x13, 0x00, 0x00, 0x00, h.length, h, 0, 0x00);
			if(JavaCardHelper.checkStatusWord(cardresponse)){
	            //System.err.println("OK!");
	            switch (mode)
	            {
	            	case PRE_AUTH:
	    	            mf.loginPanel.enableAll();
	            		break;
	            	case AFTER_AUTH:
	            		break;
	            	case WAIT_REAUTH:
	            		mf.getClient().re_login();
	            		break;
	            }
			}
	         else {
	        	//System.err.println("Erorr!");
	            switch (mode)
	            {
	            	case PRE_AUTH:
	    	            mf.loginPanel.disableAll();
	            		break;
	            	case AFTER_AUTH:
	    	            mf.changePanel(-2);
	            		break;
	            	case WAIT_REAUTH:
	            		break;
	            }
	         }
      	}     
     }
	
}

