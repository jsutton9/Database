package storageTests;

import static org.junit.Assert.*;

import java.io.File;
import main.DatabaseException;

import org.junit.Before;
import org.junit.Test;

import storage.Table;

public class TableTest {
	String[][] goodTables;
	String[] dictionaryTargets;
	String[][] badTables;
	String[][] goodRows;
	String[] tableTargets;
	String[][] badRows;

	@Before
	public void setUp() throws Exception {
		goodTables = new String[][] {
				{"fooo", "abc char  (  1  )"},
				{"bar", "baaaaaaaaaaz char(10), real real"},
				{"a", "c boolean, b date, a varchar"}
		};
		
		dictionaryTargets = new String[] {
				"fooo:\n"+
				"field | type\n"+
				"------|---------\n"+
				"abc   | CHAR(1)\n",
				
				"bar:\n"+
				"field        | type\n"+
				"-------------|---------\n"+
				"baaaaaaaaaaz | CHAR(10)\n"+
				"real         | REAL\n",
				
				"a:\n"+
				"field | type\n"+
				"------|---------\n"+
				"c     | BOOLEAN\n"+
				"b     | DATE\n"+
				"a     | VARCHAR\n"
		};
		
		badTables = new String[][] {
				{"fooo", "bar baz"},
				{"abc", "def integer,  "},
				{"a", "b char()"},
				{"c", "oneword"},
				{"aaa", "bbb integer, bbb date"},
				{"one", "  "}
		};
		
		goodRows = new String[][] {
				{"'a'", "'a'"},
				{"'abcdefghij', 1.2", "'a$&%~ \t\n-_', 5"},
				{"tRuE, '01/10/1993', 'one two three'"}
		};
		
		tableTargets = new String[] {
				"abc\t| \n"+
				"---------\n"+
				"a\t| \n"+
				"a\t| \n"+
				"\n",
				
				"baaaaaaaaaaz\t| real\t| \n"+
				"-----------------\n"+
				"abcdefghij\t| 1.2\t| \n"+
				"a$&%~ \t\n-_\t| 5.0\t| \n"+
				"\n",
				
				"c\t| b\t| a\t| \n"+
				"-------------------------\n"+
				"TRUE\t| 01/10/1993\t| one two three\t| \n"+
				"\n"
		};
		
		badRows = new String[][] {
				{"abc", "'abc', 'def'"},
				{"'abcdefghijk', 1.2", "'abcdefghij', .", "'0123456789'"},
				{"false, '1/10/1993', 'abc'", "true, '01/01/2001', 'def', "} 
		};
	}

	@Test
	public void test() throws DatabaseException {
		Table[] tables = new Table[goodTables.length];
		File dataDir = new File("testData");
		if (! dataDir.isDirectory())
			if (! dataDir.mkdir())
				throw new DatabaseException("Unable to create 'testData' directory");
		
		String dataFileName;
		String varDataFileName;
		for (String[] tableStrings : goodTables) {
			dataFileName = tableStrings[0];
			varDataFileName = tableStrings[0] + ".var";
			(new File(dataDir, dataFileName)).delete();
			(new File(dataDir, varDataFileName)).delete();
		}
		
		try {
			for (int i=0; i<goodTables.length; i++) {
				tables[i] = new Table(goodTables[i][0], goodTables[i][1], dataDir);
				assertTrue(tables[i].toString().equals(dictionaryTargets[i]));
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
		for (String[] b : badTables) {
			try {
				new Table(b[0], b[1], dataDir);
			} catch (DatabaseException e) {
				continue;
			}
			
			fail("no exception on bad table definition");
		}
		
		for (int i=0; i<tables.length; i++) {
			try {
				for (String row : goodRows[i])
					tables[i].insert(row, dataDir);
			} catch (DatabaseException e) {
				fail(e.getMessage());
			}

			assertTrue(tables[i].contentsString(dataDir).equals(tableTargets[i]));
		}
		
		for (int i=0; i<tables.length; i++) {
			try {
				for (String row : badRows[i])
					tables[i].insert(row, dataDir);
			} catch (DatabaseException e) {
				continue;
			}
			
			fail("no exception on bad row");
		}
	}

}
