package grossmann.StoreManagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import gui.ItemBox;
import gui.Main;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Printer {

	private static FileChooser chooser = new FileChooser();
	private static File file;

	private Printer() {
	}

	public static boolean printOut(ArrayList<ItemBox> items, String header) {

		chooser.setTitle("Print List: ");
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		chooser.setSelectedExtensionFilter(new ExtensionFilter("TextFiles(*.txt)", "*.txt"));
		chooser.getExtensionFilters().add(new ExtensionFilter("TextFiles(*.txt)", "*.txt"));
		chooser.setInitialFileName(header + ".txt");


		file = chooser.showSaveDialog(Main.primaryStage);

		if (file != null) {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
				
				bw.write(header + "\n\n");

				for (ItemBox item : items) {
					bw.write("- " + item.getName() + ("---------------------------------------------------------------------------" + item.getAmount()).substring(item.getName().length() + String.valueOf(item.getAmount()).length()) + "x" + "\n");
				}

				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
