package valueTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import values.IntegerValue;

public class IntegerValueTest {
	IntegerValue one;
	IntegerValue one1;
	IntegerValue two;

	@Before
	public void setUp() throws Exception {
		one = new IntegerValue("1");
		one1 = new IntegerValue("1");
		two = new IntegerValue("2");
	}

	@Test
	public void test() {
		assertTrue(one.compareTo(one1) == 0);
		assertTrue(one.compareTo(two) < 0);
		assertTrue(two.compareTo(one) > 0);
	}

}
