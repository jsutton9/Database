package commands;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;
import persistence.XMLWriter;

/**
 * This ends the interpreter program.
 */
public class ExitCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"exit\\s*;", Pattern.CASE_INSENSITIVE);

	@Override
	public boolean matches(String input) {
		Matcher matcher = pattern.matcher(input.trim());
		return matcher.matches();
	}

	@Override
	public void execute() throws DatabaseException {
		XMLWriter writer = new XMLWriter();
		try {
			writer.saveDatabase("db.xml");
		} catch (FileNotFoundException e) {
			throw new DatabaseException("Unable to access 'db.xml'");
		}
		System.out.println("exiting");
		System.exit(0);
	}

}
