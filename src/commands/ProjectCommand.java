package commands;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;
import storage.Database;
import values.Dataset;

/**
 * This outputs a table containing the elements of specified 
 * fields from a table.
 */
public class ProjectCommand extends AbstractQueryCommand {
	private Pattern pattern = Pattern.compile(
			"project\\s+(?:([a-zA-Z]+|\\$\\d+)|\\(.*\\))\\s+over\\s+(.*)\\s*;", Pattern.CASE_INSENSITIVE);
	private String tableName;
	private String fieldList;

	@Override
	public boolean matches(String newInput) {
		input = newInput;
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			tableName = matcher.group(1);
			fieldList = matcher.group(2);
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
		
		return data.project(fieldList);
	}
}
