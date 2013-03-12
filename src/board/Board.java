package board;

import player.*;

public class Board {

	private Chip[][] currBoard;
	

	/**
	 * @param args
	 */
	// creates a new board at the beginning of the game
	public Board() {
		currBoard = new Chip[8][8];
		
	}
	
	// creates a new board based on the old board and the following move
	
	public Board(Board board, Move move) {
		this.currBoard = board.currBoard;
		
		
	}
	
	// Finds the chips that form connections with a chip
	// Will iterate through each nearby chip (from [x-1:x+1][y-1:y+1])
	public Chip[] findNearby(int x, int y) {
		return null;
	}
	
	// Checks the board to see if it has any networks
	public boolean hasNetworks() {
		return true;
	}
	
	
	// Returns array of length 2 that holds the two people's scores.
	public int[] evaluate() {
		return null;
	}

}
