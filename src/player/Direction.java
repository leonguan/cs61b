package player;

public enum Direction {
	NORTH(0, 0, -1), NORTHEAST(1, 1, -1), EAST(2, 1, 0), SOUTHEAST(3, 1, 1), SOUTH(
			4, 0, 1), SOUTHWEST(5, -1, 1), WEST(6, -1, 0), NORTHWEST(7, -1, -1);

	private final byte x;
	private final byte y;
	private final byte index;

	Direction(int index, int x, int y) {
		this.index = (byte)index;
		this.x = (byte)x;
		this.y = (byte)y;
	}

	byte getX() {
		return this.x;
	}

	byte getY() {
		return this.y;
	}

	byte getIndex() {
		return this.index;
	}
}
