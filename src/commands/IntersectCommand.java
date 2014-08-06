package commands;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;
import storage.Database;
import values.Dataset;

/**
 * This outputs a table containing the intersection of two tables.
 */
public class IntersectCommand extends AbstractQueryCommand {
	private Pattern pattern = Pattern.compile(
			"intersect\\s+(?:([a-zA-Z]+|\\$\\d+)|\\(.*\\))\\s+and\\s+(?:([a-zA-Z]+|\\$\\d+)|\\(.*\\))\\s*;", Pattern.CASE_INSENSITIVE);
	private String tableNameA;
	private String tableNameB;

	@Override
	public boolean matches(String newInput) {
		input = newInput;
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			tableNameA = matcher.group(1);
			tableNameB = matcher.group(2);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Dataset getData(HashMap<String, Dataset> map) throws DatabaseException {
		Dataset dataA;
		Dataset dataB;
		
		if (map.containsKey(tableNameA))
			dataA = map.remove(tableNameA);
		else
			dataA = Database.getInstance().getData(tableNameA);
		
		if (map.containsKey(tableNameB))
			dataB = map.remove(tableNameB);
		else
			dataB = Database.getInstance().getData(tableNameB);
		
		return dataA.intersect(dataB);
	}
}
