package persistence;

import java.util.HashMap;

/**
 * This class is used to replace certain symbols
 * with XML-safe representations
 */
public class XMLEncoder {
	private static HashMap<String, String> codes;

	static {
		codes = new HashMap<String, String>();
		codes.put("\"", "&quot;");
		codes.put("&", "&amp;");
		codes.put("'", "&apos;");
		codes.put("<", "&lt;");
		codes.put(">", "&gt;");
	}

	/**
	 * Private constructor allows only static
	 * references to methods.
	 */
	private XMLEncoder(){}

	/**
	 * Replaces characters which have special
	 * syntactic meanings in XML with representations
	 * which will not cause problems with an XML
	 * file.
	 * 
	 * @param input string to be converted for use
	 * 	            as an XML entity
	 * @return input with characters substituted
	 */
	public static String encode(String input) {
		for(String x : codes.keySet()) {
			input = input.replace(x, codes.get(x));
		}

		return input;
	} 
}
