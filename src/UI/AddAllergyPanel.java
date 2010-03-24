//author chris
package UI;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import control.Client;

public class AddAllergyPanel extends Panels {
	Vector<String> noAllergyID;
	Vector<String> noAllergyName;
	Vector<String> noDescription;
	AllergyComboBox box;
	JButton more;
	int boxCount;
	
	public AddAllergyPanel(String pid, Vector<String> id, Vector<String>name, Vector<String> de){
		super();
		boxCount = 0;
		noDescription = de;
		noAllergyID = id;
		noAllergyName = name;
		this.setID(pid);
		initialize();
	}
	private String findID(String allergy){
		for(int i = 0;i < noAllergyName.size();i++){
			if ( noAllergyName.get(i).equals(allergy))
				return noAllergyID.get(i);
		}
		return null;
	}
	public boolean submit(){
		String value = ((JTextComponent) box.getEditor().getEditorComponent()).getText();
		if (value.equals("")){
			System.out.println("no value for add");
			return true;
		}
		String table[] = {"`dia-allergy_rec`"};
		String fields[] = {"id","pat_id","allergy_id","valid"};
		String values[] = {"null",getID(),findID(value),"1"};
		String[][] result2 = Client.getInstance().sendQuery("INSERT", table, fields, null, values);
		if ( result2[0][0].equals("true"))
			return true;
		else
			return false;
			
	}
	private void initialize(){
		box = new AllergyComboBox(noAllergyName);
		JLabel tag = new JLabel("Add this allergy: ");
		this.add(tag);
		this.add(box);
	}
}	
