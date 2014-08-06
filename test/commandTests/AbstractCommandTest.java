package commandTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import commands.ICommand;

public abstract class AbstractCommandTest {

	protected ICommand command;

	public abstract void setUp() throws Exception;

	protected String[] good;
	protected String[] bad;

	public AbstractCommandTest() {
		super();
	}

	@Test
	public void testMatches() {
		for (String s : good) {
			assertTrue(command.matches(s));
		}
		for (String s : bad) {
			assertFalse(command.matches(s));
		}
	}

}