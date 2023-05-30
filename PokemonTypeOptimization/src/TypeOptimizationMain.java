import Types.*;
import java.util.Scanner;
import java.util.ArrayList;

public class TypeOptimizationMain {
	public static final String[] Types = {"normal", "fire", "water", "electric", "grass", "ice", "fighting", "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon", "dark", "steel", "fairy"};
	public static final PType[] typelist = {new NormalType(), new FireType(), new WaterType(), new ElectricType(), new GrassType(), new IceType(), new FightingType(), new PoisonType(), new GroundType(),
			new FlyingType(), new PsychicType(), new BugType(), new RockType(), new GhostType(), new DragonType(), new DarkType(), new SteelType(), new FairyType()};
	public static String typeInput;
	public static String modeSelect;
	public static void main(String[] args) {
		while(true) {
			Scanner scan = new Scanner(System.in);
			System.out.println("\nSelect mode: o for offensive, d for defensive");
			modeSelect = scan.nextLine().toLowerCase();
			if(modeSelect.contains("o")) {
				while(true) {
					System.out.println("\nEnter up to 4 types: ");
					typeInput = scan.nextLine().toLowerCase();
					if(typeInput.contains("stop")) {
						break;
					}
					ArrayList<PType> parsedTypes = new ArrayList<PType>();
					for(int i = 0; i < Types.length; i++) {
						if(typeInput.contains(Types[i])) {
							parsedTypes.add(typelist[i]);
						}
					}
					if(parsedTypes.size() == 1 || parsedTypes.size() == 2 || parsedTypes.size() == 3 || parsedTypes.size() == 4) {
						OffensiveCoverageChecker o = new OffensiveCoverageChecker(parsedTypes, typelist);
						o.printResults();
						o.printResultsToFile();
					} else if(parsedTypes.size() == 0){
						System.out.println("No valid types found in input. Try again");
					} else {
						System.out.println("Only up to 4 moves at a time is currently supported");
					}
					
				}
			} else if(modeSelect.contains("d")) {
				while(true) {
					System.out.println("\nEnter up to 2 types: ");
					typeInput = scan.nextLine().toLowerCase();
					if(typeInput.contains("stop")) {
						break;
					}
					ArrayList<PType> parsedTypes = new ArrayList<PType>();
					for(int i = 0; i < Types.length; i++) {
						if(typeInput.contains(Types[i])) {
							parsedTypes.add(typelist[i]);
						}
					}
					if(parsedTypes.size() == 1 || parsedTypes.size() == 2) {
						DefensiveCoverageChecker d = new DefensiveCoverageChecker(parsedTypes);
						d.printResults();
					} else if(parsedTypes.size() == 0){
						System.out.println("No valid types found in input. Try again");
					} else {System.out.println("Only Pokemon with 1 or 2 types are currently supported");}
					System.out.println("");
				}
			}
		}
	}
}
