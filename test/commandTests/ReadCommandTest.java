package commandTests;

import org.junit.Before;

import commands.ReadCommand;

public class ReadCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new ReadCommand();
		good = new String[] {
				"read 'foo/bar.baz';",
				"rEAD 'aBc';",
				"  read    'file with  spaces'   ;"
		};
		bad = new String[] {
				"read'a';",
				"read b;",
				"read '';"
		};
	}

}
