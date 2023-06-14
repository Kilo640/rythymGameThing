import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener{

	public boolean leftActive, downActive, upActive, rightActive, startActive, escapeActive;
	public boolean upSelection, downSelection;
	public int leftArrow, downArrow, upArrow, rightArrow, start, escape, up, down;
	
	@Override
	//Unneeded method
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == leftArrow) {leftActive = true;}
		if(key == downArrow) {downActive = true;}
		if(key == upArrow) {upActive = true;}
		if(key == rightArrow) {rightActive = true;}
		if(key == start) {startActive = true;}
		if(key == escape) {escapeActive = true;}
		if(key == up) {upSelection = true;}
		if(key == down) {downSelection = true;}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == leftArrow) {leftActive = false;}
		if(key == downArrow) {downActive = false;}
		if(key == upArrow) {upActive = false;}
		if(key == rightArrow) {rightActive = false;}
		if(key == start) {startActive = false;}
		if(key == escape) {escapeActive = false;}
		if(key == up) {upSelection = false;}
		if(key == down) {downSelection = false;}
	}

}
