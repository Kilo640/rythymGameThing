import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScoreUI extends Entity {
	private BufferedImage hundreds;
	private BufferedImage tens;
	private BufferedImage ones;
	private BufferedImage tenths;
	private BufferedImage hundreths;
	private BufferedImage point;
	private BufferedImage percent;
	private BufferedImage grade;
	
	private Level level;
	private GamePanel gp;

	public ScoreUI(int x, int y, GamePanel gp, Level level) {
		super(x, y);
		this.gp = gp;
		this.level = level;
		
		try {
			point = ImageIO.read(getClass().getResourceAsStream("/UI/digits/..png"));
			percent = ImageIO.read(getClass().getResourceAsStream("/UI/digits/%.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(double accuracy) {
		int hundred = (int)(accuracy / 100);
		int ten = ((int)accuracy % 100) / 10;
		int one = (int)accuracy % 10;
		int tenth = (int)(accuracy * 10) % 10;
		int hundreth = (int)(accuracy * 100) % 10;
		
		//Rounding :(
		if(((int)(accuracy * 1000) % 10) >= 5){
			hundreth++;
			if(hundreth > 9) {
				hundreth = 9;
				tenth++;
				if(tenth > 9) {
					tenth = 9;
					one++;
					if(one > 9) {
						one = 9;
						ten++;
						if(ten > 9) {
							ten = 9;
							hundred++;
						}
					}
				}
			}
		}
		
		try {
			hundreds = ImageIO.read(getClass().getResourceAsStream("/UI/digits/" + hundred + ".png"));
			tens = ImageIO.read(getClass().getResourceAsStream("/UI/digits/" + ten + ".png"));
			ones = ImageIO.read(getClass().getResourceAsStream("/UI/digits/" + one + ".png"));
			tenths = ImageIO.read(getClass().getResourceAsStream("/UI/digits/" + tenth + ".png"));
			hundreths = ImageIO.read(getClass().getResourceAsStream("/UI/digits/" + hundreth + ".png"));
			grade = ImageIO.read(getClass().getResourceAsStream("/UI/grades/" + level.grade + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(hundreds, getX(), getY(),
				gp.TILE_SIZE, gp.TILE_SIZE, null);
		g2d.drawImage(tens, getX() + (gp.TILE_SIZE - 18), getY(),
				gp.TILE_SIZE, gp.TILE_SIZE, null);
		g2d.drawImage(ones, getX() + 2 * (gp.TILE_SIZE - 18), getY(),
				gp.TILE_SIZE, gp.TILE_SIZE, null);
		g2d.drawImage(point, getX() + 3 * (gp.TILE_SIZE - 18) - 6, getY(),
				gp.TILE_SIZE, gp.TILE_SIZE, null);
		g2d.drawImage(tenths, getX() + 4 * (gp.TILE_SIZE - 18) - 12, getY(),
				gp.TILE_SIZE, gp.TILE_SIZE, null);
		g2d.drawImage(hundreths, getX() + 5 * (gp.TILE_SIZE - 18) - 12, getY(),
				gp.TILE_SIZE, gp.TILE_SIZE, null);
		g2d.drawImage(percent, getX() + 6 * (gp.TILE_SIZE - 18) - 12, getY(),
				gp.TILE_SIZE, gp.TILE_SIZE, null);
		g2d.drawImage(grade, getX() + 8 * (gp.TILE_SIZE - 18) - 12, getY() - 24,
				(int) (1.5 * gp.TILE_SIZE), (int) (1.5 * gp.TILE_SIZE), null);
	}
}