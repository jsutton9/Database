package commands;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;
import storage.Database;
import values.Dataset;

/**
 * This outputs the contents of a table ordered by the values 
 * in a specified field, optionally in reverse order.
 */
public class OrderCommand extends AbstractQueryCommand {
	private Pattern pattern = Pattern.compile(
			"order\\s+(?:([a-zA-Z]+|\\$\\d+)|\\(.*\\))\\s+by\\s+(\\S+)(\\s+descending)?\\s*;", Pattern.CASE_INSENSITIVE);
	private String tableName;
	private String fieldName;
	private boolean descending;

	@Override
	public boolean matches(String newInput) {
		input = newInput;
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			tableName = matcher.group(1);
			fieldName = matcher.group(2);
			descending = matcher.group(3) != null;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Dataset getData(HashMap<String, Dataset> map) throws DatabaseException {
		Dataset data;
		
		if (map.containsKey(tableName))
			data = map.remove(tableName);
		else
			data = Database.getInstance().getData(tableName);
		
		return data.order(fieldName, descending);
	}
}
