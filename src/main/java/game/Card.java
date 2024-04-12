package game;

/**
 * Represents a card, has a series of getters and setters for information.
 *
 * @author Dominic Avellani
 * @author evelyn
 * @category Cards
 */
public final class Card {
	public String NAME;
	public String IMAGE;

	public int priority;

	public int uses, maxUses;

	public Action action;

	/**
	 * 0-arg constructor
	 */
	public Card() {
	}

	/**
	 * 3-arg Constructor
	 *
	 * @param name     Name of the card.
	 * @param image    Image of the card.
	 * @param priority Card priority when played
	 */
	public Card(String name, String image, int priority) {
		this.NAME = name;
		this.IMAGE = image;
		this.priority = priority;
		this.action = Action.NONE;
	}

	/**
	 * Gets the name of the card.
	 *
	 * @return Name of the card.
	 */
	public String getName() {
		return this.NAME;
	}

	/**
	 * Gets the priority of the card.
	 *
	 * @return Description of the card.
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * Gets the name of the card.
	 *
	 * @return Name of the card.
	 */
	public String getImage() {
		return this.IMAGE;
	}

	@Override
	public String toString() {
		return this.NAME;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Card) {
			Card temp = (Card) o;
			if (temp.getName().equals(this.NAME)) {
				return true;
			}
		}
		return false;
	}
}
