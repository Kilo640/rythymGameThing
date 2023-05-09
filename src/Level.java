import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Level {
	public long startTime;
	private GamePanel gp;
	private Scanner levelLoader;
	private AudioInputStream audio;
	private Clip clip;
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
		judge = new Judge(this);
		score =  new ScoreUI(6 * gp.TILE_SIZE - SHIFT - 6, gp.HEIGHT - gp.TILE_SIZE - 24, gp, this);
		grade = "F";
		combo = new ComboCounter(8 * gp.TILE_SIZE - SHIFT, 6 * gp.TILE_SIZE, gp);
		
		try {
			levelLoader = new Scanner(new File("resources/levels/" + levelName + "/chart.txt"));
			audio = AudioSystem.getAudioInputStream(new File("resources/levels/" + levelName + "/music.wav"));
			clip = AudioSystem.getClip();
			clip.open(audio);
		} catch (Exception e) {
			e.printStackTrace();
		}
		arrows = load();
		clip.start();
		startTime = System.currentTimeMillis();
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
		leftArrow.update();
		downArrow.update();
		upArrow.update();
		rightArrow.update();
		for(int i = arrows.size() - 1; i >= 0; i--) {
			arrows.get(i).update();
		}
		score.update(judge.accuracy);
		combo.update(judge.combo);
	}

	public void draw(Graphics2D g2d) {
		combo.draw(g2d);
		leftArrow.draw(g2d);
		downArrow.draw(g2d);
		upArrow.draw(g2d);
		rightArrow.draw(g2d);
		for(Arrow arrow : arrows) {arrow.draw(g2d);}
		score.draw(g2d);
	}
	

}
