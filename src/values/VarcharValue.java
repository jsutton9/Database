package values;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;

public class VarcharValue extends AbstractValue {
	private String val;
	private Pattern pattern = Pattern.compile("\\s*\\'([^']*)\\'\\s*");

	public VarcharValue(String input) throws DatabaseException {
		Matcher matcher = pattern.matcher(input);
		if (!matcher.matches())
			throw new DatabaseException("'" + input
					+ "' is not a valid varchar.");
		val = matcher.group(1);
	}

	public VarcharValue(RandomAccessFile dataRAF, RandomAccessFile varDataRAF)
			throws IOException {
		varDataRAF.seek(dataRAF.readLong());
		val = varDataRAF.readUTF();
	}

	@Override
	public int compareTo(AbstractValue o) {
		return val.compareTo(((VarcharValue) o).val);
	}

	@Override
	public String toString() {
		return val;
	}

	public void insert(RandomAccessFile dataRAF, RandomAccessFile varDataRAF)
			throws IOException {
		varDataRAF.seek(varDataRAF.length());
		dataRAF.writeLong(varDataRAF.getFilePointer());
		varDataRAF.writeUTF(val);
	}
}