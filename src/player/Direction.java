package player;

/**
 * Set of predefined constants for directions to make things easier in the other
 * classes. First byte represents the index of the direction. Second byte
 * represents the x coordinate of the direction. Last byte represents the y
 * coordinate of the direction.
 * 
 * @author James
 * 
 */
public enum Direction {
	NORTH(0, 0, -1), NORTHEAST(1, 1, -1), EAST(2, 1, 0), SOUTHEAST(3, 1, 1), SOUTH(
			4, 0, 1), SOUTHWEST(5, -1, 1), WEST(6, -1, 0), NORTHWEST(7, -1, -1);

	private final byte x;
	private final byte y;
	private final byte index;

	/**
	 * Constructs a direction with specified index, x coordinate, and y
	 * coordinate
	 * 
	 * @param index
	 * @param x
	 * @param y
	 */
	Direction(int index, int x, int y) {
		this.index = (byte) index;
		this.x = (byte) x;
		this.y = (byte) y;
	}

	/**
	 * getter method for a direction's x value.
	 * 
	 * @return the x coordinate of a direction
	 */
	byte getX() {
		return this.x;
	}

	/**
	 * getter method for a direction's y value.
	 * 
	 * @return the y coordinate of a direction
	 */
	byte getY() {
		return this.y;
	}

	/**
	 * getter method for a direction's index.
	 * 
	 * @return the index of a direction
	 */
	byte getIndex() {
		return this.index;
	}
}
