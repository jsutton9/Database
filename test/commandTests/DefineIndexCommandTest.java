package commandTests;

import org.junit.Before;

import commands.DefineIndexCommand;

public class DefineIndexCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new DefineIndexCommand();
		good = new String[] {
				"define index on foo ( bar );",
				"dEFIne InDeX oN ONE (TWO);",
				"  define   index	on   jingo(  ba  ) ;"
		};
		bad = new String[] {
				"defineindex on hot (rats);",
				"define index on (a);",
				"define index on b ();",
				"define index onfoo (bar);"
		};
	}

}
