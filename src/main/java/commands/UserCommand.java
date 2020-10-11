package commands;

import java.io.Serializable;


/**
 * The UserCommand class is used to create UserCommand objects that
 * will hold the name and text for each command that the user
 * adds to the program.  UserCommand objects will be stored in
 * an ArrayList as a field in the UserConfig class.
 * 
 * @author Christopher Johnson
 * 
 */
public class UserCommand implements Serializable {

	private static final long serialVersionUID = -925038591615228336L;
	private String name;			// To hold the name of the command
	private String text;			// To hold the text of the command
	
	
	/**
	 * Constructor
	 * @param name Creates the name of the command
	 * @param text Creates the text for the command
	 */
	public UserCommand(String name, String text) {
		// Format the contents of name.
		if(name.charAt(0) == '!')
			this.name = name;
		else
			this.name = "!" + name;
		
		this.text = text;
	}
	
	
	/**
	 * The getName method returns the String value of the name field.
	 * @return The name of the command
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * The getText method returns the String value of the text field.
	 * @return The command's text
	 */
	public String getText() {
		return text;
	}
	
	
	/**
	 * The setText method set the String value of the text field.
	 * @param text the new String to be store in the object's text field
	 */
	public void setText(String text) {
		this.text = text;
	}
}
