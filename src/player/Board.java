package player;

public class Board {
	final static int BOARD_SIZE = 8;
	final static int BLACK = 0;
	final static int WHITE = 1;
	final static int NONE = -1;

	private int[][] array;
	private int blackPieces;
	private int whitePieces;

	public Board() {
		this.array = new int[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				this.array[i][j] = -1;
			}
		}
		this.blackPieces = 0;
		this.whitePieces = 0;
	}

	public Board(Board b, Move m, int color) {
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
			if (color == Board.BLACK) {
				this.blackPieces += 1;
			} else {
				this.whitePieces += 1;
			}
			this.array[m.x1][m.y1] = color;

		} else if (m.moveKind == Move.STEP) {
			if (this.array[m.x2][m.y2] != color) {
				return false;
			}
			this.array[m.x1][m.x1] = color;
			this.array[m.x2][m.y2] = Board.NONE;
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
		return this.array[m.x1][m.y1] == Board.NONE;
	}

	/**
	 * Checks if x1,y1 and x2,y2, if the moveKind is Move.STEP, are out of
	 * bounds. Also calls positionIsNull(Move m). Checks if conditions for add/step moves are met.
	 * 
	 * @param m
	 * @return
	 */
	private boolean validMove(Move m, int color) {
		if (!positionIsNull(m)) {
			return false;
		}
		if (m.x1 < 0 || m.x1 >= BOARD_SIZE) {
			return false;
		}
		if (m.moveKind == Move.STEP) {
			boolean validStep = this.blackPieces == 10
					&& this.whitePieces == 10;
			if (m.x2 < 0 || m.x2 >= BOARD_SIZE || !validStep) {
				return false;
			}
		}
		else if (m.moveKind == Move.ADD){
			boolean validAdd = this.blackPieces <10 && this.whitePieces <10;
			return validAdd;
		}
		return true;
	}

}
