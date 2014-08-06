package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;
import main.Driver;

/**
 * This reads and applies information about a database from a 
 * file.
 */
public class ReadCommand implements ICommand {
	private Pattern pattern = Pattern.compile(
			"read\\s+\\'(.+)\\'\\s*;", Pattern.CASE_INSENSITIVE);
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
	public void execute() throws DatabaseException {
		try {
			new Driver().interpret(new Scanner(new File(fileName)), false);
		} catch (FileNotFoundException e) {
			throw new DatabaseException(
					"File '"+fileName+"' not found.", e);
		}
	}

}
