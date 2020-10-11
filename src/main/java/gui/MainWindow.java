package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import config.UserConfig;
import irc.BotSpark;
import text.EmptyFieldListener;


/**
 * The MainWindow class is responsible for building a displaying the
 * primary window that the user will see when the program begins.
 * 
 * @author Christopher Johnson
 * 
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel messagePanel;				// A panel to hold a label and text field
	private JPanel buttonPanel;					// A panel to hold the Exit, Config, and Connect buttons
	
	private JLabel message;						// A label to display a message
	private JTextField channelField;			// Uneditable text field to display channel
	
	private JButton exitBtn;					// Exit Button
	private JButton configBtn;					// Config Button
	private JButton connectBtn;					// Connect Button
	
	private Boolean connected = false;			// Determines if user is attempting to connect to server
	private UserConfig user;					// Holds a reference to the UserConfig object
	private BotSpark bot;
	private Thread botThread;
	
	
	/**
	 * Constructor
	 * @param user A reference to the UserConfig object
	 */
	public MainWindow(UserConfig user) {
		this.user = user;
		
		setTitle("Botspark Twitch Bot");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Build the message and button panels.
		buildMessagePanel();
		buildButtonPanel();
		
		// Attach an EmptyFieldListener to the channel field after the
		// text field and buttons have been initialized.
		channelField.getDocument().addDocumentListener(new EmptyFieldListener(channelField, connectBtn));
		
		// Add the message and button panels to the content pane.
		add(messagePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		// Pack and display the window.
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	
	/**
	 * The buildMessagePanel method is responsible for building label and text field
	 * components and adding them to the messagePanel.
	 * @param user A reference to the UserConfig object
	 */
	private void buildMessagePanel() {
		// Build the panel, label, and text field components.
		// The components will be displayed with using a GridLayout
		// that has 2 rows and 1 column.
		messagePanel = new JPanel();
		messagePanel.setLayout(new GridLayout(2, 1));
		messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
		
		message = new JLabel("Connect to");
		
		channelField = new JTextField(25);
		setChannelField(user.getChannelName());
		channelField.setEditable(false);
		
		
		// Add components to the panel.
		messagePanel.add(message, SwingConstants.CENTER);
		messagePanel.add(channelField);
	}
	
	
	/**
	 * The buildButtonPanel method is responsible for building the button components
	 * and adding them to the buttonPanel.
	 */
	private void buildButtonPanel() {
		// Build the panel and set a GridLayout manager
		// with 1 row, 4 columns, and a 5px horizontal gap in between cells.
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 4, 5, 0));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
		// Build the button components.
		exitBtn = new JButton("Exit");
		configBtn = new JButton("Config");
		connectBtn = new JButton("Connect");
		
		// If UserConfig object has valid channel name in the channelField
		// text field, enable the connect button, otherwise disable it.
		if (channelField.getText().equals(""))
			connectBtn.setEnabled(false);
		else
			connectBtn.setEnabled(true);
		
		// Create a ButtonListener object.
		ButtonListener btnListener = new ButtonListener(this, bot);
		
		// Attach the ButtonListener to the buttons.
		exitBtn.addActionListener(btnListener);
		configBtn.addActionListener(btnListener);
		connectBtn.addActionListener(btnListener);
		
		// Add the buttons to the panel.
		buttonPanel.add(exitBtn);
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(configBtn);
		buttonPanel.add(connectBtn);
	}
	
	
	/**
	 * The setChannelField updates the contents of the channelField
	 * with the channelName of the UserConfiig object.
	 * @param str The new channel name String
	 */
	public void setChannelField(String str) {
		channelField.setText(str);
	}
	
	
	
	/*******************************************************************************************************************
	 * Private Inner classes are used to implement various listeners.
	 *******************************************************************************************************************/
	
	/**
	 * ButtonListener implements ActionListener and handles events
	 * triggered by the Exit, Config, and Connect buttons.
	 * 
	 * @author Christopher Johnson
	 */
	private class ButtonListener implements ActionListener {
		
		private MainWindow currentWindow;		// References the MainWindow object
		
		
		/**
		 * Constructor
		 * @param mw The current MainWindow object
		 */
		public ButtonListener(MainWindow mw, BotSpark bot) {
			this.currentWindow = mw;
		}
		
		public void actionPerformed(ActionEvent e) {
			// Determine which button was clicked.
			if (e.getSource() == exitBtn) {
				// If the Exit button is clicked, close the program.
				System.exit(0);
			}
			else if (e.getSource() == configBtn) {
				// Create a new ConfigWindow object and pass in the UserConfig object
				// and the current MainWindow object as arguments.
				new ConfigWindow(user, currentWindow);
			} else if (e.getSource() == connectBtn) {
				connected = !connected;
				if (connected) {
					// Disable the Config button if the application is connected
					// to the irc server.
					configBtn.setEnabled(false);
					connectBtn.setText("Stop");
					
					bot = new BotSpark(user);
					botThread = new Thread(bot);
					botThread.start();
				} else {
					// Enable the Config button if the application is not
					// connected to the irc server.
					configBtn.setEnabled(true);
					connectBtn.setText("Connect");
					bot.stop();
					botThread = null;
				}
			}
		}
	}
}
