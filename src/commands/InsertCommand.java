package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;

import storage.Database;

/**
 * This inserts values into a table.
 */
public class InsertCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"insert\\s*\\(\\s*(.+)\\s*\\)\\s*into\\s+(\\S+)\\s*;", Pattern.CASE_INSENSITIVE);
	private String valueList;
	private String tableName;

	@Override
	public boolean matches(String input) {
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			valueList = matcher.group(1);
			tableName = matcher.group(2);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() throws DatabaseException {
		Database.getInstance().insert(tableName, valueList);
	}
}
