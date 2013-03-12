package moves;

import player.*;
import board.*;
import check.*;

public class Generate {

	private Board board;

	/**
	 * @param args takes in a board
	 */
	public Generate (Board board) {
		this.board = board;
	}
	
	// returns all valid moves in an array
	// Iterates through each position on the board and calls Check.isValid(this, move)
	// If the move isValid, then append to the list
	// Might use arraylist?
	public Move[] validMoves() {
		return null;
		
	}

}
