package main;

import java.io.*;

import config.UserConfig;
import gui.MainWindow;


/**
 * 
 * @author Christopher Johnson
 *
 */
public class Init {

	private static UserConfig user;			// Reference to the UserConfig object

	
	/**
	 * The getUserConfig method creates the UserConfig object from a binary file.
	 * If the file does not exist or cannot be read, a new UserConfig object is
	 * created.
	 */
	private static void getUserConfig() {

		try {
			FileInputStream inStream = new FileInputStream("userconfig.dat");
			ObjectInputStream userInput = new ObjectInputStream(inStream);
			user = (UserConfig) userInput.readObject();
			userInput.close();
		} catch (FileNotFoundException e) {
			user = new UserConfig("");
		} catch (IOException e) {
			user = new UserConfig("");
		} catch (ClassNotFoundException e) {
			user = new UserConfig("");
		}
	}

	public static void main(String[] args) {

		getUserConfig();
		
		new MainWindow(user);
	}

}
