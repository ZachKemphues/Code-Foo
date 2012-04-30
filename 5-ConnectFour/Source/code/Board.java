package code;
import java.util.LinkedList;

/**
 * A board for a game of Connect Four
 * @author Zach Kemphues
 */
public class Board {
	
	private final int NUM_ROWS;
	private final int NUM_COLS;
	private Piece[][] _grid;
	private int[] _playablePosition; //the height of the empty slot in each column
	private int _piecesPlayed;
	private boolean _gameWon;
	private LinkedList<Piece> _turn;//a stack of all the turns that have been taken
	private LineHandler _lines;
	
	/**
	 * Creates a board to play Connect Four on with the standard 7 columns and 6 rows
	 */
	public Board() {
		this(7,6);
	}
	/**
	 * Creates a board to play Connect Four on with the specified number of rows and columns
	 * @param cols - the number of columns on the board
	 * @param rows - the number of rows on the board
	 */
	public Board(int cols, int rows) {
		NUM_COLS = cols;
		NUM_ROWS = rows;
		_lines = new LineHandler(cols, rows);
		_gameWon = false;
		initGrid();
		_playablePosition = new int[cols]; //all default correctly to 0
		_piecesPlayed = 0;
		_turn = new LinkedList<Piece>();
		_turn.push(new Player2());
	}
	//initializes the board and places empty pieces at every location
	private void initGrid() {
		_grid = new Piece[columnsOnBoard()][rowsOnBoard()];
		for(int r = rowsOnBoard() - 1; r >= 0; r--)
			for(int c = 0; c < columnsOnBoard(); c++) {
				_grid[c][r] = new Empty();
				_grid[c][r].setPosition(c, r);
			}	
	}
	/**
	 * @return - the last piece played
	 */
	public Piece lastPiece() {
		return _turn.peek();
	}
	/**
	 * @return - the number of rows on the board
	 */
	public int rowsOnBoard() {
		return NUM_ROWS;
	}
	/**
	 * @return - the number of columns on the board
	 */
	public int columnsOnBoard() {
		return NUM_COLS;
	}
	/**
	 * @param col - the column being checked
	 * @return - true if the column is full, false otherwise
	 */
	public boolean isFull(int col) {
		return _playablePosition[col] == rowsOnBoard();
	}
	/**
	 * Attempts to place a piece in the column
	 * Does nothing and returns false if it is unable to do so because the column is full
	 * @param p - the piece being placed
	 * @param col - the column the piece is being placed in
	 * @return - true if the piece was placed successfully, false if the column was full
	 */
	public boolean place(Piece p, int col) {
		if(isFull(col))
			return false;
		int row = _playablePosition[col]++;
		_grid[col][row] = p;
		_piecesPlayed++;
		_grid[col][row].setPosition(col, row);
		_turn.push(p);
		if(_lines.place(p, col, row))
			_gameWon = true;
		return true;
	}
	//removes the piece from the board
	private void remove(Piece p) {
		int row = p.getRow();
		int col = p.getCol();
		_playablePosition[col]--;
		_grid[col][row] = new Empty();
		_lines.remove(p,col,row);
		_piecesPlayed--;
	}
	/**
	 * Reverts the board to the previous move
	 */
	public void undo() {
		_gameWon = false;
		remove(_turn.pop());
	}
	/**
	 * @param col - the column of the piece on the board
	 * @param row - the row of the piece on the board
	 * @return - the piece at col,row on the board
	 */
	public Piece pieceAt(int col, int row) {
		if(col < 0 || col >= columnsOnBoard() || row < 0 || row >= rowsOnBoard())
			return new OutOfBounds();
		return _grid[col][row];
	}
	/**
	 * @return - true if either player has won the game, false otherwise
	 */
	public boolean gameWon() {
		return _gameWon;
	}
	/**
	 * @return - true if the board is completely full, false otherwise
	 */
	public boolean fullBoard() {
		return _piecesPlayed == rowsOnBoard()*columnsOnBoard();
	}
	/**
	 * Returns the board as a string to be used in debugging or if a GUI is not desired
	 * @return - the board with each row on a line, and a space between each column.  The highest row appears as the first line.
	 */
	public String boardAsString() {
		String s = "";
		for(int r = rowsOnBoard() - 1; r >= 0; r--) {
			for(int c = 0; c < columnsOnBoard(); c++) {
				s += _grid[c][r].toString();
				s += " ";
			}
			s += "\n";
		}
		return s;
	}
	/**
	 * @return - an evaluation of the current board.  Lower values are better for the computer, higher values are better for the player.
	 * - GTN.P1_WIN indicates a winning board for the player, GTN.P2_WIN indicates a win for the computer.
	 */
	public int evaluate() {
		if(gameWon() && lastPiece().toString().equals("P1"))
			return GTN.P1_WIN;
		else if(gameWon() && lastPiece().toString().equals("P2"))
			return GTN.P2_WIN;
		else {
			int eval = _lines.oppsP1() - _lines.oppsP2();
			eval *= 10000;//places highest priority on blocking opponent win possibilities
			eval += _lines.valueP1() - _lines.valueP2();//second priority is getting closer to winning
			return eval;
		}
	}
}
