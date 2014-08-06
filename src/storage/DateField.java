package storage;

import java.io.IOException;
import java.io.RandomAccessFile;
import values.AbstractValue;
import values.DateValue;

import main.DatabaseException;

public class DateField extends AbstractField {
	/**
	 * Sets the name of the field to the first
	 * word in input and sets type to DATE.
	 * 
	 * @param input name of the field
	 */
	public DateField(String newName, int newRowPos, String newTableName) {
		name = newName;
		type = "DATE";
		rowPos = newRowPos;
		tableName = newTableName;
	}
	
	@Override
	public void insert(String input, RandomAccessFile dataRAF) 
			throws DatabaseException, IOException {
		(new DateValue(input)).insert(dataRAF);
	}

	@Override
	public String datumAsString(RandomAccessFile dataRAF) throws IOException {
		dataRAF.skipBytes(rowPos);
		return (new DateValue(dataRAF)).toString();
	}

	@Override
	public int getDatumSize() {
		return 8;
	}

	@Override
	public AbstractValue readValue(RandomAccessFile dataRAF) throws IOException {
		long fp = dataRAF.getFilePointer();
		dataRAF.skipBytes(rowPos - 1);
		DateValue value = new DateValue(dataRAF);
		dataRAF.seek(fp);
		return value;
	}

	@Override
	public AbstractValue parseValue(String valueString)
			throws DatabaseException {
		return new DateValue(valueString);
	}
}
