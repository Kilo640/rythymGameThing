import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Arrow extends Entity {

	private GamePanel gp;
	private BufferedImage arrowImage;
	
	public int scrollSpeed = 45;
	public int direction;
	private int time; //time (milliseconds) after song start that the note should be hit
	public boolean isActive = true;
	private int timeFromTarget;
	private Level level;
	
	private final int MISS_TIME = 164;
	public Arrow(GamePanel gp, int direction, int time, Level level) {
		super(0, 0);
		this.gp = gp;
		this.direction = direction;
		this.time = time;
		this.level = level;
		
		switch(direction) {
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
	}

	private void setImages(String direction) {
		try {
			arrowImage = ImageIO.read(getClass().getResourceAsStream("/arrows/" + direction + "Arrow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		timeFromTarget =  time - gp.levelTime;
		setPosition(arrowX(),arrowY(timeFromTarget));
		
		if(isActive) {
			if(timeFromTarget < -1 * MISS_TIME) {
				isActive = false;
				level.numArrows++;
				return;
			}
			
			switch(direction) {
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
		if(sensor.isActive && Math.abs(timeFromTarget) < MISS_TIME && !sensor.activeLast) {
			isActive = false;
			level.numArrows++;
			level.arrowsHit++;
			level.printScore();
		}
	}

	public void draw(Graphics2D g2d) {
		if(getY() > -75 && getY() < gp.HEIGHT && isActive) {
			g2d.drawImage(arrowImage, getX(), getY(), (int)(1.5*gp.TILE_SIZE), (int)(1.5*gp.TILE_SIZE), null);
		}
	}
	
	private int arrowX() {
		return gp.TILE_SIZE * (5 + 2 * direction);
	}
	
	private int arrowY(int currTime) {
		return (int)(gp.TILE_SIZE + (1.0 / 60) * scrollSpeed * (timeFromTarget));
	}
}
