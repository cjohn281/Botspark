package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import text.CharLimit;
import text.EmptyFieldListener;

/**
 * The AddCommandWindow class builds and displays the window
 * that allows the user to add additional commands to the
 * UserConfig object
 * 
 * @author Christopher Johnson
 * 
 */
public class AddCommandWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private ConfigWindow parent;		// Reference to the parent ConfigWindow object
	private String[] commands;			// Reference to the commands array from the parent ConfigWindow object

	private JLabel message;				// Holds a message to the user
	
	private JPanel commandPanel;		// Panel for text field and button
	public JTextField newCommand;		// For user to input name of new command
	public JButton addBtn;				// Button to add the new command to the command list
	
	
	/**
	 * Constructor
	 * @param parent A reference to the parent ConfigWindow object
	 * @param commands A reference to the parent's commands array
	 */
	public AddCommandWindow(ConfigWindow parent, String[] commands) {
		this.parent = parent;
		this.commands = commands;
		
		// Set this dialog to be a modal.
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		
		setTitle("Add Command");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		// Build label and add it to the content pane.
		message = new JLabel("Enter name of new command");
		message.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
		
		// Build the commandPanel.
		buildCommandPanel();
		
		// Add components to the content pane.
		add(message, BorderLayout.NORTH);
		add(commandPanel, BorderLayout.CENTER);
		
		// Pack and display the window.
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	
	/**
	 * The buildCommandPanel method builds the panel containing the text
	 * field and Add button.
	 */
	private void buildCommandPanel() {
		// Build components.
		commandPanel = new JPanel();
		newCommand = new JTextField(25);
		newCommand.setDocument(new CharLimit(15, false));
		addBtn = new JButton("Add");
		addBtn.setEnabled(false);
		
		// Disable Add button if there is no text in the newCommand text field.
		newCommand.getDocument().addDocumentListener(new EmptyFieldListener(newCommand, addBtn));
		
		// Attach ButtonListener to the Add button component.
		addBtn.addActionListener(new ButtonListener(parent, this));
		
		// Add components to the panel.
		commandPanel.add(newCommand);
		commandPanel.add(addBtn);
		
		// Give focus to the Add button.
		getRootPane().setDefaultButton(addBtn);
		addBtn.requestFocus();
	}
	
	
	/**
	 * The buildCommandName method formats the user input to be stored in the parent
	 * ConfigWindow object's commands array.
	 * @param str The user input String
	 * @return The formatted version of the user input String
	 */
	private String buildCommandName(String str) {
		// Only add '!' if the user did not include it.
		if (str.charAt(0) == '!')
			return str.toLowerCase();
		else
			return "!" + str.toLowerCase();
	}
	
	
	
	/*******************************************************************************************************************
	 * Private Inner classes are used to implement various listeners.
	 *******************************************************************************************************************/
	
	
	/**
	 * The ButtonListener class implements the ActionListener interface and handles
	 * the Add button click.
	 * 
	 * @author Christopher Johnson
	 *
	 */
	private class ButtonListener implements ActionListener {
		ConfigWindow parent;				// Reference to the parent ConfigWindow object.
		JDialog thisWindow;					// Reference to this JDialog object.
		
		
		/**
		 * Constructor
		 * @param parent The parent ConfigWindow object
		 * @param thisWindow The current JDialog object
		 */
		public ButtonListener(ConfigWindow parent, JDialog thisWindow) {
			this.parent = parent;
			this.thisWindow = thisWindow;
		}
		
		
		public void actionPerformed(ActionEvent e) {
			// Check the commands array to see if the new command already exists.
			boolean alreadyExists = false;
			String cmd = buildCommandName(newCommand.getText());
			
			for (int i = 0; i < commands.length; i++) {
				// If the new command already exists in the commands array,
				// set alreadyExists to true and break out of the for loop.
				if (cmd.equals(commands[i])) {
					alreadyExists = true;
					break;
				}
			}
			
			// If alreadyExists is true, display a message telling the user that the new
			// command already exists.  Otherwise, add the new command to the commands
			// array.
			if (alreadyExists) {
				JOptionPane.showMessageDialog(thisWindow, "That command already exists.");
				newCommand.setText("");
			} else {
				parent.addCommand(cmd, "Enter command text here.");
				thisWindow.dispose();
			}
		}
	}
}
