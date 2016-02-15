package grossmann.StoreManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

public class Alerter {

	private static List<String> choices = new ArrayList<>(Arrays.asList("Name", "Amount", "Categorie"));

	private Alerter() {
	}

	public static Alert getAlert(AlertType type, String title, String header, String content) {

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

	public static Optional<String> getTextDialog(String title, String header, String content) {

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);

		Optional<String> result = dialog.showAndWait();

		return result;
	}

	public static Optional<String> getChoiceDialog(String title, String header, String content) {
		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();

		return result;
	}

}
