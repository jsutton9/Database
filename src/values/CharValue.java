package values;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;

public class CharValue extends AbstractValue {
	private String val;
	private Pattern pattern = Pattern.compile("\\s*'([^']*)'\\s*");

	public CharValue(String input, int size) throws DatabaseException {
		Matcher matcher = pattern.matcher(input);
		if (!matcher.matches())
			throw new DatabaseException("'" + input + "' is not a valid char.");
		if (matcher.group(1).length() != size)
			throw new DatabaseException("'"+input+"' is not the correct length.");
		val = matcher.group(1);
	}

	public CharValue(RandomAccessFile dataRAF, int size)
			throws IOException {
		byte[] bytes = new byte[2*size];
		dataRAF.read(bytes);
		val = new String(bytes, "UTF-16");
	}

	@Override
	public int compareTo(AbstractValue o) {
		return val.compareTo(((CharValue) o).val);
	}

	@Override
	public String toString() {
		return val;
	}

	public void insert(RandomAccessFile dataRAF)
			throws IOException, DatabaseException {
		try {
			dataRAF.writeChars(val);
		} catch (UnsupportedEncodingException e) {
			throw new DatabaseException("Unable to use UTF-16 encoding");
		}
	}
}
