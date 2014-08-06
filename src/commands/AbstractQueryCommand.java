package commands;

import java.util.HashMap;

import main.DatabaseException;

import values.Dataset;

public abstract class AbstractQueryCommand implements ICommand {
	protected String input;
	
	public abstract Dataset getData(HashMap<String, Dataset> datasets) throws DatabaseException;
	
	public void execute() throws DatabaseException {
		System.out.println(QueryList.parse("("+input.replace(";", ")")));
	}
}
