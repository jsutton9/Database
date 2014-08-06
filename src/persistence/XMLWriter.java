package persistence;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import main.DatabaseException;
import storage.Database;

/**
 * This class is used to record data from the database to an XML file.
 */
public class XMLWriter {
	/**
	 * Writes an XML representation of the database to a file
	 * 
	 * @param fileName
	 *            name of the file to be written to
	 * @throws FileNotFoundException
	 *             if the named file cannot be accessed
	 * @throws DatabaseException
	 *             if the database is unable to create a directory to hold data
	 *             files
	 */
	public void saveDatabase(String fileName) throws FileNotFoundException,
			DatabaseException {
		PrintWriter out = new PrintWriter(fileName);
		out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ Database.getInstance().toXML());
		out.close();
	}
}
