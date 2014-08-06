package storage;

import java.io.IOException;
import java.io.RandomAccessFile;

import values.AbstractValue;
import values.VarcharValue;
import main.DatabaseException;

public class VarcharField extends AbstractField {
	RandomAccessFile varDataRAF;
	
	/**
	 * Sets the name of the field to the first
	 * word in input and sets type to VARCHAR.
	 * 
	 * @param input name of the field
	 */
	public VarcharField(String newName, int newRowPos, RandomAccessFile newVarDataRAF, String newTableName) {
		name = newName;
		type = "VARCHAR";
		rowPos = newRowPos;
		varDataRAF = newVarDataRAF;
		tableName = newTableName;
	}
	
	@Override
	public void insert(String input, RandomAccessFile dataRAF) throws DatabaseException, IOException {
		(new VarcharValue(input)).insert(dataRAF, varDataRAF);
	}

	@Override
	public String datumAsString(RandomAccessFile dataRAF) throws IOException {
		dataRAF.skipBytes(rowPos);
		return (new VarcharValue(dataRAF, varDataRAF)).toString();
	}

	@Override
	public int getDatumSize() {
		return 8;
	}

	@Override
	public AbstractValue readValue(RandomAccessFile dataRAF) throws IOException {
		long fp = dataRAF.getFilePointer();
		dataRAF.skipBytes(rowPos - 1);
		VarcharValue value = new VarcharValue(dataRAF, varDataRAF);
		dataRAF.seek(fp);
		return value;
	}

	@Override
	public AbstractValue parseValue(String valueString)
			throws DatabaseException {
		return new VarcharValue(valueString);
	}
}
