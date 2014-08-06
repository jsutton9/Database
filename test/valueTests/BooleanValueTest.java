package valueTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import values.BooleanValue;

public class BooleanValueTest {
	BooleanValue t;
	BooleanValue t1;
	BooleanValue f;
	BooleanValue f1;

	@Before
	public void setUp() throws Exception {
		t = new BooleanValue("true");
		t1 = new BooleanValue("true");
		f = new BooleanValue("false");
		f1 = new BooleanValue("false");
	}

	@Test
	public void test() {
		assertTrue(t.compareTo(t1) == 0);
		assertTrue(t.compareTo(f) > 0);
		assertTrue(f.compareTo(t) < 0);
		assertTrue(f.compareTo(f1) == 0);
	}

}
