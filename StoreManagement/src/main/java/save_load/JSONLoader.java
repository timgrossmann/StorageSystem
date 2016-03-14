package save_load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gui.Alerter;
import gui.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import parts.Item;

public class JSONLoader {
	private static FileChooser chooser = new FileChooser();
	private static Logger log = LogManager.getLogger(JSONLoader.class);

	private JSONLoader() {
	}

	/**
	 * Either loads from the standart File or opens LoadDialog for the user to select a file
	 * depending of the value of selectFile
	 * @param selectFile
	 * @return
	 */
	public static List<Item> load(boolean selectFile) {

		File file;

		if (selectFile) {
			log.debug("Loader called with Loaddialog");
			chooser.setTitle("Open Tournament: ");
			chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			chooser.setSelectedExtensionFilter(new ExtensionFilter("JSON Files (*.json)", "*.json"));
			chooser.getExtensionFilters().add(new ExtensionFilter("JSON Files (*.json)", "*.json"));

			file = chooser.showOpenDialog(Main.primaryStage);
		} else {
			file = new File(System.getProperty("user.home"), "Desktop/saveFile.json");
			log.debug("Loader called without Dialog");
		}

		Gson gson = new Gson();
		
		if (file != null) {

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				
				List<Item> items = gson.fromJson(br, new TypeToken<List<Item>>(){}.getType());

				return items;

			} catch (FileNotFoundException e) {
				log.error("Loader - File not Found: " + e.getMessage());
				
				Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Coudn´t find file", "Loading failed",
						"Please try again!");
				alert.showAndWait();

			} catch (Exception e) {
				log.error("Loader - Exception: " + e.getMessage());
			}
		}

		return null;

	}
}
