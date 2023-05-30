package Types;

public class PoisonType implements PType {
	private static String name = "Poison";
	private static int ID = 7;
	//{Normal, Fire, Water, Electric, Grass, Ice, Fighting, Poison, Ground, Flying, Psychic, Bug, Rock, Ghost, Dragon, Dark, Steel, Fairy}
	private static double[] offensive_coverage = {1, 1, 1, 1, 2, 1, 1, 0.5, 0.5, 1, 1, 1, 0.5, 0.5, 1, 1, 0, 2}; //deals this multiplier of damage
	private static double[] defensive_coverage = {1, 1, 1, 1, 0.5, 1, 0.5, 0.5, 2, 1, 2, 0.5, 1, 1, 1, 1, 1, 0.5}; //takes this multiplier of damage

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