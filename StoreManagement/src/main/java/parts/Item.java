package parts;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public final class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4372500228175284569L;

	public final String gtin;
	public final String outpan_url;
	public final String name;
	public final HashMap<String, String> attributes;
	public final String[] images;
	public final String[] categories;
	private int amount;

	public Item(Item item) {
		this.gtin = item.gtin;
		this.outpan_url = item.outpan_url;
		this.name = item.name;
		this.attributes = item.attributes;
		this.images = item.images;
		this.categories = item.categories;
		this.amount = 1;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + Arrays.hashCode(categories);
		result = prime * result + ((gtin == null) ? 0 : gtin.hashCode());
		result = prime * result + Arrays.hashCode(images);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((outpan_url == null) ? 0 : outpan_url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (!Arrays.equals(categories, other.categories))
			return false;
		if (gtin == null) {
			if (other.gtin != null)
				return false;
		} else if (!gtin.equals(other.gtin))
			return false;
		if (!Arrays.equals(images, other.images))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (outpan_url == null) {
			if (other.outpan_url != null)
				return false;
		} else if (!outpan_url.equals(other.outpan_url))
			return false;
		return true;
	}

}