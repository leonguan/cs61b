package player;

/**
 * A Chip keeps track of its (x,y) location on the Board it is from.
 * Each chip also keeps track of its connections in each of the 8 different directions on the Board.
 * The 8 directions are North, Northeast, East, Southeast, South, Southwest, West, and Northwest,
 * which correspond to indices 0-8 in that order.
 * The int[] connections array works in the following way:
 * If there is a chip, chip2, in a given direction from chip1, then since chip2 is on the board, it is guaranteed to have an index
 * in the Board's Chip[] Board.chips
 * The chip2's Chip[] index is stored in chip1's connections array at the index corresponding to the direction of chip2 from chip1.
 * Example:
 * Say the chip at the 8th index of the Board's Chip[] is South of chip2.
 * Then the value of chip2.connections[4] == 8.
 * A chip also knows its own color.
 * @author Andrew Liu
 * @author Matthew Miller
 *
 */
public class Chip {
	private int x;
	private int y;
	private int[] connections;
	private int color;

	/**
	 * Constructs a chip given another chip.
	 * The new chip has the exact same properties as the first chip.
	 * @param c - a chip to clone
	 */
	public Chip(Chip c) {
		if (c == null) {
			return;
		}
		this.connections = new int[8];
		for (int i =0; i<this.connections.length;i++){
			this.connections[i]=c.connections[i];
		}
		this.x = c.x;
		this.y = c.y;
		this.color = c.color;
	}

	/**
	 * Constructs a chip with a specified (x,y) position, color, and board.
	 * @param x - x position
	 * @param y - y position
	 * @param color - color
	 * @param b - board the chip is ons
	 */
	public Chip(int x, int y, int color, Board b) {
		this.connections = new int[8];
		this.x = x;
		this.y = y;
		this.color = color;
		updateChips(x, y, b);
	}

	/**
	 * After all the chips have been added to the board, no more chips may be added to the board.
	 * STEP moves can be thought of as two distinct parts:
	 * 1) removing the chip from the old location while updating the connections of the surrounding chips due to the removal
	 * 2) adding a new chip at the new location and updating the connections of the surrounding chips.
	 * This method handles the most of the first part, removing the chip from the old location
	 * While it doesn't actually remove itself, it does update the connections of the surrounding chips
	 * @param x1 - x position
	 * @param y1 - y position
	 * @param b - Board the chip is on
	 */
	public void stepChip(int x1, int y1, Board b) {
		for (int i = 0; i < 8; i++) {
			int chipNum = this.getConnection(i);
			if (chipNum != 0) {
				b.getChip(chipNum).setConnection((i + 4) % 8,
						this.getConnection((i + 4) % 8));
			}
		}
	}

	/**
	 * Public accessor that returns the x position of the chip
	 * @return x position of chip
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Public accessor that returns the y position of the chip
	 * @return y position of chip
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Public accessor that returns the color of the chip
	 * @return color of chip
	 */
	public int getColor() {
		return this.color;
	}

	/**
	 * Public accessor that returns the index in Board.chips that corresponds to the chip in a certain connection direction from this chip
	 * @param i - the direction of the connection to look at
	 * @return the index in Board.chips that corresponds to the chip in the connection direction specified. Returns 0 if there is no connection in that direction.
	 */
	public int getConnection(int i) {
		return this.connections[i];
	}

	/**
	 * Public setter that sets the value of this.connections in a certain direction, i, to be j, where j is the index
	 * that corresponds to the chip in Board.chips[j].
	 * @param i - direction of connection
	 * @param j - index of Board.chips that references the Chip that is connected with this one in the specified direction.
	 */
	public void setConnection(int i, int j) {
		this.connections[i] = j;
	}

	/**
	 * Returns a stringified representation of a Chip.
	 * For testing purposes
	 */
	public String toString() {
		return "X VALUE: " + this.x + " Y VALUE: " + this.y;
	}

	/**
	 * After all the chips have been added to the board, no more chips may be added to the board.
	 * STEP moves can be thought of as two distinct parts:
	 * 1) removing the chip from the old location while updating the connections of the surrounding chips due to the removal
	 * 2) adding a new chip at the new location and updating the connections of the surrounding chips.
	 * This method handles the first part, adding a new chip to the board at the location (x,y)
	 * It updates the connections of both this chip and all the chips it is connected with.
	 * This method is also called when simply adding chips during the first 20 turns of the game.
	 * @param x - x position
	 * @param y - y position
	 * @param board - board the chip is being added to
	 */
	private void updateChips(int x, int y, Board board) {
		for (Direction d : Direction.values()) {
			int i = 1;
			int dx = d.getX();
			int dy = d.getY();
			boolean outOfBounds = true;
			while (!board.isCornerOrOutOfBounds(x + dx * i, y + dy * i, color)) {
				int tempChip = board.getChipNumber(x + dx * i, y + dy * i);
				if (tempChip != 0) {
					this.connections[d.getIndex()] = tempChip;
					Chip neighbor = board.getChip(tempChip);
					neighbor.connections[(d.getIndex() + 4) % 8] = board
							.getChipNumber(x, y);
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
}
