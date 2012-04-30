package code;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Some JUnit tests that were used in the debugging process.
 * @author Zach Kemphues
 *
 */
public class Tester {
	
	/**
	 * Tests that the board display and the initialization are working as intended
	 */
	@Test public void testInitialBoard() {
		Board b = new Board();
		String expected = 	"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n";
		String actual = b.boardAsString();
		String feedback = "The board should have been:\n"+expected+"\nThe board looked like this instead:\n";
		Assert.assertTrue(feedback, expected.equals(actual));
	}
	
	/**
	 * Tests placing a single piece on the board
	 */
	@Test public void testPlaceOne() {
		Board b = new Board();
		b.place(new Player1(), 1);
		String expected = 	"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ P1 __ __ __ __ __ \n";
		String actual = b.boardAsString();
		String feedback = "The board should have been:\n"+expected+"\nThe board looked like this instead:\n"+actual;
		Assert.assertTrue(feedback, expected.equals(actual));
	}
	
	/**
	 * Tests placing two pieces in the same column
	 */
	@Test public void testPlaceTwoAtSameLoc() {
		Board b = new Board();
		b.place(new Player1(), 1);
		b.place(new Player2(), 1);
		String expected = 	"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ __ __ __ __ __ __ \n" +
							"__ P2 __ __ __ __ __ \n" +
							"__ P1 __ __ __ __ __ \n";
		String actual = b.boardAsString();
		String feedback = "The board should have been:\n"+expected+"\nThe board looked like this instead:\n"+actual;
		Assert.assertTrue(feedback, expected.equals(actual));
	}
	
	/**
	 * Tests to make sure placing a piece in a full column does not result in an error, but does not alter the board either
	 */
	@Test public void testPlaceInFullCol() {
		Board b = new Board();
		b.place(new Player1(), 1);
		b.place(new Player2(), 1);
		b.place(new Player1(), 1);
		b.place(new Player2(), 1);
		b.place(new Player1(), 1);
		b.place(new Player2(), 1);
		b.place(new Player1(), 1);//last one should not be placed
		String expected = 	"__ P2 __ __ __ __ __ \n" +
							"__ P1 __ __ __ __ __ \n" +
							"__ P2 __ __ __ __ __ \n" +
							"__ P1 __ __ __ __ __ \n" +
							"__ P2 __ __ __ __ __ \n" +
							"__ P1 __ __ __ __ __ \n";
		String actual = b.boardAsString();
		String feedback = "The board should have been:\n"+expected+"\nThe board looked like this instead:\n"+actual;
		Assert.assertTrue(feedback, expected.equals(actual));
	}
	/**
	 * Testing the undo functionality used by the game tree
	 */
	@Test public void undoOnce() {
		Board b1 = new Board();
		Board b2 = new Board();
		b1.place(new Player1(), 1);
		b1.place(new Player2(), 1);
		b1.place(new Player1(), 1);
		b1.undo();
		
		b2.place(new Player1(), 1);
		b2.place(new Player2(), 1);
		
		String expected = b2.boardAsString();
		String actual = b1.boardAsString();
		String feedback = "The board should have been:\n"+expected+"\nThe board looked like this instead:\n"+actual;
		Assert.assertTrue(feedback, expected.equals(actual));
	}
}
