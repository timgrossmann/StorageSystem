package grossmann.StoreManagement;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

public class Alerter {
	
	public enum AlerterType {
		INFO, WARNING, ERROR, CONFIRM;
	}
	
	private Alerter() {
	}
	
	public static Alert getAlert(AlerterType type, String title, String header, String content) {
		
		Alert alert = null;
		
		switch(type) {
		case CONFIRM:
			alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UTILITY);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(content);
			break;
		case ERROR:
			alert = new Alert(AlertType.ERROR);
			alert.initStyle(StageStyle.UTILITY);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(content);
			break;
		case INFO:
			alert = new Alert(AlertType.INFORMATION);
			alert.initStyle(StageStyle.UTILITY);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(content);
			break;
		case WARNING:
			alert = new Alert(AlertType.WARNING);
			alert.initStyle(StageStyle.UTILITY);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(content);
			break;
		default:
			break;
		}
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
	
	
	
	
	
}
