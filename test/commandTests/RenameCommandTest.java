package commandTests;

import org.junit.Before;

import commands.RenameCommand;

public class RenameCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new RenameCommand();
		good = new String[] {
				"rename table a to b;",
				"rENAme tABLE FOO TO BAR;",
				"  rename   table   abc   to   def   ;"
		};
		bad = new String[] {
				"rename tablea to b;",
				"rename table ato b;",
				"rename table a tob;",
				"rename table  to b;",
				"rename table a to ;"
		};
	}

}
