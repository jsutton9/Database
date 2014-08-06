package commandTests;

import org.junit.Before;

import commands.JoinCommand;

public class JoinCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new JoinCommand();
		good = new String[] {
				"join a and b;",
				"jOIN (iNTERsect foo AND bar) and (UnIon x and y);",
				"  join   (minus  c  and  d)   and   efg   ;"
		};
		bad = new String[] {
				"joina andb;",
				"join fooand bar;",
				"join alpha and ;"
		};
	}

}
