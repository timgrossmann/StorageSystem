package save_load;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.ItemBox;
import gui.Main;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Printer {

	public enum PrintOutType {
		OVERVIEW, SHOPPING;
	}

	private static FileChooser chooser = new FileChooser();
	private static File file;
	private static Logger log = LogManager.getLogger(Printer.class);

	private Printer() {
	}

	public static boolean printOut(ArrayList<ItemBox> items, PrintOutType type) {

		chooser.setTitle("Print List: ");
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		chooser.setSelectedExtensionFilter(new ExtensionFilter("TextFiles(*.txt)", "*.txt"));
		chooser.getExtensionFilters().add(new ExtensionFilter("TextFiles(*.txt)", "*.txt"));
		String header = "Printout";
		switch (type) {
		case OVERVIEW:
			header = "Overview";
			break;
		case SHOPPING:
			header = "Shopping";
			break;
		}
		chooser.setInitialFileName(header + ".txt");

		file = chooser.showSaveDialog(Main.primaryStage);

		if (file != null) {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

				bw.write(header + "\n\n");

				switch (type) {
				case OVERVIEW:
					log.debug("Printout as: " + type.name());
					for (ItemBox item : items) {
						bw.write("- " + item.getName()
								+ ("---------------------------------------------------------------------------"
										+ item.getAmount()).substring(
												item.getName().length() + String.valueOf(item.getAmount()).length())
								+ "x" + "\n");
					}
					break;
				case SHOPPING:
					log.debug("Printout as: " + type.name());
					for (ItemBox item : items) {
						bw.write("- " + item.getName() + "\n");
					}
					break;
				}

				return true;
			} catch (FileNotFoundException e) {
				log.error("Printout - File not Found: " + e.getMessage());
			} catch (IOException e) {
				log.error("Printout - IOException: " + e.getMessage());
			}
		} else {
			log.debug("No File selected");
			return false;
		}
		return false;
	}

}
