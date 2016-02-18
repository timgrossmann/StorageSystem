package gui;

import grossmann.StoreManagement.Item;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ItemBox extends HBox {

	private Item item;
	private Label name = new Label();
	private Label amount = new Label();
	private Label categories = new Label();
	private Image image;
	private String attributes = "";

	public ItemBox(Item item) {
		this.setSpacing(20);
		this.setAlignment(Pos.CENTER_LEFT);
		this.item = item;
		setupParts();
		this.getChildren().add(name);
		name.setPrefWidth(500);
		this.getChildren().add(amount);
		amount.setPrefWidth(30);
		if (item.categories != null) {
			this.getChildren().add(categories);
		}

	}

	private void setupParts() {

		this.name.setText(item.name);
		this.amount.setText(String.valueOf(item.getAmount()));

		this.categories.setText(getCategoriesText("short"));

		// if (item.images.length != 0) {
		// this.image = new Image(item.images[0]); //TODO
		// }
	}

	// public Image getImage() {
	// return image;
	// }

	public String getAttributes() {
		String temp = "";
		for (String key : item.attributes.keySet()) {
			temp += key + ": " + item.attributes.get(key) + "\n";
		}
		attributes = temp;
		return attributes;
	}

	public String getGtin() {
		return item.gtin;
	}

	public String getName() {
		return item.name;
	}

	public int getAmount() {
		return item.getAmount();
	}

	public void increaseAmount() {
		this.item.increaseAmount();
		this.amount.setText(String.valueOf(item.getAmount()));
	}

	public void decreaseAmount() {
		this.item.decreaseAmount();
		this.amount.setText(String.valueOf(item.getAmount()));
	}

	public Item getItem() {
		return item;
	}

	public String[] getCategories() {
		return item.categories;
	}

	public String getCategoriesText(String length) {
		if (this.item.categories != null && item.categories.length >= 1) {
			String temp = item.categories[0];

			switch (length) {
			case "short":
				for (int i = 1; i < item.categories.length && i < 3; i++) {
					temp += ",\n" + item.categories[i];
				}
				break;
			case "long":
				for (int i = 1; i < item.categories.length; i++) {
					temp += ",\n" + item.categories[i];
				}
				break;
			}

			return temp.trim();
		}
		return "";
	}

	public void setItem(Item item) {
		item.setAmount(this.getAmount());
		this.item = item;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setupParts();
			}
		});

	}

}
