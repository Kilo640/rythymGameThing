import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Settings {
	public static final int NUM_CONTROLS = 8;
	public static final int ESCAPE = 0, SETTINGS_MENU = 0;
	public static final int START = 1, KEY_BIND = 1;
	public static final int UP = 2, CHANGE_OPTION = 2;
	public static final int DOWN = 3;
	public static final int LEFT_ARROW = 4;
	public static final int DOWN_ARROW = 5;
	public static final int UP_ARROW = 6;
	public static final int RIGHT_ARROW = 7;
	public static final int SCROLL_SPEED = 8;
	public static final int SCROLL_DIR = 9;
	public static final int OFFSET = 10;
	public static final int RESET = 11;
	public static final int SAVE = 12;
	
	private PrintWriter settingsWriter;
	private Scanner settingsFile;
	private Controller controller;
	public TextDrawer writer = new TextDrawer();
	public int scrollSpeed;
	public int offset;
	public boolean upscroll = false;
	public int selectedOption = 0;
	public int state = 0;
	public String[] keys = new String[NUM_CONTROLS];
	public String[] keyNames = {"Exit", "Start", "Up Selection", "Down Selection", "Left Arrow",
			"Down Arrow", "Up Arrow", "Right Arrow"};
	public String[] options = {"", "", "", "", "", "", "", "", "", "", "", "Reset to default", 
			"Save and Exit to Menu"};
	
	
	public Settings() {
		controller = Main.game.controller;
		setSettings();
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
		keys[ESCAPE] = key;
		controller.escape = findKey(key);
		
		while(!str.equals("Start_button:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		keys[START] = key;
		controller.start = findKey(key);
		
		while(!str.equals("Up_Selection:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		keys[UP] = key;
		controller.up = findKey(key);
		
		while(!str.equals("Down_Selection:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		keys[DOWN] = key;
		controller.down = findKey(key);
		
		while(!str.equals("Left_arrow:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		keys[LEFT_ARROW] = key;
		controller.leftArrow = findKey(key);
		
		while(!str.equals("Down_arrow:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		keys[DOWN_ARROW] = key;
		controller.downArrow = findKey(key);
		
		while(!str.equals("Up_arrow:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		keys[UP_ARROW] = key;
		controller.upArrow = findKey(key);
		
		while(!str.equals("Right_arrow:")) {
			str = settingsFile.next();
		}
		key = settingsFile.next();
		keys[RIGHT_ARROW] = key;
		controller.rightArrow = findKey(key);
		
		//GAMEPLAY SETTINGS
		while(!str.contains("GAMEPLAY")) {
			str = settingsFile.nextLine();
		}
		
		while(!str.equals("Scroll_speed:")) {
			str = settingsFile.next();
		}
		scrollSpeed = Integer.parseInt(settingsFile.next());
		
		while(!str.equals("Scroll_direction:")) {
			str = settingsFile.next();
		}
		str = settingsFile.next();
		if(str.equalsIgnoreCase("upscroll")) {upscroll = true;}
		else {upscroll = false;} //!upscroll => downscroll
		
		while(!str.equals("Offset(ms):")) {
			str = settingsFile.next();
		}
		offset = Integer.parseInt(settingsFile.next());
	}
	
	public void update() {
		options[ESCAPE] =       "Exit:         " + keys[ESCAPE];
		options[START] =        "Start:        " + keys[START];
		options[UP] =           "Up Select:    " + keys[UP];
		options[DOWN] =         "Down Select:  " + keys[DOWN];
		options[LEFT_ARROW] =   "Left Arrow:   " + keys[LEFT_ARROW];
		options[DOWN_ARROW] =   "Down Arrow:   " + keys[DOWN_ARROW];
		options[UP_ARROW] =     "Up Arrow:     " + keys[UP_ARROW];
		options[RIGHT_ARROW] =  "Right Arrow:  " + keys[RIGHT_ARROW];
		options[SCROLL_SPEED] = "Scroll Speed: " + scrollSpeed;
		options[SCROLL_DIR] =   "Scroll Dir:   " + (upscroll ? "Upscroll" : "Downscroll");
		options[OFFSET] =       "Offset:       " + offset;
		
		switch(state) {
			case SETTINGS_MENU:
				if(Main.game.menu.downThisFrame && !Main.game.menu.downHeldLast) {
					selectedOption++;
					if(selectedOption >= options.length) {selectedOption = 0;}
				}

				if(Main.game.menu.upThisFrame && !Main.game.menu.upHeldLast) {
					selectedOption--;
					if(selectedOption < 0) {selectedOption = options.length - 1;}
				}
		
				if(Main.game.menu.startThisFrame && !Main.game.menu.startHeldLast) {
					if(selectedOption >= ESCAPE && selectedOption <= RIGHT_ARROW) {
						state = KEY_BIND;
					}else if(selectedOption >= SCROLL_SPEED && selectedOption <= OFFSET) {
						state = CHANGE_OPTION;
					}else if(selectedOption == RESET) {
						keys[ESCAPE] = "ESCAPE";
						keys[START] = "SPACE";
						keys[UP] = "UP";
						keys[DOWN] = "DOWN";
						keys[LEFT_ARROW] = "D";
						keys[DOWN_ARROW] = "F";
						keys[UP_ARROW] = "J";
						keys[RIGHT_ARROW] = "K";
						scrollSpeed = 75;
						offset = 0;
					}else if(selectedOption == SAVE){
						Main.game.menu.menuState = Menu.START_SCREEN;
						updateConfig();
						setSettings();
					}
				}
				
				if(Main.game.menu.escapeThisFrame && !Main.game.menu.escapeHeldLast){
					Main.game.menu.menuState = Menu.START_SCREEN;
					setSettings();
				}
				break;
			case KEY_BIND:
				break;
			case CHANGE_OPTION:
				if(selectedOption == SCROLL_SPEED) {
					if(Main.game.menu.upThisFrame && !Main.game.menu.upHeldLast) {
						scrollSpeed++;
					}

					if(Main.game.menu.downThisFrame && !Main.game.menu.downHeldLast) {
						scrollSpeed--;
						if(scrollSpeed < 0) {scrollSpeed = 0;}
					}
				}
				
				if(selectedOption == SCROLL_DIR) {
					if((Main.game.menu.upThisFrame && !Main.game.menu.upHeldLast) ||
							(Main.game.menu.downThisFrame && !Main.game.menu.downHeldLast)) {
						upscroll = !upscroll;
					}
				}
				
				if(selectedOption == OFFSET) {
					if(Main.game.menu.upThisFrame && !Main.game.menu.upHeldLast) {
						offset++;
					}

					if(Main.game.menu.downThisFrame && !Main.game.menu.downHeldLast) {
						offset--;
					}
				}
				
				if(Main.game.menu.escapeThisFrame && !Main.game.menu.escapeHeldLast) {
					state = SETTINGS_MENU;
				}
				break;
		}
		
	}
	
	public void draw(Graphics2D g2d) {
		switch(state) {
			case SETTINGS_MENU:
				drawMenu(g2d);
				break;
			case KEY_BIND:
				writer.draw("Press Desired Key for:", 1, 5, 5, g2d);
				writer.draw(keyNames[selectedOption] + " Key", 1, 5, 75, g2d);
				break;
			case CHANGE_OPTION:
				drawMenu(g2d);
		}
	}
	
	private void drawMenu(Graphics2D g2d) {
		writer.draw("Press Exit to exit without saving", 
				0.75, 5, 5, g2d);

		for(int i = 0; i < options.length; i++) {
			if((i - selectedOption) >= -1) {
				writer.draw(options[i], 1, 25, 155 + (i - selectedOption) * 75, g2d);
			}
		}
		
		writer.draw(".", 2, -30, 95, g2d);
	}
	
	private int findKey(String key) {
		if(key.length() == 1) {
			return (int)key.charAt(0);
		}else {
			return findOtherKey(key);
		}
	}
	
	public String findKeyInv(int key) {
		if(key <= 'Z' && key >= 'A') {
			return "" + (char)key;
		}else {
			return findOtherKeyInv(key);
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
	
	public String findOtherKeyInv(int key) {
		switch(key) {
		case KeyEvent.VK_ESCAPE:
			return "ESCAPE";
		case KeyEvent.VK_SPACE:
			return "SPACE";
		case KeyEvent.VK_LEFT:
			return "LEFT";
		case KeyEvent.VK_RIGHT:
			return "RIGHT";
		case KeyEvent.VK_UP:
			return "UP";
		case KeyEvent.VK_DOWN:
			return "DOWN";
		case KeyEvent.VK_BACK_SPACE:
			return "BACKSPACE";
		case KeyEvent.VK_QUOTE:
			return "QUOTE";
		case KeyEvent.VK_SEMICOLON:
			return "SEMICOLON";
		default:
			return "Invalid Key. See Config File";
		}
	}	
	
	public void updateConfig() {
		try {
			settingsWriter = new PrintWriter(new File("config/settings.cfg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		settingsWriter.println(
				"DO NOT ALTER THE STRUCTURE OF THIS FILE!!! DOING SO MAY BREAK THE GAME!!!\n"
				+ "!!!THIS INCLUDES THE DEFAULT VALUES!!!\n"
				+ "Thank you! :3\n"
				+ "\n"
				+ "INSTRUCTIONS:\n"
				+ "Type the key/value you want to use for each setting. If your key is not an alphnumeric\n"
				+ "character (ex: Spacebar, escape, arrow key, ';'), type out the key name.\n"
				+ "Examples include: \n"
				+ "\tLEFT/UP/DOWN/RIGHT\n"
				+ "\tSPACE\n"
				+ "\tESCAPE\n"
				+ "\tBACKSPACE\n"
				+ "\tSEMICOLON\n"
				+ "\tQUOTE\n"
				+ "Currently, these are the only keys of this type supported by the game, more may be\n"
				+ "added in future updates. \n"
				+ "Also, the game does not need to be restarted for changes\n"
				+ "to take effect, this file just has to be saved. :)\n"
				+ "\n"
				+ "CONTROLS: (Alphabetic characters must be uppercase)\n"
				+ "Escape_to_menu: " + keys[ESCAPE] + " (Default: ESCAPE)\n"
				+ "Start_button: " + keys[START] + " (Default: SPACE)\n"
				+ "Up_Selection: " + keys[UP] + " (Default: UP)\n"
				+ "Down_Selection: " + keys[DOWN] + " (Default: DOWN)\n"
				+ "Left_arrow: " + keys[LEFT_ARROW] + " (Default: D)\n"
				+ "Down_arrow: " + keys[DOWN_ARROW] + " (Default: F)\n"
				+ "Up_arrow: " + keys[UP_ARROW] + " (Default: J)\n"
				+ "Right_arrow: " + keys[RIGHT_ARROW] + " (Default: K)\n"
				+ "\n"
				+ "GAMEPLAY SETTINGS:\n"
				+ "Scroll_speed: " + scrollSpeed + " (Default: 75) (Note: Must be an integer, no decimal value)\n"
				+ "Scroll_direction: " + (upscroll ? "Upscroll" : "Downscroll") + " (Default: Upscroll)\n"
				+ "Offset(ms): " + offset + " (Default: 0) (Note: Must be an integer, no decimal value)"
				);
		
		settingsWriter.close();
	}
}
