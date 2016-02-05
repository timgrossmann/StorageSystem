package gui;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage primaryStage;
	public static Scene primaryScene;
	public static Controller controller;
	private static String gtin = "";
	private static boolean isThreadOn = false;

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

			if (!checkThreadOn()) {
				setIsThreadOn(true);
				new Thread(new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (controller.addButton.isSelected()) {
							// System.out.println("Add " + gtin);
							controller.addItem(gtin);
						} else if (controller.removeButton.isSelected()) {
							// System.out.println("remove " + gtin);
							controller.removeItem(gtin);
						} else {
							// System.out.println("other");
						}

						gtin = "";
						setIsThreadOn(false);

						return null;
					}
				}).start();
			}

			gtin += event.getText();

		});
	}

	private synchronized static boolean checkThreadOn() {
		if (isThreadOn) {
			return true;
		} else {
			return false;
		}
	}

	private synchronized static void setIsThreadOn(boolean state) {
		isThreadOn = state;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
