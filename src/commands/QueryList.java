package commands;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;

import values.Dataset;

public class QueryList {
	private static Pattern pattern = Pattern
			.compile("\\s*(\\(([^)^(]*?)\\))\\s*");
	private static AbstractQueryCommand[] queryCommands = new AbstractQueryCommand[] { 
			new SelectCommand(),
			new ProjectCommand(), 
			new JoinCommand(), 
			new IntersectCommand(),
			new UnionCommand(), 
			new MinusCommand(), 
			new OrderCommand() 
	};

	public static Dataset parse(String input) throws DatabaseException {
		Matcher m = pattern.matcher(input);
		HashMap<String, Dataset> map = new HashMap<String, Dataset>();
		int i=0;
		OUTER:
		while (m.find()) {
			String inside = m.group(2);
			String holderName = "$" + Integer.toString(i++);
			for (AbstractQueryCommand query : queryCommands) {
				if (query.matches(inside+";")) {
					map.put(holderName, query.getData(map));
					input = input.replace(m.group(1), " "+holderName+" ");
					m = pattern.matcher(input);
					continue OUTER;
				}
			}
			throw new DatabaseException("Error parsing query list.");
		}
		
		input = input.trim();
		if (! map.containsKey(input))
			throw new DatabaseException("Error parsing query list.");
		return map.get(input);
	}
}
