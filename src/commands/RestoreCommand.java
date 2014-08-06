package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This reads database information from a file.
 */
public class RestoreCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"restore\\s+from\\s+(.*)\\s*;", Pattern.CASE_INSENSITIVE);
	@SuppressWarnings("unused")
	private String fileName;

	@Override
	public boolean matches(String input) {
		Matcher matcher = pattern.matcher(input.trim());
		if (matcher.matches()) {
			fileName = matcher.group(1);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() {
		System.out.println("This is a correct restore command.");
	}

}
