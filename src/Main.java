import javax.swing.JFrame;

public class Main {
	public static GamePanel game = new GamePanel();
	public static Settings settings = new Settings();
	
	public static void main(String[] args) {
		game.settings = settings; //Avoid null pointer exception
		settings.setSettings();
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("RythymGameLmao"); //Undecided on name
		window.add(game);
		
		window.pack();
		window.setResizable(false);
		game.startClock();
		
		window.setVisible(true);
	}

}
