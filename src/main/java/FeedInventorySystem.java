import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import model.Animal;
import model.Zoo;

public class FeedInventorySystem {

	private static Map<Zoo, Set<Animal>> zoos = new TreeMap<Zoo, Set<Animal>>();
	private static Zoo selectedZoo = null;
	private static Animal selectedAnimal = null;

	public static void main(String[] args) {
		// before we begin lets create some dummy zoo's and animals to play
		// with...

		for (int i = 0; i < 215; i++) {
			Zoo zoo = new Zoo();
			zoo.setName("Zoo " + i);
			zoo.setInventory(ThreadLocalRandom.current().nextInt(0, 100));
			zoo.setWaste(0);

			// animals are the same across all zoos but have differing
			// properites...
			Set<Animal> animals = new TreeSet<Animal>();
			animals.add(new Animal(Animal.Species.BIRD, "Cockatoo", ThreadLocalRandom.current().nextInt(1, 2)));
			animals.add(new Animal(Animal.Species.BIRD, "Galah", ThreadLocalRandom.current().nextInt(1, 2)));
			animals.add(new Animal(Animal.Species.MARSUPIAL, "Echidna", ThreadLocalRandom.current().nextInt(1, 2)));
			animals.add(new Animal(Animal.Species.MARSUPIAL, "Kangaroo", ThreadLocalRandom.current().nextInt(2, 10)));
			animals.add(new Animal(Animal.Species.MARSUPIAL, "Koala", ThreadLocalRandom.current().nextInt(2, 5)));
			animals.add(new Animal(Animal.Species.BIRD, "Emu", ThreadLocalRandom.current().nextInt(2, 10)));
			animals.add(new Animal(Animal.Species.CANINE, "Dingo", ThreadLocalRandom.current().nextInt(5, 10)));
			animals.add(new Animal(Animal.Species.MARSUPIAL, "Tasmanian Devil",
					ThreadLocalRandom.current().nextInt(5, 10)));
			animals.add(new Animal(Animal.Species.MYTHICAL, "Bunyip", ThreadLocalRandom.current().nextInt(10, 20)));

			zoos.put(zoo, animals);
		}

		System.out.println("The International Zoo Institute (IZI)");
		System.out.println("Feed Inventory Managment System");
		System.out.println("");

		String help = "You can use the following commands\r\n" + "----------------------------------\r\n"
				+ "help : Display help menu\r\n" + "zoos : Display list of Zoos\r\n" 
				+ "select : Select a Zoo\r\n"
				+ "which : Display which Zoo is currently selected\r\n"
				+ "animals : Displays animals in selected Zoo\r\n"
				+ "understock : Displays Zoos that need more feed\r\n" 
				+ "feed : Enter animal feed\r\n"
				+ "feedings : Display selected Zoos feeding data\r\n" 
				+ "restock : Restock a Zoo with feed\r\n"
				+ "quit : exit the inventory management system\r\n";

		System.out.println(help);

		Scanner scanner = new Scanner(System.in);
		try {
			boolean wantsOut = false;

			while (!wantsOut) {
				System.out.print("Enter your command : ");
				String input = scanner.next().toLowerCase();

				switch (input) {
				case "feedings":
					if (selectedZoo == null) {
						selectedZoo = selectZoo(scanner);
					} else {
						System.out.println(selectedZoo.getName() + " animal feedings.");
					}

					Map<Animal.Species, Integer> speciesFeedings = new TreeMap<Animal.Species, Integer>();

					for (Animal animal : zoos.get(selectedZoo)) {
						Integer currentValue = speciesFeedings.get(animal.getSpecies());
						if (currentValue == null)
							currentValue = 0;

						currentValue += animal.getTimesFed();
						speciesFeedings.put(animal.getSpecies(), currentValue);
					}

					System.out.println("Number of feedings by Species");
					for (Animal.Species species : speciesFeedings.keySet()) {
						System.out.println(species + " had " + speciesFeedings.get(species) + " feedings today.");
					}

					break;
				case "restock":
					if (selectedZoo == null) {
						selectedZoo = selectZoo(scanner);
					}

					Integer restockAmount = null;
					while (restockAmount == null) {
						restockAmount = readNumber(scanner,
								"How much feed do you want to restock @ " + selectedZoo.getName() + "? : ");
						if (restockAmount < 0) {
							System.out.println("Please select a positive value.");
							restockAmount = null;
						}
					}

					// when restocking, move inventory to waste and update
					// inventory with restock amount.
					selectedZoo.setWaste(selectedZoo.getWaste() + selectedZoo.getInventory());
					selectedZoo.setInventory(restockAmount);

					break;
				case "feed":
					if (selectedZoo == null) {
						selectedZoo = selectZoo(scanner);
					}

					selectedAnimal = selectAnimal(scanner, selectedZoo);

					Integer feed = null;
					while (feed == null) {
						feed = readNumber(scanner,
								"How much feed do you want to give " + selectedAnimal.getType() + "? : ");
						if (feed < 0) {
							System.out.println("Please select a positive value.");
							feed = null;
						}

						if (feed > selectedZoo.getInventory()) {
							System.out.println("There is not enough feed in the store!");
							feed = null;
						}
					}

					// when feeding the animal, remove feed from stock and
					// update animal stats.
					selectedZoo.setInventory(selectedZoo.getInventory() - feed);
					selectedAnimal.setTimesFed(selectedAnimal.getTimesFed() + 1);
					selectedAnimal.setFeedTotal(selectedAnimal.getFeedTotal() + feed);
					if (feed < selectedAnimal.getEatsPerMeal()) {
						selectedAnimal.setUnderFed(selectedAnimal.getUnderFed() + 1);
					} else if (feed > selectedAnimal.getEatsPerMeal()) {
						selectedAnimal.setOverFed(selectedAnimal.getOverFed() + 1);
					}

					break;
				case "understock":

					int i = 0;
					DecimalFormat df = new DecimalFormat("0.00");
					for (Zoo zoo : zoos.keySet()) {
						Integer requiredFeed = 0;

						for (Animal animal : zoos.get(zoo)) {
							requiredFeed += animal.getEatsPerMeal();
						}

						double daysOnHand = zoo.getInventory() / Double.valueOf(requiredFeed);
						if (daysOnHand < 1) {
							System.out.println(i + " : " + zoo.getName() + " has " + df.format(daysOnHand)
									+ " round of meals worth of food in stock!");
						}
						i++;
					}

					break;
				case "animals":
					if (selectedZoo == null) {
						selectedZoo = selectZoo(scanner);
					}

					System.out.println("Animals @ " + selectedZoo.getName());
					displayAnimals(selectedZoo);

					break;
				case "which":
					if (selectedZoo == null) {
						System.out.println("No Zoo is currently selected");
					} else {
						System.out.println(selectedZoo.getName() + " selected");
					}
					break;
				case "select":
					selectedZoo = selectZoo(scanner);
					break;
				case "zoos":
					displayZoos();
					break;
				case "help":
					System.out.println(help);
					break;
				case "quit":
					wantsOut = true;
					break;
				default:
					System.out.println("Invalid commmand... type \"help\" for options...");

				}
			}
		} finally {
			scanner.close();
		}
	}

	/**
	 * This method allows the user to select a zoo.
	 * 
	 */
	private static Zoo selectZoo(Scanner scanner) {
		// current selections are reset.
		selectedAnimal = null;
		selectedZoo = null;

		displayZoos();
		while (true) {
			Integer index = readNumber(scanner, "Which Zoo would you like to select? : ");
			try {
				Zoo zoo = new ArrayList<Zoo>(zoos.keySet()).get(index);
				System.out.println(zoo.getName() + " selected");
				return zoo;
			} catch (IndexOutOfBoundsException ioobe) {
				System.out.println("Sorry that selection was invalid.");
			}
		}
	}

	/**
	 * This method allows the user to select a zoo.
	 * 
	 */
	private static Animal selectAnimal(Scanner scanner, Zoo zoo) {
		// current selections are reset.
		selectedAnimal = null;

		displayAnimals(zoo);
		while (true) {
			Integer index = readNumber(scanner, "Which Animal would you like to select? : ");
			try {
				Animal animal = new ArrayList<Animal>(zoos.get(zoo)).get(index);
				System.out.println(animal.getType() + " selected");
				return animal;
			} catch (IndexOutOfBoundsException ioobe) {
				System.out.println("Sorry that selection was invalid.");
			}
		}
	}

	/**
	 * Reads a input number in a user friendly manner..
	 */
	private static Integer readNumber(Scanner scanner, String question) {

		while (true) {
			System.out.print(question);
			try {
				Integer input = scanner.nextInt();
				return input;
				// lets catch all invalid inputs....
			} catch (Exception e) {
				System.out.println("Sorry that input was invalid.");
				scanner.next();
			}
		}
	}

	/**
	 * Displays a list of Zoos
	 */
	private static void displayZoos() {
		int i = 0;
		for (Zoo zoo : zoos.keySet()) {
			System.out.println(i + " : " + zoo.getName() + " (has " + zoo.getInventory() + "kg of inventory and "
					+ zoo.getWaste() + "kg of waste)");
			i++;
		}
	}

	/**
	 * Displays a list of Animals
	 * 
	 * @param zoo
	 *            Zoo of animals to list.
	 */
	private static void displayAnimals(Zoo zoo) {
		int i = 0;
		for (Animal animal : zoos.get(zoo)) {

			Integer feedPercentage = Double.valueOf(
					((animal.getFeedTotal() / Double.valueOf(animal.getTimesFed())) * 100 / animal.getEatsPerMeal()))
					.intValue();
			System.out.println(i + " : " + animal.getType() + " (" + animal.getSpecies() + " : eats "
					+ animal.getEatsPerMeal() + "kg of feed per meal and has been fed " + animal.getTimesFed()
					+ " times today, eating average : " + feedPercentage + "%)");
			i++;
		}
	}
}
