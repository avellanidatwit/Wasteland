package game;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main Function Use fadetransition for text
 *
 * @author Dominic Avellani
 */
public class Main extends Application {

	// Card dimensions
	public final int CARD_HEIGHT = 180;
	public final int CARD_WIDTH = 128;

	// Play window dimensions
	public final int WINDOW_HEIGHT = 768;
	public final int WINDOW_WIDTH = 1024;

	// Lock a players cards
	public boolean locked = true;

	// Server connections
	public JavaServer server;
	public JavaClient connectionToServer;

	// Music and font
	public Font papyrus, smallPapyrus;
	public MediaPlayer mediaPlayer;
	public boolean play = true;

	// Pane references
	public Stage stage;
	public Scene game, menu;
	public HBox hand, top;
	public VBox center, menuButtons;
	public Label status, actionsLeft, cardsInYourPile, cardsInOpponentsPile;

	// Effects
	public Lighting greenLight, orangeLight;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Light effect setup
		greenLight = new Lighting(new Light.Distant(45, 90, Color.LIMEGREEN));
		orangeLight = new Lighting(new Light.Distant(45, 90, Color.ORANGE));

		// Font
		papyrus = Font.loadFont(Main.class.getResource("/papyrus.ttf").toString(), 20);
		smallPapyrus = Font.loadFont(Main.class.getResource("/papyrus.ttf").toString(), 15);

		// Game GUI setup
		guiSetUp();

		// Menu setup
		menuSetUp();

		// Music setup and start
		musicSetUp();

		// Stage setup
		stage = new Stage();
		stage.setOnCloseRequest(event -> {
			if (server != null) {
				server.server.stop();
			}
		});
		stage.setTitle("Wasteland"); // Window title
		stage.setScene(menu); // Set start scene
		stage.show();

	}

	public void menuSetUp() throws URISyntaxException, IOException {
		BorderPane panels = new BorderPane();
		menuButtons = new VBox();

		menuButtons.setSpacing(10);
		menuButtons.setAlignment(Pos.CENTER);

		Image image = new Image(getClass().getResource("/Menu.png").toURI().toString());
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true));
		panels.setBackground(new Background(backgroundImage)); // Background

		// Button constants
		int BUTTON_WIDTH = 200;

		// Start Game button
		Button startGame = new Button();
		startGame.setFont(papyrus);
		startGame.setStyle("-fx-background-color: #dc893c; -fx-background-radius: 0;");
		startGame.setOnMouseEntered(e -> {
			startGame.setStyle("-fx-background-color: #C4A484; -fx-background-radius: 0;");
		});
		startGame.setOnMouseExited(e -> {
			startGame.setStyle("-fx-background-color: #dc893c; -fx-background-radius: 0;");
		});
		startGame.setText("Start Game");
		startGame.setMaxWidth(BUTTON_WIDTH);
		startGame.setMinWidth(BUTTON_WIDTH);
		startGame.setOnAction(e -> {
			startGame.setText("Searching...");
			if (connectionSetUp()) {
				stage.setScene(game);
				stage.show();
			} else {
				Timeline timeline = new Timeline();
				timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0), i -> {
					startGame.setText("No Server Found");
				}));
				timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5), i -> {
					startGame.setText("Start Game");
				}));
				timeline.play();
			}
		});

		// createServer button
		Button createServer = new Button();
		createServer.setFont(papyrus);
		createServer.setStyle("-fx-background-color: #dc893c; -fx-background-radius: 0;");
		createServer.setOnMouseEntered(e -> {
			createServer.setStyle("-fx-background-color: #C4A484; -fx-background-radius: 0;");
		});
		createServer.setOnMouseExited(e -> {
			createServer.setStyle("-fx-background-color: #dc893c; -fx-background-radius: 0;");
		});
		createServer.setText("Create Server");
		createServer.setMaxWidth(BUTTON_WIDTH);
		createServer.setMinWidth(BUTTON_WIDTH);
		createServer.setOnAction(e -> {
			try {
				server = new JavaServer();
				createServer.setText("Server running");
				createServer.setOnAction(i -> {
				});
			} catch (IOException e1) {
				createServer.setText("Can't create Server");
			}
		});
		
		// music button
		Button music = new Button();
		music.setFont(papyrus);
		music.setStyle("-fx-background-color: #dc893c; -fx-background-radius: 0;");
		music.setOnMouseEntered(e -> {
			music.setStyle("-fx-background-color: #C4A484; -fx-background-radius: 0;");
		});
		music.setOnMouseExited(e -> {
			music.setStyle("-fx-background-color: #dc893c; -fx-background-radius: 0;");
		});
		music.setText("Music On");
		music.setMaxWidth(BUTTON_WIDTH);
		music.setMinWidth(BUTTON_WIDTH);
		music.setOnAction(e -> {
			if (play) {
				mediaPlayer.pause();
				play = false;
				music.setText("Music Off");
			} else {
				mediaPlayer.play();
				play = true;
				music.setText("Music On");
			}
		});

		// Tutorial text
		Label title = new Label();
		int BALANCE = 250;
		title.setAlignment(Pos.CENTER);
		title.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9); -fx-background-radius: 10;");
		title.setPadding(new Insets(20));
		title.setFont(papyrus);
		title.setTextFill(Color.WHITE);
		title.setMinWidth(BALANCE);
		title.setMaxWidth(BALANCE);
		title.setWrapText(true);
		title.setText("Welcome to Wasteland!");

		Label label = new Label();
		label.setAlignment(Pos.CENTER_LEFT);
		label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9); -fx-background-radius: 10;");
		label.setPadding(new Insets(20));
		label.setFont(smallPapyrus);
		label.setTextFill(Color.WHITE);
		label.setMinWidth(BALANCE);
		label.setMaxWidth(BALANCE);
		label.setWrapText(true);
		label.setText("The goal of each match is to destroy all your opponents cards, "
				+ "so that they have no cards in hand or in their deck at the beginning of their turn.\n"
				+ "You start each game with a forest booster pack that gives you cards to use or craft togeather.\n"
				+ "Most cards are destroyed after being used. It's important to find renewable resources.\n"
				+ "Any cards that light up green can be crafted with the selected card.\n"
				+ "Any cards that light up orange can be smashed with a Hammer.\n"
				+ "Some cards have the Priority keyword which means they are targeted first by discard and destroy effects.");

		VBox centering = new VBox();
		centering.setAlignment(Pos.CENTER_LEFT);
		centering.getChildren().addAll(title, label);

		menuButtons.getChildren().addAll(startGame, createServer, music);
		panels.setCenter(menuButtons);
		panels.setLeft(centering);

		Region region = new Region();
		region.setMaxWidth(BALANCE);
		region.setMinWidth(BALANCE);
		panels.setRight(region);

		menu = new Scene(panels, WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void guiSetUp() throws URISyntaxException {
		// Create panes
		BorderPane layout = new BorderPane();
		hand = new HBox();
		top = new HBox();
		center = new VBox();
		VBox left = new VBox();
		VBox right = new VBox();

		// End Turn button
		Button endTurn = new Button();
		endTurn.setFont(smallPapyrus);
		endTurn.setStyle("-fx-background-color: #dc893c;");
		endTurn.setOnMouseEntered(e -> {
			endTurn.setStyle("-fx-background-color: #C4A484;");
		});
		endTurn.setOnMouseExited(e -> {
			endTurn.setStyle("-fx-background-color: #dc893c;");
		});
		endTurn.setText("End Turn");
		endTurn.setMaxWidth(100);
		endTurn.setMinWidth(100);
		endTurn.setOnAction(e -> {
			if (!locked) {
				changeActions(0);
				connectionToServer.client.sendTCP(new Request("End Turn"));
			}
		});

		// Left setup
		left.setAlignment(Pos.BASELINE_CENTER);
		left.setPadding(new Insets(0, 0, 20, 20));
		Region space = new Region();
		VBox.setVgrow(space, Priority.ALWAYS);
		left.getChildren().addAll(space, endTurn);
		left.setBackground(new Background(new BackgroundFill(Color.DARKOLIVEGREEN, CornerRadii.EMPTY, Insets.EMPTY))); // Background

		// Right setup
		cardsInOpponentsPile = new Label();
		cardsInOpponentsPile.setFont(smallPapyrus);
		cardsInOpponentsPile.setTextFill(Color.WHITE);
		cardsInOpponentsPile.setText("Cards in opponent's pile: 0");

		cardsInYourPile = new Label();
		cardsInYourPile.setFont(smallPapyrus);
		cardsInYourPile.setTextFill(Color.WHITE);
		cardsInYourPile.setText("Cards in your pile: 0");

		right.setAlignment(Pos.BASELINE_CENTER);
		right.setPadding(new Insets(0, 20, 20, 0));
		Region space2 = new Region();
		VBox.setVgrow(space2, Priority.ALWAYS);
		right.getChildren().addAll(space2, cardsInOpponentsPile, cardsInYourPile);
		right.setBackground(new Background(new BackgroundFill(Color.DARKOLIVEGREEN, CornerRadii.EMPTY, Insets.EMPTY))); // Background

		// Top setup
		top.setAlignment(Pos.CENTER);
		top.setSpacing(30);
		top.setBackground(new Background(new BackgroundFill(Color.DARKOLIVEGREEN, CornerRadii.EMPTY, Insets.EMPTY))); // Background

		// Music Button
		Button musicButton = new Button();
		Image sound = new Image(getClass().getResource("/sound.png").toURI().toString());
		Image noSound = new Image(getClass().getResource("/noSound.png").toURI().toString());
		ImageView imageview = new ImageView(sound);

		int s = 30;
		imageview.setFitHeight(s);
		imageview.setFitWidth(s);
		imageview.setPreserveRatio(true);
		musicButton.setBackground(null);
		musicButton.setGraphic(imageview);
		musicButton.setMaxWidth(s);
		musicButton.setMinWidth(s);
		musicButton.setMaxHeight(s);
		musicButton.setMinHeight(s);
		musicButton.setTranslateY(5);
		musicButton.setOnAction(e -> {
			if (play) {
				imageview.setImage(noSound);
				mediaPlayer.pause();
				play = false;
			} else {
				imageview.setImage(sound);
				mediaPlayer.play();
				play = true;
			}
		});

		// Status label
		status = new Label();
		status.setFont(papyrus);
		status.setTextFill(Color.WHITE);
		status.setText("Status:");
		top.getChildren().addAll(musicButton, status);

		// Actions label
		actionsLeft = new Label();
		actionsLeft.setFont(papyrus);
		actionsLeft.setTextFill(Color.WHITE);
		actionsLeft.setText("Actions Left: 0/0");
		top.getChildren().add(actionsLeft);

		// Center setup
		center.setAlignment(Pos.CENTER);
		center.setBackground(new Background(new BackgroundFill(Color.DARKOLIVEGREEN, CornerRadii.EMPTY, Insets.EMPTY))); // Background

		// Bottom bar setup
		hand.setMinHeight(CARD_HEIGHT + (CARD_HEIGHT / 5)); // Bar height
		hand.setMaxHeight(CARD_HEIGHT + (CARD_HEIGHT / 5));
		hand.setSpacing(10); // Card Spacing
		hand.setAlignment(Pos.CENTER);
		hand.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY))); // Background

		// BorderPane setup
		layout.setCenter(center);
		layout.setTop(top);
		layout.setLeft(left);
		layout.setRight(right);
		layout.setBottom(hand);
		layout.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

		game = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void musicSetUp() {
		// Background Music
		Random random = new Random();
		Media glorious = new Media(getClass().getResource("/Glorious.mp3").toString());
		Media march = new Media(getClass().getResource("/March.mp3").toString());
		MediaPlayer gloriousPlayer = new MediaPlayer(glorious);
		MediaPlayer marchPlayer = new MediaPlayer(march);
		if (random.nextInt(2) + 1 == 1) {
			mediaPlayer = marchPlayer;
		} else {
			mediaPlayer = gloriousPlayer;
		}

		mediaPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.seek(Duration.ZERO);
				mediaPlayer.pause();
				if (random.nextInt(2) + 1 == 1) {
					mediaPlayer = gloriousPlayer;
				} else {
					mediaPlayer = marchPlayer;
				}
				mediaPlayer.play();
			}
		});
		mediaPlayer.play();
	}

	public boolean connectionSetUp() {
		try {
			connectionToServer = new JavaClient(this);
		} catch (Exception e) {
			return false;
		}

		connectionToServer.client.sendTCP(new Request("Init User"));
		return true;
	}

	/**
	 * Makes the cards interactable by adding mouse behaviors.
	 */
	public void makeDraggable(ImageView node) {
		final Delta start = new Delta();
		final Delta dragDelta = new Delta();

		node.setOnMousePressed(e -> {
			// Get start position
			start.x = node.getTranslateX();
			start.y = node.getTranslateY();
			// Move to front
			node.setViewOrder(-1);
			dragDelta.x = node.getTranslateX() - e.getSceneX();
			dragDelta.y = node.getTranslateY() - e.getSceneY();
			// Change color of other nodes based on behavior
			Iterator<Node> it = hand.getChildren().iterator();
			while (it.hasNext()) {
				ImageView child = (ImageView) it.next();
				// Crafting behavior check
				Card check = CardCreator.getInstance().canCraft((Card) node.getUserData(), (Card) child.getUserData());
				if (child != node && check != null) {
					child.setEffect(greenLight);
				}

				// Hammering behavior check
				Card card = (Card) node.getUserData();
				Card hammerCheck = CardCreator.getInstance().canHammer((Card) child.getUserData());
				if (card.getName().equals("Hammer") && child != node && hammerCheck != null) {
					child.setEffect(orangeLight);
				}
			}
		});

		node.setOnMouseDragged(e -> {
			node.setTranslateX(e.getSceneX() + dragDelta.x);
			node.setTranslateY(e.getSceneY() + dragDelta.y);
		});

		node.setOnMouseReleased(e -> {
			Point2D dropPoint = new Point2D(e.getSceneX(),
					e.getSceneY() - (WINDOW_HEIGHT - (CARD_HEIGHT + (CARD_HEIGHT / 5))));

			if (dropPoint.getY() < 0) {
				// USING A CARD
				Card card = (Card) node.getUserData();
				if (card.getName().equals("Hammer")) {
					// Reset to start position
					node.setTranslateX(start.x);
					node.setTranslateY(start.y);
					node.setViewOrder(0);
				} else {
					hand.getChildren().remove(node);
					connectionToServer.client.sendTCP(card);
				}

			} else {
				boolean snap = true;
				for (Node child : hand.getChildren()) {
					if (child instanceof ImageView && !child.equals(node)) {
						if (child.getBoundsInParent().contains(dropPoint)) {
							// CRAFTING
							Card result = CardCreator.getInstance().canCraft((Card) node.getUserData(),
									(Card) child.getUserData());
							if (result != null) {
								// Add new card
								result.action = Action.CRAFT;
								connectionToServer.client.sendTCP(result);

								// Remove first card
								Card card = (Card) node.getUserData();
								card.action = Action.REMOVEFROMHAND;
								connectionToServer.client.sendTCP(card);
								hand.getChildren().remove(node);

								// Remove second card
								card = (Card) child.getUserData();
								card.action = Action.REMOVEFROMHAND;
								connectionToServer.client.sendTCP(card);
								hand.getChildren().remove(child);
								snap = false;
							}
							Card hammer = (Card) node.getUserData();
							if (hammer.getName().equals("Hammer")) {
								Card hammerTarget = CardCreator.getInstance().canHammer((Card) child.getUserData());
								if (hammerTarget != null) {
									// Add new card
									hammerTarget.action = Action.HAMMER;
									connectionToServer.client.sendTCP(hammerTarget);

									// Discard Hammer
									Card card = (Card) node.getUserData();
									card.action = Action.DISCARD;
									connectionToServer.client.sendTCP(card);
									hand.getChildren().remove(node);

									// Remove target card
									card = (Card) child.getUserData();
									card.action = Action.REMOVEFROMHAND;
									connectionToServer.client.sendTCP(card);
									hand.getChildren().remove(child);
									snap = false;
								}
							}
							break;
						}
					}
				}

				if (snap) {
					// Reset to start position
					node.setTranslateX(start.x);
					node.setTranslateY(start.y);
					node.setViewOrder(0);
				}
			}
			Iterator<Node> it = hand.getChildren().iterator();
			while (it.hasNext()) {
				ImageView child = (ImageView) it.next();
				child.setEffect(null);
			}
		});
	}

	/**
	 * Freezes the cards by removing the mouse behaviors.
	 */
	public void freeze(ImageView node) {

		node.setOnMousePressed(e -> {
		});

		node.setOnMouseDragged(e -> {
		});

		node.setOnMouseReleased(e -> {
		});
	}

	/**
	 * Freezes the player's hand.
	 */
	public void freezeHand() {
		Iterator<Node> it = hand.getChildren().iterator();
		while (it.hasNext()) {
			ImageView child = (ImageView) it.next();
			freeze(child);
		}
		locked = true;
	}

	/**
	 * Unfreezes the player's hand.
	 */
	public void unfreezeHand() {
		Iterator<Node> it = hand.getChildren().iterator();
		while (it.hasNext()) {
			ImageView child = (ImageView) it.next();
			makeDraggable(child);
		}
		locked = false;
	}

	/**
	 * Discards the players hand
	 */
	public void discardHand() {
		Iterator<Node> it = hand.getChildren().iterator();
		while (it.hasNext()) {
			Card card = (Card) it.next().getUserData();
			card.action = Action.DISCARD;
			connectionToServer.client.sendTCP(card);
			it.remove();
		}
	}

	/**
	 * Takes a card object and creates a visual representation in the players hand.
	 */
	public void removeCardFromHand(Card card) {
		Iterator<Node> it = hand.getChildren().iterator();
		while (it.hasNext()) {
			Node child = it.next();
			if (child instanceof ImageView && child.getUserData().equals(card)) {
				it.remove();
				break;
			}
		}
	}

	/**
	 * Takes a card object and creates a visual representation in the players hand.
	 */
	public void addCardToHand(Card card) {
		ImageView image = new ImageView(Main.class.getResource(card.IMAGE).toString());
		image.setFitHeight(CARD_HEIGHT);
		image.setFitWidth(CARD_WIDTH);
		image.setUserData(card);
		hand.getChildren().add(image);
		if (!locked) {
			makeDraggable(image);
		}
	}

	/**
	 * A method that creates a text label in the center of the screen to display
	 * information to the user.
	 */
	public void textNotification(String text) {
		Label label = new Label();
		label.setFont(papyrus);
		label.setTextFill(Color.WHITE);
		label.setText(text);

		center.getChildren().add(label);

		FadeTransition ft = new FadeTransition(Duration.millis(5000), label);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setCycleCount(1);
		ft.setAutoReverse(false);

		ft.setOnFinished(e -> {
			center.getChildren().remove(label);
		});

		ft.play();
	}

	/**
	 * Changes the status bar at the top of the screen
	 */
	public void changeStatus(String text) {
		status.setText("Player " + connectionToServer.client.getID() + " Status: " + text);
	}

	/**
	 * TODO: Fix the numbers Changes the number of cards in pile
	 */
	public void changeCardsInPile(int num, int playerID) {
		if (playerID == connectionToServer.client.getID()) {
			cardsInYourPile.setText("Cards in your pile: " + num);
		} else {
			cardsInOpponentsPile.setText("Cards in opponent's pile: " + num);
		}

	}

	/**
	 * Changes the actions remaining at the top of the screen
	 */
	public void changeActions(int actions) {
		actionsLeft.setText("Actions Left: " + actions + "/5");
	}

	// A 2D point
	class Delta {
		double x, y;
	}
}
