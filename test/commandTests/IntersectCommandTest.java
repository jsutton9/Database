package commandTests;

import org.junit.Before;

import commands.IntersectCommand;

public class IntersectCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new IntersectCommand();
		good = new String[] {
				"intersect a and b;",
				"iNTERSect (jOIN foo AND bar) and (UnIon x and y);",
				"  intersect   (minus  c  and  d)   and   efg   ;"
		};
		bad = new String[] {
				"intersecta andb;",
				"intersect fooand bar;",
				"intersect alpha and ;"
		};
	}

}
