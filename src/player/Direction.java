package player;
/**
 * James doo dis plzzzz
 * @author James Jia
 *
 */
public enum Direction {
	NORTH(0, 0, -1), NORTHEAST(1, 1, -1), EAST(2, 1, 0), SOUTHEAST(3, 1, 1), SOUTH(
			4, 0, 1), SOUTHWEST(5, -1, 1), WEST(6, -1, 0), NORTHWEST(7, -1, -1);

	private final int x;
	private final int y;
	private final int index;

	/**
	 * Constructor for Direction object.
	 * Takes in an index that corresponds to a direction, and an (x,y) position and returns a Direction object.
	 * If the Direction was pointed towards N, index would be 0.
	 * If the Direction was pointed NE, index would be 1.
	 * If the Direction was pointed E, index would be 2, etc.
	 * @param index - described above.
	 * @param x - x position
	 * @param y - y position
	 */
	Direction(int index, int x, int y) {
		this.index = index;
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x position of the Direction object
	 */
	int getX() {
		return this.x;
	}

	/**
	 * @return the y position of the Direction object
	 */
	int getY() {
		return this.y;
	}

	/**
	 * @return the index of the Direction object
	 */
	int getIndex() {
		return this.index;
	}
}
