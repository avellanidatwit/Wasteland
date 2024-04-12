package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Class to create all the cards locally, because local object storage is hard
 * in literally everything without native JSON support.
 *
 * @author santorsa
 *
 * @category Cards
 */
class CardCreator {
	private HashMap<String, Card> cardData;
	private HashMap<ArrayList<String>, Card> recipes = new HashMap<>();
	private HashMap<String, Card> hammerList = new HashMap<>();

	// Static variable reference of single_instance
	// of type Singleton
	private static CardCreator single_instance = null;

	// Constructor
	// Here we will be creating private constructor
	// restricted to this class itself
	/**
	 * Private 0-arg constructor for creating cards
	 */
	@SuppressWarnings("serial")
	private CardCreator() {
		cardData = new HashMap<>() {
			{
				put("Forest Booster", new Card("Forest Booster", "/Forest.png", 1));
				put("Stick", new Card("Stick", "/Stick.png", 3));
				put("Log", new Card("Log", "/Log.png", 3));
				put("Stone", new Card("Stone", "/Stone.png", 3));
				put("Sharp Stone", new Card("Sharp Stone", "/Sharp Stone.png", 3));
				put("Axe", new Card("Axe", "/Axe.png", 3));
				put("Rope", new Card("Rope", "/Rope.png", 3));
				put("Bandage", new Card("Bandage", "/Bandage.png", 5));
				put("Apple", new Card("Apple", "/Apple.png", 5));
				put("Seedling", new Card("Seedling", "/Seedling3.png", 3));
				put("Tree", new Card("Tree", "/Tree.png", 3));
				put("Glass", new Card("Glass", "/Glass.png", 3));
				put("Hammer", new Card("Hammer", "/Hammer.png", 1));
				put("Ore Vein", new Card("Ore Vein", "/Ore.png", 3));
			}
		};

		// Crafting recipes
		recipes.put(new ArrayList<String>() {
			{
				add("Stick");
				add("Sharp Stone");
			}
		}, createCard("Axe"));
		recipes.put(new ArrayList<String>() {
			{
				add("Rope");
				add("Rope");
			}
		}, createCard("Bandage"));
		recipes.put(new ArrayList<String>() {
			{
				add("Stone");
				add("Stick");
			}
		}, createCard("Hammer"));
		recipes.put(new ArrayList<String>() {
			{
				add("Tree");
				add("Tree");
			}
		}, createCard("Forest Booster"));

		// Hammer setup
		hammerList.put("Stone", createCard("Glass"));
	}

	/**
	 * Creates a card after the initial constructor run.
	 *
	 * @param name Name of card.
	 * @return The card.
	 */
	public Card createCard(String name) {
		Card card = cardData.get(name);
		return new Card(card.getName(), card.getImage(), card.getPriority());
	}

	public Card canHammer(Card card) {
		return hammerList.get(card.getName());
	}

	/**
	 * Checks if a card can craft, and if it can it returns the recipes that are
	 * available for it.
	 *
	 * @param card1 Card 1 to check with.
	 * @param card2 Card 2 to check with.
	 * @return Recipe list(?)
	 */
	public Card canCraft(Card card1, Card card2) {
		@SuppressWarnings("serial")
		ArrayList<String> input = new ArrayList<>() {
			{
				add(card1.getName());
				add(card2.getName());
			}
		};
		Collections.sort(input);
		for (ArrayList<String> key : recipes.keySet()) {
			ArrayList<String> temp = new ArrayList<>(key);
			Collections.sort(temp);
			if (input.equals(temp)) {
				return recipes.get(key);
			}
		}
		return null;
	}

	/**
	 * Static method for singleton initialization
	 *
	 * @return An instance of Card Creator.
	 */
	public static synchronized CardCreator getInstance() {
		if (single_instance == null) {
			single_instance = new CardCreator();
		}

		return single_instance;
	}
}
