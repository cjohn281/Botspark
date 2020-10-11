package irc;

import java.util.ArrayList;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import commands.UserCommand;
import config.UserConfig;


/**
 * The CommandListener class extends ListenerAdapter and is responsible for reacting
 * to user messages posted in the Twitch irc channel.
 * 
 * @author Christopher Johnson
 *
 */
public class CommandListener extends ListenerAdapter {
	
	ArrayList<UserCommand> commands;		// The ArrayList of UserCommands
	
	
	/**
	 * Constructor
	 * @param user The current UserConfig object
	 */
	public CommandListener(UserConfig user) {
		this.commands = user.getCommandList();
	}
	
	
	/**
	 * The onGenericMessage method compares user messages in the Twitch irc
	 * channel and responds with the command text if the command name is detected.
	 */
	public void onGenericMessage(GenericMessageEvent event) {
		
		for (int i = 0; i < commands.size(); i++) {
			if (event.getMessage().equalsIgnoreCase(commands.get(i).getName())) {
				event.respondWith(commands.get(i).getText());
				// break out of the current iteration of the loop if the command has
				// already been detected and responded to.
				break;
			}
		}
	}
}
