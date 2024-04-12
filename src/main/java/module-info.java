module Wasteland {
	requires transitive javafx.graphics;
	requires transitive javafx.fxml;
	requires transitive javafx.controls;
	requires transitive javafx.media;
	requires transitive javafx.base;
	requires transitive javafx.swing;
	requires transitive kryonet;
	requires com.esotericsoftware.kryo;
	
	exports game;
	
	opens game;
}