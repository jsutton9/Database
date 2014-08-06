package commandTests;

import org.junit.Before;

import commands.SelectCommand;

public class SelectCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new SelectCommand();
		good = new String[] {
				"sELECt FOO;",
				"  select   bar   where   baz='01/10/1993'   ;"
		};
		bad = new String[] {
				"selectabc;",
				"select ;"
		};
	}

}
