package persistenceTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import persistence.XMLWriter;

import storage.Database;

public class XMLWriterTest {

	@Before
	public void setUp() throws Exception {
		Database db = Database.getInstance();
		db.addTable("foo", "bar char(100), baz real");
		db.addTable("a", "b date");
		
		XMLWriter writer = new XMLWriter();
		writer.saveDatabase("write-test.xml");
	}

	@Test
	public void test() throws FileNotFoundException {
		String prefix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					  + "<TABLES>\n"
					  + "\n";
		String suffix = "</TABLES>\n";
		String[] tables = {
				  "<TABLE>\n"
				+ "  <NAME>\n"
				+ "      foo\n"
				+ "  </NAME>\n"
				+ "  <FIELD>\n"
				+ "      <NAME>\n"
				+ "          bar\n"
				+ "      </NAME>\n"
				+ "      <TYPE>\n"
				+ "          CHAR\n"
				+ "      </TYPE>\n"
				+ "      <SIZE>\n"
				+ "          100\n"
				+ "      </SIZE>\n"
				+ "  </FIELD>\n"
				+ "  <FIELD>\n"
				+ "      <NAME>\n"
				+ "          baz\n"
				+ "      </NAME>\n"
				+ "      <TYPE>\n"
				+ "          REAL\n"
				+ "      </TYPE>\n"
				+ "  </FIELD>\n"
				+ "</TABLE>\n"
				+ "\n",
				  "<TABLE>\n"
				+ "  <NAME>\n"
				+ "      a\n"
				+ "  </NAME>\n"
				+ "  <FIELD>\n"
				+ "      <NAME>\n"
				+ "          b\n"
				+ "      </NAME>\n"
				+ "      <TYPE>\n"
				+ "          DATE\n"
				+ "      </TYPE>\n"
				+ "  </FIELD>\n"
				+ "</TABLE>\n"
				+ "\n"
		};

		Scanner[] targets = {
				new Scanner(prefix+tables[0]+tables[1]+suffix),
				new Scanner(prefix+tables[1]+tables[0]+suffix)
		};
		
		outer:
		for (Scanner target : targets) {
			Scanner xml = new Scanner(new File("write-test.xml"));
			while (xml.hasNextLine() && target.hasNextLine()) {
				String xmlLine = xml.nextLine();
				String targetLine = target.nextLine();
				System.out.println(xmlLine);
				System.out.println(targetLine);
				if (! xmlLine.equals(targetLine)) {
					xml.close();
					target.close();
					continue outer;
				}
			}
			xml.close();
			target.close();
			return;
		}
		
		fail();
	}

}
