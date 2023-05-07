
public class Judge {
	public double accuracy;
	private Level level;
	public String judgment;
	public int combo;
	
	public final int OK = 180;
	private final int GOOD = 135;
	private final int GREAT = 90;
	private final int PERFECT = 45;
	private final int MARVELOUS = 23;
	
	public Judge(Level level) {
		this.level = level;
		judgment = "";
	}
	
	public void judgeHit(int deviance) {
		if(deviance <= MARVELOUS) {
			level.arrowHitWeight++;
			combo++;
			judgment = "Marvelous";
		}else if(deviance <= PERFECT) {
			level.arrowHitWeight += 0.98;
			combo++;
			judgment = "Perfect";
		}else if(deviance <= GREAT) {
			level.arrowHitWeight += 0.75;
			combo++;
			judgment = "Great";
		}else if(deviance <= GOOD) {
			level.arrowHitWeight += 0.50;
			combo = 0;
			judgment = "Good";
		}else if(deviance <= OK){
			level.arrowHitWeight += 0.25;
			combo = 0;
			judgment = "Ok";
		}else {
			combo = 0;
			judgment = "Miss";
		}
		
		accuracy = 100 * (level.arrowHitWeight/level.numArrows);
		level.printScore();
	}
}
