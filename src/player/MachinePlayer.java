/* MachinePlayer.java */

package player;

import utils.IntegerArrayList;
import utils.MoveArrayList;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 */
public class MachinePlayer extends Player {
	final static int BLACK = 0;
	final static int WHITE = 1;
	private int color;
	private int searchDepth;
	private Board board;
	private int turn = 1;

	// Creates a machine player with the given color. Color is either 0 (black)
	// or 1 (white). (White has the first move.)
	public MachinePlayer(int color) {
		this.color = color;
		this.board = new Board();
		this.searchDepth = 3;
	}

	// Creates a machine player with the given color and search depth. Color is
	// either 0 (black) or 1 (white). (White has the first move.)
	public MachinePlayer(int color, int searchDepth) {
		this.color = color;
		if (searchDepth < 1) {
			this.searchDepth = 1;
		} else {
			this.searchDepth = searchDepth;
		}
		this.board = new Board();
	}

	// Returns a new move by "this" player. Internally records the move (updates
	// the internal game board) as a move by "this" player.
	public Move chooseMove() {
		Best move = chooseMove(this.turn, -1000000, 1000000, this.searchDepth);
		Move m = move.m;
		if (m == null) {
			m = new Move();
			m.moveKind = Move.QUIT;
		}
		this.board = new Board(this.board, m, this.turn);
		this.turn++;
		return m;
	}

	/**
	 * Iterates through all possible moves
	 * and uses Minimax search with alpha-beta pruning to determine the
	 * Best move.
	 * @param turn - turn number
	 * @param alpha - alpha value for minimax search algorithm
	 * @param beta - beta value for minimax search algorithm
	 * @param depth - search depth
	 * @return
	 */
	public Best chooseMove(int turn, int alpha, int beta, int depth) {
		Best myBest = new Best();
		Best reply = new Best();
		int side = turn % 2;
		if (side == this.color) {
			myBest.score = alpha;
		} else {
			myBest.score = beta;
		}

		MoveArrayList moves = this.getMoves(side);
		int i = 0;
		while (i < moves.size()) {
			Move m = moves.get(i);
			if (this.board.validMove(m, turn)) {
				Board currBoard = this.board;
				Board afterMove = new Board(this.board, m, turn);
				this.board = afterMove;
				int chipIndex;
				if (side == MachinePlayer.BLACK) {
					chipIndex = 2;
				} else {
					chipIndex = 1;
				}
				if (this.board.hasChipsInBothGoals(side)) {
					while (chipIndex < 21) {
						IntegerArrayList list = new IntegerArrayList();
						Chip currChip = this.board.getChip(chipIndex);
						if (currChip != null
								&& (currChip.getX() == 0 || currChip.getY() == 0)) {
							if (this.board.isValidNetwork(side, 0, -1,
									currChip, list)) {
								if (side == this.color) {
									myBest.score = 100000 - 100 * turn;
								} else {
									myBest.score = -100000 + 100 * turn;
								}
								myBest.m = m;
								return myBest;
							}
						}
						chipIndex += 2;
					}
				}
				if (depth == 1) {
					reply.m = m;
					reply.score = this.board.eval(this.color);
				} else {
					reply = chooseMove(turn + 1, alpha, beta, depth - 1);
				}
				this.board = currBoard;
				if (side == this.color && reply.score > myBest.score) {
					myBest.m = m;
					myBest.score = reply.score;
					alpha = reply.score;
				} else if (side == (this.color + 1) % 2
						&& reply.score < myBest.score) {
					myBest.m = m;
					myBest.score = reply.score;
					beta = reply.score;
				}
				if (alpha >= beta) {
					return myBest;
				}
			}
			i++;
		}
		return myBest;
	}

	// If the Move m is legal, records the move as a move by the opponent
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method allows your opponents to inform you of their moves.
	public boolean opponentMove(Move m) {
		if (this.board.validMove(m, this.turn)) {
			this.board = new Board(this.board, m, this.turn);
			this.turn++;
			return true;
		}
		return false;
	}

	// If the Move m is legal, records the move as a move by "this" player
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method is used to help set up "Network problems" for your
	// player to solve.
	public boolean forceMove(Move m) {
		if (this.board.validMove(m, this.turn)) {
			this.board = new Board(this.board, m, this.turn);
			this.turn++;
			return true;
		}
		return false;
	}

	/**
	 * Returns a MoveArrayList of all possible Moves
	 * @param side - refers to the chip color
	 * @return a MoveArrayList of all possible moves
	 */
	private MoveArrayList getMoves(int side) {
		MoveArrayList list = new MoveArrayList();
		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {
				if (this.board.shouldAdd(this.turn)) {
					Move m = new Move();
					m.x1 = i;
					m.y1 = j;
					m.moveKind = Move.ADD;
					list.add(m);
				} else {
					int iter = side;
					if (iter == 0) {
						iter = 2;
					}
					while (iter < this.board.getTotalChips()) {
						Chip temp = this.board.getChip(iter);
						Move m = new Move();
						m.x1 = i;
						m.y1 = j;
						m.moveKind = Move.STEP;
						m.x2 = temp.getX();
						m.y2 = temp.getY();
						list.add(m);
						iter += 2;
					}

				}
			}
		}
		return list;
	}

	/**
	 * Public accessor that returns the current game board
	 * @return Board object representing the current game board.
	 */
	public Board getBoard() {
		return this.board;
	}
}