package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage primaryStage;
	public static Scene primaryScene;
	public static Controller controller;
	private static String gtin = "";
	private static Thread resetThread;

	@Override
	public void start(Stage primaryStage) throws Exception {

		AnchorPane root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
		Scene primaryScene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
		Main.primaryScene = primaryScene;

		primaryStage.setScene(primaryScene);
		primaryStage.setTitle("Storage Management System");
		primaryStage.setMaxWidth(root.getPrefWidth());
		primaryStage.setMaxHeight(root.getPrefHeight());
		primaryStage.setMinWidth(root.getPrefWidth());
		primaryStage.setMinHeight(root.getPrefHeight());
		Main.primaryStage = primaryStage;

		primaryStage.show();

		primaryScene.setOnKeyPressed(event -> {

			gtin += event.getText();

		});

		primaryStage.setOnCloseRequest(event -> {
			killThread();
		});

	}

	private static void killThread() {
		resetThread = null;
	}

	public static void main(String[] args) {

		Runnable gtinReset = new Runnable() {

			@Override
			public void run() {
				Thread thread = Thread.currentThread();
				while (resetThread == thread) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (!gtin.equals("")) {
						
						if(controller.addButton.isSelected()) {
							controller.addItem(gtin);
						} else if(controller.removeButton.isSelected()) {
							controller.removeItem(gtin);
						} else {
							System.out.println("other");
						}
						
						gtin = "";
					}
				}
			}
		};

		resetThread = new Thread(gtinReset);
		resetThread.start();

		launch(args);

	}

}
