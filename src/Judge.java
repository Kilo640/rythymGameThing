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
	public int maxCombo;
	private long judgeTime;
	public int ok;
	public int good;
	public int great;
	public int perfect;
	public int marvelous;
	public int miss;
	
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
		judgeTime = Integer.MIN_VALUE;
		
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
			marvelous++;
			judgeIndex = MARV_IMG;
		}else if(deviance <= PERFECT) {
			level.arrowHitWeight += 0.98;
			perfect++;
			combo++;
			judgeIndex = PERF_IMG;
		}else if(deviance <= GREAT) {
			level.arrowHitWeight += 0.75;
			great++;
			combo++;
			judgeIndex = GREAT_IMG;
		}else if(deviance <= GOOD) {
			level.arrowHitWeight += 0.50;
			good++;
			combo = 0;
			judgeIndex = GOOD_IMG;
		}else if(deviance <= OK){
			level.arrowHitWeight += 0.25;
			ok++;
			combo = 0;
			judgeIndex = OK_IMG;
		}else {
			miss++;
			combo = 0;
			judgeIndex = MISS_IMG;
		}
		
		if(maxCombo < combo) {maxCombo = combo;}
		
		accuracy = 100 * (level.arrowHitWeight/level.numHits);
		judgeTime = level.levelTime;
		return setGrade();
	}
	
	public int judgeLongNote(double fractionHeld, int duration) {
		if(fractionHeld >= 0.75 || (duration <= 200 && fractionHeld >= 0.25)) {
			level.arrowHitWeight++;
			judgeIndex = MARV_IMG;
		}else if(fractionHeld >= 0.50) {
			level.arrowHitWeight += 0.75;
			judgeIndex = GREAT_IMG;
		}else if(fractionHeld >= 0.25){
			level.arrowHitWeight += 0.25;
			judgeIndex = OK_IMG;
		}else {
			judgeIndex = MISS_IMG;
		}
		
		accuracy = 100 * (level.arrowHitWeight/level.numHits);
		judgeTime = level.levelTime;
		return setGrade();
	}
	
	private int setGrade() {
		int grade = F;
		
		if(accuracy > 99.99) {
			grade = SSPLUS;
		}else if(accuracy >= 98.995) {
			grade = SS;
		}else if(accuracy >= 96.995) {
			grade = SPLUS;
		}else if(accuracy >= 94.995) {
			grade = S;
		}else if(accuracy >= 92.995) {
			grade = APLUS;
		}else if(accuracy >= 86.995) {
			grade = A;
		}else if(accuracy >= 84.995) {
			grade = BPLUS;
		}else if(accuracy >= 76.995) {
			grade = B;
		}else if(accuracy >= 74.995) {
			grade = CPLUS;
		}else if(accuracy >= 66.995) {
			grade = C;
		}else if(accuracy >= 59.995){
			grade = D;
		}
		
		return grade;
	}
}
