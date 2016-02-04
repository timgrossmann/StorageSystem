package gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.google.gson.Gson;

import grossmann.StoreManagement.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

	ObservableList<ItemBox> items = FXCollections.observableArrayList();

	public void initialize(URL location, ResourceBundle resources) {

		Main.controller = this;

		listView.setItems(items);
		addButton.setSelected(true);

		testButton.setOnAction(event -> {
			items.add(new ItemBox());
		});

	}

	private ItemBox getNewItem(String gtin) throws IOException {

		Gson gson = new Gson();

		URL url = new URL("https://api.outpan.com/v2/products/" + gtin + "?apikey=e13a9fb0bda8684d72bc3dba1b16ae1e");

		StringBuilder temp = new StringBuilder();

		Scanner scanner = new Scanner(url.openStream());

		while (scanner.hasNext()) {
			temp.append(scanner.nextLine());
		}

		scanner.close();

		Item item = new Item(gson.fromJson(temp.toString(), Item.class));

		return new ItemBox(item.gtin, item.name, item.getAmount());
	}

	public boolean addItem(String gtin) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("Add: " + gtin);
				try {
					items.add(getNewItem(gtin));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return false;
	}

	public boolean removeItem(String gtin) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("Remove: " + gtin);

			}
		});

		return false;
	}

}
