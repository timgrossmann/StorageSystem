package save_load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.Alerter;
import gui.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import parts.Item;

public class Loader {
	private static FileChooser chooser = new FileChooser();
	private static Logger log = LogManager.getLogger(Loader.class);

	private Loader() {
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
			chooser.setSelectedExtensionFilter(new ExtensionFilter("Save Files (*.sav)", "*.sav"));
			chooser.getExtensionFilters().add(new ExtensionFilter("Save Files (*.sav)", "*.sav"));

			file = chooser.showOpenDialog(Main.primaryStage);
		} else {
			file = new File(System.getProperty("user.home"), "Desktop/saveFile.sav");
			log.debug("Loader called without Dialog");
		}

		if (file != null) {

			try (ObjectInputStream o = new ObjectInputStream(new FileInputStream(file))) {

				Object temp = o.readObject();

				@SuppressWarnings("unchecked")
				List<Item> items = (List<Item>) temp;

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
