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

	/**
	 * Initializes a 2D byte array of BOARD_SIZE.
	 */
	public Board() {
		board = new byte[BOARD_SIZE][BOARD_SIZE];
	}

	/**
	 * Updates the four corners to keep track of the number of pieces in a
	 * particular goal zone. Top left corner represents left goal of white. Top
	 * right corner represents top goal of black. Bottom left corner represents
	 * right goal of white. Bottom right corner represents bottom goal of black.
	 * 
	 * @param turn
	 *            current turn
	 * @param x
	 *            x coordinate of a position
	 * @param y
	 *            y coordinate of a position
	 * @param adder
	 *            Positive if a chip is being added to a goal, negative if a
	 *            chip is being removed from a goal
	 */
	private void adjustChecks(byte turn, byte x, byte y, byte adder) {
		byte color = (byte) (turn % 2);
		if ((x == 0 || x == BOARD_GOAL) && color == MachinePlayer.WHITE) {
			board[0][x] += adder;
		} else if ((y == 0 || y == BOARD_GOAL) && color == MachinePlayer.BLACK) {
			board[BOARD_GOAL][y] += adder;
		}
	}

	/**
	 * Grabs the value at a particular (x,y) location. Returns 0 if there is no
	 * chip there, or the index of the chip at that location otherwise.
	 * 
	 * @param x
	 *            x coordinate of a position
	 * @param y
	 *            y coordinate of a position
	 * @return the value at a particular position in the board
	 */
	protected byte fetchValue(byte x, byte y) {
		return board[x][y];
	}

	/**
	 * Updates the board based on the move that is passed in at a specified
	 * turn.
	 * 
	 * @param m
	 *            the move to be added
	 * @param turn
	 *            current turn in the game
	 */
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

	/**
	 * Updates the board by removing the change specified by the move passed in
	 * at a specific turn.
	 * 
	 * @param m
	 *            the move that was added to the board
	 * @param turn
	 *            the current turn in the game
	 */
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

	/**
	 * Heuristic function that returns an integer that determines how
	 * advantageous the board is for a particular player.
	 * 
	 * @param color
	 *            The color of the player. If color is odd, the method evaluates
	 *            the board for black. If color is even, the method evaluates
	 *            the board for white.
	 * @return an integer that determines how advantageous the board is for a
	 *         player. A greater integer means it is more advantageous for the
	 *         player of specified color.
	 */
	protected int eval(byte color) {
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
		if (color % 2 == 0) {
			evaluation -= Math.abs(corner3 - 1.5) + Math.abs(corner4 - 1.5);
			evaluation += Math.abs(corner1 - 1.5) + Math.abs(corner2 - 1.5);
		} else {
			evaluation -= Math.abs(corner1 - 1.5) + Math.abs(corner2 - 1.5);
			evaluation += Math.abs(corner3 - 1.5) + Math.abs(corner4 - 1.5);
		}
		for (byte i = 0; i < BOARD_SIZE; i++) {
			for (byte j = 0; j < BOARD_SIZE; j++) {
				if (board[i][j] != 0) {
					if (board[i][j] % 2 == color % 2) {
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

	/**
	 * Cycles through the end zones of a particular player and calls
	 * isValidNetworkRecurse to determine whether there is a valid network that
	 * stems from a particular chip in the end zone.
	 * 
	 * @param color
	 *            the color of the player that the method is finding a network
	 *            for
	 * @return true if there is a valid network for specified color
	 */
	protected boolean isValidNetwork(byte color) {
		boolean[] nodesVisited = new boolean[MAX_CHIPS / 2];
		if (color == MachinePlayer.WHITE && board[0][0] > 0
				&& board[0][BOARD_GOAL] > 0) {
			for (byte i = 1; i < BOARD_GOAL; i++) {
				if (board[0][i] != 0) {
					nodesVisited[(board[0][i] - 1) / 2] = true;
					if (isValidNetworkRecurse(color, (byte) 0, (byte) -1,
							(byte) 0, i, nodesVisited)) {
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

	/**
	 * Recurses through all the connections from a specified position and
	 * determines whether a network has been made.
	 * 
	 * @param color
	 *            the color of the player that the method is finding a network
	 *            for
	 * @param length
	 *            the current length of connections
	 * @param prevDir
	 *            the index of previous direction that led up to this position
	 *            (-1 if first node in the connections, so it has no previous
	 *            direction)
	 * @param x
	 *            x coordinate of current position
	 * @param y
	 *            y coordinate of current position
	 * @param nodesVisited
	 *            boolean array of nodes visited. Chip number is hashed into
	 *            array via (chipNumber-1)%2, which uniquely hashes each chip of
	 *            a specific color into the array. The index of the array, thus
	 *            corresponds to that particular chip. If the chip has been
	 *            visited, set the boolean in that index of the array to true.
	 * @return true if there is a valid network for specified color
	 */
	private boolean isValidNetworkRecurse(byte color, byte length,
			byte prevDir, byte x, byte y, boolean[] nodesVisited) {
		byte[] neighborChip = new byte[3];
		boolean valid;
		if (prevDir != -1 && (x == 0 || y == 0))
			return false;
		if (x == BOARD_GOAL || y == BOARD_GOAL) {
			if (length < 5)
				return false;
			return true;
		}
		for (Direction d : Direction.values()) {
			if (prevDir == d.getIndex())
				continue;
			neighborChip = extend((byte) (x + d.getX()), (byte) (y + d.getY()),
					d.getX(), d.getY(), color);
			if (neighborChip[0] == 0 || neighborChip[0] % 2 != color
					|| (neighborChip[1] == 0 && neighborChip[2] == 0))
				continue;
			if (!nodesVisited[(neighborChip[0] - 1) / 2]) {
				nodesVisited[(neighborChip[0] - 1) / 2] = true;
				valid = isValidNetworkRecurse(color, (byte) (length + 1),
						d.getIndex(), neighborChip[1], neighborChip[2],
						nodesVisited);
				nodesVisited[(neighborChip[0] - 1) / 2] = false;
				if (valid) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Given a particular position and a direction coordinate, iterates in that
	 * direction until out of bounds or "hits" a chip. If chip found is of the
	 * same color, return a byte array with information about the chip,
	 * otherwise return a byte array of all 0s.
	 * 
	 * @param x
	 *            x coordinate of the position
	 * @param y
	 *            y coordinate of the position
	 * @param dx
	 *            x coordinate of direction
	 * @param dy
	 *            y coordinate of direction
	 * @param color
	 *            color of the current chip at position x,y
	 * @return a byte array where first index is the index of the chip, second
	 *         is x coordinate of chip, third is y coordinate of chip
	 */
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

	/**
	 * Checks whether or not a theoretical chip of a specified color and (x,y)
	 * position is within bounds.
	 * 
	 * @param x
	 *            x coordinate of position
	 * @param y
	 *            y coordinate of position
	 * @param color
	 *            color of the theoretical chip
	 * @return true if position is in the boundaries.
	 */
	private boolean inBounds(byte x, byte y, byte color) {
		boolean isOtherGoal;
		if (color == MachinePlayer.WHITE) {
			isOtherGoal = y == 0 || y == Board.BOARD_SIZE - 1;
		} else {
			isOtherGoal = x == 0 || x == Board.BOARD_SIZE - 1;
		}
		return !(isCornerOutOfBounds(x, y, color) || isOtherGoal);
	}

	/**
	 * Checks whether a (x,y) position is out of bounds for a certain colored
	 * Chip or if it is a corner position on the Board
	 * 
	 * @param x
	 *            x coordinate of the chip
	 * @param y
	 *            y coordinate of the chip
	 * @param color
	 *            color of the chip
	 * @return true if position is in the corner or out of bounds
	 */
	boolean isCornerOutOfBounds(byte x, byte y, byte color) {
		boolean pos1OutBounds = x < 0 || x >= BOARD_SIZE || y < 0
				|| y >= BOARD_SIZE;
		boolean isCorner = (x == 0 && y == 0) || (x == 0 && y == BOARD_GOAL)
				|| (x == BOARD_GOAL && y == BOARD_GOAL)
				|| (x == BOARD_GOAL && y == 0);
		return pos1OutBounds || isCorner;
	}

	/**
	 * Checks if a given Move m is valid to make on the Board on a specified
	 * turn. Rules for valid moves are specified in the project instructions.
	 * 
	 * @param m
	 *            move to be checked if valid
	 * @param turn
	 *            current turn of the game
	 * @return true if the move is valid
	 */
	boolean validMove(Move m, byte turn) {
		byte color = (byte) (turn % 2);
		boolean posDidNotChange;
		if (board[m.x1][m.y1] != 0
				|| !inBounds((byte) m.x1, (byte) m.y1, color)
				|| hasTwoChips(m, color)) {
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
			return shouldAdd(turn);
		}
		return true;
	}

	/**
	 * Checks if the players should create add or step moves.
	 * 
	 * @param turn
	 *            current turn of the game
	 * @return true if players should still construct add moves.
	 */
	protected Boolean shouldAdd(byte turn) {
		return turn <= MAX_CHIPS;
	}

	/**
	 * Returns a stringified, readable state of the board.
	 * 
	 * @return string of the board
	 */
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

	/**
	 * Checks to see if placing a chip at specified location would result in
	 * three chips in a row.
	 * 
	 * @param m
	 *            move to be placed
	 * @param color
	 *            color of the player placing the move
	 * @return true if placing a move would result in a three in a row.
	 */
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
	}

	/**
	 * Recurses through a particular position to determine whether there are
	 * chips neighboring that position.
	 * 
	 * @param x
	 *            x coordinate of the position
	 * @param y
	 *            y coordinate of the position
	 * @param color
	 *            color of the player placing the move
	 * @param depth
	 *            how many iterations left to recurse.
	 * @return true if placing a chip would result in a three in a row
	 */
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

}
