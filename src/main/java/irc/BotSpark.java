package irc;

import java.io.IOException;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import config.BotConfig;
import config.UserConfig;

public class BotSpark implements Runnable{
	
	private Configuration config;
	private PircBotX bot;
	private UserConfig user;
	
	public BotSpark(UserConfig user) {
		this.user = user;
		buildIrcConfig();
		bot = new PircBotX(config);
	}

	public void run() {
		// TODO Auto-generated method stub
		
		try {
			bot.startBot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop() {
		bot.close();
	}
	
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
