package player;

/**
 * The Board class represents the 8x8 Board on which the game Networks is
 * played. It contains a 2D Byte array that represents the 60 different
 * locations on the Board. The corner locations are invalid, and are used to
 * keep track of the number of pieces in a particular goal zone. Top left corner
 * represents left goal of white. Top right corner represents top goal of black.
 * Bottom left corner represents right goal of white. Bottom right corner
 * represents bottom goal of black.
 * 
 * @author James Jia
 * @author Matt Miller
 * @author Andrew Liu
 * 
 */
public class Board {
	final static byte BOARD_SIZE = 8;
	final static byte BOARD_GOAL = 7;
	final static byte MAX_CHIPS = 20;
	private byte[][] board;

	public Board() {
		board = new byte[BOARD_SIZE][BOARD_SIZE];
	}

	private void adjustChecks(byte turn, byte x, byte y, byte adder) {
		byte color = (byte) (turn % 2);
		if ((x == 0 || x == BOARD_GOAL) && color == MachinePlayer.WHITE) {
			board[0][x] += adder;
		} else if ((y == 0 || y == BOARD_GOAL) && color == MachinePlayer.BLACK) {
			board[BOARD_GOAL][y] += adder;
		}
	}

	protected byte fetchValue(byte x, byte y) {
		return board[x][y];
	}

	protected void addMove(Move m, byte turn) {
		if (m.moveKind == Move.QUIT || !validMove(m, turn)) {
			return;
		}
		if (shouldAdd(turn)) {
			board[m.x1][m.y1] = turn;
			adjustChecks(turn, (byte) m.x1, (byte) m.y1, (byte) 1);
		} else {
			board[m.x1][m.y1] = board[m.x2][m.y2];
			board[m.x2][m.y2] = 0;
			adjustChecks(turn, (byte) m.x1, (byte) m.y1, (byte) 1);
			adjustChecks(turn, (byte) m.x2, (byte) m.y2, (byte) -1);
		}
	}

	protected void removeMove(Move m, byte turn) {
		if (m.moveKind == Move.QUIT)
			return;
		if (m.moveKind == Move.ADD) {
			board[m.x1][m.y1] = 0;
			adjustChecks(turn, (byte) m.x1, (byte) m.y1, (byte) -1);
		} else {
			board[m.x2][m.y2] = board[m.x1][m.y1];
			board[m.x1][m.y1] = 0;
			adjustChecks(turn, (byte) m.x1, (byte) m.y1, (byte) -1);
			adjustChecks(turn, (byte) m.x2, (byte) m.y2, (byte) 1);
		}
		return;
	}

	protected int eval(byte turn) {
		int evaluation = 0;
		// loop unrolling is faster because it removes pointer arithmetic.
		byte corner1 = board[0][0];
		byte corner2 = board[0][BOARD_GOAL];
		byte corner3 = board[BOARD_GOAL][0];
		byte corner4 = board[BOARD_GOAL][BOARD_GOAL];
		board[0][0] = 0;
		board[0][BOARD_GOAL] = 0;
		board[BOARD_GOAL][0] = 0;
		board[BOARD_GOAL][BOARD_GOAL] = 0;
		if (turn % 2 == 0) {
			evaluation -= Math.abs(corner3 - 1.5) + Math.abs(corner4 - 1.5);
			evaluation += Math.abs(corner1 - 1.5) + Math.abs(corner2 - 1.5);
		} else {
			evaluation -= Math.abs(corner1 - 1.5) + Math.abs(corner2 - 1.5);
			evaluation += Math.abs(corner3 - 1.5) + Math.abs(corner4 - 1.5);
		}
		for (byte i = 0; i < BOARD_SIZE; i++) {
			for (byte j = 0; j < BOARD_SIZE; j++) {
				if (board[i][j] != 0) {
					if (board[i][j] % 2 == turn % 2) {
						int temp = (int) (Math.abs(i - 3.5) + Math.abs(j - 3.5));
						evaluation = evaluation - temp;
					} else {
						evaluation += Math.abs(i - 3.5) + Math.abs(j - 3.5);
					}
				}
			}
		}

		board[0][0] = corner1;
		board[0][BOARD_GOAL] = corner2;
		board[BOARD_GOAL][0] = corner3;
		board[BOARD_GOAL][BOARD_GOAL] = corner4;
		return evaluation;
	}

	// private checkTriangles(byte x, byte y){
	//
	// }

	protected boolean isValidNetwork(byte color) {
		boolean[] nodesVisited = new boolean[MAX_CHIPS / 2];
		if (color == MachinePlayer.WHITE && board[0][0] > 0
				&& board[0][BOARD_GOAL] > 0) {
			// System.out.println("WHITE");
			for (byte i = 1; i < BOARD_GOAL; i++) {
				if (board[0][i] != 0) {
					// System.out.println("STARTING NODE");
					nodesVisited[(board[0][i] - 1) / 2] = true;
					if (isValidNetworkRecurse(color, (byte) 0, (byte) -1,
							(byte) 0, i, nodesVisited)) {
						// System.out.println("Found Network");
						return true;
					}
					nodesVisited[(board[0][i] - 1) / 2] = false;
				}
			}
		} else if (board[BOARD_GOAL][0] > 0
				&& board[BOARD_GOAL][BOARD_GOAL] > 0) {
			for (byte i = 1; i < BOARD_GOAL; i++) {
				if (board[i][0] != 0) {
					nodesVisited[(board[i][0] - 1) / 2] = true;
					if (isValidNetworkRecurse(color, (byte) 0, (byte) -1, i,
							(byte) 0, nodesVisited)) {
						return true;
					}
					nodesVisited[(board[i][0] - 1) / 2] = false;
				}
			}
		}
		return false;
	}

	private boolean isValidNetworkRecurse(byte color, byte length,
			byte prevDir, byte x, byte y, boolean[] nodesVisited) {
		// System.out.println("At X: " + x + " Y: " + y + " Length: " + length);
		byte[] neighborChip = new byte[3];
		boolean valid;
		if (prevDir != -1 && (x == 0 || y == 0))
			return false;
		if (x == BOARD_GOAL || y == BOARD_GOAL) {
			if (length < 5)
				return false;
			return true;
		}
		// System.out.println("Not in end zone");
		for (Direction d : Direction.values()) {
			// System.out.println("DIRECTION: " + d.getIndex());
			if (prevDir == d.getIndex())
				continue;
			neighborChip = extend((byte) (x + d.getX()), (byte) (y + d.getY()),
					d.getX(), d.getY(), color);
			// System.out.println("NeighborChip Value: " + neighborChip[0]
			// + " X: " + neighborChip[1] + " Y: " + neighborChip[2]);
			if (neighborChip[0] == 0 || neighborChip[0] % 2 != color
					|| (neighborChip[1] == 0 && neighborChip[2] == 0))
				continue;
			// System.out.println("Found neighbor");
			if (!nodesVisited[(neighborChip[0] - 1) / 2]) {
				nodesVisited[(neighborChip[0] - 1) / 2] = true;
				valid = isValidNetworkRecurse(color, (byte) (length + 1),
						d.getIndex(), neighborChip[1], neighborChip[2],
						nodesVisited);
				nodesVisited[(neighborChip[0] - 1) / 2] = false;
				if (valid) {
					// System.out.println("YOU WIN");
					return true;
				}
			}
		}
		return false;
	}

	private byte[] extend(byte x, byte y, byte dx, byte dy, byte color) {
		while (inBounds(x, y, color)) {
			if (board[x][y] != 0) {
				return new byte[] { board[x][y], (byte) x, (byte) y };
			}
			x += dx;
			y += dy;
		}
		return new byte[] { 0, 0, 0 };
	}

	private boolean inBounds(byte x, byte y, byte color) {
		boolean isOtherGoal;
		if (color == MachinePlayer.WHITE)
			isOtherGoal = y == 0 || y == Board.BOARD_SIZE - 1;
		else
			isOtherGoal = x == 0 || x == Board.BOARD_SIZE - 1;
		return !(isCornerOutOfBounds(x, y, color) || isOtherGoal);
	}

	boolean isCornerOutOfBounds(byte x, byte y, byte color) {
		boolean pos1OutBounds = x < 0 || x >= BOARD_SIZE || y < 0
				|| y >= BOARD_SIZE;
		boolean isCorner = (x == 0 && y == 0) || (x == 0 && y == BOARD_GOAL)
				|| (x == BOARD_GOAL && y == BOARD_GOAL)
				|| (x == BOARD_GOAL && y == 0);
		return pos1OutBounds || isCorner;
	}

	boolean validMove(Move m, byte turn) {
		byte color = (byte) (turn % 2);
		boolean posDidNotChange;
		if (board[m.x1][m.y1] != 0
				|| !inBounds((byte) m.x1, (byte) m.y1, color)
				|| hasTwoChips(m, color)) {
			// System.out.println("Target Square is occupied, not in bounds, or would cause 3 chips");
			return false;
		}
		if (m.moveKind == Move.STEP) {
			if (board[m.x2][m.y2] == 0) {
				return false;
			}
			posDidNotChange = m.x2 == m.x1 && m.y1 == m.y2;
			if (posDidNotChange || !inBounds((byte) m.x2, (byte) m.y2, color)
					|| shouldAdd(turn)) {
				return false;
			}
		} else {
			// System.out.println("Not add step");
			return shouldAdd(turn);
		}
		return true;
	}

	protected Boolean shouldAdd(byte turn) {
		return turn <= MAX_CHIPS;
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < BOARD_SIZE; i++) {
			str = str + "Row i: " + i + " ";
			for (int j = 0; j < BOARD_SIZE; j++) {
				if ((i == 0 && j == 0) || (j == BOARD_GOAL && i == 0)
						|| (j == BOARD_GOAL && i == BOARD_GOAL)
						|| (i == BOARD_GOAL && j == 0)) {
					str = str + this.board[j][i];
					continue;
				}

				if (this.board[j][i] == 0) {
					str = str + "X";
					continue;
				}
				if (this.board[j][i] % 2 == 1) {
					str = str + "W";
				} else {
					str = str + "B";
				}

			}
			str += "\n";

		}
		return str;
	}

	private boolean hasTwoChipsRecurse(byte x, byte y, byte color, byte depth) {
		byte curChip;
		byte numSurrounding = 0;
		for (byte i = -1; i < 2; i++) {
			for (byte j = -1; j < 2; j++) {
				if (!(i == 0 && j == 0)
						&& !isCornerOutOfBounds((byte) (x + i), (byte) (y + j),
								color)) {
					curChip = board[x + i][y + j];
					if (curChip != 0 && curChip % 2 == color) {
						numSurrounding += 1;
						if (numSurrounding >= 2) {
							return true;
						}
						if (depth != 0) {
							if (hasTwoChipsRecurse((byte) (x + i),
									(byte) (y + j), color, (byte) (depth - 1)))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean hasTwoChips(Move m, byte color) {
		byte stepCurrLoc = 0;
		boolean result = false;
		if (m.moveKind == Move.STEP) {
			stepCurrLoc = board[m.x2][m.y2];
			board[m.x2][m.y2] = 0;
		}
		board[m.x1][m.y1] = (byte) (color + 2);
		if (hasTwoChipsRecurse((byte) m.x1, (byte) m.y1, color, (byte) 1))
			result = true;
		board[m.x1][m.y1] = 0;
		if (m.moveKind == Move.STEP)
			board[m.x2][m.y2] = stepCurrLoc;
		return result;

		/**
		 * for(byte i = -1; i<2; i++){ for(byte j = -1; j < 2; j++){ if(!(i == 0
		 * && j == 0) && isCornerOutOfBounds((byte)(m.x1+i), (byte)(m.y1+j),
		 * color)){ curChip = board[m.x1+i][m.y1+j]; if(curChip != 0 &&
		 * curChip%2 == color){ numSurrounding += 1; if(numSurrounding >= 2){
		 * if(m.moveKind == Move.STEP) board[m.x2][m.y2]= stepCurrLoc; return
		 * true; } for(byte x = -1; x < 2; x++){ for(byte y = -1; y < 2; y++){
		 * if (!(x == 0 && y == 0) && !isCornerOutOfBounds((byte)(m.x1 + i +
		 * x),(byte)(m.y1 + j + y), color)) { curChip = board[m.x1 + i + x][m.y1
		 * + j + y]; if (curChip != 0 && curChip % 2 == color) { if (m.moveKind
		 * == Move.STEP) board[m.x2][m.y2] = stepCurrLoc; return true; } } } } }
		 * } } }
		 */
	}
}
