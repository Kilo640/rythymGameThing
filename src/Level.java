import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Level {
	public final int PLAYING = 0;
	public final int PAUSED = 1;
	
	public final int RESUME = 1;
	public final int RESTART = 2;
	public final int EXIT = 3;
	
	public final int SHIFT = 12;
	
	public long startTime;
	public long endTime;
	long pauseTime;
	long resumeTime;
	public long levelTime;
	public int levelState;
	public int pausedOption;
	public GamePanel gp;
	private Scanner levelLoader;
	private AudioInputStream audio;
	private Clip music;
	private ArrayList<Arrow> arrows;
	public ArrowSensor leftArrow;
	public ArrowSensor downArrow;
	public ArrowSensor upArrow;
	public ArrowSensor rightArrow;
	public int numHits;
	public boolean fc = false;
	public boolean sdcb = false;
	public boolean escapeThisFrame;
	public boolean escapeLastFrame;
	public boolean resuming;	
	private boolean startThisFrame;
	private boolean downThisFrame;
	private boolean upThisFrame;
	private boolean startHeldLast;
	private boolean downHeldLast;
	private boolean upHeldLast;
	public double arrowHitWeight; //Judgment based "weight" of how many arrows are hit
	public Judge judge;
	public int grade;
	public ScoreUI score;
	private ComboCounter combo;
	public TextDrawer writer;

	
	public Level(GamePanel gp, String levelName) {
		levelState = PLAYING;
		writer = new TextDrawer();
		int arrowY = gp.TILE_SIZE;
		int scoreY = gp.HEIGHT - gp.TILE_SIZE - 24;
		if(!gp.settings.upscroll) {
			arrowY = (int)(gp.HEIGHT - 2.5 * gp.TILE_SIZE);
			scoreY = gp.TILE_SIZE - 12;
		}
		
		this.gp = gp;
		leftArrow = new ArrowSensor(5*gp.TILE_SIZE - SHIFT, arrowY,
				gp, gp.controller, ArrowSensor.LEFT);
		downArrow = new ArrowSensor(7*gp.TILE_SIZE - SHIFT, arrowY, gp, 
				gp.controller, ArrowSensor.DOWN);
		upArrow = new ArrowSensor(9*gp.TILE_SIZE - SHIFT, arrowY, gp, 
				gp.controller, ArrowSensor.UP);
		rightArrow = new ArrowSensor(11*gp.TILE_SIZE - SHIFT, arrowY, 
				gp, gp.controller, ArrowSensor.RIGHT);
		score =  new ScoreUI(6 * gp.TILE_SIZE - SHIFT - 6, scoreY, gp, this);
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
		int maxEndTime = Integer.MIN_VALUE;
		
		for(Arrow arrow : arrows) {
			int endTime;
			
			if(arrow instanceof LongArrow) {
				endTime = ((LongArrow)arrow).endTime;
			}else {
				endTime = arrow.time;
			}
			
			if(maxEndTime < endTime) {maxEndTime = endTime;}
		}
		
		endTime = maxEndTime + 2000;
	}

	public ArrayList<Arrow> load(){
		levelLoader.nextLine();
		ArrayList<Arrow> levelArrows = new ArrayList<Arrow>();
		int laneIndex;
		int time;
		int duration;
		boolean longArrow;
		
		while((levelLoader.hasNext())) {
			laneIndex = levelLoader.nextInt();
			time = levelLoader.nextInt();
			longArrow = levelLoader.nextBoolean();
			
			if(longArrow) {
				duration = levelLoader.nextInt();
				levelArrows.add(new LongArrow(gp, laneIndex, time + gp.settings.offset,
						this, levelArrows, duration));
			}else {
				levelArrows.add(new Arrow(gp, laneIndex, time + gp.settings.offset, this, levelArrows));
			}
		}
		
		levelLoader.close();
		return levelArrows;
	}

	public void update() {
		escapeLastFrame = escapeThisFrame;
		escapeThisFrame = gp.controller.escapeActive;
		
		switch(levelState) {
			case PLAYING:
				if(gp.controller.escapeActive && !escapeLastFrame) {
					levelState = PAUSED;
					pausedOption = 1;
					pauseTime = System.currentTimeMillis();
				}
				
				levelTime = System.currentTimeMillis() - startTime - 2000;
				updatePlaying();
				break;
			case PAUSED:
				updatePaused();
				break;
		}
	}
	
	private void updatePlaying() {
		if(levelTime >= 0 && !music.isActive()) {music.start();}
		leftArrow.update();
		downArrow.update();
		upArrow.update();
		rightArrow.update();
		for(int i = arrows.size() - 1; i >= 0; i--) {arrows.get(i).update();}
		score.update(judge.accuracy);
		combo.update(judge.combo);
		
		if(levelTime > endTime) {endLevel(GameState.RESULTS);}
	}
	
	private void updatePaused() {
		music.stop();
		upHeldLast = upThisFrame;
		downHeldLast = downThisFrame;
		startHeldLast = startThisFrame;
		upThisFrame = Main.game.controller.upSelection;
		downThisFrame = Main.game.controller.downSelection;
		startThisFrame = Main.game.controller.startActive;
		
		if(startThisFrame  && !startHeldLast) {
			switch(pausedOption) {
				case RESUME:
					resume();
					break;
				case RESTART:
					levelState = PLAYING;
					restart();
					break;
				case EXIT:
					endLevel(GameState.MENU);
					break;
			}
		}
		
		if(downThisFrame && !downHeldLast) {
			pausedOption++;
			if(pausedOption > EXIT) {pausedOption = RESUME;}
		}

		if(upThisFrame && !upHeldLast) {
			pausedOption--;
			if(pausedOption < RESUME) {pausedOption = EXIT;}
		}
		
		if(gp.controller.escapeActive && !escapeLastFrame && !resuming) {resume();}
		
		if(resuming && System.currentTimeMillis() >= resumeTime) {
			levelState = PLAYING;
			resuming = false;
		}
	}
	
	public void resume() {
		resumeTime = System.currentTimeMillis() + 3000;
		startTime += (resumeTime - pauseTime);
		resuming = true;
	}
	
	public void restart() {
		gp.newLevel();
	}

	public void draw(Graphics2D g2d) {
		leftArrow.draw(g2d);
		downArrow.draw(g2d);
		upArrow.draw(g2d);
		rightArrow.draw(g2d);
		
		switch(levelState) {
			case PLAYING:
				combo.draw(g2d);
				judge.draw(g2d);
				for(Arrow arrow : arrows) {arrow.draw(g2d);}
				break;
			case PAUSED:
				if(resuming) {
					for(Arrow arrow : arrows) {arrow.draw(g2d);}
					int countdown = (int)(resumeTime - System.currentTimeMillis()) / 1000 + 1;
					writer.draw(countdown + "", 1, judge.getX() + 65, combo.getY(), g2d);
				}else {
					drawPauseMenu(g2d);
				}
				break;
		}
		
		score.draw(g2d);
	}
	
	public void drawPauseMenu(Graphics2D g2d) {
		writer.draw("Paused", 1, 5, 5, g2d);
		writer.draw("Resume", 1, judge.getX(), 3 * gp.TILE_SIZE - SHIFT - 6 + 75, g2d);
		writer.draw("Restart", 1, judge.getX(), 3 * gp.TILE_SIZE - SHIFT - 6 + 2 * 75, g2d);
		writer.draw("Exit", 1, judge.getX(), 3 * gp.TILE_SIZE - SHIFT - 6 + 3 * 75, g2d);
		writer.draw(".", 2, judge.getX() - 55, 3 * gp.TILE_SIZE - SHIFT + 8 + (pausedOption - 1) * 75, g2d);
	}
	
	public void endLevel(int state) {
		if(judge.maxCombo == arrows.size()) {fc = true;}
		else if(judge.miss + judge.ok + judge.good < 10) {sdcb = true;}
		music.stop();
		
		if(state == GameState.RESULTS) {gp.results = new ResultsScreen(this);}
		GameState.state = state;
	}
}
