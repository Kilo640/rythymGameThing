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
	public Clip clip;
	
	public Level(GamePanel gp, String levelName) {
		this.gp = gp;
		try {
			levelLoader = new Scanner(new File("res/levels/" + levelName + "/chart.txt"));
			audio = AudioSystem.getAudioInputStream(new File("res/levels/" + levelName + "/music.wav"));
			clip = AudioSystem.getClip();
			clip.open(audio);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Arrow> load(){
		ArrayList<Arrow> levelArrows = new ArrayList<Arrow>();
		
		while(levelLoader.hasNext()) {
			levelArrows.add(new Arrow(gp, levelLoader.nextInt(), levelLoader.nextInt()));
		}
		
		levelLoader.close();
		return levelArrows;
	}
}
