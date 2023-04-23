import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ArrowSensor extends Entity {
	private GamePanel gp;
	private Controller controller;
	private BufferedImage inactive, active, currImage;
	private int direction;
	private boolean isActive;
	
	public static final int LEFT = 0;
	public static final int DOWN = 1;
	public static final int UP = 2;
	public static final int RIGHT = 3;
	
	public ArrowSensor(int x, int y, GamePanel gp, Controller controller, int direction) {
		super(x, y);
		this.gp = gp;
		this.controller = controller;
		this.direction = direction;
		
		switch(direction) {
		case LEFT:
			setImages("Left");
			break;
		case DOWN:
			setImages("Down");
			break;
		case UP:
			setImages("Up");
			break;
		case RIGHT:
			setImages("Right");
			break;
		default:
			System.out.println("BruhSoundEffect3.mp3");
		}
	}

	public void update() {
		//See, I'm already more educated than YandereDev!
		switch(direction) {
			case LEFT:
				checkKey(controller.leftActive);
				break;
			case DOWN:
				checkKey(controller.downActive);
				break;
			case UP:
				checkKey(controller.upActive);
				break;
			case RIGHT:
				checkKey(controller.rightActive);
				break;
			default:
				System.out.println("BruhSoundEffect3.mp3");
		}
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(currImage, getX(), getY(), (int)(1.5*gp.TILE_SIZE), (int)(1.5*gp.TILE_SIZE), null);
	}
	
	private void checkKey(boolean keyPressed) {
		if(keyPressed) {
			isActive = true;
			currImage = active;
		}else {
			isActive = false;
			currImage = inactive;
		}
	}
	
	private void setImages(String direction) {
		try {
			active = ImageIO.read(getClass().getResourceAsStream(
					"/arrows/activated" + direction + "Arrow.png"));
			inactive = ImageIO.read(getClass().getResourceAsStream(
					"/arrows/" + direction + "Arrow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
