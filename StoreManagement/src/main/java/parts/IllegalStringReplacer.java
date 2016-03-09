package parts;

public class IllegalStringReplacer {
	
	/**
	 * replaces all unwanted Characters from the passed string
	 * 
	 * @param output
	 * @return
	 */
	public static String replaceIllegalChars(String output) {
		output = output.replaceAll("Ä", "Ae");
		output = output.replaceAll("ä", "ae");
		output = output.replaceAll("Ö", "Oe");
		output = output.replaceAll("ö", "oe");
		output = output.replaceAll("Ü", "Ue");
		output = output.replaceAll("ü", "ue");
		output = output.replaceAll("ß", "ss");
		output = output.replaceAll("/", "");
		output = output.replaceAll("\"", "inch");
		output = output.replaceAll("'", "");

		return output;
	}

}
