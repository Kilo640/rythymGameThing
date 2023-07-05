import java.awt.Graphics2D;
import java.io.File;

public class Menu {
	public int menuState;
	public static final int START_SCREEN = 0;
	public static final int LEVEL_SELECTION = 1;
	public static final int OPTIONS = 2;
	public static final int EXIT = 3;
	
	public int selectedLevel = 0;
	public int menuOption = 1;
	public boolean quitOption = false;
	public String[] levelNames = {"bruh"};
	public File levelsFolder = new File("resources/levels");
	public TextDrawer writer = new TextDrawer();
	
	public boolean upHeldLast = false;
	public boolean downHeldLast = false;
	public boolean startHeldLast = false;
	public boolean escapeHeldLast = false;
	public boolean upThisFrame = false;
	public boolean downThisFrame = false;
	public boolean startThisFrame = false;
	public boolean escapeThisFrame = false;
	
	public Menu() {
		levelNames = levelsFolder.list();
	}
	
	public void update() {
		upHeldLast = upThisFrame;
		downHeldLast = downThisFrame;
		startHeldLast = startThisFrame;
		upThisFrame = Main.game.controller.upSelection;
		downThisFrame = Main.game.controller.downSelection;
		startThisFrame = Main.game.controller.startActive;
		
		switch(menuState) {
			case START_SCREEN:
				if(escapeThisFrame && !escapeHeldLast){
					menuState = EXIT;
					quitOption = false;
				}
				
				if(startThisFrame  && !startHeldLast) {
					menuState = menuOption;
					if(menuState == EXIT) {quitOption = false;}
				}
				
				if(downThisFrame && !downHeldLast) {
					menuOption++;
					if(menuOption >= EXIT) {menuOption = EXIT;}
				}
		
				if(upThisFrame && !upHeldLast) {
					menuOption--;
					if(menuOption < LEVEL_SELECTION) {menuOption = 1;}
				}
				break;
			case LEVEL_SELECTION:
				if(downThisFrame && !downHeldLast) {
					selectedLevel++;
					if(selectedLevel >= levelNames.length) {selectedLevel = levelNames.length - 1;}
				}
		
				if(upThisFrame && !upHeldLast) {
					selectedLevel--;
					if(selectedLevel < 0) {selectedLevel = 0;}
				}	
		
				Main.game.currentLevel = null;
				if(startThisFrame && !startHeldLast) {
					Main.settings.setSettings();
					GameState.state = GameState.PLAYING;
				}else if(escapeThisFrame && !escapeHeldLast){
					menuState = START_SCREEN;
				}
				break;
			case OPTIONS:
				if(escapeThisFrame && !escapeHeldLast){
					menuState = START_SCREEN;
				}
				break;
			case EXIT:
				
				if(escapeThisFrame && !escapeHeldLast){
					menuState = START_SCREEN;
				}
				
				if(((downThisFrame && !downHeldLast) || (upThisFrame && !upHeldLast)) && !quitOption) {
					quitOption = true;
				}else if(((downThisFrame && !downHeldLast) || (upThisFrame && !upHeldLast)) && quitOption) {
					quitOption = false;
				}
				
				if(startThisFrame && !startHeldLast) {
					if(!quitOption) {menuState = START_SCREEN;}
					else {System.exit(0);}
				}
				break;
		}
	}
	
	public void draw(Graphics2D g2d) {
		switch(menuState) {
		case START_SCREEN:
			writer.draw("Rave University", 1.5, 50, 100, g2d);
			writer.draw("Play", 1, 205, 260, g2d);
			writer.draw("Options", 1, 205, 260 + 75, g2d);
			writer.draw("Exit", 1, 205, 260 + 2 * 75, g2d);
			writer.draw(".", 2, 150, 200 + (menuOption - 1) * 75, g2d);
			break;
		case LEVEL_SELECTION:
			writer.draw("Level Select: Press " + Main.settings.startKey + " to start", 0.75, 5, 5, g2d);
			
			for(int i = 0; i < levelNames.length; i++) {
				if((i - selectedLevel) >= -1) {
					writer.draw(levelNames[i], 1, 25, 155 + (i - selectedLevel) * 75, g2d);
				}
			}
			
			writer.draw(".", 2, -30, 95, g2d);
			break;
		case OPTIONS:
			writer.draw("Options menu under", 1, 5, 5, g2d);
			writer.draw("consturuction", 1, 5, 75, g2d);
			break;
		case EXIT:
			writer.draw("Are you sure you want", 1, 70, 100, g2d);
			writer.draw("to quit", 1, 275, 175, g2d);
			writer.draw("Yes", 1, 205, 360, g2d);
			writer.draw("No", 1, 205, 435, g2d);
			
			if(quitOption) {writer.draw(".", 2, 150, 300, g2d);}
			else {writer.draw(".", 2, 150, 375, g2d);}
		}
	}
}
