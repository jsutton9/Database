package commandTests;

import org.junit.Before;

import commands.ExitCommand;

public class ExitCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new ExitCommand();
		good = new String[] {
				"exit;",
				"eXiT;",
				"  exit   ;"
		};
		bad = new String[] {
				";"
		};
	}

}
