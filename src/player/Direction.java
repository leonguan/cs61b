package player;

public enum Direction {
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
