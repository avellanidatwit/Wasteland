package game;

/**
 * A class to represent a player's id and a number.
 *
 * @author santorsa
 */
public class Number {
	public int playerID;
	public int num;

	/**
	 * 0-arg constructor
	 */
	public Number() {
	}

	/**
	 * 2 arg constructor
	 * 
	 * @param num      Number to set.
	 * @param playerID ID to set.
	 */
	public Number(int num, int playerID) {
		this.playerID = playerID;
		this.num = num;
	}
}
