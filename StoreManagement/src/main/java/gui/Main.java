package gui;

import com.sun.javafx.binding.StringFormatter;

import javafx.application.Application;
import javafx.beans.binding.StringExpression;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
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

			if (event.getCode().isDigitKey()) {

				if (!checkThreadOn()) {
					setIsThreadOn(true);
					new Thread(new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							System.out.println("Called");
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							String output = (("0000000000000" + gtin).substring(gtin.length()));
							
							if (controller.addButton.isSelected()) {
								controller.addItem(output);
							} else if (controller.removeButton.isSelected()) {
								controller.removeItem(output);
							} else {
								 System.out.println("Option not possible");
							}

							gtin = "";
							setIsThreadOn(false);

							return null;
						}
					}).start();
				}

				gtin += event.getText();
			}

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
