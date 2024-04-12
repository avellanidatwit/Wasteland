package game;


import java.io.IOException;
import java.net.InetAddress;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener.TypeListener;

import javafx.application.Platform;

/**
 * A players connection to the game server.
 *
 * @author Dominic Avellani
 * @author evelyn
 * @category Networking
 */
public class JavaClient {
	public Client client;

	// A reference to the GUI script
	public Main gui;

	public JavaClient(Main gui) throws IOException {

		this.gui = gui;

		client = new Client();
		// Registering the classes to be sent over the network
		Network.register(client);
		InetAddress address = client.discoverHost(54777, 5000);

		Kryo kryo = client.getKryo();
		kryo.setRegistrationRequired(false);

		client.start();
		client.connect(5000, address, 54555, 54777);

		setupController();
	}

	// Server response handling
	public void setupController() {
		TypeListener typeListener = new TypeListener();

		// When the server sends a card object, the player draw the card.
		typeListener.addTypeHandler(Card.class, (connection, card) -> {
			if (card.action == Action.DISCARD) {
				card.action = Action.NONE;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						gui.removeCardFromHand(card);
					}
				});
			} else {
				// System.out.println("addCardToHand");
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						gui.addCardToHand(card);
					}
				});
			}
		});

		// When the server sends a text object, the user gets a notification.
		typeListener.addTypeHandler(Text.class, (connection, text) -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gui.textNotification(text.message);
				}
			});
		});

		// When the server sends a ActionsLeft object, the users actions update.
		typeListener.addTypeHandler(ActionsLeft.class, (connection, actions) -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gui.changeActions(actions.num);
				}
			});
		});

		// When the server sends a ActionsLeft object, the users actions update.
		typeListener.addTypeHandler(Number.class, (connection, number) -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gui.changeCardsInPile(number.num, number.playerID);
				}
			});
		});

		// When the server sends a ActionsLeft object, the users actions update.
		typeListener.addTypeHandler(Request.class, (connection, request) -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					switch (request.message) {
					case "End Turn Discard Hand":
						gui.discardHand();
						client.sendTCP(new Request("Draw Hand"));
						break;
					case "Discard Hand":
						gui.discardHand();
						break;
					}
				}
			});
		});

		// When the server sends a status object, the GUI changes the status text.
		typeListener.addTypeHandler(Status.class, (connection, status) -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					String text = null;
					switch (status.state) {
					case WAITINGFORPLAYER2:
						text = "Waiting for Player 2";
						break;
					case STARTGAME:
						client.sendTCP(new Request("Start Game"));
						text = "Starting the Game";
						break;
					case PLAYER1TURN:
						if (client.getID() == 1) {
							gui.unfreezeHand();
							text = "Your turn";
						} else {
							gui.freezeHand();
							text = "Opponent's turn";
						}
						break;
					case PLAYER2TURN:
						if (client.getID() == 2) {
							gui.unfreezeHand();
							text = "Your turn";
						} else {
							gui.freezeHand();
							text = "Opponent's turn";
						}
						break;
					case PLAYER1WINS:
						gui.freezeHand();
						if (client.getID() == 1) {
							text = "You Win";
						} else {
							text = "You Lost";
						}
						break;
					case PLAYER2WINS:
						gui.freezeHand();
						if (client.getID() == 2) {
							text = "You win";
						} else {
							text = "You Lost";
						}
						break;
					}
					gui.changeStatus(text);
				}
			});
		});

		client.addListener(typeListener);
	}
}