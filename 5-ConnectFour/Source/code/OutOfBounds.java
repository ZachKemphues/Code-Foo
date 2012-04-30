package code;

/**
 * A piece that in a location off the board
 * Used to avoid having to worry about null pointers when attempting access something off the board
 * @author Zach Kemphues
 */
public class OutOfBounds implements Piece{

	@Override
	public Piece next() {
		return this;
	}

	@Override
	public void setPosition(int c, int r) {
		
	}

	@Override
	public int getCol() {
		return -1;
	}

	@Override
	public int getRow() {
		return -1;
	}
	
	public String toString() {
		return "OB";
	}

}
