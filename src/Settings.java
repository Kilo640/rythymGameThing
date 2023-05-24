import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.event.KeyEvent;

public class Settings {
	private Scanner settingsFile;
	private Controller controller;
	public int scrollSpeed;
	public String levelName = "testLevel";
	
	public Settings() {
		controller = Main.game.controller;
	}
	
	public void setSettings() {
		try {
			settingsFile = new Scanner(new File("config/settings.cfg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String str = settingsFile.nextLine(); //Generic ahh string.
		String key = "";
		//CONTROLS
		while(!str.contains("CONTROLS")) {
			str = settingsFile.nextLine();
		}
		
		while(!str.equals("Escape_to_menu:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		controller.escape = findKey(key);
		
		while(!str.equals("Start_button:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		controller.start = findKey(key);
		
		while(!str.equals("Left_arrow:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		controller.leftArrow = findKey(key);
		
		while(!str.equals("Down_arrow:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		controller.downArrow = findKey(key);
		
		while(!str.equals("Up_arrow:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		controller.upArrow = findKey(key);
		
		while(!str.equals("Right_arrow:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		controller.rightArrow = findKey(key);
		//GAMEPLAY SETTINGS
		while(!str.contains("GAMEPLAY")) {
			str = settingsFile.nextLine();
		}
		
		while(!str.equals("Scroll_speed:")) {
			str = settingsFile.next();
		}
		scrollSpeed = Integer.parseInt(settingsFile.next());
		//CURRENT LEVEL
		while(!str.contains("LEVEL")) {
			str = settingsFile.nextLine();
		}
		
		while(!str.equals("Level_name:")) {
			str = settingsFile.next();
		}
		str = settingsFile.next();
		levelName = str.substring(1, str.length() - 1);
	}
	
	private int findKey(String key) {
		if(key.length() == 1) {
			return (int)key.charAt(0);
		}else {
			return findOtherKey(key);
		}
	}
	
	private int findOtherKey(String key) {
		switch(key) {
		case "ESCAPE":
			return KeyEvent.VK_ESCAPE;
		case "SPACE":
			return KeyEvent.VK_SPACE;
		case "LEFT":
			return KeyEvent.VK_LEFT;
		case "RIGHT":
			return KeyEvent.VK_RIGHT;
		case "UP":
			return KeyEvent.VK_UP;
		case "DOWN":
			return KeyEvent.VK_DOWN;
		case "BACKSPACE":
			return KeyEvent.VK_BACK_SPACE;
		case "QUOTE":
			return KeyEvent.VK_QUOTE;
		case "SEMICOLON":
			return KeyEvent.VK_SEMICOLON;
		default:
			return 0;
		}
	}
}
