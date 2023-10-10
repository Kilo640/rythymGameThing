import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Arrow extends Entity {

	protected GamePanel gp;
	protected BufferedImage arrowImage;

	public int scrollSpeed = Main.settings.scrollSpeed;
	public int direction;
	public int time; // time (milliseconds) after song start that the note should be hit
	public boolean isActive = true;
	public boolean isDrawable = true;
	public long timeFromTarget;
	protected Level level;
	protected Judge judge;
	protected Arrow lastArrow;

	public Arrow(GamePanel gp, int direction, int time, Level level, ArrayList<Arrow> levelArrows) {
		super(0, 0);
		this.gp = gp;
		this.direction = direction;
		this.time = time;
		this.level = level;
		judge = level.judge;

		try {
			switch (direction) {
			case ArrowSensor.LEFT:
				arrowImage = ImageIO.read(new File("resources/arrows/LeftArrow.png"));
				break;
			case ArrowSensor.DOWN:
				arrowImage = ImageIO.read(new File("resources/arrows/DownArrow.png"));
				break;
			case ArrowSensor.UP:
				arrowImage = ImageIO.read(new File("resources/arrows/UpArrow.png"));
				break;
			case ArrowSensor.RIGHT:
				arrowImage = ImageIO.read(new File("resources/arrows/RightArrow.png"));
				break;
			default:
				System.out.println("BruhSoundEffect3.mp3");
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (levelArrows.size() > 0) {
			Arrow arrow = levelArrows.get(levelArrows.size() - 1);
			for (int i = levelArrows.size() - 1; i > 0 && arrow.direction != this.direction; i--) {
				arrow = levelArrows.get(i);
			}
			if(arrow != null && this.direction == arrow.direction) {
				lastArrow = arrow;
			}
		}

	}

	public void update() {
		setPosition(arrowX(), arrowY((int)timeFromTarget));
		timeFromTarget = time - level.levelTime;
		
		if (isActive && (lastArrow == null || lastArrow.timeFromTarget < -90 || !lastArrow.isActive)) {
			if (timeFromTarget < -judge.OK) {
				isActive = false;
				isDrawable = false;
				level.numHits++;
				level.grade = judge.judgeHit((int)Math.abs(timeFromTarget));
				return;
			}
			
			switch (direction) {
			case ArrowSensor.LEFT:
				checkArrow(level.leftArrow);
				break;
			case ArrowSensor.DOWN:
				checkArrow(level.downArrow);
				break;
			case ArrowSensor.UP:
				checkArrow(level.upArrow);
				break;
			case ArrowSensor.RIGHT:
				checkArrow(level.rightArrow);
				break;
			default:
				System.out.println("BruhSoundEffect3.mp3");
			}
		}
	}
	
	protected void checkArrow(ArrowSensor sensor) {
		int deviance = (int)Math.abs(timeFromTarget);

		if (sensor.isActive && deviance < judge.OK && !sensor.activeLast) {
			isActive = false;
			isDrawable = false;
			level.numHits++;
			level.grade = judge.judgeHit(deviance);
		}
	}

	public void draw(Graphics2D g2d) {
		if (getY() > -75 && getY() < gp.HEIGHT && isDrawable) {
			g2d.drawImage(arrowImage, getX(), getY(), 
					(int) (1.5 * gp.TILE_SIZE), (int) (1.5 * gp.TILE_SIZE), null);
		}
	}

	protected int arrowX() {
		return gp.TILE_SIZE * (5 + 2 * direction) - level.SHIFT;
	}

	protected int arrowY(int currTime) {
		return (int) (gp.TILE_SIZE + (1.0 / 60) * scrollSpeed * (currTime));
	}
}
