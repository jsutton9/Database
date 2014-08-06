package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;

import storage.Database;

/**
 * This deletes a specified table from the database.
 */
public class DropCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"drop\\s+table\\s+(\\S+)\\s*;", Pattern.CASE_INSENSITIVE);
	private String tableName;

	@Override
	public boolean matches(String input) {
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			tableName = matcher.group(1);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() throws DatabaseException {
		Database.getInstance().drop(tableName);
	}

}
