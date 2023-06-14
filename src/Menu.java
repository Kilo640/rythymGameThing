import java.awt.Graphics2D;
import java.io.File;

public class Menu {
	public int menuState;
	public int selectedLevel = 0;
	public String[] levelNames = {"bruh"};
	public File levelsFolder= new File("resources/levels");
	public TextDrawer writer = new TextDrawer();
	
	public boolean upHeldLast = false;
	public boolean downHeldLast = false;
	public boolean upThisFrame = false;
	public boolean downThisFrame = false;
	
	public Menu() {
		levelNames = levelsFolder.list();
	}
	
	public void update() {
		upHeldLast = upThisFrame;
		downHeldLast = downThisFrame;
		upThisFrame = Main.game.controller.upSelection;
		downThisFrame = Main.game.controller.downSelection;
		
		if(downThisFrame && !downHeldLast) {
			selectedLevel++;
			if(selectedLevel >= levelNames.length) {selectedLevel = levelNames.length - 1;}
		}
		
		if(upThisFrame && !upHeldLast) {
			selectedLevel--;
			if(selectedLevel < 0) {selectedLevel = 0;}
		}		
		
		Main.game.currentLevel = null;
		if(Main.game.controller.startActive) {
			Main.settings.setSettings();
			GameState.state = GameState.PLAYING;
		}
	}
	
	public void draw(Graphics2D g2d) {
		writer.draw("Level Select: Press " + Main.settings.startKey + " to start", 0.75, 5, 5, g2d);
		
		for(int i = 0; i < levelNames.length; i++) {
			if((i - selectedLevel) >= -1) {
				writer.draw(levelNames[i], 1, 25, 155 + (i - selectedLevel) * 75, g2d);
			}
		}
		
		writer.draw(".", 2, -30, 95, g2d);
	}
}
