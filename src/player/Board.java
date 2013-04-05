package player;

import utils.IntegerArrayList;
/**
 * 
 * @author Andrew Liu
 * @author James Jia
 * @author Matthew Miller
 *
 */
public class Board {
	final static int BOARD_SIZE = 8;
	private Chip[] chips;
	private int TOTAL_CHIPS = 20;
	private int[][] boardLocations;

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
			this.boardLocations[m.x1][m.y1] = turn;
			this.chips[turn] = new Chip(m.x1, m.y1, turn % 2, this);

			/**
			 * if (color == MachinePlayer.BLACK) {
			 * this.blackPieces.add(this.array[m.x1][m.y1]); } else {
			 * this.whitePieces.add(this.array[m.x1][m.y1]); }
			 */
		} else if (m.moveKind == Move.STEP) {
			if (this.boardLocations[m.x2][m.y2] == 0
					|| this.chips[this.boardLocations[m.x2][m.y2]].getColor() != turn % 2) {
				return false;
			}
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
		int oppStart = 1;

		if (color == 0) {
			start += 1;
		}
		if (oppStart == 0) {
			oppStart += 1;
		}

		while (start < TOTAL_CHIPS || oppStart < TOTAL_CHIPS) {
			Chip cStart = chips[start];
			Chip cOpp = chips[oppStart];
			int cStartX;
			int cStartY;
			if (cStart != null) {
				cStartX = cStart.getX();
				cStartY = cStart.getY();
			} else {
				cStartX = -1;
				cStartY = -1;
			}
			int cOppX;
			int cOppY;
			if (cOpp != null) {
				cOppX = cOpp.getX();
				cOppY = cOpp.getY();
			} else {
				cOppX = -1;
				cOppY = -1;
			}
			for (Direction d : Direction.values()) {
				int dx = d.getX();
				int dy = d.getY();
				if (cStartX == -1 || (cStartX == 0 && dx != 1)
						|| (cStartX == 7 && dx != -1)
						|| (cStartY == 0 && dy != 1)
						|| (cStartY == 7 && dy != -1)) {
					continue;
				} else {
					evaluation += extend(cStartX, cStartY, dx, dy, color);
				}
				if (cOppX == -1 || (cOppX == 0 && dx != 1)
						|| (cOppX == 7 && dx != -1) || (cOppY == 0 && dy != 1)
						|| (cOppY == 7 && dy != -1)) {
					continue;
				} else {
					evaluation -= extend(cOppX, cOppY, dx, dy, (color + 1) % 2);
				}
			}
			start += 2;
			oppStart += 2;
		}

		if (this.hasChipsInBothGoals(color) == true) {
			evaluation += 2;
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
	 * Returns true if player with specified color has a valid network.
	 * 
	 * @param color
	 * @return
	 */
	boolean isValidNetwork(int color, int length, int prevDir, Chip currChip,
			IntegerArrayList nodesVisited) {
		int currChipNumber = this.getChipNumber(currChip.getX(),
				currChip.getY());
		if (nodesVisited.contains(currChipNumber)) {
			return false;
		} else {
			nodesVisited.add(currChipNumber);
		}
		if (currChip.getX() == 7 || currChip.getY() == 7) {
			// System.out.println("AT END GOAL for COLOR:" + color +
			// " , LENGTH: "
			// + length);
			if (length < 5) {
				return false;
			}
			return true;
		}
		for (Direction d : Direction.values()) {
			if (prevDir == d.getIndex()) {
				continue;
			}
			int neighborChipIndex = currChip.getConnection(d.getIndex());
			if (neighborChipIndex == 0) {
				continue;
			}
			Chip nextChip = this.getChip(neighborChipIndex);
			if (nextChip.getColor() != color || nextChip.getX() == 0
					|| nextChip.getY() == 0) {
				continue;
			}
			boolean valid = isValidNetwork(color, length + 1, d.getIndex(),
					nextChip, new IntegerArrayList(nodesVisited));
			if (valid) {
				return true;
			}
		}
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
		boolean isOtherGoal;
		if (color == MachinePlayer.WHITE) {
			isOtherGoal = y == 0 || y == Board.BOARD_SIZE - 1;
		} else {
			isOtherGoal = x == 0 || x == Board.BOARD_SIZE - 1;
		}
		if (isCornerOrOutOfBounds(x, y, color) || isOtherGoal) {
			return false;
		}
		return true;
	}

	boolean isCornerOrOutOfBounds(int x, int y, int color) {
		boolean pos1OutBounds = x < 0 || x >= BOARD_SIZE || y < 0
				|| y >= BOARD_SIZE;
		boolean isCorner = (x == 0 && y == 0)
				|| (x == 0 && y == BOARD_SIZE - 1)
				|| (x == BOARD_SIZE - 1 && y == BOARD_SIZE - 1)
				|| (x == BOARD_SIZE - 1 && y == 0);
		return pos1OutBounds || isCorner;
	}

	boolean hasChipsInBothGoals(int color) {
		boolean hasChipOnFirstSide = false;
		boolean hasChipOnSecondSide = false;
		if (color == MachinePlayer.WHITE) {
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
		return hasChipOnFirstSide && hasChipOnSecondSide;
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
		int stepCurrLoc = 0;
		int numSurrounding = 0;
		int color = turn % 2;
		if (m.moveKind == Move.STEP) {
			stepCurrLoc = this.boardLocations[m.x2][m.y2];
			this.boardLocations[m.x2][m.y2] = 0;
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i == 0 && j == 0)
						|| isCornerOrOutOfBounds(m.x1 + i, m.y1 + j, color)) {
					continue;
				} else {
					int first = this.boardLocations[m.x1 + i][m.y1 + j];
					if (first != 0 && first % 2 == color) {
						numSurrounding += 1;
						if (numSurrounding >= 2) {
							if (m.moveKind == Move.STEP) {
								this.boardLocations[m.x2][m.y2] = stepCurrLoc;
							}
							return true;
						}
						for (int x = -1; x < 2; x++) {
							for (int y = -1; y < 2; y++) {
								if (!(x == 0 && y == 0)
										&& !isCornerOrOutOfBounds(m.x1 + i + x,
												m.y1 + j + y, color)) {
									int second = this.boardLocations[m.x1 + i
											+ x][m.y1 + j + y];
									if (second != 0 && second % 2 == color) {
										if (m.moveKind == Move.STEP) {
											this.boardLocations[m.x2][m.y2] = stepCurrLoc;
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
			this.boardLocations[m.x2][m.y2] = stepCurrLoc;
		}
		return false;
	}
	
	public String toString(){
		String str = "";
		for (int i =0; i<BOARD_SIZE; i++){
			str= str + "Row i: " + i + " ";
			for (int j = 0; j<BOARD_SIZE; j++){
				if (this.boardLocations[j][i]==0){
					str = str + "X";
					continue;
				}
				if (this.boardLocations[j][i]%2==1){
					str = str + "W";
				}
				else{
					str=str + "B";
				}
				
			}
			str += "\n";
			
		}
		return str;
	}
}
