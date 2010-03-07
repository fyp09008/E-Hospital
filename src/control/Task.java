package control;

import UI.LoginDialog;

import com.ibm.jc.JCTerminal;
import com.ibm.jc.JCard;



import java.io.IOException;
import java.util.*;

import org.omg.CORBA.portable.IndirectionException;

import hku.hk.cs.javacard.*;


public class Task extends TimerTask{
	public LoginDialog  ld;
	public static final int PRE_AUTH = 0;
	public static final int AFTER_AUTH = 1;
	public static final int WAIT_REAUTH = 2;
	private Timer t;
	//private MainFrame mf;
	private int mode;
	public static final int PERIOD = 1000;
	
	public Task( int mode) {
		super();
		
		this.mode = mode;
	}

	public void run() {
		Date now = new Date(); 
        boolean haveCard = true;
        JavaCardManager jcm = null;
        
		try{
		jcm = new JavaCardManager(JavaCardManager.JCM_PCSC, "ACS ACR38U 0", "285921800099");
	    }
		catch(Exception e) {
      			haveCard = false;
	            switch (mode)
	            {
	            	case PRE_AUTH:{
	    	            Client.getInstance().getMf().loginPanel.disableAll();
	    	            if ( Client.getInstance().getMf().keyboard != null)
	    	            	Client.getInstance().getMf().keyboard.dispose();
	            		break;
	            	}
	            	case AFTER_AUTH:{
	            		Client.getInstance().getMf().changePanel(-2);
	            		break;
	            	}
	            	case WAIT_REAUTH:{
	            		if ( ld != null){
	            			ld.dispose();
	            			ld = null;
	            		}
	            		break;
	            	}
	            }
      	};

      	if (haveCard == true){
	      	JCard c = jcm.getCard(); 
	      	
			byte [] h= {0x00, 0x01};
			
			byte[] cardresponse = c.send(0, 0x13, 0x00, 0x00, 0x00, h.length, h, 0, 0x00);
			if(JavaCardHelper.checkStatusWord(cardresponse)){
	            switch (mode)
	            {
	            	case PRE_AUTH:{
	            		Client.getInstance().getMf().loginPanel.enableAll();
	            		//Client.getInstance().getMf().resumePopUp();
	            		break;
	            	}
	            	case AFTER_AUTH:{
	    	            if ( Client.getInstance().getMf().keyboard != null)
	    	            	Client.getInstance().getMf().keyboard.dispose();
	            		break;}
	            	case WAIT_REAUTH:{
	            		ld = new LoginDialog();
	            		//Client.getInstance().re_login();
	            		break;
	            	}
	            }
			}
	         else {
	            switch (mode)
	            {
	            	case PRE_AUTH:{
	            		Client.getInstance().getMf().loginPanel.disableAll();
	            		if ( Client.getInstance().getMf().keyboard != null)
	    	            	Client.getInstance().getMf().keyboard.dispose();
	            		break;
	            	}
	            	case AFTER_AUTH:{
	            		Client.getInstance().getMf().changePanel(-2);
	            		break;
	            	}
	            	case WAIT_REAUTH:{
	            		if ( ld != null){
	            			ld.dispose();
	            			ld = null;
	            		}
	            		break;
	            	}
	            }
	       }
	       
      	   
     }

	}
}



