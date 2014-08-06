package storage;

import java.io.IOException;
import java.io.RandomAccessFile;

import values.AbstractValue;
import values.RealValue;
import main.DatabaseException;

public class RealField extends AbstractField {
	/**
	 * Sets the name of the field to the first
	 * word in input and sets type to REAL.
	 * 
	 * @param input name of the field
	 */
	public RealField(String newName, int newRowPos, String newTableName) {
		name = newName;
		type = "REAL";
		rowPos = newRowPos;
		tableName = newTableName;
	}
	
	@Override
	public void insert(String input, RandomAccessFile dataRAF) 
			throws DatabaseException, IOException {
		(new RealValue(input)).insert(dataRAF);
	}
	
	@Override
	public String datumAsString(RandomAccessFile dataRAF) throws IOException {
		dataRAF.skipBytes(rowPos);
		return (new RealValue(dataRAF)).toString();
	}

	@Override
	public int getDatumSize() {
		return 4;
	}

	@Override
	public AbstractValue readValue(RandomAccessFile dataRAF) throws IOException {
		long fp = dataRAF.getFilePointer();
		dataRAF.skipBytes(rowPos - 1);
		RealValue value = new RealValue(dataRAF);
		dataRAF.seek(fp);
		return value;
	}

	@Override
	public AbstractValue parseValue(String valueString)
			throws DatabaseException {
		return new RealValue(valueString);
	}
}
