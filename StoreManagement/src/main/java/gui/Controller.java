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
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import save_load.Loader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class Controller implements Initializable {

	@FXML
	ListView<ItemBox> listView = new ListView<ItemBox>();
	@FXML
	Button testButton = new Button();
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
	RadioButton attributeSearch;

	ObservableMap<String, ItemBox> itemsMap = FXCollections.observableMap(new HashMap<String, ItemBox>());
	ObservableList<ItemBox> items = FXCollections.observableArrayList(itemsMap.values());
	ObservableList<ItemBox> searchItems = FXCollections.observableArrayList();

	public void initialize(URL location, ResourceBundle resources) {

		Main.controller = this;

		// itemsMap.put("12345", new ItemBox());
		// itemsMap.put("14513", new ItemBox());
		// itemsMap.put("51212", new ItemBox());
		// itemsMap.put("12382", new ItemBox());
		// itemsMap.put("84322", new ItemBox());
		// itemsMap.put("12456", new ItemBox());
		// itemsMap.put("98999", new ItemBox());

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

		testButton.setOnAction(event -> {
			List<Item> temp = Loader.load();

			for (Item item : temp) {
				itemsMap.put(item.gtin, new ItemBox(item));
			}

			updateList();
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
					} else {
						itemsMap.get(gtin).increaseAmount();
						;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateList() {
		items = FXCollections.observableArrayList(itemsMap.values());
		listView.setItems(items);
	}

	public boolean removeItem(String gtin) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("Remove: " + gtin);

				if (itemsMap.containsKey(gtin) && itemsMap.get(gtin).getAmount() > 0) {
					itemsMap.get(gtin).decreaseAmount();
					;
				} else if (itemsMap.containsKey(gtin)) {
					Alert alert = Alerter.getAlert(AlertType.INFORMATION, "No Item in Stock", null,
							"There is no more Item in Stock");
					alert.showAndWait();
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
