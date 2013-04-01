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
}
