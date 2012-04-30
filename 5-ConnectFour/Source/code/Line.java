package code;

/**
 * An abstract representation of a line used by the AI to determine the number of ways either player can win
 * @author Zach Kemphues
 */
public class Line {
	private int _numP1;
	private int _numP2;
	
	/**
	 * Creates a line with no pieces from either player in it
	 */
	public Line() {
		_numP1 = 0;
		_numP2 = 0;
	}
	/**
	 * Adds a piece to the line
	 * @param p - the type of piece being added
	 * @return - true if the piece completes the line indicating a winning line, false otherwise
	 */
	public boolean add(Piece p) {
		if(p.toString().equals("P1")) 
			return ++_numP1 == 4;
		else 
			return ++_numP2 == 4;
	}
	/**
	 * Removes a piece from the line
	 * @param p - the type of piece being removed
	 */
	public void remove(Piece p) {
		if(p.toString().equals("P1"))
			_numP1--;
		else
			_numP2--;
	}
	
	/**
	 * @return - The number of Player1 pieces in the line, or 0 if Player2 has any pieces in the line
	 */
	public int valueP1() {
		if(_numP2 > 0)	//if the line contains a P2 piece
			return 0;	//the line can't be completed by P1
		return _numP1;
	}
	/**
	 * @return - The number of Player2 pieces in the line, or 0 if Player1 has any pieces in the line
	 */
	public int valueP2() {
		if(_numP1 > 0)	//if the line contains a P1 piece
			return 0;	//the line can't be completed by P2
		return _numP2;
	}
	/**
	 * @return - 1 if the line can be completed by Player1, 0 otherwise
	 */
	public int openP1() {
		if(_numP2 > 0)
			return 0;
		return 1;
	}
	/**
	 * @return - 1 if the line can be completed by Player2, 0 otherwise
	 */
	public int openP2() {
		if(_numP1 > 0)
			return 0;
		return 1;
	}
}
