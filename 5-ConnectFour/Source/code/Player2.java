package code;
/**
 * A piece for the computer opponent
 * @author Zach Kemphues
 */
public class Player2 implements Piece{
	private int myRow;
	private int myCol;
	
	@Override
	public Piece next() {
		return new Player1();
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
		return "P2";
	}
}
