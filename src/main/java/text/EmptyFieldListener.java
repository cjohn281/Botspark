package text;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * EmptyFieldListener implements DocumentListener and
 * disables a button when a text field contains an empty
 * string.
 * 
 * @author Christopher Johnson
 * 
 */
public class EmptyFieldListener implements DocumentListener {
	
	private JTextField textField;		// To reference the text field passed in to the constructor
	private JButton button;				// To reference the button passed in to the constructor
	
	
	/**
	 * Constructor
	 * @param textField The text field to be checked for an empty string.
	 * @param button The button to be disabled if the text field is set to an empty string
	 */
	public EmptyFieldListener(JTextField textField, JButton button) {
		this.textField = textField;
		this.button = button;
	}

	
	/**
	 * All changes to the document should invoke the checkEmpty method
	 * to determine if the referenced button should be disabled.
	 */
	public void insertUpdate(DocumentEvent e) {
		checkEmpty();
	}

	public void removeUpdate(DocumentEvent e) {
		checkEmpty();
	}

	public void changedUpdate(DocumentEvent e) {
		checkEmpty();
	}
	
	
	/**
	 * The checkEmpty method determines the contents of the text field
	 * and enables or disables the button accordingly.
	 */
	private void checkEmpty() {
		if (textField.getText().equals(""))
			button.setEnabled(false);
		else
			button.setEnabled(true);
	}
	
}