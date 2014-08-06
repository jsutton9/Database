package valueTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import values.VarcharValue;

public class VarcharValueTest {
	VarcharValue abc;
	VarcharValue abc2;
	VarcharValue abz;
	VarcharValue abcd;

	@Before
	public void setUp() throws Exception {
		abc = new VarcharValue("'abc'");
		abc2 = new VarcharValue("'abc'");
		abz = new VarcharValue("'abz'");
		abcd = new VarcharValue("'abcd'");
	}

	@Test
	public void test() {
		assertTrue(abc.compareTo(abc2) == 0);
		assertTrue(abc.compareTo(abz) < 0);
		assertTrue(abz.compareTo(abc) > 0);
		assertTrue(abc.compareTo(abcd) < 0);
		assertTrue(abcd.compareTo(abc) > 0);
	}

}
