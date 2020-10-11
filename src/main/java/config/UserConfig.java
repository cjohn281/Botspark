package config;

import commands.UserCommand;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * The UserConfig class holds the name of the user's Twitch channel,
 * as well as an ArrayList of UserCommand objects.
 * 
 * @author Christopher Johnson
 * 
 */
public class UserConfig implements Serializable {

	private static final long serialVersionUID = 3605381313235358341L;
	private String channelName;													// To hold the user's channel name
	private ArrayList<UserCommand> commands = new ArrayList<UserCommand>();		// To hold the user's commands
	
	
	/**
	 * Constructor
	 * @param channelName Sets the name of the channel.
	 */
	public UserConfig(String channelName) {
		this.channelName = channelName;
		
		// Create a new default UserCommand object and add
		// it the commands ArrayList.
		commands.add(new UserCommand("!commands", ""));
	}
	
	
	/**
	 * The getChannelName method returns the name of the channel.
	 * @return The name of the channel
	 */
	public String getChannelName() {
		return channelName;
	}
	
	
	/**
	 * The getCommandList method returns the ArrayList of UserCommand
	 * objects.
	 * @return The ArrayList of commands.
	 */
	public ArrayList<UserCommand> getCommandList() {
		return commands;
	}
	
	
	/**
	 * The getCommandName method returns the name of a command.
	 * @param index The command's index in the commands ArrayList
	 * @return The name of the command
	 */
	public String getCommandName(int index) {
		return commands.get(index).getName();
	}
	
	
	/**
	 * The getCommandText method returns the text of a command.
	 * @param index The command's index in the commands ArrayList
	 * @return The command's text
	 */
	public String getCommandText(int index) {
		return commands.get(index).getText();
	}
	
	
	/**
	 * The updateUserConfig method sets the channelName and commands fields
	 * to new values and writes the UserConfig object to a binary file.
	 * @param channelName The new channel name
	 * @param commands The new commands ArrayList
	 */
	public void updateUserConfig(String channelName, ArrayList<UserCommand> commands) {
		this.channelName = channelName;
		this.commands = commands;
		
		// Write this UserConfig object to a binary file. This will replace
		// the file if it already exists.
		writeToFile();
	}
	
	
	/**
	 * The writeToFile method writes the current state of the UserConfig object
	 * to a binary file.
	 */
	private void writeToFile() {		
		try {
			FileOutputStream outStream = new FileOutputStream("userconfig.dat");
			ObjectOutputStream output = new ObjectOutputStream(outStream);
			
			output.writeObject(this);
			
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// TODO More eloquent exception handling to be added in the future.
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// TODO More eloquent exception handling to be added in the future.
			e.printStackTrace();
		}
	}
}
