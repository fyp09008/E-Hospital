package UI;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;

public class AddAllergyPanel extends Panels {
	Vector<String> allergyName;
	Vector<String> description;
	Vector<String> allergyID;
	
	public AddAllergyPanel(String id, Vector<String>name){
		super();
		allergyName = name;
		this.setID(id);
		this.add(new AllergyComboBox(allergyName));
		initialize();
	}
	private void initialize(){
		JComboBox combo = new JComboBox();
		//combo.add(allergyName);
	}
}	
