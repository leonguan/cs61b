package player;

public class Board {
	final static int SIZE = 8;
	private int[][] array;

	public Board() {
		this.array = new int[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				this.array[i][j] = -1;
			}
		}
	}

	/**
	 * Updates the gameboard if the move is valid.
	 * 
	 * @param m
	 *            - move to be added
	 * @param color
	 *            - color of piece to be added
	 * @return
	 */
	boolean addMove(Move m, int color) {
		if (m.moveKind == Move.QUIT) {
			return true;
		}
		if (!positionIsNull(m) || !validMove(m)) {
			return false;
		}
		if (m.moveKind == Move.ADD) {
			this.array[m.x1][m.y1] = color;
		} else if (m.moveKind == Move.STEP) {
			if (this.array[m.x2][m.y2] != color) {
				return false;
			}
			this.array[m.x1][m.x1] = color;
			this.array[m.x2][m.y2] = -1;
		}
		return true;
	}

	/**
	 * Evaluates the current game board.
	 * 
	 * @param color
	 * @return
	 */
	int eval(int color) {
		int evaluation = 0;
		return evaluation;
	}

	/**
	 * Checks if position at x1,y1 in the board is null. Returns true if null.
	 * 
	 * @param m
	 * @return
	 */
	private boolean positionIsNull(Move m) {
		return this.array[m.x1][m.y1] == -1;
	}

	/**
	 * Checks if x1,y1 and x2,y2, if the moveKind is Move.STEP, are out of
	 * bounds.
	 * 
	 * @param m
	 * @return
	 */
	private boolean validMove(Move m) {
		if (m.x1 < 0 || m.x1 >= SIZE) {
			return false;
		}
		if (m.moveKind == Move.STEP) {
			if (m.x2 < 0 || m.x2 >= SIZE) {
				return false;
			}
		}
		return true;
	}

}
