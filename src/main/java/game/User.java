package game;


import java.util.ArrayList;
import java.util.Random;

/**
 * Class to establish a user's identity and hand.
 *
 * @author evelyn
 * @category GameSystems
 */
public final class User {

	final protected String USERNAME;
	protected Deck pile;
	protected Deck hand;
	protected Deck trash;
	protected static int totalId = 0;
	protected int id;

	public User() {
		this.USERNAME = null;
		this.pile = null;
		this.trash = null;
		this.id = -1;
	}

	/**
	 * 1-arg Constructor
	 *
	 * @param username A user's username.
	 */
	public User(String username) {
		// Create id and username
		this.id = totalId;
		totalId++;
		this.USERNAME = username;

		// Add the forest booster to starter deck
		this.pile = new Deck();
		pile.addCard(CardCreator.getInstance().createCard("Forest Booster"));
		pile.shuffleDeck();

		// Create the deck objects for the player
		this.trash = new Deck();
		this.hand = new Deck();
	}

	/**
	 * Returns the user's username.
	 *
	 * @return The user's username.
	 */
	public String getUsername() {
		return this.USERNAME;
	}

	/**
	 * Draws a card from the pile. If the pile is empty, it reshuffles the discard
	 * and adds it to the pile.
	 */
	public Card drawCard() {
		if (!pile.isEmpty()) {
			Card card = pile.drawCard();
			hand.addCard(card);
			return card;
		} else {
			return null;
		}
	}

	/**
	 * Removes card from hand and adds to bottom of the pile.
	 */
	public void discardCard(Card card) {
		moveToPile(card);
		hand.removeCard(card);
	}

	/**
	 * Moves a card to the pile and shuffles it.
	 */
	public void moveToPile(Card card) {
		pile.addCard(card);
		pile.shuffleDeck();
	}

	/**
	 * Moves a card to the hand and shuffles it.
	 */
	public void moveToHand(Card card) {
		hand.addCard(card);
	}

	/**
	 * Trashes a card in the pile.
	 */
	public Card destroyPileCard() {
		Card card = pile.getCard();
		trash.addCard(card);
		return card;
	}

	public ArrayList<Card> ForestBooster() {
		ArrayList<Card> list = new ArrayList<>();
		list.add(booster("Apple"));
		list.add(booster("Ore Vein"));
		list.add(booster("Seedling"));
		list.add(booster("Seedling"));
		list.add(booster("Tree"));
		list.add(booster("Rope"));
		list.add(booster("Rope"));
		list.add(booster("Sharp Stone"));
		list.add(booster("Stick"));

		for (int i = 0; i < 3; i++) {
			switch (randomIntRange(1, 3)) {
			case 1:
				list.add(booster("Log"));
				break;
			case 2:
				list.add(booster("Stone"));
				break;
			case 3:
				list.add(booster("Rope"));
				break;
			}
		}

		for (int i = 0; i < randomIntRange(3, 4); i++) {
			switch (randomIntRange(1, 2)) {
			case 1:
				list.add(booster("Log"));
				break;
			case 2:
				list.add(booster("Stick"));
				break;
			}
		}
		for (int i = 0; i < randomIntRange(3, 4); i++) {
			switch (randomIntRange(1, 2)) {
			case 1:
				list.add(booster("Stone"));
				break;
			case 2:
				list.add(booster("Sharp Stone"));
				break;
			}
		}
		return list;
	}

	public Card booster(String name) {
		Card card = CardCreator.getInstance().createCard(name);
		moveToPile(card);
		return card;
	}

	public ArrayList<Card> Log() {
		int range = randomIntRange(1, 3);
		ArrayList<Card> list = new ArrayList<>();
		Card card;
		if (range == 1 || range == 2) {
			for (int i = 0; i < 3; i++) {
				card = CardCreator.getInstance().createCard("Stick");
				moveToPile(card);
				list.add(card);
			}
		} else {
			for (int i = 0; i < 2; i++) {
				card = CardCreator.getInstance().createCard("Stick");
				moveToPile(card);
				list.add(card);
			}
		}
		return list;
	}

	public ArrayList<Card> Tree() {
		ArrayList<Card> list = new ArrayList<>();
		Card card;
		switch (randomIntRange(1, 3)) {
		case 1:
			card = CardCreator.getInstance().createCard("Log");
			moveToHand(card);
			list.add(card);
			break;
		case 2:
			card = CardCreator.getInstance().createCard("Apple");
			moveToHand(card);
			list.add(card);
			break;
		case 3:
			card = CardCreator.getInstance().createCard("Stick");
			moveToHand(card);
			list.add(card);
			card = CardCreator.getInstance().createCard("Stick");
			moveToHand(card);
			list.add(card);
			break;
		}
		return list;
	}

	public ArrayList<Card> OreVein() {
		ArrayList<Card> list = new ArrayList<>();
		Card card;
		switch (randomIntRange(1, 3)) {
		case 1:
			card = CardCreator.getInstance().createCard("Stone");
			moveToHand(card);
			list.add(card);
			break;
		case 2:
			card = CardCreator.getInstance().createCard("Stone");
			moveToHand(card);
			list.add(card);
			card = CardCreator.getInstance().createCard("Stone");
			moveToHand(card);
			list.add(card);
			break;
		case 3:
			card = CardCreator.getInstance().createCard("Sharp Stone");
			moveToHand(card);
			list.add(card);
			card = CardCreator.getInstance().createCard("Sharp Stone");
			moveToHand(card);
			list.add(card);
			break;
		}
		return list;
	}

	public ArrayList<Card> Stone() {
		int range = randomIntRange(2, 3);
		ArrayList<Card> list = new ArrayList<>();
		Card card;
		for (int i = 0; i < range; i++) {
			card = CardCreator.getInstance().createCard("Sharp Stone");
			moveToPile(card);
			list.add(card);
		}
		return list;
	}

	public ArrayList<Card> Bandage() {
		ArrayList<Card> list = new ArrayList<>();
		Card card;
		for (int i = 0; i < 2; i++) {
			card = CardCreator.getInstance().createCard("Bandage");
			moveToPile(card);
			list.add(card);
		}
		return list;
	}

	public ArrayList<Card> Apple() {
		ArrayList<Card> list = new ArrayList<>();
		Card card = CardCreator.getInstance().createCard("Seedling");
		moveToPile(card);
		list.add(card);
		return list;
	}

	public ArrayList<Card> Stick() {
		ArrayList<Card> list = new ArrayList<>();
		switch (pile.cards.size()) {
		case 0:
			list = null;
			break;
		case 1:
			list.add(pile.getCard());
			break;
		default:
			list.add(pile.getCard());
			list.add(pile.getCard());
			break;
		}
		return list;
	}

	public Card SharpStone() {
		Card card;
		if (hand.cards.size() == 0) {
			return null;
		} else {
			card = hand.getCard();
			return card;
		}
	}

	public ArrayList<Card> Rope() {
		ArrayList<Card> list = new ArrayList<>();
		switch (hand.cards.size()) {
		case 0:
			list = null;
			break;
		case 1:
			Card card = hand.getCard();
			list.add(card);
			discardCard(card);
			break;
		default:
			card = hand.getCard();
			list.add(card);
			this.discardCard(card);
			card = hand.getCard();
			list.add(card);
			this.discardCard(card);
			break;
		}
		return list;
	}

	public Card Seedling() {
		Card card = CardCreator.getInstance().createCard("Tree");
		moveToPile(card);
		return card;
	}

	public ArrayList<Card> Axe() {
		ArrayList<Card> list = new ArrayList<>();
		if (pile.cards.size() == 0) {
			return null;
		} else {
			for (int i = 0; i < 10; i++) {
				if (pile.cards.size() > 0) {
					list.add(pile.getCard());
				} else {
					break;
				}
			}
			return list;
		}
	}

	/**
	 * Generates a random number between a min and max(inclusive)
	 *
	 * @param min
	 * @param max
	 * @return number between min and max
	 */
	public int randomIntRange(int start, int end) {
		Random random = new Random();
		int number = random.nextInt((end - start) + 1) + start;
		return number;
	}

	public boolean checkLoss() {
		if (hand.cards.size() < 1 && pile.cards.size() < 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Username: " + this.USERNAME;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			User temp = (User) o;
			if (this.USERNAME.equals(temp.getUsername())) {
				return true;
			}
		}
		return false;
	}
}
