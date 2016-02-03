package grossmann.StoreManagement;

import com.google.gson.annotations.SerializedName;

public final class Item {
	public final String gtin;
	public final String outpan_url;
	public final String name;
	public final Attributes attributes;
	public final String[] images;
	public final String[] videos;
	public final String[] categories;
	private int amount = 1;

	public Item(String gtin, String outpan_url, String name, Attributes attributes, String[] images, String[] videos,
			String[] categories) {
		this.gtin = gtin;
		this.outpan_url = outpan_url;
		this.name = name;
		this.attributes = attributes;
		this.images = images;
		this.videos = videos;
		this.categories = categories;
	}

	public int getAmount() {
		return amount;
	}
	
	public void increaseAmount() {
		amount++;
	}

	public void decreaseAmount() {
		if (amount > 0) {
			amount--;
		}
	}

	public class Attributes {
		@SerializedName(value = "Package Contents")
		public final String packageContents;
		@SerializedName(value = "Volume")
		public final String volume;

		public Attributes(String packageContents, String volume) {
			this.packageContents = packageContents;
			this.volume = volume;
		}
	}

}