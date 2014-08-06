package storageTests;

import static org.junit.Assert.*;

import main.DatabaseException;

import org.junit.Test;

import storage.Database;

public class DatabaseTest {

	@Test
	public void test() throws DatabaseException {
		Database db = Database.getInstance();
		String[] target;
		
		try {
			db.addTable("FOO", "bar \t integer, bAAAAAAz date");
			db.addTable("abc", "def char(20)");
			db.addTable("a", "b varchar, c boolean");
			db.drop("abc");
			db.rename("foo", "New");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
		target = new String[] {
				 "a:\n"
			   + "field | type\n"
			   + "------|---------\n"
			   + "b     | VARCHAR\n"
			   + "c     | BOOLEAN\n"
			   + "\n",
			     "New:\n"
			   + "field    | type\n"
			   + "---------|---------\n"
			   + "bar      | INTEGER\n"
			   + "bAAAAAAz | DATE\n"
			   + "\n"
		};
		
		assertTrue(db.toString().equals(target[0]+target[1]) ||
				db.toString().equals(target[1]+target[0]));
		
		try {
			db.addTable("a", "abc real");
			fail();
		} catch (DatabaseException e) {}
		
		try {
			db.rename("a", "new");
			fail();
		} catch (DatabaseException e) {}
		
		try {
			db.rename("foo", "bar");
			fail();
		} catch (DatabaseException e) {}
		
		try {
			db.drop("abc");
			fail();
		} catch (DatabaseException e) {}
		
		assertTrue(db.toString().equals(target[0]+target[1]) ||
				db.toString().equals(target[1]+target[0]));
	}

}
