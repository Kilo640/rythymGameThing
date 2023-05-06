import java.awt.Graphics2D;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Level {
	public long startTime;
	private GamePanel gp;
	private Scanner levelLoader;
	private URL soundFile;
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
	
	public Level(GamePanel gp, String levelName) {
		this.gp = gp;
		leftArrow = new ArrowSensor(5*gp.TILE_SIZE, gp.TILE_SIZE, gp, gp.controller, ArrowSensor.LEFT);
		downArrow = new ArrowSensor(7*gp.TILE_SIZE, gp.TILE_SIZE, gp, gp.controller, ArrowSensor.DOWN);
		upArrow = new ArrowSensor(9*gp.TILE_SIZE, gp.TILE_SIZE, gp, gp.controller, ArrowSensor.UP);
		rightArrow = new ArrowSensor(11*gp.TILE_SIZE, gp.TILE_SIZE, gp, gp.controller, ArrowSensor.RIGHT);
		judge = new Judge(this);
		
		try {
			InputStream is = getClass().getResourceAsStream("/levels/" + levelName + "/chart.txt");
			levelLoader = new Scanner(is);
			soundFile = getClass().getResource("/levels/" + levelName + "/music.wav");
			audio = AudioSystem.getAudioInputStream(soundFile);
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
		for(Arrow arrow : arrows) {
			arrow.update();
		}
	}

	public void draw(Graphics2D g2d) {
		leftArrow.draw(g2d);
		downArrow.draw(g2d);
		upArrow.draw(g2d);
		rightArrow.draw(g2d);
		for(Arrow arrow : arrows) {arrow.draw(g2d);}
	}
	
	public void printScore() {
		String grade = "";
		double accuracy = judge.accuracy;
		
		if(accuracy > 99.99) {
			grade = "SS+";
		}else if(accuracy >= 99) {
			grade = "SS";
		}else if(accuracy >= 97) {
			grade = "S+";
		}else if(accuracy >= 95) {
			grade = "S";
		}else if(accuracy >= 93) {
			grade = "A+";
		}else if(accuracy >= 87) {
			grade = "A";
		}else if(accuracy >= 83) {
			grade = "A-";
		}else if(accuracy >= 77) {
			grade = "B+";
		}else if(accuracy >= 73) {
			grade = "B";
		}else if(accuracy >= 67) {
			grade = "C+";
		}else if(accuracy >= 60) {
			grade = "C";
		}else if(accuracy >= 50){
			grade = "D";
		}else{
			grade = "F";
		}
		
		System.out.printf("%s! %.2f%% Accuracy (%s) %n", judge.judgment, accuracy, grade);
	}
}
