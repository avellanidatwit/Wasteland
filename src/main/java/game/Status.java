package game;

/**
 * Class that manages the turn state.
 *
 * @author santorsa
 * @category GameSystems
 */
public class Status {
	public TurnStates state;

	/**
	 * 0 arg constructor
	 */
	public Status() {
	}

	/**
	 * 1 arg constructor
	 *
	 * @param state The current turn state.
	 */
	public Status(TurnStates state) {
		this.state = state;
	}
}
