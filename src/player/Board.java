package player;

import utils.IntegerArrayList;
/**
 * The Board class represents the 8x8 Board on which the game Networks is played.
 * It contains a 2D int array that represents the 60 different locations on the Board. The corner locations are invalid.
 * It contains a Chip[] that holds the Chips that are on the Board. In the Chip[], index 0 does not hold anything and represents "non-Chips"
 * Afterwards, each even index holds a black Chip and each odd index holds a white Chip.
 * There can be at most 20 distinct Chips on the Board at any one time. There can also be multiple copies of a "non-Chip"
 * which represents the lack of a colored Chip.
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

	/**
	 * A 0-arg constructor that produces a blank board with no chips placed.
	 * All Boards are BOARD_SIZE x BOARD_SIZE and hold a total of TOTAL_CHIPS+1 Chips.
	 */
	public Board() {
		this.chips = new Chip[TOTAL_CHIPS + 1];
		this.boardLocations = new int[BOARD_SIZE][BOARD_SIZE];
	}

	/**This constructor takes in a Board object, a Move object, and the turn number
	 * and returns a new Board object that results in making the specified Move on the specified Board.
	 * Each new Board object contains its own copies of Chips.
	 * @param b - The current board that the new board will be based off of.
	 * @param m - A specified Move to make on the Board
	 * @param turn - the turn number
	 */
	public Board(Board b, Move m, int turn) {
		this();
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
	 * @return The 2D int array that represents the different locations on the current Board object
	 */
	int[][] getLocations() {
		return this.boardLocations;
	}

	/**
	 * Sets the specified x,y location on the current Board to a given value.
	 * @param x - The x coordinate on the current Board object
	 * @param y - The y coordinate on the current Board object
	 * @param value - sets the location on the board to the specified value.
	 */
	void setLocation(int x, int y, int value) {
		this.boardLocations[x][y] = value;
		return;
	}

	/**
	 * @return Chip[] that holds the Chips on the Board object.
	 */
	Chip[] getChips() {
		return this.chips;
	}

	/**
	 * @param i - index of Chip[] we wish to retrieve from.
	 * @return Returns the Chip object at the specified index of the Chip[], this.chips.
	 */
	Chip getChip(int i) {
		return this.chips[i];
	}

	/**
	 * Retrieves the Chip at a certain location on the Board
	 * @param x - x location on Board
	 * @param y - y location on Board
	 * @return Returns the chip located at (x,y) on the Board
	 */
	int getChipNumber(int x, int y) {
		return this.boardLocations[x][y];
	}

	/**
	 * @return the total number of Chips possible on the Board.
	 */
	int getTotalChips() {
		return this.TOTAL_CHIPS;
	}

	/**
	 * Updates the game board if the move is valid.
	 * 
	 * @param m - move to be added
	 * @param color - color of piece to be added
	 * @return bool - returns true if a move was not made.
	 */
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
	 * LEAVING THIS UP TO JAMES JIA
	 * BRIEFLY EXPLAIN HOW IT EVALUATES THE BOARD
	 * @param color - the color corresponding to which side the board is being evaluated for
	 * @return - an integer representing a score for the Board. The higher the evaluation score
	 * for a given color, the better the position that side is in.
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

	/**
	 * From a given (x,y) location on the board, it searches in a given direction whose slope is (my/mx)
	 * and returns a score for the part of the board that it searched.
	 * Stops once it reaches the outer bounds of the Board.
	 * @param x - x coordinate on the Board
	 * @param y - y coordinate on the Board
	 * @param mx - change in x for every iteration
	 * @param my - change in y for every iteration
	 * @param color - the color/team to evaluate the searched area for
	 * @return - an integer representing how good of a position the color-side is in based on the searched area.
	 */
	int extend(int x, int y, int mx, int my, int color) {
		int i = 1;
		int total = 0;
		while (true) {
			if (inBounds(x + mx * i, y + my * i, color)) {
				if ((color == 1 && (x + mx * i == 0 || x + mx * i == 7))
						|| (color == 0 && (y + my * i == 0 || y + my * i == 7))) {
					total += 1;
					if (boardLocations[x + mx * i][y + my * i] != 0) {
						if (boardLocations[x + mx * i][y + my * i] % 2 == color) {
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
					
					total += 2;
				}
				return total;
			}
			i += 1;
		}
	}

	/**
	 * Returns true if player with specified color has a valid network.
	 * JAMES JIA - CAN YOU EXPLAIN HOW IT DOES THIS?
	 * Something about starting at a chip from the goal area.
	 * Then checks through all possible connections
	 * Adds the index of Chip in the Board's Chip[] to the IntegerArrayList of nodesVisited
	 * Makes a recursive call on the new chip
	 * @param color - the color of team we want to examine to determine whether or not they have a network
	 * @param length - the length of the current series of connections
	 * @param prevDir - a number denoting the direction of the previous Chip in the connection.
	 * 0 refers to upwards/North, 1 refers to upwards and to the right/Northeast, 2 refers to right/East, etc...
	 * @param currChip - the current chip we are working with in the connection so far
	 * @param nodesVisited - an IntegerArrayList that hold the indices of all of the Chips in the connection so far.
	 * The indices correspond to the indices of the Chip in the Board's Chip array chips. 
	 * @return True iff the Board contains a network of Chips of the given color
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
	 * Checks whether or not 20 turns have passed or not. During the first 20 turns, Chips
	 * may only be added. Thus, if the turn exceeds the total number of chips allowed,
	 * Chips cannot be added.
	 * @param turn - the current turn in the game.
	 * @return true iff Chips can be added to the Board
	 */
	boolean shouldAdd(int turn) {
		return turn <= TOTAL_CHIPS;
	}

	/**
	 * Checks if a given Move m is valid to make on the Board on a specified turn.
	 * Rules for valid moves are specified in the project instructions.
	 * @param m - Move to be made
	 * @param turn - turn number
	 * @return true iff a move is valid.
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

	/**
	 * Checks whether or not a theoretical chip of a specified color and (x,y) position is within bounds.
	 * @param x - x position on the board
	 * @param y - y position on the board
	 * @param color - color of the would-be chip
	 * @return true iff the location is within bounds for a specified color as defined by the rules.s
	 */
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

	/**
	 * 
	 * @param x
	 * @param y
	 * @param color
	 * @return
	 */
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
