package commandTests;

import org.junit.Before;

import commands.OrderCommand;

public class OrderCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new OrderCommand();
		good = new String[] {
				"order a by b descending;",
				"oRDEr (iNTERsect foo AND bar) by x;",
				"  order   (minus  c  and  d)   by   efg   ;"
		};
		bad = new String[] {
				"ordera byb;",
				"order fooby bar;",
				"order x by ydescending",
				"order alpha by ;"
		};
	}

}
