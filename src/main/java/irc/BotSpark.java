package irc;

import java.io.IOException;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import config.BotConfig;
import config.UserConfig;


/**
 * The BotSpark class implements the Runnable interface and builds the configuration for the irc bot, 
 * as well as initiating it's processes.
 * 
 * @author Christopher Johnson
 *
 */
public class BotSpark implements Runnable{
	
	private Configuration config;		// The PircBotX's configuration
	private PircBotX bot;				// The PircBotX object
	private UserConfig user;			// The UserConfig object
	
	
	/**
	 * Constructor
	 * @param user A reference to the current UserConfig object.
	 */
	public BotSpark(UserConfig user) {
		this.user = user;
		buildIrcConfig();
		bot = new PircBotX(config);
	}

	
	/**
	 * The run method attempts to execute the PircBotX object's
	 * startBot method.
	 */
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			bot.startBot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// TODO More eloquent exception handling to be added in the future.
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			// TODO More eloquent exception handling to be added in the future.
			e.printStackTrace();
		}
	}
	
	
	/**
	 * the stop method executes the PircBotX object's close method.
	 */
	public void stop() {
		bot.close();
	}
	
	
	/**
	 * The buildIrcConfig method initializes a new Configuration object
	 * and builds it with data from the UserConfig object and the
	 * BotConfig class.
	 */
	private void buildIrcConfig() {
		config = new Configuration.Builder()
				.setAutoNickChange(false)
				.addServer(BotConfig.SERVER_NAME)
				.setName(BotConfig.NAME)
				.setServerPassword(BotConfig.PASSWORD)
				.addAutoJoinChannel("#" + user.getChannelName())
				.addListener(new CommandListener(user))
				.buildConfiguration();
	}

}
