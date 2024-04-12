package game;

/**
 * Enum for the purpose of controlling the turns
 *
 * @author santorsa
 * @category GameSystems
 * @category Networking
 */
public enum TurnStates {
	/**
	 * Waiting for opponent
	 */
	WAITINGFORPLAYER2,
	/**
	 * Waiting for opponent
	 */
	STARTGAME,
	/**
	 * Activates the 1st user's turn.
	 */
	PLAYER1TURN,
	/**
	 * Activates the 2nd user's turn.
	 */
	PLAYER2TURN,
	/**
	 * Player 1 wins
	 */
	PLAYER1WINS,
	/**
	 * Player 2 wins
	 */
	PLAYER2WINS
}
