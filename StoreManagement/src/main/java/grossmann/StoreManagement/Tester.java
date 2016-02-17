package grossmann.StoreManagement;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class Tester {
	public static void main(String[] args) {

		Map<String, Item> items = new HashMap<String, Item>();

		try {

			Gson gson = new Gson();

			Scanner scan = new Scanner(System.in);

			String input = scan.nextLine();

			scan.close();

			URL url = new URL(
					"https://api.outpan.com/v2/products/" + input + "?apikey=e13a9fb0bda8684d72bc3dba1b16ae1e");

			StringBuilder temp = new StringBuilder();

			Scanner scanner = new Scanner(url.openStream());

			while (scanner.hasNext()) {
				temp.append(scanner.nextLine());
			}

			scanner.close();

			System.out.println(temp.toString());

			System.out.println();

			Item item = new Item(gson.fromJson(temp.toString(), Item.class));

			items.put(item.gtin, item);

			System.out.println(item.gtin);
			System.out.println(item.name);
			System.out.println(item.categories[0]);
//			System.out.println(item.images[0]);
			System.out.println(item.attributes);

			System.out.println(items.get(item.gtin).getAmount());

			if (items.containsKey(item.gtin)) {
				items.get(item.gtin).increaseAmount();
			} else {
				items.put(item.gtin, item);
			}

			System.out.println(items.get(item.gtin).getAmount());

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
