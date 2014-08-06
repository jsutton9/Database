package commandTests;

import org.junit.Before;

import commands.DeleteCommand;

public class DeleteCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new DeleteCommand();
		good = new String[] {
				"delete foo where a=b;",
				"dELETe bar;",
				"  delete   alpha     where    bravo  <  charlie   ;"
		};
		bad = new String[] {
				"delete ;",
				"deletetest;",
				"delete testwhere abc;",
				"delete a whereb;"
		};
	}

}
