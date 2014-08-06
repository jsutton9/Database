package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This establishes an index, allowing specified data to be 
 * accessed more quickly.
 */
public class DefineIndexCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"define\\s+index\\s+on\\s+(\\S+)\\s*\\(\\s*(\\S+)\\s*\\)\\s*;", Pattern.CASE_INSENSITIVE);
	@SuppressWarnings("unused")
	private String tableName;
	@SuppressWarnings("unused")
	private String fieldName;

	@Override
	public boolean matches(String input) {
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			tableName = matcher.group(1);
			fieldName = matcher.group(2);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() {
		System.out.println("This is a correct define index command.");
	}

}
