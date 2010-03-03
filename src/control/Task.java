package control;

import com.ibm.jc.JCTerminal;
import com.ibm.jc.JCard;



import java.io.IOException;
import java.util.*;

import org.omg.CORBA.portable.IndirectionException;

import hku.hk.cs.javacard.*;

/*public class Task extends TimerTask{
	
	public static final int PRE_AUTH = 0;
	public static final int AFTER_AUTH = 1;
	public static final int WAIT_REAUTH = 2;
	private int mode;
	public static final int PERIOD = 1000;
	
	public Task(int mode ) {
		super();
		this.mode = mode;
	}
	
	public void run(){
		System.out.println("## "+Thread.currentThread().getName());
		if (Client.getInstance().sc.isPlug){
            switch (mode)
            {
            	case PRE_AUTH:{
            		System.out.println("Card in before auth" + this.hashCode());
            		Client.getInstance().getMf().loginPanel.enableAll();
            		break;
            	}
            	case AFTER_AUTH:{
            		System.out.println("Card in after auth" + this.hashCode());
            		break;}
            	case WAIT_REAUTH:{
            		System.out.println("Card in while waiting reauth" + this.hashCode());
            		Client.getInstance().re_login();
            		break;
            	}
            }
		}
		else{
            switch (mode)
            {
            	case PRE_AUTH:{
            
            		System.out.println("No card before auth" + this.hashCode());
    	            Client.getInstance().getMf().loginPanel.disableAll();
            		break;
            	}
            	case AFTER_AUTH:{
            		System.out.println("No card after auth" + this.hashCode());
            		Client.getInstance().getMf().changePanel(-2);
            		//System.out.println("!!!");
            		break;
            	}
            	case WAIT_REAUTH:{
            		System.out.println("No card while waiting auth" + this.hashCode());
            	
            		break;
            	}
            }
		}
	}
}*/
public class Task extends TimerTask{

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
	            	case PRE_AUTH:{
	            		//Logger.println("No card before auth");
	    	            Client.getInstance().getMf().loginPanel.disableAll();
	            		break;
	            	}
	            	case AFTER_AUTH:{
	            		//Logger.println("No card after auth");
	            		Client.getInstance().getMf().changePanel(-2);
	            		break;
	            	}
	            	case WAIT_REAUTH:{
	            		//Logger.println("No card while waiting auth");
	            		break;
	            	}
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
	            	case PRE_AUTH:{
	            		//Logger.println("Card in before auth");
	            		Client.getInstance().getMf().loginPanel.enableAll();
	            		break;
	            	}
	            	case AFTER_AUTH:{
	            		//Logger.println("Card in after auth");
	            		break;}
	            	case WAIT_REAUTH:{
	            		//Logger.println("Card in while waiting reauth");
	            		Client.getInstance().re_login();
	            		break;
	            	}
	            }
			}
	         else {
	        	//System.err.println("Erorr!");
	            switch (mode)
	            {
	            	case PRE_AUTH:{
	            		//Logger.println("No card before auth 2");
	            		Client.getInstance().getMf().loginPanel.disableAll();
	            		break;
	            	}
	            	case AFTER_AUTH:{
	            		//Logger.println("No card after auth 2");
	            		Client.getInstance().getMf().changePanel(-2);
	            		break;
	            	}
	            	case WAIT_REAUTH:{
	            		//Logger.println("No card while waiting re-auth 2");
	            		break;
	            	}
	            }
	       }
	       
      	   
     }

	}
}



