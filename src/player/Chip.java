package player;

enum Direction {
	NORTH(0, 0, -1), NORTHEAST(1, 1, -1), EAST(2, 1, 0), SOUTHEAST(3, 1, 1), SOUTH(
			4, 0, 1), SOUTHWEST(5, -1, 1), WEST(6, -1, 0), NORTHWEST(7, -1, -1);

	private final int x;
	private final int y;
	private final int index;

	Direction(int index, int x, int y) {
		this.index = index;
		this.x = x;
		this.y = y;
	}

	int getX() {
		return this.x;
	}

	int getY() {
		return this.y;
	}

	int getIndex() {
		return this.index;
	}
}

public class Chip {
	int x;
	int y;
	int[] connections;
	int color;

	public Chip(Chip c) {
		this.connections = new int[8];
		if (c == null) {
			return;
		}
		this.x = c.x;
		this.y = c.y;
//		for (int i = 0; i < this.connections.length; i++) {
//			this.connections[i] = c.connections[i];
//			if (c.connections[i] != 0) {
//				System.out.println("INDEX: " + i + " CONNECTION: "
//						+ c.connections[i]);
//			}
//		}

		this.color = c.color;
	}

	// A chip knows about its color, which board it is from, its position, and
	// the existence of
	// every chip of the same color on the same board.
	public Chip(int x, int y, int color, Board b) {
		this.connections = new int[8];
		this.x = x;
		this.y = y;
		this.color = color;
		updateChips(x, y, b);
//		for (int i = 0; i < this.connections.length; i++) {
//			if (this.connections[i] != 0) {
//				System.out.println("INDEX: " + i + " CONNECTION: "
//						+ this.connections[i]);
//			}
//		}
	}

	public void stepChip(int x1, int y1, Board b) {
		for (int i = 0; i < 8; i++) {
			int chipNum = this.getConnection(i);
			if (chipNum != 0) {
				b.getChip(chipNum).setConnection((i + 4) % 8,
						this.getConnection((i + 4) % 8));
			}
		}
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getConnection(int i) {
		return this.connections[i];
	}

	public void setConnection(int i, int j) {
		this.connections[i] = j;
	}

	public String toString() {
		return "X VALUE: " + this.x + " Y VALUE: " + this.y;
	}

	private void updateChips(int x, int y, Board board) {
		for (Direction d : Direction.values()) {
			int i = 1;
			// TODO CHANGE NULL TO SOMETHING ELSE
			int dx = d.getX();
			int dy = d.getY();
			boolean outOfBounds = true;
			while (!board.isCornerOrBounds(x + dx * i, y + dy * i, color)) {
				int tempChip = board.getChipNumber(x + dx * i, y + dy * i);
				if (tempChip != 0) {
					this.connections[d.getIndex()] = tempChip;
					Chip neighbor = board.getChip(tempChip);
					neighbor.connections[(d.getIndex()+4)%8]=board.getChipNumber(x, y);
					outOfBounds = false;
					break;
				}
				i++;
			}
			if (outOfBounds) {
				this.connections[d.getIndex()] = 0;
			}
		}
	}
	// Returns an array of all the other chips of the same color on the same
	// board
	/**
	 * public Chip[] sameColorChips() { Chip[] chips; if (this.color ==
	 * MachinePlayer.BLACK) { chips = new Chip[this.board.blackPieces().size()];
	 * for (int i = 0; i < this.board.blackPieces().size(); i++) { chips[i] =
	 * this.board.blackPieces().get(i); } } else { chips = new
	 * Chip[this.board.whitePieces().size()]; for (int i = 0; i <
	 * this.board.whitePieces().size(); i++) { chips[i] =
	 * this.board.whitePieces().get(i); } } return chips; }
	 * 
	 * public Chip[] otherColorChips() { Chip[] chips; if (this.color ==
	 * MachinePlayer.BLACK) { chips = new Chip[this.board.whitePieces().size()];
	 * for (int i = 0; i < this.board.whitePieces().size(); i++) { chips[i] =
	 * this.board.whitePieces().get(i); } } else { chips = new
	 * Chip[this.board.blackPieces().size()]; for (int i = 0; i <
	 * this.board.blackPieces().size(); i++) { chips[i] =
	 * this.board.blackPieces().get(i); } } return chips; }
	 * 
	 * // Given another chip and a chip[] of all the chips of the other color,
	 * // returns whether or not the chip can see the other chip public boolean
	 * canSee(Chip chip) { if (this.color == chip.color || // must be the same
	 * color this.x == chip.x // in the same column || this.y == chip.y // in
	 * the same row || (this.x - chip.x) == (this.y - chip.y) // diagonals ||
	 * (this.x - chip.x) == (chip.y - this.y)) { // Figure out which direction
	 * we're going
	 * 
	 * // slope would be undefined so we have to catch this case first if
	 * (this.x == chip.x) {
	 * 
	 * } else { int slope = (this.y - chip.y) / (this.x - chip.x); // Figure out
	 * whether it is left or right of the current // position if (this.x >
	 * chip.x) { // We check whether the locations between the two chips // have
	 * any chips of the opposite color for (int i = this.x - 1, j = this.y -
	 * slope; i > chip.x; i--, j = j - slope) { // check this logic, I subtract
	 * the slope // right? if (this.board.board()[i][j].color != this.color) {
	 * return false; } } } else {
	 * 
	 * // We check whether the locations between the two chips // have any chips
	 * of the opposite color for (int i = this.x + 1, j = this.y + slope; i >
	 * chip.x; i--, j = j + slope) { // check this logic, I subtract the slope
	 * // right? if (this.board.board()[i][j].color != this.color) { return
	 * false; } } } }
	 * 
	 * }
	 * 
	 * return false; }
	 */
}
