import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Arrow extends Entity {

	private GamePanel gp;
	private BufferedImage arrowImage, hit, currImage;
	
	public int scrollSpeed = 38; //number of pixels moved every frame (60th of a second)
	private int direction;
	private int time; //time (milliseconds) after song start that the note should be hit
	
	public Arrow(GamePanel gp, int direction, int time) {
		super(0, 0);
		this.gp = gp;
		this.direction = direction;
		this.time = time;
		
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
			arrowImage = ImageIO.read(getClass().getResourceAsStream(
					"/arrows/" + direction + "Arrow.png"));
			hit = ImageIO.read(getClass().getResourceAsStream("/arrows/arrowHit.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		setPosition(arrowX(),arrowY(gp.levelTime));
		currImage = arrowImage;
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(currImage, getX(), getY(), (int)(1.5*gp.TILE_SIZE), (int)(1.5*gp.TILE_SIZE), null);
	}
	
	private int arrowX() {
		return gp.TILE_SIZE * (5 + 2 * direction);
	}
	
	private int arrowY(int currTime) {
		return (int)(gp.TILE_SIZE + (1.0 / 60) * scrollSpeed * (time - currTime));
	}
}
