package player;

import utils.IntegerArrayList;

public class Board {
	final static int BOARD_SIZE = 8;
	// Don't need anymore because we have chips. Will just set to null
	// final static int NONE = null;
	private Chip[] chips;
	private int TOTAL_CHIPS = 20;
	private int[][] boardLocations;

	// private Chip[][] array;

	// Changed the representation of simply keeping track of count
	// To keeping track of the actual pieces on the board
	// private int blackPieces;
	// private ChipArrayList whitePieces;
	// private ChipArrayList blackPieces;

	public Board() {
		this.chips = new Chip[TOTAL_CHIPS + 1];
		this.boardLocations = new int[BOARD_SIZE][BOARD_SIZE];
	}

	// Given a board and a move, produces a new board
	public Board(Board b, Move m, int turn) {
		this();
		// Needs a new ChipArrayList to keep track of the black and white pieces
		// on this board
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				this.boardLocations[i][j] = b.boardLocations[i][j];
			}
		}
		for (int i = 0; i < TOTAL_CHIPS + 1; i++) {
			if (b.chips[i] != null) {
				this.chips[i] = new Chip(b.chips[i]);
			}
		}
		this.addMove(m, turn);
	}

	/**
	 * ChipArrayList whitePieces() { return this.whitePieces; }
	 * 
	 * ChipArrayList blackPieces() { return this.blackPieces; }
	 */
	int[][] getLocations() {
		return this.boardLocations;
	}

	void setLocation(int x, int y, int value) {
		this.boardLocations[x][y] = value;
		return;
	}

	Chip[] getChips() {
		return this.chips;
	}

	Chip getChip(int i) {
		return this.chips[i];
	}

	int getChipNumber(int x, int y) {
		return this.boardLocations[x][y];
	}

	int getTotalChips() {
		return this.TOTAL_CHIPS;
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
	// boolean addMove(Move m, int color) {
	boolean addMove(Move m, int turn) {
		if (m.moveKind == Move.QUIT) {
			return true;
		}
		if (!validMove(m, turn)) {
			return false;
		}
		if (m.moveKind == Move.ADD) {
			// this.boardLocations[m.x1][m.y1] = new Chip(m.x1, m.y1, color,
			// this);
			this.boardLocations[m.x1][m.y1] = turn;
			this.chips[turn] = new Chip(m.x1, m.y1, turn % 2, this);
			System.out.println(this.chips[turn]);
			System.out.println("TEST");
			for (int i = 0; i < this.getChip(1).connections.length; i++) {
				if (this.getChip(1).connections[i] != 0) {
					System.out.println("INDEX: " + i + " CONNECTION: "
							+ this.getChip(1).connections[i]);
				}
			}
			System.out.println();

			/**
			 * if (color == MachinePlayer.BLACK) {
			 * this.blackPieces.add(this.array[m.x1][m.y1]); } else {
			 * this.whitePieces.add(this.array[m.x1][m.y1]); }
			 */
		} else if (m.moveKind == Move.STEP) {
			if (this.boardLocations[m.x2][m.y2] == 0
					|| this.chips[this.boardLocations[m.x2][m.y2]].color != turn % 2) {
				return false;
			}
			// Does this work?
			// For step moves, we should split this into a removeChip and then
			// call addChip on it
			int chipNum = this.getChipNumber(m.x2, m.y2);
			setLocation(m.x2, m.y2, 0);
			this.getChip(chipNum).stepChip(m.x1, m.y1, this);
			this.chips[chipNum] = new Chip(m.x1, m.y1, turn % 2, this);
			setLocation(m.x1, m.y1, chipNum);
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
		int start = 1;
		if (color == 0) {
			start += 1;
		}
		for (; start < TOTAL_CHIPS; start += 2) {
			Chip c = chips[start];
			if (c == null) {
				continue;
			}
			for (Direction d : Direction.values()) {
				// System.out.println();
				// System.out.println();
				// System.out.println("CHIP:" + c.toString());
				if ((c.getX() == 0 && d.getX() != 1)
						|| (c.getX() == 7 && d.getX() != -1)
						|| (c.getY() == 0 && d.getY() != 1)
						|| (c.getY() == 7 && d.getY() != -1)) {
					continue;
				}
				evaluation += extend(c.getX(), c.getY(), d.getX(), d.getY(),
						color);
			}
		}
		return evaluation;
	}

	int extend(int x, int y, int mx, int my, int color) {
		int i = 1;
		int total = 0;
		while (true) {
			if (inBounds(x + mx * i, y + my * i, color)) {
				if ((color == 1 && (x + mx * i == 0 || x + mx * i == 7))
						|| (color == 0 && (y + my * i == 0 || y + my * i == 7))) {
					// System.out.println("XVALUE: "+x+", YVALUE: "+y+", MXVALUE: "+mx+", MYVALUE: "+my+" HIT END ZONE");
					total += 1;
					if (boardLocations[x + mx * i][y + my * i] != 0) {
						if (boardLocations[x + mx * i][y + my * i] % 2 == color) {
							// System.out.println("XVALUE: "+x+", YVALUE: "+y+", MXVALUE: "+mx+", MYVALUE: "+my+"FOUND CHIP");

							total += 2;
						}
					}
					return total;
				}
			} else {
				return total;
			}
			if (boardLocations[x + mx * i][y + my * i] != 0) {
				if (boardLocations[x + mx * i][y + my * i] % 2 == color) {
					// System.out.println("XVALUE: "+x+", YVALUE: "+y+", MXVALUE: "+mx+", MYVALUE: "+my+"FOUND CHIP");

					total += 2;
				}
				return total;
			}
			i += 1;
		}
	}

	/**
	 * Returns true if player with specified color has a valid network. NOTE I
	 * THINK IT"S NECESSARY TO IMPLEMENT AN ARRAYLIST FOR THIS METHOD.
	 * 
	 * @param color
	 * @return
	 */
	boolean isValidNetwork(int turn, boolean chipOnFirstSide,
			boolean chipOnSecondSide) {
		boolean hasChipOnFirstSide = chipOnFirstSide;
		boolean hasChipOnSecondSide = chipOnSecondSide;
		int color = turn % 2;
		if (turn < 11) {
			return false;
		}
		if (!chipOnFirstSide || !chipOnSecondSide) {
			if (color == MachinePlayer.BLACK) {
				for (int i = 1; i < 7; i++) {
					if (this.boardLocations[0][i] != 0) {
						hasChipOnFirstSide = true;
					}
					if (this.boardLocations[7][i] != 0) {
						hasChipOnSecondSide = true;
					}
				}
			} else {
				for (int i = 1; i < 7; i++) {
					if (this.boardLocations[i][0] != 0) {
						hasChipOnFirstSide = true;
					}
					if (this.boardLocations[i][7] != 0) {
						hasChipOnSecondSide = true;
					}
				}
			}
			if (!hasChipOnFirstSide || !hasChipOnSecondSide) {
				return false;
			}
		}
		IntegerArrayList visitList = new IntegerArrayList();
		for (int i = 1; i < 7; i++) {
			if (color == MachinePlayer.WHITE) {
				if (boardLocations[0][i] != 0) {
					if (dfs(boardLocations[0][i], visitList, -1)) {
						return true;
					}
				}
			} else {
				if (boardLocations[i][0] != 0) {
					if (dfs(boardLocations[i][0], visitList, -1)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * current visited lastDirection
	 * 
	 */
	public boolean dfs(int current, IntegerArrayList chippy, int direction) {
		Chip cur = getChip(current);
		if (cur.getX() == 7 || cur.getY() == 7) {
			if (chippy.size() >= 6) {
				return true;
			} else {
				return false;
			}
		}
		if (cur.getX() == 0 || cur.getY() == 0) {
			return false;
		}
		int nextNode;
		chippy.add(new Integer(current));
		for (int i = 0; i < 8; i++) {
			if (i == direction) {
				continue;
			}
			nextNode = cur.getConnection(i);
			if (nextNode != 0 && current % 2 != nextNode % 2
					&& !chippy.contains(nextNode)) {
				if (dfs(nextNode, chippy, i)) {
					return true;
				}
			}
		}
		chippy.remove(current);
		return false;
	}

	/**
	 * Gets chip at position (x,y).
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	/**
	 * Chip getChip(int x, int y) { return this.array[x][y]; }
	 */
	boolean shouldAdd(int turn) {
		// return this.blackPieces.size() < 10 || this.whitePieces.size() < 10;
		return turn <= TOTAL_CHIPS;
	}

	/**
	 * Checks if x1,y1 and x2,y2, if the moveKind is Move.STEP, are out of
	 * bounds. Also calls positionIsNull(Move m). Checks if conditions for
	 * add/step moves are met.
	 * 
	 * @param m
	 * @return
	 */
	boolean validMove(Move m, int turn) {
		int color = turn % 2;
		if (this.boardLocations[m.x1][m.y1] != 0) {
			return false;
		}

		if (!inBounds(m.x1, m.y1, color) || hasTwoChips(m, color)) {
			return false;
		}
		if (m.moveKind == Move.STEP && m.x2 != 0 && m.y2 != 0) {
			boolean posDidNotChange = m.x2 == m.x1 && m.y1 == m.y2;
			if (posDidNotChange || !inBounds(m.x2, m.y2, color)
					|| shouldAdd(turn)) {
				return false;
			}
		} else if (m.moveKind == Move.ADD) {
			return shouldAdd(turn);
		}
		return true;
	}

	boolean inBounds(int x, int y, int color) {
		boolean pos1OutBounds = x < 0 || x >= BOARD_SIZE || y < 0
				|| y >= BOARD_SIZE;
		boolean isCorner = (x == 0 && y == 0)
				|| (x == 0 && y == BOARD_SIZE - 1)
				|| (x == BOARD_SIZE - 1 && y == BOARD_SIZE - 1)
				|| (x == BOARD_SIZE - 1 && y == 0);
		boolean isEdge;
		if (color == MachinePlayer.BLACK) {
			isEdge = x == 0 || x == Board.BOARD_SIZE - 1;
		} else {
			isEdge = y == 0 || y == Board.BOARD_SIZE - 1;
		}
		if (pos1OutBounds || isCorner || isEdge) {
			return false;
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
	private boolean hasTwoChips(Move m, int turn) {
		int temp = 0;
		if (m.moveKind == Move.STEP) {
			temp = this.boardLocations[m.x2][m.y2];
			this.boardLocations[m.x2][m.y2] = 0;
		}
		int color = turn % 2;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0 || m.x1 + i >= BOARD_SIZE || m.x1 + i < 0
						|| m.y1 + j >= BOARD_SIZE || m.y1 + j < 0) {
					continue;
				} else {
					int first = this.boardLocations[m.x1 + i][m.y1 + j];
					if (first != 0 && first % 2 == color) {
						for (int x = -1; x < 2; x++) {
							for (int y = -1; y < 2; y++) {
								if ((x == 0 && y == 0)
										|| m.x1 + i + x >= BOARD_SIZE
										|| m.x1 + i + x < 0 || m.y1 + j + y < 0
										|| m.y1 + j + y >= BOARD_SIZE) {
									continue;
								} else {
									int second = this.boardLocations[m.x1 + i
											+ x][m.y1 + j + y];
									if (second != 0 && second % 2 == color) {
										if (m.moveKind == Move.STEP) {
											this.boardLocations[m.x2][m.y2] = temp;
										}
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		if (m.moveKind == Move.STEP) {
			this.boardLocations[m.x2][m.y2] = temp;
		}
		return false;
	}
}
