package values;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DatabaseException;

public class DateValue extends AbstractValue {
	private long val;
	private Pattern pattern = Pattern.compile(
			"\\s*'([0-9][0-9])/([0-9][0-9])/([0-9][0-9][0-9][0-9])'\\s*");
	
	public DateValue(String input) throws DatabaseException {
		Matcher matcher = pattern.matcher(input);
		if (! matcher.matches())
			throw new DatabaseException("'"+input+"' is not a valid date.");
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Integer.parseInt(matcher.group(3)), 
				Integer.parseInt(matcher.group(1))-1, 
				Integer.parseInt(matcher.group(2)), 
				0, 0, 0);
		val = cal.getTimeInMillis();
	}
	
	public DateValue(RandomAccessFile dataRAF) throws IOException {
		val = dataRAF.readLong();
	}

	@Override
	public int compareTo(AbstractValue o) {
		DateValue v = (DateValue) o;
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(val);
		int[] date = {cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH)};
		
		cal.setTimeInMillis(v.val);
		int[] otherDate = {cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH)};

		if (date[0] == otherDate[0]) {
			if (date[1] == otherDate[1])
				return date[2] - otherDate[2];
			else
				return date[1] - otherDate[1];
		} else {
			return date[0] - otherDate[0];
		}
	}

	@Override
	public String toString() {
		return (new SimpleDateFormat("MM/dd/yyyy")).format(val);
	}

	public void insert(RandomAccessFile dataRAF) throws IOException{
		dataRAF.writeLong(val);
	}
}
