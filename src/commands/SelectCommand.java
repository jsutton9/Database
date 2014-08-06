package commands;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;
import storage.Database;
import values.Dataset;

/**
 * This outputs the contents of a specified table, optionally 
 * with only rows meeting a specified condition included.
 */
public class SelectCommand extends AbstractQueryCommand {
	private Pattern pattern = Pattern.compile(
			"select\\s+(?:([a-zA-Z]+|\\$\\d+)|\\(.*\\))(?:\\s+where\\s+([a-zA-Z\\.]+)\\s*(=|!=|<=|>=|<|>)\\s*(.*))?\\s*;", Pattern.CASE_INSENSITIVE);
	private String tableName;
	private String fieldName;
	private String relop;
	private String value;

	@Override
	public boolean matches(String newInput) {
		input = newInput;
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			tableName = matcher.group(1);
			fieldName = matcher.group(2);
			relop = matcher.group(3);
			value = matcher.group(4);
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
		
		return data.select(fieldName, relop, value);
	}
}
