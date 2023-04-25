import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable{
	private final int UNSCALED_TILE_SIZE = 16;
	private final int SCALE = 3;
	public final int TILE_SIZE = UNSCALED_TILE_SIZE * SCALE;
	private final int SCREEN_COLS = 17;
	private final int SCREEN_ROWS = 13;
	private final int FPS = 60;
	
	//Screen Size: 816x624
	public final int WIDTH = TILE_SIZE * SCREEN_COLS;
	public final int HEIGHT = TILE_SIZE * SCREEN_ROWS;
	
	public Controller controller = new Controller();
	private Thread gameClock;
	public int levelTime;
	public Level currentLevel = new Level(this, "testLevel");
	
	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.GRAY);
		setDoubleBuffered(true);
		
		addKeyListener(controller);
		setFocusable(true);
	}

	public void startClock() {
		gameClock = new Thread(this);
		gameClock.start();
	}
	
	@Override
	public void run() {
		double frameTime = 1000000000 / FPS;
		double nextFrameTime = System.nanoTime() + frameTime;
		
		while(gameClock != null) {
			update();
			repaint();
			
			try {
				double remainingTime = nextFrameTime - System.nanoTime();
				if(remainingTime < 0) {remainingTime = 0;}
				Thread.sleep((long)remainingTime / 1000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			nextFrameTime += frameTime;
		}
	}
	
	public void update() {
		levelTime = (int)(System.currentTimeMillis() - currentLevel.startTime);
		currentLevel.update();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		currentLevel.draw(g2d);
		
		g2d.dispose();
	}
}
