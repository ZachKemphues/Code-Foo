package code;

/**
 * An abstract game tree used to determine the best move for an AI player
 * @author Zach Kemphues
 */
public class GTN {
	public static final int P1_WIN = Integer.MAX_VALUE;
	public static final int P2_WIN = Integer.MIN_VALUE;
	
	private Board _game;
	private int _bestMove;
	private int _bestMoveValue;
	private int _logicLevel;
	private final int AI_DIFFICULTY;
	
	/**
	 * Creates, traverses, and evaluates the game tree to a depth of d to determine the best move for a player
	 * @param b - the game board
	 * @param l - the current level of the game tree
	 * @param d - the depth to search the game tree
	 */
	public GTN(Board b, int l, int d) {
		AI_DIFFICULTY = d;
		_game = b;
		_logicLevel = l;
		_bestMove = -1;
		boolean gameOver = false;
		int gameVal = _game.evaluate();
		if(_game.lastPiece().toString().equals("P1")) {
			if(gameVal == P1_WIN)
				gameOver = true;
			_bestMoveValue = P1_WIN;
		}
		else {
			if(gameVal == P2_WIN)
				gameOver = true;
			_bestMoveValue = P2_WIN;
		}
		if(_logicLevel != AI_DIFFICULTY && !gameOver && !_game.fullBoard()) 
			findBestMove();
		else
			_bestMoveValue = gameVal;
	}
	//evaluates all the children of the current node by placing a piece on the board, evaluating, then removing it,
	//and updates the best move based on the best child
	private void findBestMove() {
		GTN nextChild;
		for(int col = 0; col < _game.columnsOnBoard(); col++) {
			if(_game.isFull(col))
				continue;
			nextChild = makeChild(col);
			if(_logicLevel%2 == 0) {//children have P2 placements, ie. it's the computer's move
				if(_bestMoveValue >= nextChild.evaluate()) {
					_bestMoveValue = nextChild.evaluate();
					_bestMove = col;
					if(_bestMoveValue == P2_WIN) {//guaranteed to be the best move
						_game.undo();
						break;
					}
				}
			}
			else {
				if(_bestMoveValue <= nextChild.evaluate()) {//it's the player's move
					_bestMoveValue = nextChild.evaluate();
					_bestMove = col;
					if(_bestMoveValue == P1_WIN) {//guaranteed to be the best move
						_game.undo();
						break;
					}
				}
			}
			_game.undo();
		}
	}
	//creates and returns the child created by placing in col on the current board
	private GTN makeChild(int col) {
		if(_game.isFull(col))
			return null;
		if(_game.lastPiece().toString().equals("P1"))
			_game.place(new Player2(), col);
		else
			_game.place(new Player1(), col);
		GTN child = new GTN(_game, _logicLevel +1, AI_DIFFICULTY);
		return child;
	}
	/**
	 * @return - the value of the best move this node can make
	 */
	public int evaluate() {
		return _bestMoveValue;
	}
	/**
	 * @return - the move with the best value for this node
	 */
	public int getBestMove() {
		return _bestMove;
	}
}
