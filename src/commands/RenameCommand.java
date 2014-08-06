package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;

import storage.Database;

/**
 * This changes the name of a table.
 */
public class RenameCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"rename\\s+table\\s+(.+)\\s+to\\s+(.+);", Pattern.CASE_INSENSITIVE);
	private String tableName;
	private String newName;

	@Override
	public boolean matches(String input) {
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			tableName = matcher.group(1);
			newName = matcher.group(2);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() throws DatabaseException {
		Database.getInstance().rename(tableName, newName);
	}
}
