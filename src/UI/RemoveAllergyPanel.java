//author chris
package UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import control.Client;
public class RemoveAllergyPanel extends Panels {
	Vector<String> hasAllergyName;
	Vector<String> description;
	Vector<String> allergyID;
	Vector<JCheckBox> boxes;
	JScrollPane scrollPane;
	public RemoveAllergyPanel(String pid,Vector<String> id,Vector<String> name,
			Vector<String> de){
		super();
		this.setID(pid);
		hasAllergyName = name;
		description = de;
		allergyID = id;
		boxes = new Vector<JCheckBox>();
		initialize();
	}
	public boolean submit(){
//		String ids = null;
//		for(int i = 0; i < boxes.size(); i++){
//			if (  boxes.get(i).isSelected()){
//				if ( ids == null){
//					ids = new String(allergyID.get(i));
//				}
//				else{
//					ids = ids + ", " + allergyID.get(i);
//				}
//			}
//		}
//		if ( ids == null){
//			System.out.println("no remove");
//			return true;
//		}
//		String[] table = {"`dia-allergy_rec`"};
//		String[] field = {"valid"};
//		String where = "`dia-allergy_rec`.allergy_id IN (" + ids + " )";
//		String[] value = {"0"};
//		String result2[][] = Client.getInstance().sendQuery("UPDATE", table, field, where, value );
		boolean result2 = false;
		try {
			String[] param = new String[boxes.size()]; 
			String sql = "UPDATE `dia-allergy_rec` SET valid=0 WHERE allergy_id IN (";
			for(int i = 0; i < boxes.size(); i++){
				if (i == 0)
					sql += "?";
				else sql += ", ?";
				param[i] = allergyID.get(i);
			}
			sql += ");";
			result2 = Client.getInstance().sendUpdate(sql, param);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (result2)
			return true;
		else
			return false;
	}
	private void initialize(){
		
		String[] tables = {"allergy","`dia-allergy_rec`"};
		String[] fields = {"allergy_id","name","description"};
		String where = "allergy.id = `dia-allergy_rec`.allergy_id and `dia-allergy_rec`.valid = 1 and" +
				"`dia-allergy_rec`.pat_id = " + getID();
		String[] param = {getID()};
//		String[][] result = Client.getInstance().sendQuery("SELECT", tables, fields, where , null);
		String[][] result = null;
		try {
			result = Client.getInstance().sendQuery("SELECT allergy_id, name, description" +
					" FROM allergy, `dia-allergy_rec` WHERE allergy.id=`dia-allergy_rec`.allergy_id AND valid=1 AND pat_id=?;", param);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GridLayout gl = new GridLayout(1,1);
		this.setLayout(gl);

		for(int i = 0;i < allergyID.size(); i++){
			JPanel p = new JPanel();
			JLabel name = new JLabel(hasAllergyName.get(i));
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
	}
	

}
