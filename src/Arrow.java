import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Arrow extends Entity {

	private GamePanel gp;
	private BufferedImage arrowImage;

	public int scrollSpeed = 75;
	public int direction;
	public int time; // time (milliseconds) after song start that the note should be hit
	public boolean isActive = true;
	public long timeFromTarget;
	private Level level;
	private Judge judge;
	Arrow lastArrow;

	public Arrow(GamePanel gp, int direction, int time, Level level, ArrayList<Arrow> arrows) {
		super(0, 0);
		this.gp = gp;
		this.direction = direction;
		this.time = time;
		this.level = level;
		judge = level.judge;

		switch (direction) {
		case ArrowSensor.LEFT:
			setImages("Left");
			break;
		case ArrowSensor.DOWN:
			setImages("Down");
			break;
		case ArrowSensor.UP:
			setImages("Up");
			break;
		case ArrowSensor.RIGHT:
			setImages("Right");
			break;
		default:
			System.out.println("BruhSoundEffect3.mp3");
		}

		if (arrows.size() > 0) {
			Arrow arrow = arrows.get(arrows.size() - 1);
			for (int i = arrows.size() - 1; i > 0 && arrow.direction != this.direction; i--) {
				arrow = arrows.get(i);
			}
			lastArrow = arrow;
		}

	}

	private void setImages(String direction) {
		try {
			arrowImage = ImageIO.read(new File("resources/arrows/" + direction + "Arrow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		timeFromTarget = time - level.levelTime;
		setPosition(arrowX(), arrowY((int)timeFromTarget));
		
		if (isActive && (lastArrow == null || lastArrow.timeFromTarget < -90 || !lastArrow.isActive)) {
			if (timeFromTarget < -1 * judge.OK) {
				isActive = false;
				level.numArrows++;
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

	private void checkArrow(ArrowSensor sensor) {
		int deviance = (int)Math.abs(timeFromTarget);

		if (sensor.isActive && deviance < judge.OK && !sensor.activeLast) {
			isActive = false;
			level.numArrows++;
			level.grade = judge.judgeHit(deviance);
		}
	}

	public void draw(Graphics2D g2d) {
		if (getY() > -75 && getY() < gp.HEIGHT && isActive) {
			g2d.drawImage(arrowImage, getX(), getY(), 
					(int) (1.5 * gp.TILE_SIZE), (int) (1.5 * gp.TILE_SIZE), null);
		}
	}

	private int arrowX() {
		return gp.TILE_SIZE * (5 + 2 * direction) - level.SHIFT;
	}

	private int arrowY(int currTime) {
		return (int) (gp.TILE_SIZE + (1.0 / 60) * scrollSpeed * (timeFromTarget));
	}
}
