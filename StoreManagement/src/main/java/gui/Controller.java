package gui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.google.gson.Gson;

import grossmann.StoreManagement.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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

	ObservableMap<String, ItemBox> itemsMap = FXCollections.observableMap(new HashMap<String, ItemBox>());
	ObservableList<ItemBox> items = FXCollections.observableArrayList(itemsMap.values());

	public void initialize(URL location, ResourceBundle resources) {

		Main.controller = this;

		listView.setItems(items);
		addButton.setSelected(true);

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

		return new Item(gson.fromJson(temp.toString(), Item.class)); 
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return false;
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
