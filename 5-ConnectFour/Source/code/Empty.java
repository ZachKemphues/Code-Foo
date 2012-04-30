package code;

/**
 * A piece that has not been placed yet
 * Used to avoid worrying about null pointers when checking an unknown piece on the board
 * @author Zach Kemphues
 */
public class Empty implements Piece{
	private int myRow;
	private int myCol;
	
	@Override
	public Piece next() {
		return this;
	}

	@Override
	public void setPosition(int c, int r) {
		myRow = r;
		myCol = c;
	}

	@Override
	public int getCol() {
		return myCol;
	}

	@Override
	public int getRow() {
		return myRow;
	}
	
	@Override
	public String toString() {
		return "__";
	}

}
