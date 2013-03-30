package player;

import utils.ChipArrayList;

public class Chip {
	int x;
	int y;
	int color;
	Board board;
	
	// A chip knows about its color, which board it is from, its position, and the existence of
	// every chip of the same color on the same board.
	public Chip(int x, int y, int color, Board board) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.board = board;
		
	}
	
	// Returns an array of all the other chips of the same color on the same board
	public Chip[] sameColorChips() {
		Chip[] chips;
		if (this.color == MachinePlayer.BLACK) {
			chips = new Chip[this.board.blackPieces().size()];
			for (int i = 0; i < this.board.blackPieces().size(); i++) {
				chips[i] = this.board.blackPieces().get(i);
			}
		}
		else {
			chips = new Chip[this.board.whitePieces().size()];
			for (int i = 0; i < this.board.whitePieces().size(); i++) {
				chips[i] = this.board.whitePieces().get(i);
			}
		}
		return chips;
	}
	
	public Chip[] otherColorChips() {
		Chip[] chips;
		if (this.color == MachinePlayer.BLACK) {
			chips = new Chip[this.board.whitePieces().size()];
			for (int i = 0; i < this.board.whitePieces().size(); i++) {
				chips[i] = this.board.whitePieces().get(i);
			}
		}
		else {
			chips = new Chip[this.board.blackPieces().size()];
			for (int i = 0; i < this.board.blackPieces().size(); i++) {
				chips[i] = this.board.blackPieces().get(i);
			}
		}
		return chips;
	}
	
	// Given another chip and a chip[] of all the chips of the other color,
	// returns whether or not the chip can see the other chip
	public boolean canSee(Chip chip) {
		if (this.color == chip.color || // must be the same color
				this.x == chip.x // in the same column
				|| this.y == chip.y // in the same row
				|| (this.x - chip.x) == (this.y - chip.y) // diagonals
				|| (this.x - chip.x) == (chip.y - this.y)) {
			// Figure out which direction we're going
			
			// slope would be undefined so we have to catch this case first
			if (this.x == chip.x) {
				
			}
			else {
				int slope = (this.y - chip.y) / (this.x - chip.x);
				// Figure out whether it is left or right of the current position
				if (this.x > chip.x) {
					// We check whether the locations between the two chips
					// have any chips of the opposite color 
					for (int i = this.x - 1, j = this.y - slope; i > chip.x; i--, j = j - slope) { // check this logic, I subtract the slope right?
						if (this.board.board()[i][j].color != this.color) {
							return false;
						}
					}
				}
				else {
					
					// We check whether the locations between the two chips
					// have any chips of the opposite color 
					for (int i = this.x + 1, j = this.y + slope; i > chip.x; i--, j = j + slope) { // check this logic, I subtract the slope right?
						if (this.board.board()[i][j].color != this.color) {
							return false;
						}
					}
				}
			}


			
		}
		
		return false;
	}
	

}
