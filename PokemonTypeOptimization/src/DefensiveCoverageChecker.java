import Types.PType;
import java.util.ArrayList;

public class DefensiveCoverageChecker {
	
	public static String[] Types = {"Normal", "Fire", "Water", "Electric", "Grass", "Ice", "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"};
	public ArrayList<String> neutral;
	public ArrayList<String> resists;
	public ArrayList<String> weakTo;
	public ArrayList<String> quadResists;
	public ArrayList<String> quadWeakTo;
	public ArrayList<String> immunities;
	String typeDisplay;
	
	public DefensiveCoverageChecker(ArrayList<PType> types) {
		neutral = new ArrayList<String>();
		resists = new ArrayList<String>();
		weakTo = new ArrayList<String>();
		quadResists = new ArrayList<String>();
		quadWeakTo = new ArrayList<String>();
		immunities = new ArrayList<String>();
		
		if(types.size() == 1) {
			typeDisplay = types.get(0) + " Type";
			double[] defensivecoverage = types.get(0).getDefensiveCoverage();
			for(int i = 0; i < Types.length; i++) {
				if(defensivecoverage[i] == 1) {
					neutral.add(Types[i]);
				} else if(defensivecoverage[i] == 0.5) {
					resists.add(Types[i]);
				} else if(defensivecoverage[i] == 2) {
					weakTo.add(Types[i]);
				} else if(defensivecoverage[i] == 0) {
					immunities.add(Types[i]);
				}
			}
		}
		if(types.size() == 2) {
			typeDisplay = types.get(0) + "/" + types.get(1) + " Type";
			double[] coverage1 = types.get(0).getDefensiveCoverage();
			double[] coverage2 = types.get(1).getDefensiveCoverage();
			for(int i = 0; i < Types.length; i++) {
				double coverage = coverage1[i] * coverage2[i];
				if(coverage == 1) {
					neutral.add(Types[i]);
				} else if(coverage == 0.5) {
					resists.add(Types[i]);
				} else if(coverage == 0.25) {
					quadResists.add(Types[i]);
				} else if(coverage == 2) {
					weakTo.add(Types[i]);
				} else if(coverage == 4) {
					quadWeakTo.add(Types[i]);
				} else if(coverage == 0) {
					immunities.add(Types[i]);
				}
			}
		}
	}
	public void printResults() {
		if(neutral.size() + weakTo.size() + quadWeakTo.size() + resists.size() + quadResists.size() + immunities.size() == Types.length) {
			System.out.println("Displaying Results for " + typeDisplay + ":");
		} else {System.out.println("Something went wrong! (Type count mismatch). Displaying results anyway for " + typeDisplay + ":");}
			
		if(neutral.size() > 0) {
			System.out.println("Your Pokemon is damaged normally by the following types: " + neutral);
		} else {System.out.println("Your Pokemon is not damaged normally by any types!");}
		if(weakTo.size() > 0) {
			System.out.println("Your Pokemon is weak to the following types (2x): " + weakTo);
		} else {System.out.println("Your Pokemon has no 2x weaknesses! :)");}
		if(quadWeakTo.size() > 0) {
			System.out.println("Your Pokemon is very weak to the following types (4x): " + quadWeakTo);
		} else {System.out.println("Your Pokemon has no 4x weaknesses! :)");}
		if(resists.size() > 0) {
			System.out.println("Your Pokemon resists the following types (2x): " + resists);
		} else {System.out.println("Your Pokemon has no 2x resistances! :(");}
		if(quadResists.size() > 0) {
			System.out.println("Your Pokemon strongly resists the following types (4x): " + quadResists);
		} else {System.out.println("Your Pokemon has no 4x resistances! :(");}
		if(immunities.size() > 0) {
			System.out.println("Your Pokemon is immune to the following types: " + immunities);
		} else {System.out.println("Your Pokemon has no immunities");}
	}	
}
