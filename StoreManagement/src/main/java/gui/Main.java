package gui;

import java.util.ArrayList;
import java.util.List;

import grossmann.StoreManagement.Item;
import javafx.application.Application;
import javafx.application.Platform;import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import save_load.Loader;
import save_load.Saver;

public class Main extends Application {

	public static Stage primaryStage;
	public static Scene primaryScene;
	public static Controller controller;
	private static String gtin = "";
	private static boolean isThreadOn = false;
	private static boolean saveThreadActive = false;

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
		
		// Load the current file with Programm start
		controller.loadFile(false);

		primaryScene.setOnKeyPressed(event -> {

			if (event.getCode().isDigitKey()) {

				if (!checkThreadOn()) {
					setIsThreadOn(true);
					new Thread(new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							System.out.println("Called");
							try {
								Thread.sleep(200);
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

							if (!checkSaveThreadOn()) {
								setSaveThreadActive(true);
								new Thread(new Task<Void>() {

									@Override
									protected Void call() throws Exception {
										System.out.println("Save Thread activated");
										try {
											Thread.sleep(60000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}

										serializeItems();

										setSaveThreadActive(false);

										return null;
									}

								}).start();
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

	public static void serializeItems() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				List<ItemBox> temp = new ArrayList<ItemBox>(controller.items);

				List<Item> items = new ArrayList<Item>();

				for (ItemBox itemBox : temp) {
					items.add(itemBox.getItem());
				}

				Saver.save(items, false);
				
				System.out.println("serialized");
			}
		});
	}

	private static boolean checkThreadOn() {
		if (isThreadOn) {
			return true;
		} else {
			return false;
		}
	}

	private static void setIsThreadOn(boolean state) {
		isThreadOn = state;
	}

	private static boolean checkSaveThreadOn() {
		if (saveThreadActive) {
			return true;
		} else {
			return false;
		}
	}

	private static void setSaveThreadActive(boolean state) {
		saveThreadActive = state;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
