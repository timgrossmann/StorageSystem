package gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import grossmann.StoreManagement.Item;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import save_load.Saver;

public class Main extends Application {

	public static Stage primaryStage;
	public static Scene primaryScene;
	public static Controller controller;
	private static String gtin = "";
	private static boolean isThreadOn = false;
	private static boolean saveThreadActive = false;
	private static Logger log = LogManager.getLogger(Main.class);

	@Override
	public void start(Stage primaryStage) throws Exception {

		AnchorPane root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
		log.debug("GUI.fxml loaded");
		Scene primaryScene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
		Main.primaryScene = primaryScene;

		primaryScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setScene(primaryScene);
		primaryStage.setTitle("Storage Management System");
		Main.primaryStage = primaryStage;

		primaryStage.show();

		// Load the current file with Programm start
		controller.loadFile(false);

		primaryStage.setOnCloseRequest(event -> {
			log.debug("Window closed");
		});

		primaryScene.setOnKeyPressed(event -> {

			if (event.getCode().isDigitKey()) {

				if (!checkThreadOn()) {
					setIsThreadOn(true);
					new Thread(new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							log.debug("ScanThread called");
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								log.error("Threadsleep error: " + e.getMessage());
							}

							String output = (("0000000000000" + gtin).substring(gtin.length()));

							if (controller.addButton.isSelected()) {
								controller.addItem(output);
							} else if (controller.removeButton.isSelected()) {
								controller.removeItem(output);
							} else {
								log.info("Option not possible");
								controller.addRemoveToggle.selectToggle(controller.addButton);
							}

							if (!checkSaveThreadOn()) {
								setSaveThreadActive(true);
								new Thread(new Task<Void>() {

									@Override
									protected Void call() throws Exception {
										log.debug("Save Thread called");
										try {
											Thread.sleep(60000);
										} catch (InterruptedException e) {
											log.error("SaveThreadsleep error: " + e.getMessage());
										}

										serializeItems();

										setSaveThreadActive(false);

										return null;
									}

								}).start();
							}

							gtin = "";
							log.info("Barcode set back to ''");
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

				log.info("Items serialized");
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
		log.debug("GUI launching");
		launch(args);
		log.debug("Program ended");
	}

}
