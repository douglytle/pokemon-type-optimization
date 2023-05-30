package Types;

public class DragonType implements PType {
	//{Normal, Fire, Water, Electric, Grass, Ice, Fighting, Poison, Ground, Flying, Psychic, Bug, Rock, Ghost, Dragon, Dark, Steel, Fairy}
	private static String name = "Dragon";
	private static int ID = 14;
	private static double[] offensive_coverage = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 0.5, 0}; //deals this multiplier of damage
	private static double[] defensive_coverage = {1, 0.5, 0.5, 0.5, 0.5, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2}; //takes this multiplier of damage

	public int getID() {
		return ID;
	}
	public String toString() {
		return name;
	}
	public double[] getOffensiveCoverage() {
		return offensive_coverage;
	}
	public double[] getDefensiveCoverage() {
		return defensive_coverage;
	}
}
