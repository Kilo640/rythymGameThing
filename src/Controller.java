import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener{

	public boolean leftActive, downActive, upActive, rightActive, spaceActive, escActive;
	
	@Override
	//Unneeded method
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_D) {leftActive = true;}
		if(key == KeyEvent.VK_F) {downActive = true;}
		if(key == KeyEvent.VK_J) {upActive = true;}
		if(key == KeyEvent.VK_K) {rightActive = true;}
		if(key == KeyEvent.VK_SPACE) {spaceActive = true;}
		if(key == KeyEvent.VK_ESCAPE) {escActive = true;}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_D) {leftActive = false;}
		if(key == KeyEvent.VK_F) {downActive = false;}
		if(key == KeyEvent.VK_J) {upActive = false;}
		if(key == KeyEvent.VK_K) {rightActive = false;}
		if(key == KeyEvent.VK_SPACE) {spaceActive = false;}
		if(key == KeyEvent.VK_ESCAPE) {escActive = false;}
	}

}
