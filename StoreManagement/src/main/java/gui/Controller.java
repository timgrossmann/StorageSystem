package gui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import exceptions.NoNameForProductException;
import grossmann.StoreManagement.Alerter;
import grossmann.StoreManagement.Item;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import save_load.Loader;
import save_load.Printing;
import save_load.SaveToFile;
import save_load.SaveToFile.PrintOutType;

public class Controller implements Initializable {

	@FXML
	ListView<ItemBox> listView = new ListView<ItemBox>();
	@FXML
	ToggleGroup addRemoveToggle;
	@FXML
	ToggleGroup searchToggle;
	@FXML
	ToggleButton addButton;
	@FXML
	ToggleButton removeButton;
	@FXML
	TextField searchBox;
	@FXML
	RadioButton nameSearch;
	@FXML
	RadioButton amountSearch;
	@FXML
	RadioButton barcodeSearch;
	@FXML
	RadioButton categorieSearch;
	@FXML
	MenuItem loadMenu;
	@FXML
	MenuItem saveMenu;
	@FXML
	MenuItem exitMenu;
	@FXML
	MenuItem updateMenu;
	@FXML
	MenuItem updateAllMenu;
	@FXML
	MenuItem deleteMenu;
	@FXML
	MenuItem groupByMenu;
	@FXML
	MenuItem repeatMenu;
	@FXML
	MenuItem aboutMenu;
	@FXML
	MenuItem printMenu;
	@FXML
	MenuItem printShoppingMenu;
	@FXML
	Label nameLabel;
	@FXML
	Label gtinLabel;
	@FXML
	Label amountLabel;
	@FXML
	Label categoriesLabel;
	@FXML
	Label attributesLabel;
	@FXML
	ImageView imageView;

	ObservableMap<String, ItemBox> itemsMap = FXCollections.observableMap(new HashMap<String, ItemBox>());
	ObservableList<ItemBox> items = FXCollections.observableArrayList(itemsMap.values());
	ObservableList<ItemBox> searchItems = FXCollections.observableArrayList();
	private static String lastCommand;
	private static Logger log = LogManager.getLogger(Controller.class);

	public void initialize(URL location, ResourceBundle resources) {

		Main.controller = this;

		updateList();
		log.debug("List updated");

		listView.setFixedCellSize(60);
		listView.setItems(items);
		addButton.setSelected(true);
		log.debug("ListView cellSize changed, items assigned");

		searchBox.textProperty().addListener((observable, oldVal, newVal) -> {
			renewSearch(newVal);
		});

		searchToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				renewSearch(searchBox.getText());
			}
		});

		listView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<ItemBox>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends ItemBox> c) {
				if (!listView.getSelectionModel().isEmpty()) {
					ItemBox itemBox = listView.getSelectionModel().getSelectedItem();

					nameLabel.setText(itemBox.getName());
					amountLabel.setText(String.valueOf(itemBox.getAmount()) + "x");
					gtinLabel.setText(itemBox.getGtin());
					categoriesLabel.setText(itemBox.getCategoriesText("long"));
					attributesLabel.setText(itemBox.getAttributes());
					// imageView.setImage(itemBox.getImage());
					log.info("Overview set to " + itemBox.getName());
				}
			}
		});

		setupMenuItems();

	}

	private void setupMenuItems() {
		aboutMenu.setOnAction(event -> {
			AnchorPane root;
			try {
				root = FXMLLoader.load(getClass().getResource("about.fxml"));

				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				stage.initStyle(StageStyle.UTILITY);
				stage.setTitle("About");
				stage.show();
			} catch (Exception e) {
				log.error("Error loading About Window - " + e.getMessage());
			}
		});

		exitMenu.setOnAction(event -> {
			Platform.exit();
			log.debug("Window closed");
		});

		loadMenu.setOnAction(event -> loadFile(true));

		saveMenu.setOnAction(event -> Main.serializeItems());

		groupByMenu.setOnAction(event -> {
			Optional<String> sortOption = Alerter.getChoiceDialog("Sorting", null, "Select how you want to group: ");
			sortOption.ifPresent(letter -> sortItems(letter));
		});

		updateAllMenu.setOnAction(event -> {

			new Thread(new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					log.debug("UpdateAll Thread Triggered");

					itemsMap.forEach((a, b) -> {
						Item temp;
						try {
							temp = getNewItem(a);

							if (!temp.equals(b.getItem())) {
								log.info(temp.name + " unequal to " + b.getItem().name);
								b.setItem(temp);
								log.info("Changed to " + temp.name);
							}
						} catch (Exception e1) {
							log.error("Error updating Item " + b.getName() + " - " + e1.getMessage());
						}
					});

					updateList();

					Main.serializeItems();

					log.debug("UpdateAll Thread terminated successfully");
					return null;
				}
			}).start();

		});

		updateMenu.setOnAction(event -> {

			if (!listView.getSelectionModel().isEmpty()) {
				Item temp;
				ItemBox itemBox = listView.getSelectionModel().getSelectedItem();

				try {
					temp = getNewItem(itemBox.getGtin());

					if (!temp.equals(itemBox.getItem())) {
						log.info(temp.name + " unequal to " + itemBox.getItem().name);
						itemBox.setItem(temp);
						log.info("Changed to " + temp.name);
					}
				} catch (Exception e1) {
					log.error("Error updating Item " + itemBox.getName() + " - " + e1.getMessage());
				}
			} else {
				Alert alert = Alerter.getAlert(AlertType.INFORMATION, "No Item selected", null,
						"Please select the Item you want to update!");
				alert.showAndWait();
				log.debug("Info Popup triggered, No item selected");
			}

		});

		deleteMenu.setOnAction(event -> {
			ItemBox rem = itemsMap.remove(listView.getSelectionModel().getSelectedItem().getGtin());
			log.info("Item: " + rem.getName() + " removed");
			updateList();
		});

		repeatMenu.setOnAction(event -> {

			if (lastCommand != null) {
				String[] props = lastCommand.split(" ");
				log.info("Repeat called with: " + lastCommand);

				switch (props[0]) {
				case "ADD":
					addItem(props[1]);
					break;
				case "RM":
					removeItem(props[1]);
					break;
				}
			}
		});

		printMenu.setOnAction(event -> {
			printOut(PrintOutType.OVERVIEW);
		});

		printShoppingMenu.setOnAction(event -> {
			printOut(PrintOutType.SHOPPING);
		});

	}

	public void printOut(PrintOutType type) {

		boolean output = false;
		String fileToPrint = "";

		switch (type) {
		case OVERVIEW:
			output = SaveToFile.printOut(new ArrayList<ItemBox>(items), type, false);
			fileToPrint = "Overview.txt";
			break;
		case SHOPPING:
			ArrayList<ItemBox> temp = new ArrayList<>();

			for (ItemBox item : items) {
				if (item.getAmount() == 0) {
					temp.add(item);
				}
			}
			output = SaveToFile.printOut(temp, type, false);
			fileToPrint = "Shopping.txt";
			break;
		}

		if (output) {
			log.debug(type.name() + " Successfully Saved PrintFile");
			File file = new File(System.getProperty("user.home") + "/Desktop/" + fileToPrint);

			if (file != null) {
				boolean printOut = Printing.printFile(file);

				if (printOut) {
					log.debug("Successfully printed File");
					Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Print", null, "Successfully printed.");
					alert.showAndWait();
				} else {
					log.debug("File was saved but could not be printed");
					Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Print Failed", null,
							"File saved but could not be printed.");
					alert.showAndWait();
				}
			}

		} else {
			Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Error", null,
					"File could not be saved and printed.");
			alert.showAndWait();
			log.debug(type.name() + "PrintFile could NOT be saved and printed");
		}
	}

	public void renewSearch(String newVal) {
		listView.getSelectionModel().clearSelection();
		searchItems.clear();

		if (newVal.equals("")) {
			listView.setItems(items);
			log.info("All items are displayed");
		} else {

			if (searchToggle.getSelectedToggle().equals(nameSearch)) {
				itemsMap.forEach((a, b) -> {
					if (b.getName().toLowerCase().contains(newVal.toLowerCase())) {
						searchItems.add(b);
					}
				});
				log.info("Only items with '" + newVal + "' in their name are displayed");

			} else if (searchToggle.getSelectedToggle().equals(amountSearch)) {
				itemsMap.forEach((a, b) -> {
					if (String.valueOf(b.getAmount()).contains(newVal)) {
						searchItems.add(b);
					}
				});
				log.info("Only items with '" + newVal + "' as their amount are displayed");

			} else if (searchToggle.getSelectedToggle().equals(barcodeSearch)) {
				itemsMap.forEach((a, b) -> {
					if (b.getGtin().contains(newVal)) {
						searchItems.add(b);
					}
				});
				log.info("Only items with '" + newVal + "' in their barcode are displayed");

			} else if (searchToggle.getSelectedToggle().equals(categorieSearch)) {
				itemsMap.forEach((a, b) -> {
					for (String cat : b.getCategories()) {
						if (cat.toLowerCase().contains(newVal.toLowerCase())) {
							searchItems.add(b);
							break;
						}
					}
				});
				log.info("Only items with '" + newVal + "' in their categories are displayed");
			}

			listView.setItems(searchItems);
		}
	}

	///////////////////////////////////////////////////////////////////////
	public void sortItems(String order) {

		ArrayList<ItemBox> temp = new ArrayList<>(items);

		switch (order) {
		case "Name":
			Collections.sort(temp, (a, b) -> {
				return a.getName().compareTo(b.getName());
			});
			break;
		case "Amount":
			Collections.sort(temp, (a, b) -> {
				return a.getAmount() - b.getAmount();
			});
			break;
		case "Categories":
			// TODO Think about how to group by categories
			break;
		}

		items = FXCollections.observableArrayList(temp);
		listView.setItems(items);

	}
	///////////////////////////////////////////////////////////////////////

	public void loadFile(boolean state) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				log.debug("Loading File");

				List<Item> temp = Loader.load(state);

				if (temp != null) {
					for (Item item : temp) {
						itemsMap.put(item.gtin, new ItemBox(item));
					}
					updateList();
				}

				log.debug("File Loaded");
			}
		});
	}

	private Item getNewItem(String gtin) throws IOException {

		log.info("Getting Item with Barcode: " + gtin);

		Gson gson = new Gson();

		URL url = new URL("https://api.outpan.com/v2/products/" + gtin + "?apikey=e13a9fb0bda8684d72bc3dba1b16ae1e");

		StringBuilder temp = new StringBuilder();

		Scanner scanner = new Scanner(url.openStream());

		while (scanner.hasNext()) {
			temp.append(scanner.nextLine());
		}

		scanner.close();

		Item item = new Item(gson.fromJson(temp.toString(), Item.class));

		if (item.name != null) {
			return item;
		} else {
			throw new NoNameForProductException();
		}

	}

	public boolean addItem(String gtin) {

		lastCommand = "ADD " + gtin;
		log.debug("LastCommand set to: " + lastCommand);

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				log.info("Add: " + gtin);
				try {
					if (!itemsMap.containsKey(gtin)) {
						itemsMap.put(gtin, new ItemBox(getNewItem(gtin)));
						updateList();
						listView.getSelectionModel().select(itemsMap.get(gtin));
					} else {
						itemsMap.get(gtin).increaseAmount();
						listView.getSelectionModel().select(itemsMap.get(gtin));
					}
				} catch (NoNameForProductException e) {
					log.error("Item not Found");

					Optional<String> result = Alerter.getTextDialog("Item not Found", "The Item is not yet listed",
							"Please enter the name of the Product:");
					result.ifPresent(name -> listNewItem(gtin, name));

				} catch (IOException e) {
					log.debug("Entered Barcode is not Valid");
					Alert alert = Alerter.getAlert(AlertType.WARNING, "Not a valid Barcode", null,
							"The entered Barcode is not valid.\nPlease try again");
					alert.showAndWait();
				}
			}
		});

		return false;
	}

	private void listNewItem(String gtin, String name) {

		try {
			URL url = new URL("https://api.outpan.com/v2/products/" + gtin + "/name"
					+ "?apikey=e13a9fb0bda8684d72bc3dba1b16ae1e");

			HttpsURLConnection httpCon = (HttpsURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("POST");

			String content = "name=" + name;
			DataOutputStream out = new DataOutputStream(httpCon.getOutputStream());

			out.writeBytes(content);
			out.flush();

			log.debug(httpCon.getResponseCode() + " - " + httpCon.getResponseMessage());
			out.close();

			if (httpCon.getResponseCode() == 200) {
				Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Item Added", null, "Item is now listed.");
				alert.showAndWait();
				log.info("Item '" + name + "' now listed");

				addItem(gtin);
			} else {
				log.debug("Item could not be listed");
				Alert alert = Alerter.getAlert(AlertType.WARNING, "Item not Added", null,
						"Item could not be listed, please try again.");
				alert.showAndWait();
			}

		} catch (MalformedURLException e) {
			log.error("MalformedURLException: " + e.getMessage());
		} catch (IOException e) {
			log.error("IOException: " + e.getMessage());
		}

	}

	public void updateList() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				items = FXCollections.observableArrayList(itemsMap.values());

				listView.setItems(items);

				log.info("List updated");
			}
		});
	}

	public boolean removeItem(String gtin) {

		lastCommand = "RM " + gtin;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				log.info("Remove: " + gtin);

				if (itemsMap.containsKey(gtin)) {
					if (itemsMap.get(gtin).getAmount() == 2) {
						log.info("One Item of '" + gtin + "' left");
						Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Only one Item left", null,
								"Only one of this Item is left in Stock");
						alert.showAndWait();
					} else if (itemsMap.get(gtin).getAmount() == 1) {
						log.info("No Item of '" + gtin + "' left");
						Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Last Item removed", null,
								"This was the last one of this Item\nPlease rebuy");
						alert.showAndWait();
					} else if (itemsMap.get(gtin).getAmount() == 0) {
						log.info("No Item '" + gtin + "' in Stock");
						Alert alert = Alerter.getAlert(AlertType.WARNING, "No more Item", null,
								"No more item of this kind in stock");
						alert.showAndWait();
					}

					itemsMap.get(gtin).decreaseAmount();
					listView.getSelectionModel().select(itemsMap.get(gtin));

				} else {
					log.debug("Item '" + gtin + "' not found");
					Alert alert = Alerter.getAlert(AlertType.WARNING, "No Item Found", null,
							"There is no Item with this Barcode");
					alert.showAndWait();
				}
			}
		});

		return false;
	}

}
