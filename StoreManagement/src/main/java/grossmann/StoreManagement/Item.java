package grossmann.StoreManagement;

public final class Item {
	public final String gtin;
	public final String outpan_url;
	public final String name;
//	public final Attributes attributes;
//	public final String[] images;
//	public final String[] videos;
//	public final String[] categories;
	private int amount;

	public Item(Item item) {
		this.gtin = item.gtin;
		this.outpan_url = item.outpan_url;
		this.name = item.name;
//		this.attributes = item.attributes;
//		this.images = item.images;
//		this.videos = item.videos;
//		this.categories = item.categories;
		this.amount = 1;
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

//	public class Attributes {
//		@SerializedName(value = "Package Contents")
//		public final String packageContents;
//		@SerializedName(value = "Volume")
//		public final String volume;
//
//		public Attributes(String packageContents, String volume) {
//			this.packageContents = packageContents;
//			this.volume = volume;
//		}
//	}

}