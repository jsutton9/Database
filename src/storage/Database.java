package storage;

import java.io.File;
import java.util.HashMap;

import values.Dataset;

import main.DatabaseException;

/**
 * A container for all of the tables in a database.
 */
public class Database {
	private static Database db;
	private HashMap<String, Table> tables;
	private File dataDir;

	/**
	 * Private constructor prevents arbitrary external instantiation of
	 * Databases.
	 * @throws DatabaseException if a directory cannot be created to hold data files
	 */
	private Database() throws DatabaseException {
		tables = new HashMap<String, Table>();
		dataDir = new File("data");
		if (! dataDir.isDirectory())
			if (! dataDir.mkdir())
				throw new DatabaseException("Unable to create 'data' directory");
	}

	/**
	 * Returns the singleton instance of this class, instantiating it if
	 * necessary.
	 * 
	 * @return the lone instance of this class
	 * @throws DatabaseException if a directory cannot be created to hold data files
	 */
	public static Database getInstance() throws DatabaseException {
		if (db == null)
			db = new Database();
		return db;
	}

	/**
	 * Adds a table to the database with name name and fields described by
	 * fields.
	 * 
	 * @param name
	 *            the name of the new table
	 * @param fields
	 *            the names and types of the fields of the new table
	 * @throws DatabaseException
	 *             if fields contains an invalid field description or there is
	 *             already a table called name
	 */
	public void addTable(String name, String fields) throws DatabaseException {
		addTable(new Table(name, fields, dataDir));
	}

	/**
	 * Adds the given table to the database
	 * 
	 * @param table
	 *            table to be added
	 * @throws DatabaseException
	 *             if there is already a field by the same name
	 */
	public void addTable(Table table) throws DatabaseException {
		if (tables.containsKey(table.getName().toLowerCase()))
			throw new DatabaseException("There is already a table named '"
					+ table.getName() + "'.");
		tables.put(table.getName().toLowerCase(), table);
	}

	/**
	 * Changes the name of a table.
	 * 
	 * @param name
	 *            original name of the table
	 * @param newName
	 *            new name for the table
	 * @throws DatabaseException
	 *             if there is no table called name or there is already a table
	 *             called newName (case insensitive)
	 */
	public void rename(String name, String newName) throws DatabaseException {
		String nameLower = name.toLowerCase();
		String newNameLower = newName.toLowerCase();

		Table t = tables.get(nameLower);

		if (t == null)
			throw new DatabaseException("There is no table named '" + name
					+ "'.");

		Table prev = tables.put(newNameLower, t);

		if (prev != null) {
			tables.put(newNameLower, prev);
			throw new DatabaseException("There is already a table named '"
					+ newName + "'.");
		}

		t.setName(newName, dataDir);
		tables.remove(nameLower);
	}

	/**
	 * Removes the field with the given name from the database.
	 * 
	 * @param name
	 *            the name of the table to be removed
	 * @throws DatabaseException
	 *             if there is no table with the given name in the database
	 */
	public void drop(String name) throws DatabaseException {
		get(name).drop(dataDir);
		tables.remove(name);
	}

	/**
	 * Returns the names of the tables in the database with charts listing the
	 * names and types of the fields
	 * 
	 * @return a string describing the tables and their fields
	 */
	public String toString() {
		String out = "";
		for (Table t : tables.values())
			out += t.toString() + "\n";

		return out;
	}

	/**
	 * Returns an XML representation of the database describing its tables
	 * 
	 * @return an XML representation of the database
	 */
	public String toXML() {
		String out = "<TABLES>\n\n";
		for (Table table : tables.values())
			out += table.toXML() + "\n";
		out += "</TABLES>\n";
		return out;
	}

	/**
	 * Inserts a row described by values into the table named name
	 * 
	 * @param values
	 *            a string describing a list of values
	 * @param name
	 *            name of the table into which the values are to be inserted
	 * @throws DatabaseException
	 *             if there is no table named name or values does not match the
	 *             types of the fields of the table
	 */
	public void insert(String name, String values) throws DatabaseException {
		get(name).insert(values, dataDir);
	}

	/**
	 * Returns a string describing the contents of the specified table
	 * 
	 * @param name
	 *            name of the table
	 * @return the contents of the named field
	 * @throws DatabaseException
	 *             if there is no table with the specified name
	 */
	public String tableString(String name) throws DatabaseException {
		return get(name).contentsString(dataDir);
	}
	
	/**
	 * Gets the file object for the directory in which files for table
	 * data are to be stored
	 * 
	 * @return the data directory
	 */
	public File getDataDir() {
		return dataDir;
	}

	/**
	 * Sets the given field to the given value in rows of the given table
	 * in which the value of the field indicated by condFieldName meets
	 * the condition specified by relop and condValueString or all rows
	 * if conditional is false
	 * 
	 * @param tableName
	 *            name of the table to be updated
	 * @param fieldName
	 *            name of the field to be updated
	 * @param valueString
	 *            value to which the field is to be set
	 * @param isConditional
	 *            true iff the update is conditional
	 * @param condFieldName
	 *            name of the field being checked for a condition
	 * @param relop
	 *            relation operator - "=", "!=", "<", ">", "<=", or ">="
	 * @param condValueString
	 *            value against which the field is to be compared
	 * @throws DatabaseException 
	 *             if there is no table with the given name or the table
	 *             does not contain fields with the given names or there
	 *             is a problem accessing the table's data files
	 */
	public void update(String tableName, String fieldName, String valueString,
			boolean isConditional, String condFieldName, String relop, 
			String condValueString) throws DatabaseException {
		get(tableName).update(fieldName, valueString, isConditional, 
				condFieldName, relop, condValueString, dataDir);
	}

	/**
	 * Deletes all rows of the given table in which the value of the given field
	 * meets the condition specified by relop and value or all rows if conditional
	 * is false
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
	 * @throws DatabaseException
	 *             if there is no table named tableName, the table has no field named 
	 *             fieldName, value does not correctly describe a datum of the field's 
	 *             type, or there is a problem accessing a data file of the table
	 */
	public void delete(String tableName, boolean conditional, String fieldName,
			String relop, String value) throws DatabaseException {
		get(tableName).delete(conditional, fieldName, relop, value, dataDir);
	}
	
	/**
	 * Returns a string representation of the rows of the specified table 
	 * in which the given field has a value meeting the condition specified 
	 * by relop and valueString
	 * 
	 * @param tableName
	 *            name of the table being queried
	 * @param fieldName
	 *            name of the field to be checked for the condition
	 * @param relop
	 *            relation operator - "=", "!=", "<", ">", "<=", or ">="
	 * @param valueString
	 *            value against which the field is to be compared
	 * @return a table of rows meeting the condition
	 * @throws DatabaseException
	 *             if there is no table with the given name, the table
	 *             has no field with the given name, valueString is not a
	 *             valid datum of the field's type, or there is a problem
	 *             accessing the table's files
	 */
	public String select(String tableName, String fieldName, String relop,
			String value) throws DatabaseException {
		return get(tableName).getData(dataDir).select(fieldName, relop, value).toString();
	}
	
	public String project(String tableName, String fieldList) throws DatabaseException {
		return get(tableName).getData(dataDir).project(fieldList).toString();
	}
	
	public String join(String tableNameA, String tableNameB) throws DatabaseException {
		return get(tableNameA).getData(dataDir).join(get(tableNameB).getData(dataDir)).toString();
	}

	public String order(String tableName, String fieldName, boolean descending) throws DatabaseException {
		return get(tableName).getData(dataDir).order(fieldName, descending).toString();
	}

	public String union(String tableNameA, String tableNameB) throws DatabaseException {
		return get(tableNameA).getData(dataDir).union(get(tableNameB).getData(dataDir)).toString();
	}

	public String intersect(String tableNameA, String tableNameB) throws DatabaseException {
		return get(tableNameA).getData(dataDir).intersect(get(tableNameB).getData(dataDir)).toString();
	}

	public String minus(String tableNameA, String tableNameB) throws DatabaseException {
		return get(tableNameA).getData(dataDir).minus(get(tableNameB).getData(dataDir)).toString();
	}

	public Dataset getData(String tableName) throws DatabaseException {
		return get(tableName).getData(dataDir);
	}
	
	private Table get(String tableName) throws DatabaseException {
		tableName = tableName.toLowerCase();
		if (! tables.containsKey(tableName))
			throw new DatabaseException("There is no table named '" + tableName
					+ "'.");
		return tables.get(tableName);
	}
}
