package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

public class Alerter {

	private static List<String> choices = new ArrayList<>(Arrays.asList("Name", "Amount", "Categorie"));
	private static Logger log = LogManager.getLogger(Alerter.class);


	private Alerter() {
	}

	/**
	 * returns an with the parameter assigned Texts for shorter Code in the Controller Class
	 * @param type
	 * @param title
	 * @param header
	 * @param content
	 * @return
	 */
	public static Alert getAlert(AlertType type, String title, String header, String content) {
		
		log.info("GetAlert called with: " + type.name() + " - " + title + " - " + header + " - " + content);
		Alert alert = null;

		switch (type) {
		case CONFIRMATION:
			alert = new Alert(AlertType.CONFIRMATION);
			break;
		case ERROR:
			alert = new Alert(AlertType.ERROR);
			break;
		case INFORMATION:
			alert = new Alert(AlertType.INFORMATION);
			break;
		case WARNING:
			alert = new Alert(AlertType.WARNING);
			break;
		default:
			break;
		}

		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		return alert;
	}

	/**
	 * returns the in the dialog entered Text 
	 * For shorter Code in the Controller.class
	 * @param title
	 * @param header
	 * @param content
	 * @return
	 */
	public static Optional<String> getTextDialog(String title, String header, String content) {
		
		log.info("GetAlert called with: " + title + " - " + header + " - " + content);

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);

		Optional<String> result = dialog.showAndWait();

		return result;
	}

	/**
	 * returns the selected Choice of the choices array
	 * @param title
	 * @param header
	 * @param content
	 * @return
	 */
	public static Optional<String> getChoiceDialog(String title, String header, String content) {

		log.info("GetAlert called with: " + title + " - " + header + " - " + content);
		
		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);

		Optional<String> result = dialog.showAndWait();

		return result;
	}

}
