package storage;

import java.io.IOException;
import java.io.RandomAccessFile;

import values.AbstractValue;
import values.BooleanValue;
import main.DatabaseException;

public class BooleanField extends AbstractField {
	/**
	 * Sets the name of the field to the first
	 * word in input and sets type to BOOLEAN.
	 * 
	 * @param input name of the field
	 */
	public BooleanField(String newName, int newRowPos, String newTableName) {
		name = newName;
		type = "BOOLEAN";
		rowPos = newRowPos;
		tableName = newTableName;
	}

	@Override
	public void insert(String input, RandomAccessFile dataRAF) 
			throws DatabaseException, IOException {
		(new BooleanValue(input)).insert(dataRAF);
	}

	@Override
	public String datumAsString(RandomAccessFile dataRAF) throws IOException {
		dataRAF.skipBytes(rowPos);
		return (new BooleanValue(dataRAF)).toString();
	}

	@Override
	public int getDatumSize() {
		return 1;
	}

	@Override
	public AbstractValue readValue(RandomAccessFile dataRAF) throws IOException {
		long fp = dataRAF.getFilePointer();
		dataRAF.skipBytes(rowPos - 1);
		BooleanValue value = new BooleanValue(dataRAF);
		dataRAF.seek(fp);
		return value;
	}

	@Override
	public AbstractValue parseValue(String valueString) throws DatabaseException {
		return new BooleanValue(valueString);
	}
}
