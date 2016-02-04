package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {

		AnchorPane root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
		Scene primaryScene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
		
		primaryStage.setScene(primaryScene);
		primaryStage.setTitle("Storage Management System");
		primaryStage.setMaxWidth(root.getPrefWidth());
		primaryStage.setMaxHeight(root.getPrefHeight());
		primaryStage.setMinWidth(root.getPrefWidth());
		primaryStage.setMinHeight(root.getPrefHeight());
		Main.primaryStage = primaryStage;
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
