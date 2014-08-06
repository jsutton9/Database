package values;

public class Row {
	private AbstractValue[] values;
	
	public Row(AbstractValue[] newValues) {
		values = newValues;
	}
	
	public String toString() {
		String out = "";
		for (AbstractValue v : values)
			out += v.toString() + "\t| ";
		out += "\n";
		return out;
	}

	public boolean equals(Row other) {
		if (values.length != other.values.length)
			return false;
		for (int i=0; i<values.length; i++)
			if (values[i].compareTo(other.values[i]) != 0)
				return false;
		return true;
	}

	public Row join(Row other) {
		AbstractValue[] newValues = new AbstractValue[values.length+other.values.length];
		for (int i=0; i<values.length; i++)
			newValues[i] = values[i];
		for (int i=0; i<other.values.length; i++)
			newValues[values.length+i] = other.values[i];
		return new Row(newValues);
	}

	public Row project(int[] fieldIndices) {
		AbstractValue[] newValues = new AbstractValue[fieldIndices.length];
		for (int i=0; i<fieldIndices.length; i++)
			newValues[i] = values[fieldIndices[i]];
		return new Row(newValues);
	}
	
	public AbstractValue get(int i) {
		return values[i];
	}
}
