package persistence;

import java.io.IOException;
import main.DatabaseException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import storage.Database;
import storage.Table;

/**
 * This class is used to read data from an XML file and add its contents to the
 * database.
 */
public class XMLLoader extends DefaultHandler {
	private Database db;

	private boolean inField;
	private boolean inName;
	private boolean inType;
	private boolean inSize;

	private String fieldName;
	private String fieldType;
	private String fieldSize;
	private Table table;

	/**
	 * Parses an XML file, applying its contents to the database.
	 * 
	 * @param fileName
	 *            name of the file to be parsed
	 * @throws DatabaseException
	 *             if there is a problem parsing the file
	 */
	public void load(String fileName) throws DatabaseException {
		XMLReader reader;
		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(this);
			reader.parse(fileName);
		} catch (SAXException e) {
			throw new DatabaseException(
					"There was a problem with the XMLLoader.", e);
		} catch (IOException e) {
			throw new DatabaseException("There was a problem parsing '"
					+ fileName + "'.", e);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		try {
			db = Database.getInstance();
		} catch (DatabaseException e) {
			throw new SAXException(e.getMessage(), e);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equalsIgnoreCase("TABLE")) {
			table = new Table();
		} else if (localName.equalsIgnoreCase("FIELD")) {
			fieldName = null;
			fieldType = null;
			fieldSize = null;
			inField = true;
		} else if (localName.equalsIgnoreCase("NAME")) {
			inName = true;
		} else if (localName.equalsIgnoreCase("TYPE")) {
			inType = true;
		} else if (localName.equalsIgnoreCase("SIZE")) {
			inSize = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equalsIgnoreCase("TABLE")) {
			try {
				db.addTable(table);
			} catch (DatabaseException e) {
				throw new SAXException(e.getMessage(), e);
			}
		} else if (localName.equalsIgnoreCase("FIELD")) {
			try {
				table.addField(fieldName, fieldType, fieldSize, db.getDataDir());
			} catch (DatabaseException e) {
				throw new SAXException(e.getMessage(), e);
			}
			inField = false;
		} else if (localName.equalsIgnoreCase("NAME")) {
			inName = false;
		} else if (localName.equalsIgnoreCase("TYPE")) {
			inType = false;
		} else if (localName.equalsIgnoreCase("SIZE")) {
			inSize = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String text = new String(ch, start, length).trim();
		if (inName) {
			if (inField)
				fieldName = text;
			else {
				try {
					table.setName(text, db.getDataDir());
				} catch (DatabaseException e) {
					throw new SAXException(e.getMessage(), e);
				}
			}
		} else if (inType) {
			fieldType = text;
		} else if (inSize) {
			fieldSize = text;
		}
	}
}
