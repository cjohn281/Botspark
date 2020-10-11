package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import commands.UserCommand;
import config.UserConfig;
import text.CharLimit;


/**
 * The ConfigWindow class builds and displays the configuration window.
 * 
 * @author Christopher Johnson
 * 
 */
public class ConfigWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel channelPanel;					// To hold channel components
	private JPanel commandPanel;					// To hold JTable component
	private JPanel buttonPanel;						// To hold button components
	
	private JLabel channelLabel;					// Holds the channel name label
	private JTextField channelTextField;			// Allows user to change server channel
	private String channelNameText;					// Holds the channel name string
	
	private String[] commandNameArray;				// A string array of all command names
	private String[] commandDetailArray;			// A string array of all command details 
	private JList<String> commandList;				// Displays the user's commands
	private DefaultListModel<String> listModel;		// A list model for the commandList
	private JTextArea commandDetail;				// Displays the command's text
	private JScrollPane scrollList;					// A scroll pane to hold command list
	private JScrollPane scrollDetail;				// A Scroll pane to hold command text
	int index;										// Holds the index of the selected commandList item
	
	private JButton addBtn;							// Button to add command
	private JButton removeBtn;						// Button to remove command
	private JButton saveBtn;						// Button to save changes to commands
	
	private UserConfig user;						// Holds a reference to the UserConfig object
	private MainWindow parent;						// Holds a reference to the parent MainWindow object
	
	
	/**
	 * Constructor
	 * @param user The UserConfig object
	 * @param parent The parent MainWindow object
	 */
	public ConfigWindow(UserConfig user, MainWindow parent) {
		this.user = user;
		this.parent = parent;
		
		// Set this dialog to be a modal.
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		setTitle("Configuration");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		// Build the panels.
		buildChannelPanel();
		buildCommandPanel();
		buildButtonPanel();
		
		// Add panels to the content pane.
		add(channelPanel, BorderLayout.NORTH);
		add(commandPanel, BorderLayout.WEST);
		add(buttonPanel, BorderLayout.EAST);
		
		// Select the first commandList item by default after
		// all components have been initialized.
		commandList.setSelectedIndex(0);
		
		// Pack and display the window.
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	
	/**
	 * The buildChannelPanel method builds the panel containing the
	 * channel label and text field.
	 */
	private void buildChannelPanel() {
		channelNameText = user.getChannelName();
		
		// Build the panel and assign a GridLayout manager
		channelPanel = new JPanel();
		channelPanel.setLayout(new GridLayout(2, 1, 0, 5));
		channelPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));
		
		// Build the components
		channelLabel = new JLabel("Your Twitch username:");
		channelTextField = new JTextField(25);
		channelTextField.setDocument(new CharLimit(25, false));					// Set Max character limit to 25 characters.
		channelTextField.setText(channelNameText);
		
		// Add components to the panel.
		channelPanel.add(channelLabel);
		channelPanel.add(channelTextField);
	}
	
	
	/**
	 * The buildCommandPanel method builds the panel containing
	 * the user's commands.
	 */
	private void buildCommandPanel() {
		// Populate commandNameArray and commandDetailArray from the UserConfig object.
		populateArrays();
		
		// Build the panel.
		commandPanel = new JPanel();
		commandPanel.setBorder(BorderFactory.createCompoundBorder(
											BorderFactory.createEmptyBorder(0, 10, 10, 0),
											BorderFactory.createTitledBorder("Commands")));
		
		// Build components.
		listModel = new DefaultListModel<String>();
		commandList = new JList<String>(listModel);
		
		commandList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		commandList.setVisibleRowCount(10);
		commandList.setFixedCellWidth(150);
		commandList.addListSelectionListener(new CommandListListener());
		buildCommandList();
		
		commandDetail = new JTextArea(11, 25);
		commandDetail.setMargin(new Insets(2, 2, 2, 2));
		commandDetail.setLineWrap(true);
		commandDetail.setWrapStyleWord(true);
		commandDetail.setDocument(new CharLimit(500, true));		// Set Max character limit to 500 characters.
		commandDetail.setEditable(false);
		commandDetail.addMouseListener(new TextMouseListener());
		commandDetail.getDocument().addDocumentListener(new CommandDetailListener());
		
		// Build the scroll panes.
		scrollList = new JScrollPane(commandList);
		scrollList.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(0, 3, 0, 3)));
		scrollDetail = new JScrollPane(commandDetail);
		scrollDetail.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		

		// Add scroll panes to the panel.
		commandPanel.add(scrollList);
		commandPanel.add(scrollDetail);
		
	}
	
	
	/**
	 * The populateArrays method initializes the arrays used to maintain
	 * command data and populate the arrays with data from the UserConfig object.
	 */
	private void populateArrays() {
		commandNameArray = new String[user.getCommandList().size()];
		commandDetailArray = new String[user.getCommandList().size()];
		for (int i = 0; i < commandNameArray.length; i++) {
			commandNameArray[i] = user.getCommandName(i);
			commandDetailArray[i] = user.getCommandText(i);
		}
	}
	
	
	/**
	 * The buildButtonPanel method builds the panel containing
	 * the buttons.
	 */
	private void buildButtonPanel() {
		// Build the panel and assign a GridLayout manager.
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5, 1, 0, 5));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 11, 10));
		
		// Build the buttons
		addBtn = new JButton("Add");
		removeBtn = new JButton("Remove");
		removeBtn.setEnabled(false);
		saveBtn = new JButton("Save");
		
		// Create a ButtonListener and attach it to the buttons.
		ButtonListener btnListener = new ButtonListener(this);
		addBtn.addActionListener(btnListener);
		removeBtn.addActionListener(btnListener);
		saveBtn.addActionListener(btnListener);
		
		// Add the buttons to the panel.
		buttonPanel.add(addBtn);
		buttonPanel.add(removeBtn);
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(saveBtn);
	}
	
	
	/**
	 * The buildCommandList method updates the list of the commands
	 * in the commandList list.
	 */
	private void buildCommandList() {
		
		listModel.clear();
		for (int i = 0; i < commandNameArray.length; i++) {
			listModel.addElement(commandNameArray[i]);
		}
		
		updateDefaultCommandDetail();
	}
	
	
	/**
	 * The updateDefaultCommand method updates the command detail for the
	 * !commands command with a list of all other commands that the user
	 * has added.
	 */
	private void updateDefaultCommandDetail() {
		String newDetail = "";
		if (commandNameArray.length > 1) {
			for (int i = 1; i < commandNameArray.length; i++) {
				if (i == commandNameArray.length - 1)
					newDetail += commandNameArray[i];
				else
					newDetail += commandNameArray[i] + ", ";
			}
		}
		
		commandDetailArray[0] = newDetail;
	}
	
	
	/**
	 * The addCommand method adds the command name and detail text to their
	 * respective arrays.
	 */
	public void addCommand(String newName, String newDetail) {
		
		String newNameArr[] = new String[commandNameArray.length + 1];
		String newDetailArr[] = new String[commandDetailArray.length + 1];
		
		for (int i = 0; i < commandNameArray.length; i++) {
			newNameArr[i] = commandNameArray[i];
			newDetailArr[i] = commandDetailArray[i];
		}
		newNameArr[commandNameArray.length] = newName;
		newDetailArr[commandDetailArray.length] = newDetail;
		
		commandNameArray = newNameArr;
		commandDetailArray = newDetailArr;
		
		buildCommandList();
		commandList.setSelectedIndex(commandNameArray.length - 1);
	}
	
	
	/**
	 * The removeCommand method removes the command name and detail text
	 * from their respective arrays.
	 */
	private void removeCommand() {
		String newNameArr[] = new String[commandNameArray.length - 1];
		String newDetailArr[] = new String[commandDetailArray.length - 1];
		
		int skip = commandList.getSelectedIndex();
		int newArrIndex = 0;
		
		for (int i = 0; i < commandNameArray.length; i++) {
			if (i != skip) {
				newNameArr[newArrIndex] = commandNameArray[i];
				newDetailArr[newArrIndex] = commandDetailArray[i];
				newArrIndex++;
			}
		}
		
		commandNameArray = newNameArr;
		commandDetailArray = newDetailArr;
		buildCommandList();
		commandList.setSelectedIndex(0);
	}
	
	
	
	/*******************************************************************************************************************
	 * Private Inner classes are used to implement various listeners.
	 *******************************************************************************************************************/
	
	/**
	 * The private inner class CommandListListener handles list
	 * selection events
	 */
	private class CommandListListener implements ListSelectionListener {
		// If the first command is selected, set the command detail text area to not editable,
		// and disable the remove button.
		public void valueChanged(ListSelectionEvent e) {
			index = commandList.getSelectedIndex();
			if (index > 0) {
				commandDetail.setEditable(true);
				removeBtn.setEnabled(true);
			}
			else {
				commandDetail.setEditable(false);
				removeBtn.setEnabled(false);
			}
			
			// Only display command detail text if custom commands exist,
			// otherwise, inform the user there are no custom commands.
			if (index >= 0) {
				if (!commandDetailArray[index].equals(""))
					commandDetail.setText(commandDetailArray[index]);
				else {
					if (index == 0)
						commandDetail.setText("You have no custom commands yet.");
					else
						commandDetail.setText("Enter command text here.");
				}
			}
		}
	}
	
	
	/**
	 * The private class CommandDetailListener implements DocumentListener
	 * and detects changes made to the text in the commandDetail text area.
	 */
	private class CommandDetailListener implements DocumentListener {

		public void insertUpdate(DocumentEvent e) {
			updateText();			
		}

		public void removeUpdate(DocumentEvent e) {
			updateText();
		}

		public void changedUpdate(DocumentEvent e) {
			updateText();
		}
		
		private void updateText() {
			commandDetailArray[commandList.getSelectedIndex()] = commandDetail.getText();
		}
	}
	
	
	/**
	 * The private class ButtonListener implements ActionListener and
	 * performs actions based on which button is clicked.
	 */
	private class ButtonListener implements ActionListener {
		
		ConfigWindow thisDialog;
		
		public ButtonListener(ConfigWindow thisDialog) {
			this.thisDialog = thisDialog;
		}
		
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == addBtn) {
				commandList.clearSelection();
				new AddCommandWindow(thisDialog, commandNameArray);
			} else if (e.getSource() == removeBtn) {
				removeCommand();
			} else if (e.getSource() == saveBtn) {
				// Operations to be performed when the save button is click.
				saveOperations();
			}
		}
		
		
		/**
		 * The saveOperations method updates the UserConfig object
		 * with the new channel name and commandList.
		 */
		private void saveOperations() {
			
			String updatedChannelName;
			if (channelTextField.getText() != null)
				updatedChannelName = channelTextField.getText().toLowerCase();
			else
				updatedChannelName = "";
			
			ArrayList<UserCommand> newCmds = new ArrayList<UserCommand>();
			for (int i = 0; i < commandNameArray.length; i++) {
				newCmds.add(new UserCommand(commandNameArray[i], commandDetailArray[i]));
			}
			
			user.updateUserConfig(updatedChannelName, newCmds);
			
			// Set the channelField in the parent MainWindow object.
			parent.setChannelField(user.getChannelName());
			
			// Close the current dialog after saving.
			thisDialog.dispose();
		}
	}
	
	
	/**
	 * The TextMouseListener implements the MouseListener interface and
	 * clears default commandDetail text for new commands when the text
	 * area is clicked.
	 * @author Christopher Johnson
	 *
	 */
	private class TextMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (commandDetail.getText().equals("Enter command text here.")) {
				commandDetail.setText("");
			}
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
			// Not needed.
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
			// Not needed.
			
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
			// Not needed.
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
			// Not needed.
			
		}
	}
}
