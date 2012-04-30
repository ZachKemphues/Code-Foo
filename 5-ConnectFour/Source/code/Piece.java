package code;

/**
 * The pieces that make up the board.
 * @author Zach Kemphues
 */
public interface Piece {
	/**
	 * @return - the piece that follows this one in the turn order.
	 */
	public Piece next();
	/**
	 * Sets the position of the piece on the board for future reference
	 * @param c - the column of the piece
	 * @param r - the row of the piece
	 */
	public void setPosition(int c, int r);
	/**
	 * @return - the column the piece is in
	 */
	public int getCol();
	/**
	 * @return - the row the piece is in
	 */
	public int getRow();
	
	@Override
	public String toString();
}
