package values;

import java.io.IOException;
import java.io.RandomAccessFile;

import main.DatabaseException;

public class BooleanValue extends AbstractValue {
	private boolean val;

	public BooleanValue(String input) throws DatabaseException {
		input = input.trim();
		if (input.equalsIgnoreCase("true"))
			val = true;
		else if (input.equalsIgnoreCase("false"))
			val = false;
		else
			throw new DatabaseException("'" + input
					+ "' is not a valid boolean.");
	}

	public BooleanValue(RandomAccessFile dataRAF)
			throws IOException {
		val = dataRAF.readBoolean();
	}

	@Override
	public int compareTo(AbstractValue o) {
		BooleanValue v = (BooleanValue) o;
		if (val && !v.val)
			return 1;
		else if (!val && v.val)
			return -1;
		else
			return 0;
	}

	@Override
	public String toString() {
		if (val)
			return "TRUE";
		else
			return "FALSE";
	}

	public void insert(RandomAccessFile dataRAF) throws IOException {
		dataRAF.writeBoolean(val);
	}
}
