//author chris
package ehospital.client.ui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import ehospital.client.control.Client;

/**
 * Panel for Adding Allergy
 * @author   Chun
 */
public class AddAllergyPanel extends Panels {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Vector<String> noAllergyID;
	Vector<String> noAllergyName;
	Vector<String> noDescription;
	/**
	 * @uml.property  name="box"
	 * @uml.associationEnd  
	 */
	AllergyComboBox box;
	JButton more;
	int boxCount;
	
	/**
	 * Only Constructor.
	 * @param pid
	 * @param id
	 * @param name
	 * @param de
	 */
	public AddAllergyPanel(String pid, Vector<String> id, Vector<String>name, Vector<String> de){
		super();
		boxCount = 0;
		noDescription = de;
		noAllergyID = id;
		noAllergyName = name;
		this.setID(pid);
		initialize();
	}
	/**
	 * Search and return the ID of the allergy
	 * @param allergy
	 * @return the ID of the allergy
	 */
	private String findID(String allergy){
		for(int i = 0;i < noAllergyName.size();i++){
			if ( noAllergyName.get(i).equals(allergy))
				return noAllergyID.get(i);
		}
		return null;
	}
	/**
	 * 
	 * @return true if successful
	 */
	public boolean submit(){
		String value = ((JTextComponent) box.getEditor().getEditorComponent()).getText();
		if (value.equals("")){
			System.out.println("no value for add");
			return true;
		}
		//String table[] = {"`dia-allergy_rec`"};
		//String fields[] = {"id","pat_id","allergy_id","valid"};
		//String values[] = {"null",getID(),findID(value),"1"};
		String sql = "INSERT INTO `dia-allergy_rec` (pat_id, allergy_id, valid) VALUES (?, ? , ?);";
		String[] param = {getID(),findID(value),"1"};
		boolean result2 = false;
		try {
			result2 = Client.getInstance().sendUpdate(sql, param);
		} catch (RemoteException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		} catch (NotBoundException e) {
			Client.getInstance().getLogger().debug(this.getClass().getName(), e.getMessage());
		}
		
		return result2;
			
	}
	
	/**
	 * Initialize UI.
	 */
	private void initialize(){
		box = new AllergyComboBox(noAllergyName);
		JLabel tag = new JLabel("Add this allergy: ");
		this.add(tag);
		this.add(box);
	}
}	
