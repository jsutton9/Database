package valueTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import values.DateValue;

public class DateValueTest {
	DateValue today;
	DateValue today1;
	DateValue tomorrow;

	@Before
	public void setUp() throws Exception {
		today = new DateValue("'04/09/2014'");
		Thread.sleep(1000);
		today1 = new DateValue("'04/09/2014'");
		tomorrow = new DateValue("'04/10/2014'");
	}

	@Test
	public void test() {
		assertTrue(today.compareTo(today1) == 0);
		assertTrue(today.compareTo(tomorrow) < 0);
		assertTrue(tomorrow.compareTo(today) > 0);
	}

}
