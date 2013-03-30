package player;

import utils.ChipArrayList;

public class Board {
	final static int BOARD_SIZE = 8;
	// Don't need anymore because we have chips. Will just set to null
	// final static int NONE = null;

	private Chip[][] array;
	
	// Changed the representation of simply keeping track of count
	// To keeping track of the actual pieces on the board
	// private int blackPieces;
	private ChipArrayList whitePieces;
	private ChipArrayList blackPieces;

	public Board() {
		this.array = new Chip[BOARD_SIZE][BOARD_SIZE];
		this.whitePieces = new ChipArrayList();
		this.blackPieces = new ChipArrayList();
	}
	
	// Given a board and a move, produces a new board 
	public Board(Board b, Move m, int color) {
		this.array = new Chip[BOARD_SIZE][BOARD_SIZE];
		
		// Needs a new ChipArrayList to keep track of the black and white pieces on this board
		this.whitePieces = new ChipArrayList();
		this.blackPieces = new ChipArrayList();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				// Each board needs distinctly new Chips, not ones from previous boards
				this.array[i][j] = new Chip(b.array[i][j].x, b.array[i][j].y, b.array[i][j].color, this);
				if (this.array[i][j].color == MachinePlayer.BLACK) {
					this.blackPieces.add(this.array[i][j]);
				}
				else {
					this.whitePieces.add(this.array[i][j]);
				}
			}
		}
		this.addMove(m, color);
	}
	
	ChipArrayList whitePieces() {
		return this.whitePieces;
	}
	
	ChipArrayList blackPieces() {
		return this.blackPieces;
	}
	
	Chip[][] board() {
		return this.array;
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
			this.array[m.x1][m.y1] = new Chip(m.x1, m.y1, color, this);
			if (color == MachinePlayer.BLACK) {
				this.blackPieces.add(this.array[m.x1][m.y1]);
			} else {
				this.whitePieces.add(this.array[m.x1][m.y1]);
			}
			

		} else if (m.moveKind == Move.STEP) {
			if (this.array[m.x2][m.y2] == null
					|| this.array[m.x2][m.y2].color != color) {
				return false;
			}
			// Does this work?
			this.array[m.x1][m.x1] = this.array[m.x2][m.y2];
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
	 *  NOTE I THINK IT"S NECESSARY TO IMPLEMENT AN ARRAYLIST FOR THIS METHOD.
	 * @param color
	 * @return
	 */
	boolean isValidNetwork(int color, boolean chipOnFirstSide,
			boolean chipOnSecondSide) {
		boolean hasChipOnFirstSide = chipOnFirstSide;
		boolean hasChipOnSecondSide = chipOnSecondSide;
		if (!chipOnFirstSide || !chipOnSecondSide) {
			if (color == MachinePlayer.BLACK) {
				for (int i = 1; i < 7; i++) {
					if (this.array[0][i] != null){
						hasChipOnFirstSide = true;
					}
					if (this.array[7][i] != null){
						hasChipOnSecondSide = true;
					}
				}
			}
			else{
				for (int i = 1; i < 7; i++) {
					if (this.array[i][0] != null){
						hasChipOnFirstSide = true;
					}
					if (this.array[i][7] != null){
						hasChipOnSecondSide = true;
					}
				}
			}
			if (!hasChipOnFirstSide || !hasChipOnSecondSide){
				return false;
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
	Chip getChip(int x, int y) {
		return this.array[x][y];
	}

	boolean shouldAdd() {
		return this.blackPieces.size() < 10 || this.whitePieces.size() < 10;
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
		if (pos1OutBounds || isCorner || isEdge || hasTwoChips(m, color)) {
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
	private boolean hasTwoChips(Move m, int color) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0 || m.x1 + i >= BOARD_SIZE || m.x1 + i < 0
						|| m.y1 + j >= BOARD_SIZE || m.y1 + j < 0) {
					continue;
				} else {
					Chip first = this.array[m.x1 + i][m.y1 + j];
					if (first != null && first.color == color) {
						for (int x = -1; x < 2; x++) {
							for (int y = -1; y < 2; y++) {
								if (x == 0 && y == 0
										|| m.x1 + i + x >= BOARD_SIZE
										|| m.x1 + i + x < 0 || m.y1 + j + y < 0
										|| m.y1 + j + y >= BOARD_SIZE) {
									continue;
								} else {
									Chip second = this.array[m.x1 + i + x][m.y1
											+ j + y];
									if (second != null && second.color == color) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
