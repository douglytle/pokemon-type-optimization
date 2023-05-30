import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.io.*;

import Types.PType;

public class OffensiveCoverageChecker {
	
	public static String[] Types = {"Normal", "Fire", "Water", "Electric", "Grass", "Ice", "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"};
	public PType[] typelist;
	public ArrayList<String> monotypeNeutral;
	public ArrayList<String> monotypeResists;
	public ArrayList<String> monotypeWeakTo;
	public ArrayList<String> monotypeImmunities;
	public ArrayList<ArrayList<String>> neutral;
	public ArrayList<ArrayList<String>> resists;
	public ArrayList<ArrayList<String>> weakTo;
	public ArrayList<ArrayList<String>> quadResists;
	public ArrayList<ArrayList<String>> quadWeakTo;
	public ArrayList<ArrayList<String>> immunities;
	public ArrayList<ArrayList<PType>> combinationsList;
	String typeDisplay;

	public OffensiveCoverageChecker(ArrayList<PType> types, PType[] typelist) {
		
		this.typelist = typelist;
		monotypeNeutral = new ArrayList<String>();
		monotypeResists = new ArrayList<String>();
		monotypeWeakTo = new ArrayList<String>();
		monotypeImmunities = new ArrayList<String>();
		neutral = new ArrayList<ArrayList<String>>();
		resists = new ArrayList<ArrayList<String>>();
		weakTo = new ArrayList<ArrayList<String>>();
		quadResists = new ArrayList<ArrayList<String>>();
		quadWeakTo = new ArrayList<ArrayList<String>>();
		immunities = new ArrayList<ArrayList<String>>();
		
		combinationsList = generateTypeCombinations();
		
		if(types.size() == 1) {
			double[] coverage = types.get(0).getOffensiveCoverage();
			for(int i = 0; i < Types.length; i++) {
				if(coverage[i] == 1) {
					monotypeNeutral.add(Types[i]);
				} else if(coverage[i] == 0.5) {
					monotypeResists.add(Types[i]);
				} else if(coverage[i] == 2) {
					monotypeWeakTo.add(Types[i]);
				} else if(coverage[i] == 0) {
					monotypeImmunities.add(Types[i]);
				}
			}
			int typeID = types.get(0).getID();
			for(int i = 0; i < combinationsList.size(); i++) {
				double[] defensiveCoverage = generateCombinedDefensiveCoverage(combinationsList.get(i));
				if(defensiveCoverage[typeID] == 1) {
					ArrayList<String> combination = new ArrayList<String>();
					combination.add(combinationsList.get(i).get(0).toString());
					combination.add(combinationsList.get(i).get(1).toString());
					neutral.add(combination);
				} else if(defensiveCoverage[typeID] == 0.5) {
					ArrayList<String> combination = new ArrayList<String>();
					combination.add(combinationsList.get(i).get(0).toString());
					combination.add(combinationsList.get(i).get(1).toString());
					resists.add(combination);
				} else if(defensiveCoverage[typeID] == 0.25) {
					ArrayList<String> combination = new ArrayList<String>();
					combination.add(combinationsList.get(i).get(0).toString());
					combination.add(combinationsList.get(i).get(1).toString());
					quadResists.add(combination);
				} else if(defensiveCoverage[typeID] == 2) {
					ArrayList<String> combination = new ArrayList<String>();
					combination.add(combinationsList.get(i).get(0).toString());
					combination.add(combinationsList.get(i).get(1).toString());
					weakTo.add(combination);
				} else if(defensiveCoverage[typeID] == 4) {
					ArrayList<String> combination = new ArrayList<String>();
					combination.add(combinationsList.get(i).get(0).toString());
					combination.add(combinationsList.get(i).get(1).toString());
					quadWeakTo.add(combination);
				} else if(defensiveCoverage[typeID] == 0) {
					ArrayList<String> combination = new ArrayList<String>();
					combination.add(combinationsList.get(i).get(0).toString());
					combination.add(combinationsList.get(i).get(1).toString());
					immunities.add(combination);
				}
			}
		}
	}
	
	public void printResults() {
		System.out.println("\nVS Monotypes:\n");
		if(monotypeNeutral.size() > 0) {
			System.out.println("Your move deals normal damage to the following types: " + monotypeNeutral);
		} else {System.out.println("Your move does not deal normal damage to any types!");}
		if(monotypeWeakTo.size() > 0) {
			System.out.println("Your move is super effective (2x) against the following types: " + monotypeWeakTo);
		} else {System.out.println("Your move is not super effective (2x) against any types");}
		if(monotypeResists.size() > 0) {
			System.out.println("Your move is not very effective (0.5x) against the following types: " + monotypeResists);
		} else {System.out.println("Your move is not 0.5x effective against any types");}
		if(monotypeImmunities.size() > 0) {
			System.out.println("Your move deals 0 damage to the following types: " + monotypeImmunities);
		} else {System.out.println("No types are immune to your move");}
		
		System.out.println("\nVS Dual-Types\n");
		System.out.println("Your move deals neutral damage against " + neutral.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)neutral.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move is super-effective (2x) against " + weakTo.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)weakTo.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move is SUPER-EFFECTIVE (4x) against " + quadWeakTo.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)quadWeakTo.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move is not very effective (0.5x) against " + resists.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)resists.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move is NOT VERY EFFECTIVE (0.25x) against " + quadResists.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)quadResists.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move deals 0 damage against " + immunities.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)immunities.size() / 153) * 100)*100)/100 + "%)");
		//if(neutral.size() > 0) {
		//	System.out.println("Your move deals normal damage to the following types: " + neutral);
		//} else {System.out.println("Your move does not deal normal damage to any types!");}
		//if(weakTo.size() > 0) {
		//	System.out.println("Your move is super effective against the following types (2x): " + weakTo);
		//} else {System.out.println("Your move is not super effective against any types (2x)");}
		//if(quadWeakTo.size() > 0) {
		//	System.out.println("Your move is SUPER EFFECTIVE against the following types (4x): " + quadWeakTo);
		//} else {System.out.println("Your move is not quad effective against any types (4x)");}
		//if(resists.size() > 0) {
		//	System.out.println("Your move is not very effective against the following types (0.5x): " + resists);
		//} else {System.out.println("Your move is not 0.5x effective against any types");}
		//if(quadResists.size() > 0) {
		//	System.out.println("Your move is strongly resisted by the following types (0.25x): " + quadResists);
		//} else {System.out.println("Your move is not strongly resisted by any types");}
		//if(immunities.size() > 0) {
		//	System.out.println("Your move deals 0 damage to the following types: " + immunities);
		//} else {System.out.println("No types are immune to your move");}
	}
	
	public void printResultsToFile() {
		try {
			FileWriter writer = new FileWriter("results.txt");
			writer.write("neutral:\n\n");
			writer.append(neutral.toString());
			writer.append("\n\nse:\n\n");
			writer.append(weakTo.toString());
			writer.append("\n\n\nsse:\n\n");
			writer.append(quadWeakTo.toString());
			writer.append("\n\nnve:\n\n");
			writer.append(resists.toString());
			writer.append("\n\nnnve:\n\n");
			writer.append(quadResists.toString());
			writer.append("\n\nimmune:\n\n");
			writer.append(immunities.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<ArrayList<PType>> generateTypeCombinations(){
		ArrayList<ArrayList<PType>> combinationsList = new ArrayList<ArrayList<PType>>();
		for(int i = 0; i < Types.length; i++) {
			for(int j = 0; j < Types.length; j++) {
				if(i != j) {
					ArrayList<PType> combination = new ArrayList<PType>();
					if(typelist[i].getID() < typelist[j].getID()) {
						combination.add(typelist[i]);
						combination.add(typelist[j]);
					} else {
						combination.add(typelist[j]);
						combination.add(typelist[i]);
					}
					if(! combinationsList.contains(combination)) {
						combinationsList.add(combination);
					}
				}
			}
		}
		

		try {
			FileWriter writer = new FileWriter("combinations.txt");
			writer.write(combinationsList.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return combinationsList;
	}
	
	public double[] generateCombinedDefensiveCoverage(ArrayList<PType> types){
		double[] combinedDefensiveCoverage = new double[Types.length];
		double[] coverage1 = types.get(0).getDefensiveCoverage();
		double[] coverage2 = types.get(1).getDefensiveCoverage();
		for(int i = 0; i < Types.length; i++) {
			combinedDefensiveCoverage[i] = coverage1[i] * coverage2[i];
		}
		//System.out.println(Arrays.toString(combinedDefensiveCoverage));
		return combinedDefensiveCoverage;
	}
}
