package gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

import code.Board;
import code.Piece;

/**
 * A specialized JPanel used to draw a single column of a connect four board.
 * 
 * @author Zach Kemphues
 */
public class DrawingPane extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Color P1 = new Color(0,150,0); //green
	private static final Color P2 = new Color(150,0,150);//purple
	private static final Color EMPTY = Color.WHITE;
	private static final Color H_COL = new Color(40,40,40);//dark grey
	private static final Color H_COL_FULL = new Color(100,0,0);//dark red
	private static final Color H_P1 = new Color(150,255,150);//light green
	private static final Color H_P2 = new Color(255,150,255);//light purple
	
	private Board _board;
	private int _col;
	private boolean _highlighted;
	
	/**
	 * Initializes the pane and keeps track of the column of the game board it represents
	 * @param b - the game board being played on
	 * @param col - the column of the board represented by this pane
	 */
	public DrawingPane(Board b, int col) {
		super();
		_col = col;
		_board = b;
		_highlighted = false;
	}
	/**
	 * Toggles the highlighted state of the column.  Highlighting indicates whether a piece can be placed in the column
	 * and is only active when the mouse is over the pane.
	 */
	public void toggleHighlight() {
		_highlighted = !_highlighted;
	}
	/**
	 * @return - the column of the board represented by this pane
	 */
	public int getCol() {
		return _col;
	}
	
	@Override public void paintComponent(Graphics g) {
		//draw the background with the correct color
		g.setColor(this.getBackground());
		if(_highlighted && !_board.gameWon()) {
			if(_board.isFull(_col))
				g.setColor(H_COL_FULL); //indicates that a piece cannot be placed in this column
			else
				g.setColor(H_COL); //indicates that a piece can be placed in this column
		}
		g.fillRect(0,0,this.getWidth(), this.getHeight());
		
		//draw all the pieces with the correct color for each, including faded colors indicating where the next piece will fall
		for(int row = 0; row < _board.rowsOnBoard(); row++) {
			Piece p = _board.pieceAt(_col, _board.rowsOnBoard() - row - 1);
			Color c;
			
			if(p.toString().equals("__")) {//The location is empty
				Piece next = _board.pieceAt(_col, _board.rowsOnBoard()-row-2);
				if(!next.toString().equals("__") && _highlighted && !_board.gameWon()) { //if either the bottom of the board, or another piece is below
					if(_board.lastPiece().toString().equals("P2"))
						c = H_P1;	//highlight the location the player's next piece will land
					else
						c = H_P2;
				}
				else
					c = EMPTY;
			}
			else if(p.toString().equals("P1"))
				c = P1;
			else
				c = P2;
			g.setColor(c);
			int width = this.getWidth();
			int height = this.getHeight();
			int n = _board.rowsOnBoard();
			int xOffset;
			int yOffset;
			int size;
			//determine the appropriate piece size
			if(width <= height/n)
				size = 4*width/5;
			else 
				size = 4*height/(5*n);
			//determine offsets so that the pieces are centered and evenly spaced in the column
			xOffset = (width-size)/2;
			yOffset = size/8;
			g.fillOval(xOffset, yOffset + (size+yOffset*2)*row, size, size);
		}
	}
}
