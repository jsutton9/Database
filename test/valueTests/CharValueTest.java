package valueTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import values.CharValue;

public class CharValueTest {
	CharValue abc;
	CharValue abc1;
	CharValue azc;

	@Before
	public void setUp() throws Exception {
		abc = new CharValue("'abc'", 3);
		abc1 = new CharValue("'abc'", 3);
		azc = new CharValue("'azc'", 3);
	}

	@Test
	public void test() {
		assertTrue(abc.compareTo(abc1) == 0);
		assertTrue(abc.compareTo(azc) < 0);
		assertTrue(azc.compareTo(abc) > 0);
	}

}
