import java.awt.Graphics2D;

public class ResultsScreen {
	private Level level;
	private TextDrawer writer = new TextDrawer();
	
	public ResultsScreen(Level level) {
		this.level = level;
	}
	
	public void draw(Graphics2D g2d) {
		writer.draw("Results:", 1, 0, 5, g2d);
		writer.draw(" Marvelous: " + level.judge.marvelous, 0.75, 0, 70, g2d);
		writer.draw(" Perfect:   " + level.judge.perfect, 0.75, 0, 120, g2d);
		writer.draw(" Great:     " + level.judge.great, 0.75, 0, 170, g2d);
		writer.draw(" Good:      " + level.judge.good, 0.75, 0, 220, g2d);
		writer.draw(" Ok:        " + level.judge.ok, 0.75, 0, 270, g2d);
		writer.draw(" Miss:      " + level.judge.miss, 0.75, 0, 320, g2d);
		
		if(level.judge.accuracy >= 66.95) {
			writer.draw("Accuracy: " + level.score.accuracyString + " Passed", 1, 20, 435, g2d);
		}
		else {writer.draw("Accuracy: " + level.score.accuracyString + " Failed", 1, 20, 435, g2d);}
		
		if(level.fc) {writer.draw("Combo:     " + level.judge.maxCombo + " (FC)", 1, 20, 510, g2d);}
		else if(level.sdcb) {writer.draw("Combo:     " + level.judge.maxCombo + " (SDCB)", 1, 20, 510, g2d);}
		else {writer.draw("Combo:     " + level.judge.maxCombo, 1, 20, 510, g2d);}
		
		writer.draw("Press " + Main.settings.keys[Settings.ESCAPE] + " to exit to menu", 0.75, 20, 580, g2d);
		
		g2d.drawImage(level.score.grade, 420, 50, 300, 300, null);
	}
}
