package ehospital.client.control;



import hku.hk.cs.javacard.JavaCardHelper;
import hku.hk.cs.javacard.JavaCardManager;

import java.util.TimerTask;

import com.ibm.jc.JCard;

import ehospital.client.ui.LoginDialog;


/**
 * Class to check whether the card is plugged in. It is a TimerTask class. It will run periodically. 
 * @author  Chun
 */
public class Task extends TimerTask{
	/**
	 * @uml.property  name="ld"
	 * @uml.associationEnd  
	 */
	public LoginDialog  ld;
	public static final int PRE_AUTH = 0;
	public static final int AFTER_AUTH = 1;
	public static final int WAIT_REAUTH = 2;
	public static final int WAIT_SESSION = 3;
	private int mode;
	public static final int PERIOD = 1000;
	
	public Task( int mode) {
		super();
		
		this.mode = mode;
	}

	public void run() {
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
	            		break;
	            	}
	            	case AFTER_AUTH:{
	    	            if ( Client.getInstance().getMf().keyboard != null){
	    	            	Client.getInstance().getMf().keyboard.dispose();
	    	            }
	            		break;}
	            	case WAIT_REAUTH:{
	            		ld = new LoginDialog();
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



