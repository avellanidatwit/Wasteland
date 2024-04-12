package game;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class that establishes a deck, for use in the user hand and the overall deck
 * of cards that will be drawn from.
 *
 * @author santorsa
 * @category Cards
 */
public class Deck {
	protected ArrayList<Card> cards;

	/**
	 * No-arg constructor.
	 */
	public Deck() {
		this.cards = new ArrayList<>();
	}

	/**
	 * Gets cards in the deck.
	 *
	 * @return Returns the cards in the deck.
	 */
	public ArrayList<Card> getCards() {
		return this.cards;
	}

	/**
	 * Takes a deck, sets this deck to the input, and shuffles
	 */
	public void reshuffleDeck(ArrayList<Card> list) {
		this.cards = new ArrayList<>(list);
		this.shuffleDeck();
	}

	/**
	 * Checks if the deck is empty
	 *
	 * @return True if empty. False if not.
	 */
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	/**
	 * Checks if the deck is empty
	 *
	 * @return True if empty. False if not.
	 */
	public void emptyDeck() {
		cards.clear();
	}

	/**
	 * Adds a card to the deck.
	 *
	 * @param c Card to add.
	 */
	public void addCard(Card c) {
		this.cards.add(c);
	}

	/**
	 * Removes a card from the deck.
	 *
	 * @param c Card to remove.
	 */
	public void removeCard(Card c) {
		this.cards.remove(c);
	}

	/**
	 * Shuffles the deck.
	 */
	public void shuffleDeck() {
		Collections.shuffle(this.cards);
	}

	/**
	 * Draws the top card of the deck.
	 *
	 * @return Top card of the deck
	 */
	public Card drawCard() {
		Card card = this.cards.get(cards.size() - 1);
		this.cards.remove(card);
		return card;
	}

	/**
	 * Returns and removes a card in the deck after sorting by priority.
	 *
	 * @return First card in the priority.
	 */
	public Card getCard() {
		ArrayList<Card> list = new ArrayList<>(this.cards);
		Collections.shuffle(list);
		list.sort(new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				return o2.priority - o1.priority;
			}
		});
		Card card = list.get(0);
		this.removeCard(card);
		return card;
	}

	@Override
	public String toString() {
		return "Cards: " + this.cards;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Deck) {
			Deck temp1 = this;
			Deck temp2 = (Deck) o;
			temp1.cards.sort(null);
			temp2.cards.sort(null);
			if (temp1.cards.equals(temp2.cards)) {
				return true;
			}
		}
		return false;

	}
}
