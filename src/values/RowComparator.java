package values;

import java.util.Comparator;

public class RowComparator implements Comparator<Row> {
	private int index;
	private boolean descending;
	
	public RowComparator(int fieldIndex, boolean isDescending) {
		index = fieldIndex;
		descending = isDescending;
	}

	@Override
	public int compare(Row row0, Row row1) {
		if (descending)
			return row1.get(index).compareTo(row0.get(index));
		else
			return row0.get(index).compareTo(row1.get(index));
	}
}
