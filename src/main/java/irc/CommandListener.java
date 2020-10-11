package irc;

import java.util.ArrayList;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import commands.UserCommand;
import config.UserConfig;

public class CommandListener extends ListenerAdapter {
	
	ArrayList<UserCommand> commands;
	
	public CommandListener(UserConfig user) {
		this.commands = user.getCommandList();
	}
	
	public void onGenericMessage(GenericMessageEvent event) {
		
		for (int i = 0; i < commands.size(); i++) {
			if (event.getMessage().equalsIgnoreCase(commands.get(i).getName())) {
				event.respondWith(commands.get(i).getText());
				break;
			}
		}
	}
}
