package gui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

import exceptions.NoNameForProductException;
import grossmann.StoreManagement.Alerter;
import grossmann.StoreManagement.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import save_load.Loader;

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
	MenuItem sortMenu;
	@FXML
	MenuItem repeatMenu;
	@FXML
	MenuItem aboutMenu;

	ObservableMap<String, ItemBox> itemsMap = FXCollections.observableMap(new HashMap<String, ItemBox>());
	ObservableList<ItemBox> items = FXCollections.observableArrayList(itemsMap.values());
	ObservableList<ItemBox> searchItems = FXCollections.observableArrayList();

	public void initialize(URL location, ResourceBundle resources) {

		Main.controller = this;

		updateList();

		listView.setItems(items);
		addButton.setSelected(true);

		searchBox.textProperty().addListener((observable, oldVal, newVal) -> {

			searchItems.clear();

			if (newVal.equals("")) {
				listView.setItems(items);
			} else {

				if (searchToggle.getSelectedToggle().equals(nameSearch)) {
					itemsMap.forEach((a, b) -> {
						if (b.getName().toLowerCase().contains(newVal.toLowerCase())) {
							searchItems.add(b);
						}
					});
				} else if (searchToggle.getSelectedToggle().equals(amountSearch)) {
					itemsMap.forEach((a, b) -> {
						if (String.valueOf(b.getAmount()).contains(newVal)) {
							searchItems.add(b);
						}
					});
				} else if (searchToggle.getSelectedToggle().equals(barcodeSearch)) {
					itemsMap.forEach((a, b) -> {
						if (b.getGtin().contains(newVal)) {
							searchItems.add(b);
						}
					});
				}

				listView.setItems(searchItems);

			}
		});

		listView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<ItemBox>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends ItemBox> c) {
				System.out.println(listView.getSelectionModel().getSelectedItem());
				// TODO load Item into preview (Add GUI for Preview)
			}
		});

		setupMenuItems();

	}

	private void setupMenuItems() {
		aboutMenu.setOnAction(event -> {
			System.out.println("About");
			// TODO
		});

		exitMenu.setOnAction(event -> Platform.exit());

		loadMenu.setOnAction(event -> loadFile(true));

		saveMenu.setOnAction(event -> Main.serializeItems());

		sortMenu.setOnAction(event -> {
			Optional<String> sortOption = Alerter.getChoiceDialog("Sorting", null, "Select how you want to sort: ");
			sortOption.ifPresent(letter -> sortItems(letter));
			// TODO
		});

		updateAllMenu.setOnAction(event -> {

			new Thread(new Task<Void>() {
				@Override
				protected Void call() throws Exception {

					System.out.println("Update called");

					itemsMap.forEach((a, b) -> {
						Item temp;
						try {
							temp = getNewItem(a);

							if (!temp.equals(b.getItem())) {
								System.out.println(temp.name + " unequal to " + b.getItem().name);
								b.setItem(temp);
								System.out.println("Changed to " + temp.name);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					});

					updateList();

					Main.serializeItems();

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
						System.out.println(temp.name + " unequal to " + itemBox.getItem().name);
						itemBox.setItem(temp);
						System.out.println("Changed to " + temp.name);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				Alert alert = Alerter.getAlert(AlertType.INFORMATION, "No Item selected", null,
						"Please select the Item you want to update!");
				alert.showAndWait();
			}

		});

		deleteMenu.setOnAction(event -> {
			itemsMap.remove(listView.getSelectionModel().getSelectedItem().getGtin());
			updateList();
		});

		repeatMenu.setOnAction(event -> {
			// TODO
		});

	}

	public void sortItems(String order) {

		switch (order) {
		case "Name":
			break;
		case "Amount":
			break;
		case "Categorie":
			break;
		}

	}

	public void loadFile(boolean state) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				List<Item> temp = Loader.load(state);

				if (temp != null) {
					for (Item item : temp) {
						itemsMap.put(item.gtin, new ItemBox(item));
					}
					updateList();
				}
			}
		});
	}

	private Item getNewItem(String gtin) throws IOException {

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

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("Add: " + gtin);
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
					System.out.println("Item not Found");

					Optional<String> result = Alerter.getTextDialog("Item not Found", "The Item is not yet listed",
							"Please enter the name of the Product:");
					result.ifPresent(name -> listNewItem(gtin, name));

				} catch (IOException e) {
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

			System.out.println(httpCon.getResponseCode());
			System.out.println(httpCon.getResponseMessage());

			out.close();

			if (httpCon.getResponseCode() == 200) {
				Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Item Added", null, "Item is now listed.");
				alert.showAndWait();

				addItem(gtin);
			} else {
				Alert alert = Alerter.getAlert(AlertType.WARNING, "Item not Added", null,
						"Item could not be listed, please try again.");
				alert.showAndWait();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateList() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				items = FXCollections.observableArrayList(itemsMap.values());
				listView.setItems(items);
			}
		});
	}

	public boolean removeItem(String gtin) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("Remove: " + gtin);

				if (itemsMap.containsKey(gtin)) {
					itemsMap.get(gtin).decreaseAmount();
					listView.getSelectionModel().select(itemsMap.get(gtin));
					if (itemsMap.get(gtin).getAmount() == 1) {
						Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Last Item", null,
								"Only one of this kind left in Stock");
						alert.showAndWait();
					} else if (itemsMap.get(gtin).getAmount() == 0) {
						Alert alert = Alerter.getAlert(AlertType.WARNING, "No more Item", null,
								"This was the last one of this Item\nPlease rebuy");
						alert.showAndWait();
					}
				} else {
					Alert alert = Alerter.getAlert(AlertType.WARNING, "No Item Found", null,
							"There is no Item with this Barcode");
					alert.showAndWait();
				}
			}
		});

		return false;
	}

}
