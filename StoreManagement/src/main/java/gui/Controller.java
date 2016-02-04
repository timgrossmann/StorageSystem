package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class Controller implements Initializable{
	
	@FXML
	ListView<ItemBox> listView = new ListView<ItemBox>();
	@FXML
	Button testButton = new Button();
	
	ObservableList<ItemBox> items = FXCollections.observableArrayList();
	

	public void initialize(URL location, ResourceBundle resources) {

		listView.setItems(items);
		
		testButton.setOnAction(event -> {
			items.add(new ItemBox());
		});
		
	}
	
	

}
