package valueTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import values.RealValue;

public class RealValueTest {
	RealValue pointFive;
	RealValue pointFive1;
	RealValue onePointFive;

	@Before
	public void setUp() throws Exception {
		pointFive = new RealValue(".5");
		pointFive1 = new RealValue(".5");
		onePointFive = new RealValue("1.5");
	}

	@Test
	public void test() {
		assertTrue(pointFive.compareTo(pointFive1) == 0);
		assertTrue(pointFive.compareTo(onePointFive) < 0);
		assertTrue(onePointFive.compareTo(pointFive) > 0);
	}

}
