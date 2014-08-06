package commands;

import main.DatabaseException;

/**
 * An interface for classes representing database commands.
 */
public interface ICommand {
	
	/**
	 * Tests if input is a valid command.
	 * 
	 * @param input		String which may be a valid command.
	 * @return			true iff input matches the pattern for the
	 * 					command
	 */
	public boolean matches(String input);
	
	/**
	 * Executes the command.
	 */
	public void execute() throws DatabaseException;
}
