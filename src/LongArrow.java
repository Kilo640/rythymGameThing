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
	public double lastFrameDuration;
	private int frames;
	public double durationHeld;
	public int length;
	public boolean availible;
	public boolean availibleLast;
	
	public LongArrow(GamePanel gp, int direction, int time, Level level, 
		ArrayList<Arrow> levelArrows, int duration) {
		super(gp, direction, time, level, levelArrows);
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
			frames++;
		}
		
		if(!availible && availibleLast) {
			level.numHits++;
			level.grade = judge.judgeLongNote(durationHeld / duration, duration);
			isDrawable = false;
		}
	}
	
	protected void checkLongArrow(ArrowSensor sensor){
		if(sensor.isActive && availible) {
			durationHeld += (1000.0 / gp.FPS);
			length = (int)((1.0 / 60) * scrollSpeed * (endTime - level.levelTime));
			setPosition(arrowX(), arrowY(0));
			lastFrameDuration = (1000.0 / gp.FPS) * frames;
			isDrawable = true;
		}else {
			setPosition(arrowX(), arrowY((int)(timeFromTarget + lastFrameDuration)));
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
