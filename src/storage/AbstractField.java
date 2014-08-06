package storage;

import java.io.IOException;
import java.io.RandomAccessFile;

import main.DatabaseException;
import persistence.XMLEncoder;
import values.AbstractValue;

/**
 * An abstract class for classes representing specific types of fields.
 */
public abstract class AbstractField {
	protected String name;
	protected String type;
	protected int rowPos;
	protected String tableName;

	/**
	 * Gets the length of the name of the field.
	 * 
	 * @return the length of the name of the field
	 */
	public int getNameLen() {
		return name.length();
	}

	/**
	 * Checks if the field has the specified name.
	 * 
	 * @param n
	 *            string which may be the same as the field name
	 * @return True iff n is the same (case insensitively) as the field name or
	 *         tableName.fieldName
	 */
	public boolean isNamed(String n) {
		return n.equalsIgnoreCase(name)
				|| n.equalsIgnoreCase(tableName + "." + name);
	}

	/**
	 * Returns a string representation of the name and type of the field
	 * formatted for a table.
	 * 
	 * @param width
	 *            width of the field column of the table the output is to be
	 *            formatted for
	 * @return the name and type of the field
	 */
	public String toString(int width) {
		return String.format("%-" + Integer.toString(width) + "s| %s\n", name,
				type);
	}

	/**
	 * Returns an XML representation of the field describing its name and type
	 * 
	 * @return an XML representation of the field
	 */
	public String toXML() {
		return "  <FIELD>\n" 
				 + "      <NAME>\n" 
				 + "          " + XMLEncoder.encode(name) + "\n" 
				 + "      </NAME>\n"
				 + "      <TYPE>\n" 
				 + "          " + XMLEncoder.encode(type)+ "\n" 
				 + "      </TYPE>\n" 
				 + "  </FIELD>\n";
	}

	/**
	 * Writes the datum represented by input to the provided files
	 * 
	 * @param input
	 *            String which describes a datum of this field's type
	 * @param dataRAF
	 *            File to which to write fixed length data
	 * @throws DatabaseException
	 *             if input does not correctly describe a datum of the field's
	 *             type
	 * @throws IOException
	 *             if there is a problem writing to a file
	 */
	public abstract void insert(String input, RandomAccessFile dataRAF)
			throws DatabaseException, IOException;

	/**
	 * Gets the name of the field
	 * 
	 * @return the name of the field
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the name of the field in the form tableName.fieldName
	 * 
	 * @return the extended name of the field
	 */
	public String getExtendedName() {
		return tableName + "." + name;
	}

	/**
	 * Returns a string representation of the datum of this field in the row
	 * pointed at by dataRAF
	 * 
	 * @param dataRAF
	 *            file containing the fixed length data of a file
	 * @return the next item in the file
	 * @throws IOException
	 *             if there is a problem reading a file
	 */
	public abstract String datumAsString(RandomAccessFile dataRAF)
			throws IOException, DatabaseException;

	/**
	 * Returns the number of bytes a datum of the field's type takes up in a
	 * file
	 * 
	 * @return size of a datum in bytes
	 */
	public abstract int getDatumSize();

	/**
	 * Gets the type of the field
	 * 
	 * @return the type of the field
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns a value of this field's type read from the given files and sets
	 * the file pointer of dataRAF to its original value
	 * 
	 * @param dataRAF
	 *            file containing the fixed length data of the table with its
	 *            file pointer one byte into a row
	 * @return a value read from the files
	 * @throws IOException
	 *             if there is a problem reading a file
	 */
	public abstract AbstractValue readValue(RandomAccessFile dataRAF)
			throws IOException;

	/**
	 * Returns a value of this field's type represented by valueString
	 * 
	 * @param valueString
	 *            string representation of the value
	 * @return the value represented by valueString
	 * @throws DatabaseException
	 *             if valueString does not correctly describe a value of this
	 *             fields type
	 */
	public abstract AbstractValue parseValue(String valueString)
			throws DatabaseException;

	/**
	 * Sets the field to valueString in the row pointed to by dataRAF and sets
	 * its file pointer to its original value
	 * 
	 * @param valueString
	 *            value to which the field is to be set
	 * @param dataRAF
	 *            file containing the fixed length data of the table with its
	 *            file pointer one byte into a row
	 * @throws IOException
	 *             if there is a problem accessing a file
	 * @throws DatabaseException
	 *             if valueString does not correctly describe a datum of the
	 *             field's type
	 */
	public void update(String valueString, RandomAccessFile dataRAF)
			throws IOException, DatabaseException {
		long fp = dataRAF.getFilePointer();
		dataRAF.skipBytes(rowPos - 1);
		insert(valueString, dataRAF);
		dataRAF.seek(fp);
	}
}
