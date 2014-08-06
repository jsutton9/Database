package main;

import java.io.File;
import java.util.Scanner;

import persistence.XMLLoader;

import commands.BackupCommand;
import commands.DefineIndexCommand;
import commands.DefineTableCommand;
import commands.DeleteCommand;
import commands.DropCommand;
import commands.ExitCommand;
import commands.ICommand;
import commands.InsertCommand;
import commands.IntersectCommand;
import commands.JoinCommand;
import commands.MinusCommand;
import commands.OrderCommand;
import commands.PrintCommand;
import commands.ProjectCommand;
import commands.ReadCommand;
import commands.RenameCommand;
import commands.RestoreCommand;
import commands.SelectCommand;
import commands.UnionCommand;
import commands.UpdateCommand;

/**
 * A class for executing database commands from a text stream.
 */
public class Driver {
	private ICommand[] commands = new ICommand[] {
			new BackupCommand(),
			new DefineIndexCommand(),
			new DefineTableCommand(),
			new DeleteCommand(),
			new DropCommand(),
			new ExitCommand(),
			new InsertCommand(),
			new IntersectCommand(),
			new JoinCommand(),
			new MinusCommand(),
			new OrderCommand(),
			new PrintCommand(),
			new ProjectCommand(),
			new ReadCommand(),
			new RenameCommand(),
			new RestoreCommand(),
			new SelectCommand(),
			new UnionCommand(),
			new UpdateCommand()
	};
	
	/**
	 * Reads database commands from sc and executes them until
	 * EOF is reached or an exit command is executed.
	 * 
	 * @param sc		This is the scanner from which commands
	 * 					are read.
	 * @param prompt	If true, a prompt will be printed to
	 * 					standard output. If false, EOF will be
	 * 					checked for, for reading from a file.
	 */
	public void interpret(Scanner sc, boolean prompt) {
		OUTER:
		while (prompt || sc.hasNextLine()) {
			if (prompt) {
				System.out.print("> ");
			}
			String input = sc.nextLine().trim();
			while (! input.contains(";") && 
					(prompt || sc.hasNextLine())) {
				if (prompt) {
					System.out.print(". ");
				}
				input += " " + sc.nextLine().trim();
			}
			for (ICommand command : commands) {
				if (command.matches(input)) {
					try {
						command.execute();
					} catch (DatabaseException e) {
						System.out.println(e.getMessage());
					}
					continue OUTER;
				}
			}
			System.out.println("This is not a valid command.");
		}
	}
	
	/**
	 * Produces a command line interface which will accept database
	 * commands until an exit command is executed.
	 * 
	 * @param args		Takes no arguments.
	 */
	public static void main(String[] args) {
		try {
			if (new File("db.xml").isFile())
				new XMLLoader().load("db.xml");
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
		}
		new Driver().interpret(new Scanner(System.in), true);
	}
}
