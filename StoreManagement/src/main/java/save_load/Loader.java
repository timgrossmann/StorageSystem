package save_load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import grossmann.StoreManagement.Alerter;
import grossmann.StoreManagement.Item;
import gui.ItemBox;
import gui.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.StageStyle;

public class Loader {
	private static FileChooser chooser = new FileChooser();

	private Loader() {
	}

	public static List<Item> load(boolean selectFile) {

		File file;

		if (selectFile) {
			chooser.setTitle("Open Tournament: ");
			chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			chooser.setSelectedExtensionFilter(new ExtensionFilter("Save Files (*.sav)", "*.sav"));
			chooser.getExtensionFilters().add(new ExtensionFilter("Save Files (*.sav)", "*.sav"));

			file = chooser.showOpenDialog(Main.primaryStage);
		} else {
			file = new File(System.getProperty("user.home"), "Desktop/saveFile.sav");
		}

		if (file != null) {

			try (ObjectInputStream o = new ObjectInputStream(new FileInputStream(file))) {

				Object temp = o.readObject();

				List<Item> items = (List<Item>) temp;

				return items;

			} catch (FileNotFoundException e) {

				Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Coudn´t find file", "Saving failed",
						"Please try again!");
				alert.showAndWait();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;

	}
}
