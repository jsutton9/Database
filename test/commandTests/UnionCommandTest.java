package commandTests;

import org.junit.Before;

import commands.UnionCommand;

public class UnionCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new UnionCommand();
		good = new String[] {
				"union a and b;",
				"uNION (iNTERsect foo AND bar) and (JoIn x and y);",
				"  union   (join  c  and  d)   and   efg   ;"
		};
		bad = new String[] {
				"uniona andb;",
				"union fooand bar;",
				"union alpha and ;"
		};
	}

}
