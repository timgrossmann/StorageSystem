package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

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

	public boolean addItem(String gtin) {
		System.out.println("Add: "+ gtin);
		return false;
	}

	public boolean removeItem(String gtin) {
		System.out.println("Remove: "+ gtin);
		return false;
	}

}
