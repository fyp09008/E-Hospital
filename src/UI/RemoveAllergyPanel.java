package UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import control.Client;
public class RemoveAllergyPanel extends Panels {
	Vector<String> allergyName;
	Vector<String> description;
	Vector<String> allergyID;
	Vector<JCheckBox> boxes;
	JScrollPane scrollPane;
	public RemoveAllergyPanel(String pid,Vector<String> id,Vector<String> name,
			Vector<String> de){
		super();
		this.setID(pid);
		allergyName = name;
		description = de;
		allergyID = id;
		boxes = new Vector<JCheckBox>();
		initialize();
	}
	private void initialize(){
		
		String[] tables = {"allergy","`dia-allergy_rec`"};
		String[] fields = {"allergy_id","name","description"};
		String where = "allergy.id = `dia-allergy_rec`.allergy_id and `dia-allergy_rec`.valid = 1 and" +
				"`dia-allergy_rec`.pat_id = " + getID();
		String[][] result = Client.getInstance().sendQuery("SELECT", tables, fields, where , null);
		GridLayout gl = new GridLayout(1,1);
		this.setLayout(gl);

		for(int i = 0;i < allergyID.size(); i++){
			JPanel p = new JPanel();
			JLabel name = new JLabel(allergyName.get(i));
			name.setFont(new Font("Helvetica",Font.BOLD,15));
			name.setToolTipText(result[i][2]);
			p.add(name);
			JLabel warn = new JLabel(" check to remove this allergy ");
			warn.setFont(new Font("Helvetica",Font.BOLD,10));
			warn.setBackground(Color.red);
			p.add(warn);
			JCheckBox jb = new JCheckBox();
			jb.setSelected(false);
			boxes.add(jb);
			p.add(jb);
		 	this.add(p);
		 	gl.setRows(gl.getRows()+1);
		}
		JButton button = new JButton("delete");
		button.addActionListener(new ButtonAction());
		this.add(button);
		
	}
	class ButtonAction implements ActionListener{
		String ids = null;
		public void actionPerformed(ActionEvent arg0) {
			for(int i = 0; i < boxes.size(); i++){
				if (  boxes.get(i).isSelected()){
					if ( ids == null){
						ids = new String("'"+allergyID.get(i)+"'");
					}
					else{
						ids = ids + ", '" + allergyID.get(i)+"'";
					}
				}
			}
			String[] table = {"`dia-allergy_rec`"};
			String[] field = {"valid"};
			String where = "`dia-allergy_rec`.allergy_id IN (" + ids + " )";
			String[] value = {"0"};
			Client.getInstance().sendQuery("UPDATE", table, field, where, value );
		}
	}
}
