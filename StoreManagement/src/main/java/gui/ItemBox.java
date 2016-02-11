package gui;

import grossmann.StoreManagement.Item;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ItemBox extends HBox {

	private Item item;
	private Label gtin = new Label();
	private Label name = new Label();
	private Label amount = new Label();
	private Image image;
	private ImageView imageView;

	private static int counter = 0;

	public ItemBox() {
		
		gtin.setText("12345");
		amount.setText("10");
		
		if (counter < 2) {
			name.setText("Number: " + counter++);
		} else {
			name.setText("Umber: " + counter++);
		}
		
		this.getChildren().add(gtin);
		this.getChildren().add(name);
		this.getChildren().add(amount);
	}

	public ItemBox(Item item) {
		this.item = item;
		setupParts();
		this.getChildren().add(gtin);
		this.getChildren().add(name);
		this.getChildren().add(amount);
	}

	private void setupParts() {
		this.gtin.setText(item.gtin);
		this.name.setText(item.name);
		this.amount.setText(String.valueOf(item.getAmount()));

//		if (item.images == null) {
//			this.image = new Image(item.images[0]);
//			this.imageView = new ImageView(image);
//		}
	}

	public String getGtin() {
		return gtin.getText();
	}

	public void setGtin(String gtin) {
		this.gtin.setText(gtin);
	}

	public String getName() {
		return name.getText();
	}

	public int getAmount() {
		return Integer.parseInt(amount.getText());
	}

	public void setAmount(int amount) {
		this.amount.setText(String.valueOf(amount));
	}

}
