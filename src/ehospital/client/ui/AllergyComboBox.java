
package ehospital.client.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.*;

/**
 * Combo Box for adding allergy.
 * @author Chun
 *
 */
public class AllergyComboBox extends JComboBox
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DefaultComboBoxModel model = null;
	public AllergyComboBox(Vector<String> allergies)
	{
		super(allergies);
		model = new DefaultComboBoxModel();
		for(int i = 0; i < allergies.size(); i++)
			model.addElement(allergies.get(i));
		this.addActionListener(new ComboBoxAction());
		setModel(model);
		setEditable(true);
		setEditor(new AutoCompleteEditor(this));
	}
	public class ComboBoxAction implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	AllergyComboBox cb = (AllergyComboBox)e.getSource();
	        String petName = (String)cb.getSelectedItem();
	        ((JTextComponent)cb.getEditor().getEditorComponent()).setText(petName);
	      
	       
	    }

	}
	public class AutoCompleteEditor extends BasicComboBoxEditor
	{
		private JTextField editor = null;
		public AutoCompleteEditor(JComboBox combo)
		{
			super();
			editor = new AutoCompleteEditorComponent(combo);
		}

		public Component getEditorComponent()
		{
			return editor;
		}
	}

	public class AutoCompleteEditorComponent extends JTextField
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		JComboBox combo = null;
		public AutoCompleteEditorComponent(JComboBox combo)
		{
			super();
			this.combo = combo;
		}

		protected Document createDefaultModel()
		{
			return new PlainDocument()
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				

				public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
				{
					if (str == null || str.length() == 0)
						return;
					int size = combo.getItemCount();
					String text = getText(0, getLength());
					for (int i = 0; i < size; i++)
					{
						String item = combo.getItemAt(i).toString();
						if (getLength() + str.length() > item.length())
							continue;
	
						if ((text + str).equalsIgnoreCase(item))
						{
							combo.setSelectedIndex(i);
							super.remove(0, getLength());
							super.insertString(0, item, a);
							return;
						}
						else if (item.substring(0, getLength() + str.length()).equalsIgnoreCase(text + str))
						{
							combo.setSelectedIndex(i);
							if (!combo.isPopupVisible())
									combo.setPopupVisible(true);
								super.remove(0, getLength());
								super.insertString(0, item, a);
								return;
						}
					}
				}
			};
		}
	}
}
