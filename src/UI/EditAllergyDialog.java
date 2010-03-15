package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import control.Client;

public class EditAllergyDialog extends Dialogs {
	
	Vector<String> allergyName;
	Vector<String> description;
	Vector<String> allergyID;
	
	public EditAllergyDialog(String pid){
		super();
		setID(pid);
		allergyName = new Vector<String>();
		allergyID = new Vector<String>();
		description = new Vector<String>();
		initialize();
	}
	protected void initialize(){

		String[] tables = {"allergy","`dia-allergy_rec`"};
		String[] fields = {"allergy_id","name","description"};
		String where = "allergy.id = `dia-allergy_rec`.allergy_id and `dia-allergy_rec`.valid = 1 and" +
				"`dia-allergy_rec`.pat_id = " + getID() + " ORDER BY name";
		String[][] result = Client.getInstance().sendQuery("SELECT", tables, fields, where , null);
		
		for(int i = 0;i < result.length; i++){
			allergyID.add(result[i][0]);
			allergyName.add(result[i][1]);
			description.add(result[i][2]);
		}
		this.setLayout(new GridLayout(2,1));
		AddAllergyPanel aap = new AddAllergyPanel(getID(),allergyName);
		JScrollPane scroll = new JScrollPane(aap);
		RemoveAllergyPanel rap = new RemoveAllergyPanel(getID(),allergyID,allergyName,description);
		JScrollPane scroll1 = new JScrollPane(rap);
		this.add(scroll);
		this.add(scroll1);
		this.setSize(300,400);
		this.setVisible(true);
	}
}
