import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable{
	private final int UNSCALED_TILE_SIZE = 16;
	private final int SCALE = 3;
	private final int TILE_SIZE = UNSCALED_TILE_SIZE * SCALE;
	private final int SCREEN_COLS = 16;
	private final int SCREEN_ROWS = 12;
	
	//Screen Size: 768x576
	private final int WIDTH = TILE_SIZE * SCREEN_COLS;
	private final int HEIGHT = TILE_SIZE * SCREEN_ROWS;
	
	private Controller controller;
	private Thread gameClock;
	private Color arrowColor;
	private int scrollSpeed;
	
	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		
		controller = new Controller();
		addKeyListener(controller);
		setFocusable(true);
		scrollSpeed = 1152; //Pixels per second to travel the screen in 0.5 seconds
		arrowColor = Color.WHITE;
	}

	public void startClock() {
		gameClock = new Thread(this);
		gameClock.start();
	}
	
	@Override
	public void run() {
		while(gameClock != null) {
			update();
			repaint();
		}
	}
	
	public void update() {
		if(controller.leftActive) {arrowColor = Color.GRAY;}
		else {arrowColor = Color.WHITE;}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(arrowColor);
		g2d.fillRect(WIDTH / 2, TILE_SIZE, TILE_SIZE, TILE_SIZE);
		
		g2d.dispose();
	}
}
