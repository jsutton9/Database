package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;
import storage.Database;

/**
 * This deletes fields from a table meeting a specified
 * condition, or all fields if no condition is specified.
 */
public class DeleteCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"delete\\s+(\\S+)(?:\\s+where\\s+([a-zA-Z]+)\\s*(=|!=|<=|>=|<|>)\\s*(.*))?\\s*;", Pattern.CASE_INSENSITIVE);
	private String tableName;
	private boolean conditional;
	private String fieldName;
	private String relop;
	private String value;

	@Override
	public boolean matches(String input) {
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			tableName = matcher.group(1);
			fieldName = matcher.group(2);
			relop = matcher.group(3);
			value = matcher.group(4);
			conditional = fieldName != null;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() throws DatabaseException {
		Database.getInstance().delete(tableName, conditional, fieldName, relop, value);
	}

}
