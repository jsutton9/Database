package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;

import storage.Database;

/**
 * This creates a new table in the database with the specified 
 * fields.
 */
public class DefineTableCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"define\\s+table\\s+(\\S+)\\s+having\\s+fields\\s*\\((.*)\\)\\s*;", Pattern.CASE_INSENSITIVE);
	private String tableName;
	private String fieldList;

	@Override
	public boolean matches(String input) {
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
	public void execute() throws DatabaseException {
		Database.getInstance().addTable(tableName, fieldList);
	}
}
