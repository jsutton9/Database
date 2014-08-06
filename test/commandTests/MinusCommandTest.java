package commandTests;

import org.junit.Before;

import commands.MinusCommand;

public class MinusCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new MinusCommand();
		good = new String[] {
				"minus a and b;",
				"mINUS (iNTERsect foo AND bar) and (UnIon x and y);",
				"  minus   (join  c  and  d)   and   efg   ;"
		};
		bad = new String[] {
				"minusa andb;",
				"minus fooand bar;",
				"minus alpha and ;"
		};
	}

}
