package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persistence.XMLEncoder;
import values.AbstractValue;
import values.Dataset;
import values.Row;
import main.DatabaseException;

/**
 * A class representing a database table.
 */
public class Table {
	private ArrayList<AbstractField> fields = new ArrayList<AbstractField>();
	private String name;
	private int rowSize;

	/**
	 * Returns a table with the name tableName and the fields described by
	 * fieldString and creates data files if necessary
	 * 
	 * @param tableName
	 *            the name to be given to the table
	 * @param fields
	 *            a string listing the names and types of the fields the table
	 *            will have
	 * @param newDataDir
	 *            the directory in which the table's data files are to be stored
	 * @throws DatabaseException
	 *             if fieldString contains an invalid field description or there
	 *             is a problem accessing the data files
	 */
	public Table(String tableName, String fieldString, File dataDir)
			throws DatabaseException {
		name = tableName;
		rowSize = 1;
		openFiles(dataDir);

		String fieldList[] = fieldString.split(",");
		for (String field : fieldList)
			addField(field.trim(), dataDir);
	}

	public Table() {
		rowSize = 1;
	}

	/**
	 * Opens the files which are used to store the table's data, creating them
	 * if necessary
	 * 
	 * @param dataDir
	 *            the directory in which the table's data files are to be stored
	 * @throws DatabaseException
	 *             if there is a problem accessing a file
	 */
	private void openFiles(File dataDir) throws DatabaseException {
		try {
			File dataFile = new File(dataDir, name);
			if (!dataFile.exists())
				dataFile.createNewFile();
		} catch (FileNotFoundException e) {
			throw new DatabaseException("Unable to access file '"
					+ name + "'", e);
		} catch (IOException e) {
			throw new DatabaseException("Unable to create file '"
					+ name + "'", e);
		}

		try {
			File varDataFile = new File(dataDir, name+".var");
			if (!varDataFile.exists())
				varDataFile.createNewFile();
		} catch (FileNotFoundException e) {
			throw new DatabaseException("Unable to access file '"
					+ name + ".var'", e);
		} catch (IOException e) {
			throw new DatabaseException("Unable to create file '"
					+ name + ".var'", e);
		}
	}

	/**
	 * Adds a new field, as described by input, to the table
	 * 
	 * @param input
	 *            the name and type of the field
	 * @param dataDir
	 *            the directory in which the table's data files are to be stored
	 * @throws DatabaseException
	 *             if input is not a valid field description or a field name is
	 *             duplicated
	 */
	public void addField(String input, File dataDir) throws DatabaseException {
		Matcher matcher = Pattern.compile(
				"\\s*(\\S+)\\s+([a-zA-Z]+)(\\s*\\(\\s*([0-9]+)\\s*\\))?\\s*",
				Pattern.CASE_INSENSITIVE).matcher(input);
		if (!matcher.matches())
			throw new DatabaseException("'" + input
					+ "' not a valid field description.");

		String name = matcher.group(1);
		String type = matcher.group(2).toLowerCase().trim();
		String size = null;
		if (matcher.groupCount() > 2)
			size = matcher.group(4);
		addField(name, type, size, dataDir);
	}

	/**
	 * Adds a new field, with the specified name, type, and (optionally) size to
	 * the table.
	 * 
	 * @param fieldName
	 *            name of the field
	 * @param type
	 *            type of the field
	 * @param size
	 *            size of the field if applicable
	 * @param dataDir
	 *            the directory in which the table's data files are to be stored
	 * @throws DatabaseException
	 *             if type is not a valid type description, a field name is
	 *             duplicated, or the type is char and no size is given
	 */
	public void addField(String fieldName, String type, String size, File dataDir)
			throws DatabaseException {
		if (type.equalsIgnoreCase("char") && size == null)
			throw new DatabaseException(
					"Cannot create char field without a size.");
		for (AbstractField f : fields)
			if (f.isNamed(fieldName))
				throw new DatabaseException("There is already a field named '"
						+ fieldName + "'.");

		AbstractField f;
		try {
			if (type.equalsIgnoreCase("boolean"))
				f = new BooleanField(fieldName, rowSize, name);
			else if (type.equalsIgnoreCase("char"))
				f = new CharField(fieldName, size, rowSize, name);
			else if (type.equalsIgnoreCase("date"))
				f = new DateField(fieldName, rowSize, name);
			else if (type.equalsIgnoreCase("integer"))
				f = new IntegerField(fieldName, rowSize, name);
			else if (type.equalsIgnoreCase("real"))
				f = new RealField(fieldName, rowSize, name);
			else if (type.equalsIgnoreCase("varchar"))
				f = new VarcharField(fieldName, rowSize, 
						new RandomAccessFile(new File(dataDir, name+".var"), "rw"), name);
			else
				throw new DatabaseException("'" + type
						+ "' not a valid type description.");
		} catch (FileNotFoundException e) {
			throw new DatabaseException("Unable to access file '"
					+ name + ".var'", e);
		}
		fields.add(f);
		rowSize += f.getDatumSize();
	}

	/**
	 * Sets the name of the table to newName and creates data files if necessary
	 * 
	 * @param newName
	 *            the new name for the table
	 * @param dataDir
	 *            the directory in which the table's data files are to be stored
	 * @throws DatabaseException
	 *             if there is a problem accessing the data files
	 */
	public void setName(String newName, File dataDir) throws DatabaseException {
		name = newName;
		openFiles(dataDir);
	}

	/**
	 * Gets the name of the table
	 * 
	 * @return the name of the table
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name of the table followed by a table with the names and
	 * types of all its fields
	 * 
	 * @return a string containing a description of the table and its fields
	 */
	public String toString() {
		String out = name + ":\n";

		int width = 6;
		for (AbstractField field : fields)
			if (field.getNameLen() + 1 > width)
				width = field.getNameLen() + 1;

		out += String.format("%-" + Integer.toString(width) + "s| type\n",
				"field");
		for (int i = 0; i < width; i++)
			out += "-";
		out += "|---------\n";

		for (AbstractField field : fields)
			out += field.toString(width);

		return out;
	}

	/**
	 * Returns an XML representation of the table describing its name and
	 * fields.
	 * 
	 * @return an XML representation of the table
	 */
	public String toXML() {
		String out = "<TABLE>\n" 
				   + "  <NAME>\n" 
				   + "      " + XMLEncoder.encode(name) + "\n" 
				   + "  </NAME>\n";
		for (AbstractField field : fields)
			out += field.toXML();
		out += "</TABLE>\n";
		return out;
	}

	/**
	 * Returns a string representation of all of the data contained by the table
	 * 
	 * @param dataDir
	 *            directory in which are stored the table's data files
	 * @return the contents of the table
	 * @throws DatabaseException
	 *             if there is a problem accessing the data files
	 */
	public String contentsString(File dataDir) throws DatabaseException {
		String out = "";
		for (AbstractField f : fields)
			out += f.getName() + "\t| ";
		out += "\n-";
		for (int i = 0; i < fields.size(); i++)
			out += "--------";
		out += "\n";

		try {
			RandomAccessFile dataRAF = new RandomAccessFile(new File(dataDir,
					name), "rw");
			for (int i = 0; i < dataRAF.length(); i += rowSize) {
				dataRAF.seek(i);
				if (dataRAF.readBoolean()) {
					for (AbstractField f : fields) {
						dataRAF.seek(i);
						out += f.datumAsString(dataRAF) + "\t| ";
					}
					out += "\n";
				}
			}
		} catch (IOException e) {
			throw new DatabaseException("Unable to access files '" + name
					+ "' and/or '" + name + ".var'", e);
		}

		out += "\n";
		return out;
	}

	/**
	 * Inserts a row described by values into the table
	 * 
	 * @param values
	 *            a string describing the values to be inserted
	 * @param dataDir
	 *            directory in which are stored the table's data files
	 * @throws DatabaseException
	 *             if values contains the wrong number of elements or their
	 *             types are incorrect
	 */
	public void insert(String values, File dataDir) throws DatabaseException {
		String[] valueList = values.split(",");
		if (valueList.length != fields.size())
			throw new DatabaseException(
					"The value list contains the wrong number of values.");

		try {
			RandomAccessFile dataRAF = new RandomAccessFile(new File(dataDir,
					name), "rw");
			long dataLen = dataRAF.length();
			dataRAF.seek(dataLen);
			dataRAF.writeBoolean(true);
			try {
				for (int i = 0; i < fields.size(); i++)
					fields.get(i).insert(valueList[i].trim(), dataRAF);
			} catch (DatabaseException e) {
				dataRAF.setLength(dataLen);
				throw e;
			}
		} catch (IOException e) {
			throw new DatabaseException("Unable to access files '" + name
					+ "' and/or '" + name + ".var'", e);
		}
	}

	/**
	 * Deletes the data files used by this table
	 * 
	 * @param dataDir
	 *            directory in which are stored the table's data files
	 * @throws DatabaseException
	 *             if a file cannot be deleted
	 */
	public void drop(File dataDir) throws DatabaseException {
		if (!(new File(dataDir, name).delete()))
			throw new DatabaseException("Unable to delete file '"
					+ name + "'");
		if (!(new File(dataDir, name+".var").delete()))
			throw new DatabaseException("Unable to delete file '"
					+ name + ".var'");
	}

	/**
	 * Sets the given field to the given value in rows in which the value of the
	 * field indicated by condFieldName meets the condition specified by relop
	 * and condValueString or all rows if conditional is false
	 * 
	 * @param fieldName
	 *            name of the field to be updated
	 * @param valueString
	 *            value to which the field is to be set
	 * @param conditional
	 *            true iff the update is conditional
	 * @param condFieldName
	 *            name of the field being checked for a condition
	 * @param relop
	 *            relation operator - "=", "!=", "<", ">", "<=", or ">="
	 * @param condValueString
	 *            value against which the field is to be compared
	 * @param dataDir
	 *            directory in which are stored the table's data files
	 * @throws DatabaseException
	 *             if there are no fields with the given names, valueString does
	 *             not correctly describe a value of the field's type, or there
	 *             is a problem accessing a data file
	 */
	public void update(String fieldName, String valueString,
			boolean conditional, String condFieldName, String relop,
			String condValueString, File dataDir) throws DatabaseException {
		AbstractField field = null;
		AbstractField condField = null;
		for (AbstractField f : fields) {
			if (f.isNamed(fieldName))
				field = f;
			if (conditional && f.isNamed(condFieldName))
				condField = f;
		}
		if (field == null)
			throw new DatabaseException("'" + name + "' has no field called '"
					+ fieldName + "'.");
		if (conditional && condField == null)
			throw new DatabaseException("'" + name + "' has no field called '"
					+ condFieldName + "'.");

		AbstractValue condValue = null;
		if (conditional)
			condValue = condField.parseValue(condValueString);

		try {
			RandomAccessFile dataRAF = new RandomAccessFile(new File(dataDir,
					name), "rw");
			for (int i = 0; i < dataRAF.length(); i += rowSize) {
				dataRAF.seek(i);
				if (dataRAF.readBoolean()
						&& (!conditional || compare(condField, relop, 
								condValue, dataRAF)))
					field.update(valueString, dataRAF);
			}
		} catch (IOException e) {
			throw new DatabaseException("Unable to access files '" + name
					+ "' and/or '" + name + ".var'", e);
		}
	}

	/**
	 * Deletes all rows in which the value of the given field meets the
	 * condition specified by relop and value or all rows if conditional is
	 * false
	 * 
	 * @param tableName
	 *            name of the table from which rows are to be deleted
	 * @param conditional
	 *            true iff deletion is conditional
	 * @param fieldName
	 *            name of the field being checked for a condition
	 * @param relop
	 *            relation operator - "=", "!=", "<", ">", "<=", or ">="
	 * @param value
	 *            value against which the field is to be compared
	 * @param dataDir
	 *            directory in which are stored the table's data files
	 * @throws DatabaseException
	 *             if there is no field named fieldName, value does not
	 *             correctly describe a datum of the field's type, or there is a
	 *             problem accessing a data file
	 */
	public void delete(boolean conditional, String fieldName, String relop,
			String valueString, File dataDir) throws DatabaseException {
		AbstractField condField = null;
		AbstractValue condValue = null;
		if (conditional) {
			for (AbstractField f : fields) {
				if (f.isNamed(fieldName)) {
					condField = f;
					break;
				}
			}
			if (condField == null)
				throw new DatabaseException("'" + name
						+ "' has no field called '" + fieldName + "'.");

			condValue = condField.parseValue(valueString);
		}

		try {
			RandomAccessFile dataRAF = new RandomAccessFile(new File(dataDir,
					name), "rw");
			for (int i = 0; i < dataRAF.length(); i += rowSize) {
				dataRAF.seek(i);
				if (dataRAF.readBoolean()
						&& (!conditional || compare(condField, relop,
								condValue, dataRAF))) {
					dataRAF.seek(i);
					dataRAF.writeBoolean(false);
				}
			}
		} catch (IOException e) {
			throw new DatabaseException("Unable to access files '" + name
					+ "' and/or '" + name + ".var'", e);
		}
	}

	private boolean compare(AbstractField field, String relop,
			AbstractValue value, RandomAccessFile dataRAF) throws IOException {
		int comp = field.readValue(dataRAF).compareTo(value);
		return relop.equals("=") && comp == 0 
				|| relop.equals("!=") && comp != 0 
				|| relop.equals("<") && comp < 0
				|| relop.equals(">") && comp > 0 
				|| relop.equals("<=") && comp <= 0 
				|| relop.equals(">=") && comp >= 0;
	}
	
	public Dataset getData(File dataDir) throws DatabaseException {
		Dataset dataset = new Dataset(new ArrayList<AbstractField>(fields), true);
		
		try {
			RandomAccessFile dataRAF = new RandomAccessFile(new File(dataDir,
					name), "rw");
			for (int i=0; i<dataRAF.length(); i += rowSize) {
				dataRAF.seek(i);
				if (dataRAF.readBoolean()) {
					AbstractValue[] values = new AbstractValue[fields.size()];
					for (int j=0; j<fields.size(); j++)
						values[j] = fields.get(j).readValue(dataRAF);
					dataset.add(new Row(values));
				}
			}
		} catch (IOException e) {
			throw new DatabaseException("Unable to access files '" + name
					+ "' and/or '" + name + ".var'", e);
		}
		
		return dataset;
	}
}