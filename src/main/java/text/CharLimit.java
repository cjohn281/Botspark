package text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * The JTextLimit class inherits from PlainDocument and
 * is used to limit the number of characters that can be 
 * entered into the channel name text field and the command
 * detail text area.
 * 
 * @author Christopher Johnson
 * 
 */
public class CharLimit extends PlainDocument {

	private static final long serialVersionUID = 1L;
	private int limit;				// The maximum number of characters that can be entered
	private boolean allowSpace;		// Determines if whitespace characters are allowed
	
	
	/**
	 * Constructor
	 * @param limit The maximum character limit
	 * @param allowSpace Determines if whitespace characters are allowed
	 */
	public CharLimit(int limit, boolean allowSpace) {
		super();
		this.limit = limit;
		this.allowSpace = allowSpace;
	}
	
	
	/**
	 * The insertString method validates user input and uses it to replace the contents
	 * of a text field or text area component.
	 */
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		StringBuilder newStr = new StringBuilder();
		if (str == null)
			return;
		
		// Allow new characters or strings if the character limit has not been exceeded.
		if ((getLength() + str.length()) <= limit) {
			
			if (allowSpace) {
				// If whitespace is allowed, only filter tab and newline characters.
				if (str.charAt(0) != '\n' && str.charAt(0) != '\t')
					super.insertString(offset, str, attr);	
			} else {
				// If whitespace is not allowed, filter out all whitespace characters.
				for (int i = 0; i < str.length(); i++) {
					if (!Character.isWhitespace(str.charAt(i)) || Character.isDigit(str.charAt(i))) {
						newStr.append(str.charAt(i));
					}
				}
				super.insertString(offset, newStr.toString(), attr);
			}
		}
	}
}
