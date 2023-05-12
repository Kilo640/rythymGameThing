import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Judge extends Entity{
	public double accuracy;
	private Level level;
	public int judgeIndex;
	public int combo;
	private long judgeTime;
	private BufferedImage[] judgeImages;
	//Judgment windows (milliseconds)
	public final int OK = 180;
	private final int GOOD = 135;
	private final int GREAT = 90;
	private final int PERFECT = 45;
	private final int MARVELOUS = 23;
	private final int DISPLAY_TIME = 500;
	//Indexes for each grade in image array
	public final int F = 0;
	private final int D = 1;
	private final int C = 2;
	private final int CPLUS = 3;
	private final int B = 4;
	private final int BPLUS = 5;
	public final int A = 6;
	private final int APLUS = 7;
	private final int S = 8;
	private final int SPLUS = 9;
	private final int SS = 10;
	private final int SSPLUS = 11;
	//Indexes for each judgment image
	public final int MISS_IMG = 0;
	private final int OK_IMG = 1;
	private final int GOOD_IMG = 2;
	private final int GREAT_IMG = 3;
	private final int PERF_IMG = 4;
	private final int MARV_IMG = 5;
	
	public Judge(int x, int y, Level level) {
		super(x, y);
		this.level = level;
		judgeIndex = MISS_IMG;
		judgeImages = new BufferedImage[6];
		judgeTime = -Integer.MAX_VALUE;
		
		try {
			judgeImages[MISS_IMG] = ImageIO.read(new File("resources/UI/gameplay/judgments/Miss.png"));
			judgeImages[OK_IMG] = ImageIO.read(new File("resources/UI/gameplay/judgments/Ok.png"));
			judgeImages[GOOD_IMG] = ImageIO.read(new File("resources/UI/gameplay/judgments/Good.png"));
			judgeImages[GREAT_IMG] = ImageIO.read(new File("resources/UI/gameplay/judgments/Great.png"));
			judgeImages[PERF_IMG] = ImageIO.read(new File("resources/UI/gameplay/judgments/Perfect.png"));
			judgeImages[MARV_IMG] = ImageIO.read(new File("resources/UI/gameplay/judgments/Marvelous.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2d) {
		if((level.levelTime - judgeTime) < DISPLAY_TIME) {
			g2d.drawImage(judgeImages[judgeIndex], getX(), getY(), 2 * judgeImages[judgeIndex].getWidth(),
					2 * judgeImages[judgeIndex].getHeight(), null);
		}
	}
	
	public int judgeHit(int deviance) {
		if(deviance <= MARVELOUS) {
			level.arrowHitWeight++;
			combo++;
			judgeIndex = MARV_IMG;
		}else if(deviance <= PERFECT) {
			level.arrowHitWeight += 0.98;
			combo++;
			judgeIndex = PERF_IMG;
		}else if(deviance <= GREAT) {
			level.arrowHitWeight += 0.75;
			combo++;
			judgeIndex = GREAT_IMG;
		}else if(deviance <= GOOD) {
			level.arrowHitWeight += 0.50;
			combo = 0;
			judgeIndex = GOOD_IMG;
		}else if(deviance <= OK){
			level.arrowHitWeight += 0.25;
			combo = 0;
			judgeIndex = OK_IMG;
		}else {
			combo = 0;
			judgeIndex = MISS_IMG;
		}
		
		accuracy = 100 * (level.arrowHitWeight/level.numArrows);
		judgeTime = level.levelTime;
		return setGrade();
	}
	
	private int setGrade() {
		int grade = F;
		
		if(accuracy > 99.99) {
			grade = SSPLUS;
		}else if(accuracy >= 99) {
			grade = SS;
		}else if(accuracy >= 97) {
			grade = SPLUS;
		}else if(accuracy >= 95) {
			grade = S;
		}else if(accuracy >= 93) {
			grade = APLUS;
		}else if(accuracy >= 87) {
			grade = A;
		}else if(accuracy >= 85) {
			grade = BPLUS;
		}else if(accuracy >= 77) {
			grade = B;
		}else if(accuracy >= 75) {
			grade = CPLUS;
		}else if(accuracy >= 67) {
			grade = C;
		}else if(accuracy >= 60){
			grade = D;
		}
		
		return grade;
	}
}
