import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {
	public static GamePanel game = new GamePanel();
	public static Settings settings = new Settings();
	public static JFrame window = new JFrame();
	
	public static void main(String[] args) {
		game.settings = settings; //Avoid null pointer exception
		settings.setSettings();
		ImageIcon windowIcon = new ImageIcon("resources/icon.png");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("kiloGame"); //Undecided on name
		window.setIconImage(windowIcon.getImage());
		window.add(game);
		
		window.pack();
		window.setResizable(false);
		game.startClock();
		
		window.setVisible(true);
	}

}
