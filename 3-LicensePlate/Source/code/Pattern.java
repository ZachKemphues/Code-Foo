package code;

/**
 * Representation of a pattern to be used in creating license plates
 * the knows the total number of plates the pattern will create
 */
public class Pattern {
	private long _total;
	private String _pattern;

	/**
	 * Creates an empty pattern that would create 1 blank plate
	 */
	public Pattern() {
		_total = 1;
		_pattern = "";
	}
	/**
	 * Creates a pattern that would create v total plates and is 
	 * represented by s
	 * @param v - the total number of plates that can be created by this pattern
	 * @param s - the representation of the pattern as a string
	 */
	public Pattern(long v, String s){
		_total = v;
		_pattern = s;
	}
	/**
	 * @return - the total number of plates that can be created by this pattern
	 */
	public long getTotal() {
		return _total;
	}
	/**
	 * @return - the string representation of the pattern
	 */
	public String getPattern() {
		return _pattern;
	}
}
