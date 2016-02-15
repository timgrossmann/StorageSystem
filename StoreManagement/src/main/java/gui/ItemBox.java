package gui;

import grossmann.StoreManagement.Item;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ItemBox extends HBox {

	private Item item;
	private Label gtin = new Label();
	private Label name = new Label();
	private Label amount = new Label();
	private Label categories = new Label();
	// private Image image;
	// private ImageView imageView;

	public ItemBox(Item item) {
		this.setSpacing(20);
		this.setAlignment(Pos.CENTER_LEFT);
		this.item = item;
		setupParts();
		this.getChildren().add(gtin);
		this.getChildren().add(name);
		this.getChildren().add(amount);
		if (item.categories != null) {
			this.getChildren().add(categories);
		}

	}

	private void setupParts() {
		this.gtin.setText(item.gtin);
		this.name.setText(item.name);
		this.amount.setText(String.valueOf(item.getAmount()));

		if (this.item.categories != null && item.categories.length >= 1) {
			String temp = item.categories[0];

			for (int i = 1; i < item.categories.length; i++) {
				temp += ",\n" + item.categories[i];
			}

			this.categories.setText(temp.trim());
		}

		// if (item.images == null) {
		// this.image = new Image(item.images[0]);
		// this.imageView = new ImageView(image);
		// }
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

	public void setItem(Item item) {
		item.setAmount(this.getAmount());
		this.item = item;
		setupParts();
	}

}
