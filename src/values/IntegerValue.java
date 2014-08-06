package values;

import java.io.IOException;
import java.io.RandomAccessFile;

import main.DatabaseException;

public class IntegerValue extends AbstractValue {
	int val;
	
	public IntegerValue(String input) throws DatabaseException {
		try {
			val = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			throw new DatabaseException("'"+input+"' is not a valid integer.", e);
		}
	}

	public IntegerValue(RandomAccessFile dataRAF) throws IOException {
		val = dataRAF.readInt();
	}

	@Override
	public int compareTo(AbstractValue o) {
		return Integer.compare(val, ((IntegerValue) o).val);
	}

	@Override
	public String toString() {
		return Integer.toString(val);
	}

	public void insert(RandomAccessFile dataRAF) throws IOException {
		dataRAF.writeInt(val);
	}
}
