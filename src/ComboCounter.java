import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ComboCounter extends Entity {
	private GamePanel gp;
	private ArrayList<Integer> digits;
	private int comboDigits;
	
	public ComboCounter(int x, int y, GamePanel gp) {
		super(x, y);
		this.gp = gp;
		digits = new ArrayList<Integer>();
	}
	
	public void update(int n) {
		digits.clear();
		comboDigits = (int)Math.log10(n) + 1;
		
		for(int i = 0; i <= comboDigits; i++) {
			int digit = n % 10;
			digits.add(digit);
			n /= 10;
		}
	}

	public void draw (Graphics2D g2d) {
		double middleDigit = (double)comboDigits / 2;
		for(int i = comboDigits - 1; i >= 0; i--) {
			int digitNum = comboDigits - i;
			int x = (int)(getX() + ((digitNum - middleDigit) * (gp.TILE_SIZE - 18)));
			int digit = digits.get(i);
			try {
				BufferedImage digitImage = ImageIO.read(new File("resources/UI/digits/" + digit + ".png"));
				g2d.drawImage(digitImage, x, getY(), gp.TILE_SIZE, gp.TILE_SIZE, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
