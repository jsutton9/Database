package values;

import java.util.ArrayList;
import java.util.Collections;
import storage.AbstractField;
import main.DatabaseException;

public class Dataset {
	private ArrayList<Row> rows;
	private ArrayList<AbstractField> fields;
	private boolean singleTable;
	
	public Dataset(ArrayList<AbstractField> newFields, boolean fromSingleTable) {
		rows = new ArrayList<Row>();
		fields = newFields;
		singleTable = fromSingleTable;
	}
	
	public void add(Row row) {
		rows.add(row);
	}
	
	public Dataset select(String fieldName, String relop, String valueString) 
			throws DatabaseException {
		Dataset dataset = new Dataset(fields, singleTable);
		int index = fieldIndex(fieldName);
		AbstractValue value = fields.get(index).parseValue(valueString);
		
		for (int i=0; i<rows.size(); i++) {
			int comp = rows.get(i).get(index).compareTo(value);
			if (relop.equals("=") && comp == 0 
					|| relop.equals("!=") && comp != 0 
					|| relop.equals("<") && comp < 0
					|| relop.equals(">") && comp > 0 
					|| relop.equals("<=") && comp <= 0 
					|| relop.equals(">=") && comp >= 0)
				dataset.add(rows.get(i));
		}
		
		return dataset;
	}
	
	public Dataset project(String targetFields) throws DatabaseException {
		String[] targetFieldNames = targetFields.split(",");
		int[] fieldIndices = new int[targetFieldNames.length];
		for (int i=0; i<targetFieldNames.length; i++)
			fieldIndices[i] = fieldIndex(targetFieldNames[i].trim());
		
		ArrayList<AbstractField> newFields = new ArrayList<AbstractField>();
		for (int index : fieldIndices)
			newFields.add(fields.get(index));
		Dataset dataset = new Dataset(newFields, singleTable);
		
		for (Row row : rows)
			dataset.add(row.project(fieldIndices));
		return dataset;
	}
	
	public Dataset join(Dataset other) {
		ArrayList<AbstractField> newFields = new ArrayList<AbstractField>(fields);
		newFields.addAll(other.fields);
		Dataset dataset = new Dataset(newFields, false);
		
		for (Row row : rows)
			for (Row otherRow : other.rows)
				dataset.add(row.join(otherRow));
		
		return dataset;
	}
	
	public Dataset order(String fieldName, boolean descending) throws DatabaseException {
		Dataset dataset = new Dataset(fields, singleTable);
		ArrayList<Row> newRows = new ArrayList<Row>(rows);
		Collections.sort(newRows, new RowComparator(fieldIndex(fieldName), descending));
		for (Row row : newRows)
			dataset.add(row);
		return dataset;
	}
	
	public Dataset union(Dataset other) throws DatabaseException {
		assertCompatible(other);
		
		Dataset dataset = new Dataset(fields, singleTable);
		for (Row row : rows)
			dataset.add(row);
		OUTER:
		for (Row otherRow : other.rows) {
			for (Row row : rows)
				if (row.equals(otherRow))
					continue OUTER;
			dataset.add(otherRow);
		}
		
		return dataset;
	}
	
	public Dataset intersect(Dataset other) throws DatabaseException {
		assertCompatible(other);
		
		Dataset dataset = new Dataset(fields, singleTable);
		for (Row row : rows) {
			for (Row otherRow : other.rows) {
				if (row.equals(otherRow)) {
					dataset.add(row);
					break;
				}
			}
		}
		
		return dataset;
	}
	
	public Dataset minus(Dataset other) throws DatabaseException {
		assertCompatible(other);
		
		Dataset dataset = new Dataset(fields, singleTable);
		OUTER:
		for (Row row : rows) {
			for (Row otherRow : other.rows)
				if (row.equals(otherRow))
					continue OUTER;
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public String toString() {
		String out = "";
		for (AbstractField f : fields) {
			if (singleTable)
				out += f.getName()+"\t| ";
			else
				out += f.getExtendedName()+"\t| ";
		}
		out += "\n";
		for (int i = 0; i < fields.size(); i++)
			out += "--------";
		out += "\n";
		
		for (Row row : rows)
			out += row.toString();
		return out;
	}
	
	private int fieldIndex(String fieldName) throws DatabaseException {
		for (int i=0; i<fields.size(); i++)
			if (fields.get(i).isNamed(fieldName))
				return i;
		throw new DatabaseException("There is no field named '"+fieldName+"'.");
	}
	
	private void assertCompatible(Dataset other) throws DatabaseException {
		if (fields.size() != other.fields.size())
			throw new DatabaseException("These datasets are not union compatible.");
		for (int i=0; i<fields.size(); i++)
			if (! fields.get(i).getType().equals(other.fields.get(i).getType()))
				throw new DatabaseException("These datasets are not union compatible.");
	}
}
