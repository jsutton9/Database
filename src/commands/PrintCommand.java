package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;
import storage.Database;

/**
 * This prints the contents of a table or lists the tables in 
 * the database and their fields.
 */
public class PrintCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"print\\s+(\\S+)\\s*;", Pattern.CASE_INSENSITIVE);
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
		if (tableName.equalsIgnoreCase("dictionary"))
			System.out.println(Database.getInstance());
		else
			System.out.println(Database.getInstance().tableString(tableName));
	}

}
