package persistenceTests;

import static org.junit.Assert.*;

import java.io.PrintWriter;

import main.DatabaseException;

import org.junit.Before;
import org.junit.Test;

import persistence.XMLLoader;
import storage.Database;

public class XMLLoaderTest {
	private String[] targets;

	@Before
	public void setUp() throws Exception {
		PrintWriter out = new PrintWriter("read-test.xml");
		out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<TABLES>\n"
				+ "\n"
				+ "<TABLE>\n"
				+ "  <NAME>\n"
				+ "    a\n"
				+ "  </NAME>\n"
				+ "  <FIELD>\n"
				+ "    <NAME>\n"
				+ "      b\n"
				+ "    </NAME>\n"
				+ "    <TYPE>\n"
				+ "      DATE\n"
				+ "    </TYPE>\n"
				+ "  </FIELD>\n"
				+ "</TABLE>\n"
				+ "\n"
				+ "<TABLE>\n"
				+ "  <NAME>\n"
				+ "    foo\n"
				+ "  </NAME>\n"
				+ "  <FIELD>\n"
				+ "    <NAME>\n"
				+ "      bar\n"
				+ "    </NAME>\n"
				+ "    <TYPE>\n"
				+ "      CHAR\n"
				+ "    </TYPE>\n"
				+ "    <SIZE>\n"
				+ "      100\n"
				+ "    </SIZE>\n"
				+ "  </FIELD>\n"
				+ "  <FIELD>\n"
				+ "    <NAME>\n"
				+ "      baz\n"
				+ "    </NAME>\n"
				+ "    <TYPE>\n"
				+ "      REAL\n"
				+ "    </TYPE>\n"
				+ "  </FIELD>\n"
				+ "</TABLE>\n"
				+ "\n"
				+ "</TABLES>");
		out.close();
		
		targets = new String[] {
				"a:\n"
			  + "field | type\n"
			  + "------|---------\n"
			  + "b     | DATE\n"
			  + "\n",
			  	"foo:\n"
			  + "field | type\n"
			  + "------|---------\n"
			  + "bar   | CHAR(100)\n"
			  + "baz   | REAL\n"
			  + "\n"
		};
	}

	@Test
	public void test() throws DatabaseException {
		XMLLoader loader = new XMLLoader();
		loader.load("read-test.xml");
		Database db = Database.getInstance();
		System.out.println(db.toString());
		assertTrue(db.toString().equals(targets[0]+targets[1]) || 
				db.toString().equals(targets[1]+targets[0]));
	}

}
