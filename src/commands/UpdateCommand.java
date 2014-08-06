package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;

import storage.Database;

/**
 * This sets a field to a specified value for all rows in a table, optionally
 * including only those rows meeting a specified condition.
 */
public class UpdateCommand implements ICommand {
	private Pattern pattern = Pattern
			.compile(
					"update\\s+(\\S+)\\s+set\\s+(\\S+)\\s*=\\s*(.*?)(?:\\s+where\\s+([a-zA-Z]+)\\s*(=|!=|<=|>=|<|>)\\s*(.*))?\\s*;",
					Pattern.CASE_INSENSITIVE);
	private String tableName;
	private String fieldName;
	private String value;
	private boolean conditional;
	private String condFieldName;
	private String relop;
	private String condValue;

	@Override
	public boolean matches(String input) {
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			tableName = matcher.group(1);
			fieldName = matcher.group(2);
			value = matcher.group(3);
			condFieldName = matcher.group(4);
			relop = matcher.group(5);
			condValue = matcher.group(6);
			conditional = condFieldName != null;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() throws DatabaseException {
		Database.getInstance().update(tableName, fieldName, value,
				conditional, condFieldName, relop, condValue);
	}

}
