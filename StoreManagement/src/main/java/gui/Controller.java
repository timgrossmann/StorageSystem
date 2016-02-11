package gui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

import exceptions.NoNameForProductException;
import grossmann.StoreManagement.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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
	ToggleButton addButton;
	@FXML
	ToggleButton removeButton;
	@FXML
	TextField searchBox;

	ObservableMap<String, ItemBox> itemsMap = FXCollections.observableMap(new HashMap<String, ItemBox>());
	ObservableList<ItemBox> items = FXCollections.observableArrayList(itemsMap.values());
	ObservableList<ItemBox> searchItems = FXCollections.observableArrayList();

	public void initialize(URL location, ResourceBundle resources) {

		Main.controller = this;

		itemsMap.put("12345", new ItemBox());
		itemsMap.put("14513", new ItemBox());
		itemsMap.put("51212", new ItemBox());
		itemsMap.put("12382", new ItemBox());
		itemsMap.put("84322", new ItemBox());
		itemsMap.put("12456", new ItemBox());
		itemsMap.put("98999", new ItemBox());

		updateList();

		listView.setItems(items);
		addButton.setSelected(true);

		searchBox.textProperty().addListener((observable, oldVal, newVal) -> {

			searchItems.clear();

			if (newVal.equals("")) {
				listView.setItems(items);
			} else {

				itemsMap.forEach((a, b) -> {
					if (b.getName().toLowerCase().contains(newVal.toLowerCase())) {
						searchItems.add(b);
					}
				});

				listView.setItems(searchItems);
			}
		});

		testButton.setOnAction(event -> {
			itemsMap.put(event.hashCode() + "", new ItemBox(null));
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
						itemsMap.get(gtin).setAmount(itemsMap.get(gtin).getAmount() + 1);
					}
				} catch (NoNameForProductException e) {
					System.out.println("Item not Found");

					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle("Item not Found");
					dialog.setHeaderText("The Item is not yet listed");
					dialog.setContentText("Please enter the name of the Product:");

					Optional<String> result = dialog.showAndWait();
					result.ifPresent(name -> listNewItem(gtin, name));

				} catch (IOException e) {
					System.out.println("Not a Barcode!");
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

			if(httpCon.getResponseCode() == 200) {
				addItem(gtin);
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
				items = FXCollections.observableArrayList(itemsMap.values());
				listView.setItems(items);

			}
		});

		return false;
	}

}
