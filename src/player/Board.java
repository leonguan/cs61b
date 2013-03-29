package player;

public class Board {
	final static int BOARD_SIZE = 8;
	// Don't need anymore because we have chips. Will just set to null
	// final static int NONE = null;

	private Chip[][] array;
	private int blackPieces;
	private int whitePieces;

	public Board() {
		this.array = new Chip[BOARD_SIZE][BOARD_SIZE];
		this.blackPieces = 0;
		this.whitePieces = 0;
	}

	public Board(Board b, Move m, int color) {
		this.array = new Chip[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				this.array[i][j] = b.array[i][j];
			}
		}
		this.blackPieces = b.blackPieces;
		this.whitePieces = b.whitePieces;
		addMove(m, color);
	}

	/**
	 * Updates the game board if the move is valid.
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
		if (!validMove(m, color)) {
			return false;
		}
		if (m.moveKind == Move.ADD) {
			if (color == MachinePlayer.BLACK) {
				this.blackPieces += 1;
			} else {
				this.whitePieces += 1;
			}
			this.array[m.x1][m.y1] = new Chip(m.x1, m.y1, color);

		} else if (m.moveKind == Move.STEP) {
			if (this.array[m.x2][m.y2].color != color) {
				return false;
			}
			this.array[m.x1][m.x1] = new Chip(m.x1, m.y1, color);
			this.array[m.x2][m.y2] = null;
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
	 * Returns true if player with specified color has a valid network.
	 * 
	 * @param color
	 * @return
	 */
	boolean isValidNetwork(int color) {
		return false;
	}

	/**
	 * Gets chip at position (x,y).
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	Chip getChip(int x, int y) {
		return this.array[x][y];
	}

	boolean shouldAdd() {
		return this.blackPieces < 10 && this.whitePieces < 10;
	}

	/**
	 * Checks if x1,y1 and x2,y2, if the moveKind is Move.STEP, are out of
	 * bounds. Also calls positionIsNull(Move m). Checks if conditions for
	 * add/step moves are met.
	 * 
	 * @param m
	 * @return
	 */
	boolean validMove(Move m, int color) {
		if (!(this.array[m.x1][m.y1] == null)) {
			return false;
		}
		boolean pos1OutBounds = m.x1 < 0 || m.x1 >= BOARD_SIZE || m.y1 < 0
				|| m.y1 >= BOARD_SIZE;
		boolean isCorner = (m.x1 == 0 && m.y1 == 0)
				|| (m.x1 == 0 && m.y1 == BOARD_SIZE - 1)
				|| (m.x1 == BOARD_SIZE - 1 && m.y1 == BOARD_SIZE - 1)
				|| (m.x1 == BOARD_SIZE - 1 && m.y1 == 0);
		boolean isEdge;
		if (color == MachinePlayer.BLACK) {
			isEdge = m.x1 == 0 || m.x1 == Board.BOARD_SIZE - 1;
		} else {
			isEdge = m.y1 == 0 || m.y1 == Board.BOARD_SIZE - 1;
		}
		if (pos1OutBounds || isCorner || isEdge || connectedChips(m, color)) {
			return false;
		}

		if (m.moveKind == Move.STEP) {
			if (m.x2 < 0 || m.x2 >= BOARD_SIZE || shouldAdd()) {
				return false;
			}
		} else if (m.moveKind == Move.ADD) {
			return shouldAdd();
		}
		return true;
	}

	/***
	 * Returns true if there are already two chips in a row in the vicinity of a
	 * move.
	 * 
	 * @param m
	 * @param color
	 *            - color of the chip that will be placed by Move m.
	 * @return
	 */
	private boolean connectedChips(Move m, int color) {
		boolean connected = false;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (i == 0 && j == 0) {
					continue;
				} else {
					Chip first = this.array[m.x1 + i][m.x1 + j];
					if (first != null && first.color == color) {
						for (int x = 0; x < 2; x++) {
							for (int y = 0; y < 2; y++) {
								if (i == 0 && j == 0) {
									continue;
								} else {
									Chip second = this.array[m.x1 + i + x][m.y1
											+ j + y];
									if (second != null && second.color == color) {
										connected = true;
									}
								}
							}
						}
					}
				}
			}
		}
		return connected;
	}
}
