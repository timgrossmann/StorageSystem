package gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ItemBox extends HBox{
	
	private Label gtin = new Label();
	private Label name = new Label();
	private Label amount = new Label();
	private Image image;
	private ImageView imageView;

	public ItemBox() {
		this.getChildren().add(gtin);
		this.getChildren().add(name);
		this.getChildren().add(amount);
	}
	
	public ItemBox(String gtin, String name, int amount) {
		this();
		this.gtin.setText(gtin);
		this.name.setText(name);
		this.amount.setText(String.valueOf(amount));
	}
	
	public ItemBox(String gtin, String name, int amount, String image) {
		this(gtin, name, amount);
		this.image = new Image(image);
		this.imageView = new ImageView(this.image);
		this.getChildren().add(this.imageView);
	}
	
	
	
}
