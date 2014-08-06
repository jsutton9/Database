package commandTests;

import org.junit.Before;

import commands.UpdateCommand;

public class UpdateCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new UpdateCommand();
		good = new String[] {
				"update a set b = 5.5 where c <= 10;",
				"uPDATe fOO set Bar = 'bAz';",
				"  update   abc   set   def=true   where  ghi>2 ;"
		};
		bad = new String[] {
				"update   set a = 0;",
				"updateb set c = 1;",
				"update xset y = 2;",
				"update one settwo = 345;"
		};
	}

}
