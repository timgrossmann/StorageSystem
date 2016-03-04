package gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import parts.Item;
import save_load.JSONSaver;

public class Main extends Application {

	public static Stage primaryStage;
	public static Scene primaryScene;
	public static Controller controller;
	private static String gtin = "";
	private static boolean isThreadOn = false;
	private static boolean saveThreadActive = false;
	private static Logger log = LogManager.getLogger(Main.class);

	
	/**
	 * JavaFX GUI startUp Method
	 * Loads the GUI.fxml File as the startup Window gui
	 * 
	 * Main logic for the case of entering a Barcode (if the focus is not in the search-Textbox) 
	 * -> primaryScene.setOnKeyPressed
	 * 
	 * 
	 */
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
		primaryStage.setFullScreen(true);

		primaryStage.show();

		// Load the current file with Programm start
		controller.loadFile(false);

		primaryStage.setOnCloseRequest(event -> {
			log.debug("Window closed");
		});

		primaryScene.setOnKeyPressed(event -> {
			//only gets Called if the pressed Key if a Digit, preventing a lot of errors
			if (event.getCode().isDigitKey()) {

				//if the WorkerThread is not already running, it creates a new Thread executing the add/remove
				if (!checkThreadOn()) {
					setIsThreadOn(true);
					new Thread(new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							log.debug("ScanThread called");
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								log.error("Threadsleep error: " + e.getMessage());
							}

							//waits for the Thread.sleep(ms) time and then gets the Barcode with leading Zeroes 
							//easy way to get leading zeroes without a Formatter
							String output = (("0000000000000" + gtin).substring(gtin.length()));

							//checks for the currently selected Button and starts the corresponding tast
							//with the now formatted Barcode
							if (controller.addButton.isSelected()) {
								controller.addItem(output);
							} else if (controller.removeButton.isSelected()) {
								controller.removeItem(output);
							} else {
								log.info("Option not possible");
								controller.addRemoveToggle.selectToggle(controller.addButton);
							}

							//if the saveThread wasn't already started, it will be started
							if (!checkSaveThreadOn()) {
								//this prevents the saveThread from starting over with every Productscan
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

										//after the Thread.sleep(ms) time, the current state gets saved out
										serializeItems();
										setSaveThreadActive(false);

										return null;
									}
								}).start();
							}
							//here the current Barcode gets reseted to empty, so the next scan won't have any
							//digits already assigned
							gtin = "";
							log.info("Barcode set back to ''");
							
							//ends the current scan process and the next item can be scanned
							setIsThreadOn(false);
							return null;
						}
					}).start();
				}
				
				//adds the pressed key to the barcode variable
				gtin += event.getText();
			}
		});
	}

	/**
	 * Calls the serializeItems action on the GUI Thread 
	 */
	public static void serializeItems() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				List<ItemBox> temp = new ArrayList<ItemBox>(controller.items);

				//Create a list with just the Items for searialzation 
				List<Item> items = new ArrayList<Item>();
				for (ItemBox itemBox : temp) {
					items.add(itemBox.getItem());
				}

				JSONSaver.save(items, false);
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

	/**
	 * Launching the actual GUI and therefore starting the Programm
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		log.debug("GUI launching");
		launch(args);
		log.debug("Program ended");
	}

}
