import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class LongArrow extends Arrow {
	private BufferedImage tailImage;
	private BufferedImage tailBoundry;
	
	public int duration;
	public int endTime;
	private double durationHeld;
	private int length;
	private boolean availible;
	private boolean availibleLast;
	
	public LongArrow(GamePanel gp, int direction, int time, Level level, 
		ArrayList<Arrow> arrows, int duration) {
		super(gp, direction, time, level, arrows);
		this.duration = duration;
		this.length = (int)((1.0 / 60) * scrollSpeed * duration);
		endTime = time + duration;
		
		try {
			switch (direction) {
			case ArrowSensor.LEFT:
				tailImage = ImageIO.read(new File("resources/arrows/LeftArrowTail.png"));
				break;
			case ArrowSensor.DOWN:
				tailImage = ImageIO.read(new File("resources/arrows/DownArrowTail.png"));
				break;
			case ArrowSensor.UP:
				tailImage = ImageIO.read(new File("resources/arrows/UpArrowTail.png"));
				break;
			case ArrowSensor.RIGHT:
				tailImage = ImageIO.read(new File("resources/arrows/RightArrowTail.png"));
				break;
			default:
				System.out.println("BruhSoundEffect3.mp3");
			}
			tailBoundry = ImageIO.read(new File("resources/arrows/TailBoundry.png"));
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		availibleLast = availible;

		super.update();
		
		if (timeFromTarget <= 0 && timeFromTarget > -duration) {availible = true;}
		else{availible = false;}
		
		if(availible) {
			switch (direction) {
			case ArrowSensor.LEFT:
				checkLongArrow(level.leftArrow);
				break;
			case ArrowSensor.DOWN:
				checkLongArrow(level.downArrow);
				break;
			case ArrowSensor.UP:
				checkLongArrow(level.upArrow);
				break;
			case ArrowSensor.RIGHT:
				checkLongArrow(level.rightArrow);
				break;
			}
		}
		
		if(!availible && availibleLast) {
			level.numArrows++;
			level.grade = judge.judgeArrowTail(durationHeld / duration, duration);
		}
	}
	
	protected void checkLongArrow(ArrowSensor sensor){
		if(sensor.isActive && availible) {
			durationHeld += (1000.0 / gp.FPS);
			length = (int)((1.0 / 60) * scrollSpeed * (endTime - level.levelTime));
			setPosition(arrowX(), arrowY(0));
			isDrawable = true;
		}
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(tailImage, getX(), (int)(getY() + 0.5 * gp.TILE_SIZE),
				(int)(gp.TILE_SIZE * 1.5), length, null);
		g2d.drawImage(tailBoundry, getX(), getY() + length - gp.TILE_SIZE,
				(int)(gp.TILE_SIZE * 1.5), (int)(gp.TILE_SIZE * 1.5), null);
				
		super.draw(g2d);
		

	}
}
