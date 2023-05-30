import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.io.*;

import Types.PType;

public class OffensiveCoverageChecker {
	
	public static String[] Types = {"Normal", "Fire", "Water", "Electric", "Grass", "Ice", "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"};
	public PType[] typelist;
	public String typeDisplay;
	
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
	
	public ArrayList<String> bestOfMonotypeNeutral;
	public ArrayList<String> bestOfMonotypeResists;
	public ArrayList<String> bestOfMonotypeWeakTo;
	public ArrayList<String> bestOfMonotypeImmunities;
	public ArrayList<ArrayList<String>> bestOfNeutral;
	public ArrayList<ArrayList<String>> bestOfResists;
	public ArrayList<ArrayList<String>> bestOfWeakTo;
	public ArrayList<ArrayList<String>> bestOfQuadResists;
	public ArrayList<ArrayList<String>> bestOfQuadWeakTo;
	public ArrayList<ArrayList<String>> bestOfImmunities;

	
	public OffensiveCoverageChecker(ArrayList<PType> types, PType[] typelist) {
		
		this.typelist = typelist;
		combinationsList = generateTypeCombinations();
		typeDisplay = types.toString();
		
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
		
		if(types.size() == 1) {
			generateOffensiveCoverage(types.get(0));
			cloneLists();
		} else {
			generateOffensiveCoverage(types.get(0));
			
			cloneLists();
			clearLists();
			
			for(int i = 1; i < types.size(); i++) {
				generateOffensiveCoverage(types.get(i));
				//mono
				//bestOf lists grab elements they don't yet have
				monotypeWeakTo.forEach((t) -> {if(! bestOfMonotypeWeakTo.contains(t)) {bestOfMonotypeWeakTo.add(t);}});
				monotypeNeutral.forEach((t) -> {if(! bestOfMonotypeNeutral.contains(t)) {bestOfMonotypeNeutral.add(t);}});
				monotypeResists.forEach((t) -> {if(! bestOfMonotypeResists.contains(t)) {bestOfMonotypeResists.add(t);}});
				monotypeImmunities.forEach((t) -> {if(! bestOfMonotypeImmunities.contains(t)) {bestOfMonotypeImmunities.add(t);}});
				//and then 'clean' themselves (remove elements from 'lower' lists present in 'upper' lists)
				bestOfMonotypeNeutral.removeAll(bestOfMonotypeWeakTo);
				bestOfMonotypeResists.removeAll(bestOfMonotypeWeakTo);
				bestOfMonotypeResists.removeAll(bestOfMonotypeNeutral);
				bestOfMonotypeImmunities.removeAll(bestOfMonotypeWeakTo);
				bestOfMonotypeImmunities.removeAll(bestOfMonotypeNeutral);
				bestOfMonotypeImmunities.removeAll(bestOfMonotypeResists);
				//dual
				//grab
				quadWeakTo.forEach((t) -> {if(! bestOfQuadWeakTo.contains(t)) {bestOfQuadWeakTo.add(t);}});
				weakTo.forEach((t) -> {if(! bestOfWeakTo.contains(t)) {bestOfWeakTo.add(t);}});
				neutral.forEach((t) -> {if(! bestOfNeutral.contains(t)) {bestOfNeutral.add(t);}});
				resists.forEach((t) -> {if(! bestOfResists.contains(t)) {bestOfResists.add(t);}});
				quadResists.forEach((t) -> {if(! bestOfQuadResists.contains(t)) {bestOfQuadResists.add(t);}});
				immunities.forEach((t) -> {if(! bestOfImmunities.contains(t)) {bestOfImmunities.add(t);}});
				//clean
				bestOfImmunities.removeAll(bestOfQuadResists);
				bestOfImmunities.removeAll(bestOfResists);
				bestOfImmunities.removeAll(bestOfNeutral);
				bestOfImmunities.removeAll(bestOfWeakTo);
				bestOfImmunities.removeAll(bestOfQuadWeakTo);
				bestOfQuadResists.removeAll(bestOfResists);
				bestOfQuadResists.removeAll(bestOfNeutral);
				bestOfQuadResists.removeAll(bestOfWeakTo);
				bestOfQuadResists.removeAll(bestOfQuadWeakTo);
				bestOfResists.removeAll(bestOfNeutral);
				bestOfResists.removeAll(bestOfWeakTo);
				bestOfResists.removeAll(bestOfQuadWeakTo);
				bestOfNeutral.removeAll(bestOfWeakTo);
				bestOfNeutral.removeAll(bestOfQuadWeakTo);
				bestOfWeakTo.removeAll(bestOfQuadWeakTo);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cloneLists() {
		bestOfMonotypeNeutral = (ArrayList<String>) monotypeNeutral.clone();
		bestOfMonotypeResists = (ArrayList<String>) monotypeResists.clone();
		bestOfMonotypeWeakTo = (ArrayList<String>) monotypeWeakTo.clone();
		bestOfMonotypeImmunities = (ArrayList<String>) monotypeImmunities.clone();
		bestOfNeutral = (ArrayList<ArrayList<String>>) neutral.clone();
		bestOfResists = (ArrayList<ArrayList<String>>) resists.clone();
		bestOfWeakTo = (ArrayList<ArrayList<String>>) weakTo.clone();
		bestOfQuadResists = (ArrayList<ArrayList<String>>) quadResists.clone();
		bestOfQuadWeakTo = (ArrayList<ArrayList<String>>) quadWeakTo.clone();
		bestOfImmunities = (ArrayList<ArrayList<String>>) immunities.clone();
	}
	
	public void clearLists() {
		monotypeNeutral.clear();
		monotypeResists.clear();
		monotypeWeakTo.clear();
		monotypeImmunities.clear();
		neutral.clear();
		resists.clear();
		weakTo.clear();
		quadResists.clear();
		quadWeakTo.clear();
		immunities.clear();
	}
	
	public void generateOffensiveCoverage(PType type) {
		double[] coverage = type.getOffensiveCoverage();
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
		int typeID = type.getID();
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
	
	public void printResults() {
		System.out.println("Displaying best-case results for " + typeDisplay + " movepool:");
		System.out.println("\nVS Monotypes:\n");
		if(bestOfMonotypeNeutral.size() > 0) {
			System.out.println("Your move(s) deal normal damage to the following types: " + bestOfMonotypeNeutral);
		} else {System.out.println("Your move(s) do not deal normal damage to any types!");}
		if(bestOfMonotypeWeakTo.size() > 0) {
			System.out.println("Your move(s) are super effective (2x) against the following types: " + bestOfMonotypeWeakTo);
		} else {System.out.println("Your move(s) are not super effective (2x) against any types");}
		if(bestOfMonotypeResists.size() > 0) {
			System.out.println("Your move(s) are not very effective (0.5x) against the following types: " + bestOfMonotypeResists);
		} else {System.out.println("Your move(s) are not 0.5x effective against any types");}
		if(bestOfMonotypeImmunities.size() > 0) {
			System.out.println("Your move(s) deal 0 damage to the following types: " + bestOfMonotypeImmunities);
		} else {System.out.println("No types are immune to your move(s)");}
		
		System.out.println("\nVS Dual-Types:\nSee \"results.txt\" for detailed information\n");
		System.out.println("Your move(s) deal neutral damage against " + bestOfNeutral.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)bestOfNeutral.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move(s) are super-effective (2x) against " + bestOfWeakTo.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)bestOfWeakTo.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move(s) are SUPER-EFFECTIVE (4x) against " + bestOfQuadWeakTo.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)bestOfQuadWeakTo.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move(s) are not very effective (0.5x) against " + bestOfResists.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)bestOfResists.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move(s) are NOT VERY EFFECTIVE (0.25x) against " + bestOfQuadResists.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)bestOfQuadResists.size() / 153) * 100)*100)/100 + "%)");
		System.out.println("Your move(s) deal 0 damage against " + bestOfImmunities.size() + " out of the 153 possible type combinations (" + (double)Math.round((((double)bestOfImmunities.size() / 153) * 100)*100)/100 + "%)");
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
			writer.append(bestOfNeutral.toString());
			writer.append("\n\nse:\n\n");
			writer.append(bestOfWeakTo.toString());
			writer.append("\n\n\nsse:\n\n");
			writer.append(bestOfQuadWeakTo.toString());
			writer.append("\n\nnve:\n\n");
			writer.append(bestOfResists.toString());
			writer.append("\n\nnnve:\n\n");
			writer.append(bestOfQuadResists.toString());
			writer.append("\n\nimmune:\n\n");
			writer.append(bestOfImmunities.toString());
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
