import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextDrawer {
	private final int NUMCHARS = 39;
	private final int PERCENT = 'Z' - 'A' + '9' - '0' + 2;
	private final int POINT = PERCENT + 1;
	private final int COLON = POINT + 1;
	private BufferedImage[] chars = new BufferedImage[NUMCHARS];
			
	public TextDrawer() {
		try {
			chars[PERCENT] = ImageIO.read(new File("resources/UI/characters/%.png"));
			chars[POINT] = ImageIO.read(new File("resources/UI/characters/..png"));
			chars[COLON] = ImageIO.read(new File("resources/UI/characters/colon.png"));
			for(char c = 'A'; c <= 'Z'; c++) {
				chars[indexOfChar(c)] = ImageIO.read(new File("resources/UI/characters/" + c + ".png"));
			}
			for(char c = '0'; c <= '9'; c++) {
				chars[indexOfChar(c)] = ImageIO.read(new File("resources/UI/characters/" + c + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int indexOfChar(int character) {
		if(character >= 'A' && character <= 'Z') {return character - 'A';}
		else if(character >= '0' && character <= '9'){return 'Z' - 'A' + character - '0' + 1;}
		else{return -1;}
	}
	
	public void draw(String str, double scale, int x, int y, Graphics2D g2d) {
		str = str.toUpperCase();
		for(int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if((ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
				drawChar(indexOfChar(ch), scale, x, y, g2d);
			}else {
				switch(ch) {
				case '%':
					drawChar(PERCENT, scale, x, y, g2d);
					break;
				case '.':
					drawChar(POINT, scale, x, y, g2d);
					break;
				case ':':
					drawChar(COLON, scale, x, y, g2d);
					break;
				}
			}
			
			x += (int)(scale * (Main.game.TILE_SIZE - 18));
		}
	}
	
	private void drawChar(int index, double scale, int x, int y, Graphics2D g2d) {
		g2d.drawImage(chars[index], x, y,
				(int)(scale * Main.game.SCALE * chars[index].getWidth()), 
				(int)(scale * Main.game.SCALE * chars[index].getHeight()), null);
	}
}
