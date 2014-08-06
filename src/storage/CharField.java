package storage;

import java.io.IOException;
import java.io.RandomAccessFile;
import persistence.XMLEncoder;
import values.AbstractValue;
import values.CharValue;
import main.DatabaseException;

public class CharField extends AbstractField {
	private int size;

	/**
	 * Sets the name of the field to the first
	 * word in input and sets type to CHAR of
	 * a size specified in input.
	 * 
	 * @param input string with which the field
	 *              was specified
	 */
	public CharField(String newName, String newSize, int newRowPos, String newTableName) 
			throws DatabaseException {
		name = newName;
		size = Integer.parseInt(newSize);
		type = "CHAR";
		rowPos = newRowPos;
		tableName = newTableName;
	}
	
	@Override
	public String toString(int width) {
		return String.format("%-"+Integer.toString(width)+"s| %s(%d)\n", 
				name, type, size);
	}
	
	@Override
	public String toXML() {
		return "  <FIELD>\n"
			 + "      <NAME>\n"
			 + "          "+XMLEncoder.encode(name)+"\n"
			 + "      </NAME>\n"
			 + "      <TYPE>\n"
			 + "          "+XMLEncoder.encode(type)+"\n"
			 + "      </TYPE>\n"
			 + "      <SIZE>\n"
			 + "          "+XMLEncoder.encode(Integer.toString(size))+"\n"
			 + "      </SIZE>\n"
			 + "  </FIELD>\n";
	}
	
	@Override
	public void insert(String input, RandomAccessFile dataRAF) 
			throws DatabaseException, IOException {
		(new CharValue(input, size)).insert(dataRAF);
	}

	@Override
	public String datumAsString(RandomAccessFile dataRAF) 
			throws IOException, DatabaseException {
		dataRAF.skipBytes(rowPos);
		return (new CharValue(dataRAF, size)).toString();
	}

	@Override
	public int getDatumSize() {
		return 2*size;
	}

	@Override
	public AbstractValue readValue(RandomAccessFile dataRAF) throws IOException {
		long fp = dataRAF.getFilePointer();
		dataRAF.skipBytes(rowPos - 1);
		CharValue value = new CharValue(dataRAF, size);
		dataRAF.seek(fp);
		return value;
	}

	@Override
	public AbstractValue parseValue(String valueString)
			throws DatabaseException {
		return new CharValue(valueString, size);
	}
}
