package commandTests;

import org.junit.Before;

import commands.PrintCommand;

public class PrintCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new PrintCommand();
		good = new String[] {
				"print dictionary;",
				"pRINT BaR;",
				"  print   abcd   ;"
		};
		bad = new String[] {
				"printa;",
				"printdictionary;",
				"print ;"
		};
	}

}
