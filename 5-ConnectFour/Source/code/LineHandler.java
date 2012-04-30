package code;
import java.util.LinkedList;

/**
 * A collection of all the lines on the game board with information about how many are open for use by the AI
 * Allows for O(1) win condition checking, and O(1) AI board evaluation
 * @author Zach Kemphues
 */
public class LineHandler {
	private LinkedList<Line>[][] _line;
	private static final int HORIZONTAL = 1;
	private static final int VERTICAL = 2;
	private static final int DIAGONAL_LEFT = 3;
	private static final int DIAGONAL_RIGHT = 4;
	
	private int _numCols;
	private int _numRows;
	
	private int _P1val;
	private int _P1opps;
	private int _P2val;
	private int _P2opps;
	
	/**
	 * Initializes all the lines and the matrix of lists used to access them in O(1) time
	 * @param cols - the number of columns on the board
	 * @param rows - the number of rows on the board
	 */
	public LineHandler(int cols, int rows) {
		_line = (LinkedList<Line>[][]) new LinkedList[cols][rows]; //using new LinkedList<Line>[cols][rows] gives a compiler error
		_numCols = cols;
		_numRows = rows;
		_P1val = 0;
		_P2val = 0;
		_P1opps = 0;
		_P2opps = 0;
		initLines();
	}
	
	/**
	 * Updates all the lines that contain the placed piece and returns true if the piece resulted in a win
	 * Also updates all the opportunity and value variables for both players with the new line data
	 * @param p - the piece that was placed
	 * @param col - the column the piece was placed in
	 * @param row - the row the piece was placed in
	 * @return - true if the game is won, false otherwise
	 */
	public boolean place(Piece p, int col, int row) {
		boolean foundWin = false;
		
		int oldVal1 = 0;
		int newVal1 = 0;
		int oldOpp1 = 0;
		int newOpp1 = 0;
		
		int oldVal2 = 0;
		int newVal2 = 0;
		int oldOpp2 = 0;
		int newOpp2 = 0;
		
		for(Line l: _line[col][row]) {
			oldVal1 += l.valueP1();
			oldVal2 += l.valueP2();
			oldOpp1 += l.openP1();
			oldOpp2 += l.openP2();
			
			if(l.add(p))
				foundWin = true;
		}
		for(Line l: _line[col][row]) {
			newVal1 += l.valueP1();
			newVal2 += l.valueP2();
			newOpp1 += l.openP1();
			newOpp2 += l.openP2();
		}
		_P1val += newVal1 - oldVal1;
		_P2val += newVal2 - oldVal2;
		_P1opps += newOpp1 - oldOpp1;
		_P2opps += newOpp2 - oldOpp2;
		return foundWin;
	}
	/**
	 * Updates all the lines that contain the removed piece
	 * Also updates all the opportunity and value variables for both players with the new line data
	 * @param p - the piece that was removed
	 * @param col - the column the piece was removed from
	 * @param row - the row the piece was removed from
	 */
	public void remove(Piece p, int col, int row) {
		int oldVal1 = 0;
		int newVal1 = 0;
		int oldOpp1 = 0;
		int newOpp1 = 0;
		
		int oldVal2 = 0;
		int newVal2 = 0;
		int oldOpp2 = 0;
		int newOpp2 = 0;
		
		for(Line l: _line[col][row]) {
			oldVal1 += l.valueP1();
			oldVal2 += l.valueP2();
			oldOpp1 += l.openP1();
			oldOpp2 += l.openP2();
			
			l.remove(p);
		}
		for(Line l: _line[col][row]) {
			newVal1 += l.valueP1();
			newVal2 += l.valueP2();
			newOpp1 += l.openP1();
			newOpp2 += l.openP2();
		}
		_P1val += newVal1 - oldVal1;
		_P2val += newVal2 - oldVal2;
		_P1opps += newOpp1 - oldOpp1;
		_P2opps += newOpp2 - oldOpp2;
	}
	//Initializes all the lines and creates references to them by row and column using lists
	private void initLines() {
		//Initialize the lists
		for(int r = 0; r < _numRows; r++)
			for(int c = 0; c < _numCols; c++)
				_line[c][r] = new LinkedList<Line>();
		
		//Add lines to the lists
		for(int r = 0; r < _numRows; r++) {
			for(int c = 0; c < _numCols; c++) {
				if(c+3 < _numCols) 
					makeLine(c,r,HORIZONTAL);
				if(r+3 < _numRows)
					makeLine(c,r,VERTICAL);
				if(c+3 < _numCols && r+3 < _numRows)
					makeLine(c,r,DIAGONAL_RIGHT);
				if(c-3 >= 0 && r+3 < _numRows)
					makeLine(c,r,DIAGONAL_LEFT);
			}
		}
	}
	//Creates a new line and adds references to it in all the appropriate lists
	private void makeLine(int col, int row, int dir) {
		Line l = new Line();
		_P1opps++;
		_P2opps++;
		switch(dir) {
		case HORIZONTAL:
			for(int c = col; c < col+4; c++)
				_line[c][row].push(l);
			break;
		case VERTICAL:
			for(int r = row; r < row+4; r++)
				_line[col][r].push(l);
			break;
		case DIAGONAL_RIGHT:
			for(int c = col; c < col+4; c++)
				_line[c][row++].push(l);
			break;
		case DIAGONAL_LEFT:
			for(int c = col; c > col-4; c--)
				_line[c][row++].push(l);
			break;
		default:
			break;
		}	
	}
	/**
	 * @return - the number of pieces Player1 has placed in lines that can still win
	 */
	public int valueP1() {
		return _P1val;
	}
	/**
	 * @return - the number of pieces Player2 has placed in lines that can still win
	 */
	public int valueP2() {
		return _P2val;
	}
	/**
	 * @return - the number of lines that could allow Player1 to win
	 */
	public int oppsP1() {
		return _P1opps;
	}
	/**
	 * @return - the number of lines that could allow Player2 to win
	 */
	public int oppsP2() {
		return _P2opps;
	}
}
