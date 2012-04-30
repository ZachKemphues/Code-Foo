package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

import code.*;

/**
 * A graphical board to play connect four against a computer opponent.  The player will always go first.
 * 
 * @author Zach Kemphues
 */
public class GUI implements MouseListener{
	private static final int PIECE_SIZE = 100;//starting piece size
	private static final int NUM_ROWS = 6;
	private static final int NUM_COLS = 7;
	
	private Board _game;
	private JPanel _boardArea;
	private DrawingPane[] _col;
	private JFrame _frame;
	private Piece _player;
	private boolean _aiEnabled;

	/**
	 * Completely sets up a new board and the gui for it
	 */
	public GUI() {
		init();
	}
	//Initializes a fresh game board
	private void init() {
		_game = new Board(NUM_COLS,NUM_ROWS);
		_col = new DrawingPane[NUM_COLS];
		_aiEnabled = true;
		_player = new Player1();
		
		//set up the game board
		_boardArea = new JPanel();
		_boardArea.setBackground(Color.BLACK);
		_boardArea.setLayout(new GridLayout(1,NUM_COLS));
		for(int c = 0; c < NUM_COLS; c++) {
			_col[c] = new DrawingPane(_game,c);
			_col[c].setBackground(new Color(0,0,0));
			_col[c].addMouseListener(this);
			_boardArea.add(_col[c]);
		}
		
		//set up the containing frame
		_frame = new JFrame("Connect Four");
		_frame.setSize((PIECE_SIZE*(2+NUM_COLS)/5 + PIECE_SIZE*NUM_COLS),(PIECE_SIZE*(2+NUM_ROWS)/5 + PIECE_SIZE*NUM_ROWS));
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setVisible(true);
		_frame.add(_boardArea);
	}
	/**
	 * Initializes the gui for the connect four game
	 * @param args - none
	 */
	public static void main(String args[]) {
		new GUI();
	}
	/**
	 * Attempts to place a piece for the active player in the column clicked on.
	 * Immediately makes the ai opponent's move if there is one.
	 * Pops up a dialog if either player wins, or the game ends in a draw.
	 * Does nothing if the initial placement was not valid.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		DrawingPane p = source(e);
		if(p == null || _game.gameWon())//wasn't relevant to the game board, or the game is already over
			return;
		if(_game.place(_player, p.getCol())) {//true only if the placement was valid
			_boardArea.repaint();
			if(_game.gameWon()) {
				playerWin();
				return;
			}
			_player = _player.next();
			if(_aiEnabled) {//always true for now
				GTN ai = new GTN(_game, 0, 6);
				_game.place(_player, ai.getBestMove());
				_boardArea.repaint();
				if(_game.gameWon()) {
					computerWin();
					return;
				}
				_player = _player.next();	
			}
			if(_game.fullBoard()) {//neither player won, and the board is now full
				catsGame();
				return;
			}
		}
	}
	//Displays a victory message for the player and resets the board
	private void playerWin() {
		JOptionPane.showMessageDialog(null, "Congratulations!  You have defeated the computer in a gruelling game of connect four!\n" +
				"The board will reset and you may play again once you close this window.");
		_frame.dispose();//remove the old game
		init();//create a new one
		
	}
	//Displays a defeat message for the player and resets the board
	private void computerWin() {
		JOptionPane.showMessageDialog(null, "Oh the horror!  You have been defeated by the computer in a gruelling game of connect four!\n" +
				"The board will reset and you may try again once you close this window.");
		_frame.dispose();//remove the old game
		init();//create a new one
	}
	//Displays a draw message for the player and resets the board
	private void catsGame() {
		JOptionPane.showMessageDialog(null, "Well, on the plus side you managed to avoid losing... however you also avoided winning!\n" +
				"The board will reset and you may try again once you close this window.");
		_frame.dispose();//remove the old game
		init();//create a new one
	}
	/**
	 * Highlights the column that the mouse entered
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		DrawingPane p = source(e);
		if(p == null)//wasn't relevant to the game board
			return;
		p.toggleHighlight();
		p.repaint();
	}
	/**
	 * Un-highlights the column that the mouse left
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		DrawingPane p = source(e);
		if(p == null)//wasn't relevant to the game board
			return;
		p.toggleHighlight();
		p.repaint();
	}
	@Override
	public void mousePressed(MouseEvent e) {}//do nothing
	@Override
	public void mouseReleased(MouseEvent e) {}//do nothing
	
	//returns the column clicked on, or null if the click was not on the board
	private DrawingPane source(MouseEvent e) {
		for(int i = 0; i < NUM_COLS; i++) {
			if(e.getSource().equals(_col[i])) {
				return _col[i];
			}
		}
		return null;//source was not one of the columns
	}
}
