package commandTests;

import org.junit.Before;

import commands.DefineTableCommand;

public class DefineTableCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new DefineTableCommand();
		good = new String[] {
				"dEFINe tABle aA haVIng fIelds (bb, cc, dd);",
				"  define   table   alpha   having   fields(  beta  )   ;"
		};
		bad = new String[] {
				"definetable foo having fields (bar);",
				"define table ana havingfields (kata);",
				"define tableFOO having fields (bar);",
				"define table a having fields b;"
		};
	}

}
