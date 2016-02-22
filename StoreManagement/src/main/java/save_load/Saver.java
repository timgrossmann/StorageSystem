package save_load;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import grossmann.StoreManagement.Alerter;
import grossmann.StoreManagement.Item;
import gui.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Saver {

	private static FileChooser chooser = new FileChooser();
	private static Logger log = LogManager.getLogger(Saver.class);

	private Saver() {
	}

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
			file = new File(System.getProperty("user.home"), "Desktop/saveFile.sav");
		}

		if (file != null) {

			try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file))) {

				o.writeObject(items);

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
