import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Level {
	public long startTime;
	public long endTime;
	public long levelTime;
	public GamePanel gp;
	private Scanner levelLoader;
	private AudioInputStream audio;
	private Clip music;
	private ArrayList<Arrow> arrows;
	public ArrowSensor leftArrow;
	public ArrowSensor downArrow;
	public ArrowSensor upArrow;
	public ArrowSensor rightArrow;
	public int numArrows;
	public double arrowHitWeight; //Judgment based "weight" of how many arrows are hit
	public Judge judge;
	public String grade;
	private ScoreUI score;
	private ComboCounter combo;
	public boolean playing;
	
	public final int SHIFT = 12;
	
	public Level(GamePanel gp, String levelName) {
		this.gp = gp;
		leftArrow = new ArrowSensor(5*gp.TILE_SIZE - SHIFT, gp.TILE_SIZE,
				gp, gp.controller, ArrowSensor.LEFT);
		downArrow = new ArrowSensor(7*gp.TILE_SIZE - SHIFT, gp.TILE_SIZE, gp, 
				gp.controller, ArrowSensor.DOWN);
		upArrow = new ArrowSensor(9*gp.TILE_SIZE - SHIFT, gp.TILE_SIZE, gp, 
				gp.controller, ArrowSensor.UP);
		rightArrow = new ArrowSensor(11*gp.TILE_SIZE - SHIFT, gp.TILE_SIZE, 
				gp, gp.controller, ArrowSensor.RIGHT);
		score =  new ScoreUI(6 * gp.TILE_SIZE - SHIFT - 6, gp.HEIGHT - gp.TILE_SIZE - 24, gp, this);
		grade = "F";
		combo = new ComboCounter(8 * gp.TILE_SIZE - SHIFT - 6, 6 * gp.TILE_SIZE, gp);
		judge = new Judge(7 * gp.TILE_SIZE - SHIFT - 6, combo.getY() + gp.TILE_SIZE + 12, this);
		try {
			levelLoader = new Scanner(new File("resources/levels/" + levelName + "/chart.txt"));
			audio = AudioSystem.getAudioInputStream(new File("resources/levels/" + levelName + "/music.wav"));
			music = AudioSystem.getClip();
			music.open(audio);
		} catch (Exception e) {
			e.printStackTrace();
		}
		arrows = load();
		startTime = System.currentTimeMillis();
		endTime = arrows.get(arrows.size() - 1).time + 2000;
		playing = true;
	}

	public ArrayList<Arrow> load(){
		ArrayList<Arrow> levelArrows = new ArrayList<Arrow>();

		while((levelLoader.hasNext())) {
			levelArrows.add(new Arrow(gp, levelLoader.nextInt(), levelLoader.nextInt(), this, levelArrows));
		}
		
		levelLoader.close();
		return levelArrows;
	}

	public void update() {
		levelTime = (int)(System.currentTimeMillis() - startTime - 2000);
		if(levelTime >= 0 && !music.isActive()) {music.start();}
		leftArrow.update();
		downArrow.update();
		upArrow.update();
		rightArrow.update();
		for(int i = arrows.size() - 1; i >= 0; i--) {arrows.get(i).update();}
		score.update(judge.accuracy);
		combo.update(judge.combo);
	}

	public void draw(Graphics2D g2d) {
		if(levelTime <= endTime && playing) {
			combo.draw(g2d);
			judge.draw(g2d);
			leftArrow.draw(g2d);
			downArrow.draw(g2d);
			upArrow.draw(g2d);
			rightArrow.draw(g2d);
			score.draw(g2d);
			for(Arrow arrow : arrows) {arrow.draw(g2d);}
		}else {
			exitToMenu();
		}
		
	}
	
	public void exitToMenu() {
		playing = false;
		music.stop();
		GameState.state = GameState.MENU;
	}
}
