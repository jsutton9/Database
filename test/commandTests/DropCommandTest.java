package commandTests;

import org.junit.Before;

import commands.DropCommand;

public class DropCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new DropCommand();
		good = new String[] {
				"drop table a;",
				"dROP TaBlE B;",
				"  drop   table   foo   ;"
		};
		bad = new String[] {
				"droptable a;",
				"drop tableb;",
				"drop table ;"
		};
	}

}
