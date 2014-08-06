package values;

import java.io.IOException;
import java.io.RandomAccessFile;

import main.DatabaseException;

public class RealValue extends AbstractValue {
	private float val;
	
	public RealValue(String input) throws DatabaseException {
		try {
			val = Float.parseFloat(input);
		} catch (NumberFormatException e) {
			throw new DatabaseException("'"+input+"' is not a valid real.", e);
		}
	}

	public RealValue(RandomAccessFile dataRAF) throws IOException{
		val = dataRAF.readFloat();
	}

	@Override
	public int compareTo(AbstractValue o) {
		return Float.compare(val, ((RealValue) o).val);
	}

	@Override
	public String toString() {
		return Float.toString(val);
	}

	public void insert(RandomAccessFile dataRAF) throws IOException, DatabaseException {
		dataRAF.writeFloat(val);
	}
}