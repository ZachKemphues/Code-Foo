package code;

/**
 * A piece for the human player
 * @author Ivor
 */
public class Player1 implements Piece{
	private int myRow;
	private int myCol;
	
	@Override
	public Piece next() {
		return new Player2();
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
		return "P1";
	}
}
