package commandTests;

import org.junit.Before;

import commands.InsertCommand;

public class InsertCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new InsertCommand();
		good = new String[] {
				"insert (INTEGER, CHAR(42)) into foo;",
				"InSeRt (DATE) iNTO BAR;",
				"  insert(  alpha  ,  beta  )into   gamma   ;"
		};
		bad = new String[] {
				"insert () into abc;",
				"insert (varchar, boolean) intoTEST;",
				"insert (integer) into ;"
		};
	}

}
