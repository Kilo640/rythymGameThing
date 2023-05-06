
public class Judge {
	public double accuracy;
	private Level level;
	public String judgment;
	
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
			judgment = "Marvelous";
		}else if(deviance <= PERFECT) {
			level.arrowHitWeight += 0.98;
			judgment = "Perfect";
		}else if(deviance <= GREAT) {
			level.arrowHitWeight += 0.75;
			judgment = "Great";
		}else if(deviance <= GOOD) {
			level.arrowHitWeight += 0.50;
			judgment = "Good";
		}else if(deviance <= OK){
			level.arrowHitWeight += 0.25;
			judgment = "Ok";
		}else {
			judgment = "Miss";
		}
		
		accuracy = 100 * (level.arrowHitWeight/level.numArrows);
		level.printScore();
	}
}
