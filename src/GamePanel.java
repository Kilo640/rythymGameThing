import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable{
	private final int UNSCALED_TILE_SIZE = 16;
	public final int SCALE = 3;
	public final int TILE_SIZE = UNSCALED_TILE_SIZE * SCALE;
	private final int SCREEN_COLS = 17;
	private final int SCREEN_ROWS = 13;
	public final int FPS = 120;
	
	//Screen Size: 816x624
	public final int WIDTH = TILE_SIZE * SCREEN_COLS;
	public final int HEIGHT = TILE_SIZE * SCREEN_ROWS;
	
	public Controller controller = new Controller();
	public Settings settings;
	private Thread gameClock;
	public Level currentLevel;
	public ResultsScreen results;
	public TextDrawer writer = new TextDrawer();
	
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
		switch(GameState.state) {
		case GameState.MENU:
			currentLevel = null;
			if(controller.startActive) {
				settings.setSettings();
				GameState.state = GameState.PLAYING;
			}
			break;
		case GameState.PLAYING:
			if(currentLevel == null) {currentLevel = new Level(this, settings.levelName);}
			else {currentLevel.update();}
			if(controller.escapeActive) {currentLevel.endLevel(GameState.MENU);}
			break;
		case GameState.RESULTS:
			//results.update();
			if(controller.escapeActive) {GameState.state = GameState.MENU;}
		}
	}	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
	
		switch(GameState.state) {
			case GameState.MENU:
				writer.draw("Press " + settings.startKey + " to start", 1, 100, 250, g2d);
				writer.draw("Current Level:", 0.75, 5, 535, g2d);
				writer.draw(settings.levelName, 0.75, 5, 585, g2d);
				break;
			case GameState.PLAYING:
				if(currentLevel != null) {currentLevel.draw(g2d);}
				break;
			case GameState.RESULTS:
				results.draw(g2d);
			}
	
		g2d.dispose();
	}
}

