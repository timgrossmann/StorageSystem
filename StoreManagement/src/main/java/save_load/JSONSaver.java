package save_load;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

public class JSONSaver {

	private static FileChooser chooser = new FileChooser();
	private static Logger log = LogManager.getLogger(JSONSaver.class);

	private JSONSaver() {
	}

	/**
	 * Serializes the items either to the standart File location or a location
	 * chosen with the SaveDialog
	 * 
	 * @param items
	 * @param setLocation
	 * @return
	 */
	public static boolean save(List<Item> items, boolean setLocation) {

		File file;

		if (setLocation) {
			log.debug("Saver called with Loaddialog");
			chooser.setTitle("Save Tournament: ");
			chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			chooser.setSelectedExtensionFilter(new ExtensionFilter("SaveFiles(*.sav)", "*.sav"));
			chooser.getExtensionFilters().add(new ExtensionFilter("Save Files(*.sav)", "*.sav"));

			file = chooser.showSaveDialog(Main.primaryStage);
		} else {
			log.debug("Saver called without Dialog");
			file = new File(System.getProperty("user.home"), "Desktop/saveFile.json");
		}

		Gson gson = new Gson();
		
		if (file != null) {

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
				
				gson.toJson(items, new TypeToken<List<Item>>(){}.getType(), bw);

				return true;

			} catch (FileNotFoundException e3) {
				log.error("Saver - File not Found: " + e3.getMessage());

				Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Coudn´t find file", "Saving failed",
						"Please try again!");
				alert.showAndWait();

			} catch (IOException e3) {
				log.error("Saver - Exception: " + e3.getMessage());

				Alert alert = Alerter.getAlert(AlertType.INFORMATION, "Coudn´t find memory", "Saving failed",
						"Please try again!");
				alert.showAndWait();
			}
		}

		return false;
	}

}
