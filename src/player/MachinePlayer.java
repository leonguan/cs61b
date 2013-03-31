/* MachinePlayer.java */

package player;

import utils.ChipArrayList;
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
	// either 0 (black) or 1 (white). (White has the first move.)can y
	public MachinePlayer(int color, int searchDepth) {
		this.color = color;
		this.searchDepth = searchDepth;
		this.board = new Board();
	}

	// Returns a new move by "this" player. Internally records the move (updates
	// the internal game board) as a move by "this" player.
	public Move chooseMove() {
		// TODO update gameBoard & make move.
		Best move = chooseMove(this.turn, -1, 1, this.searchDepth);
		Move m = move.m;
		this.board = new Board(this.board, m, this.turn);
		this.turn++;
		return m;
	}

	public Best chooseMove(int turn, double alpha, double beta, int depth) {
		Best myBest = new Best();
		Best reply;
		int side = turn % 2;
		if (this.board.isValidNetwork(turn, false, false)) {
			if (side == this.color) {
				myBest.score = 100;
			} else {
				myBest.score = -100;
			}
			return myBest;
		}
		if (depth == 0) {
			myBest.score = this.board.eval(side);
			System.out.println("SCORE: " + myBest.score);
			return myBest;
		}
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
				reply = chooseMove(turn + 1, alpha, beta, depth - 1);
				this.board = currBoard;
				if (side == this.color && (reply.score > myBest.score)) {
					myBest.m = m;
					myBest.score = reply.score;
					alpha = reply.score;
				} else if (side == (this.color + 1) % 2
						&& (reply.score <= myBest.score)) {
					myBest.m = m;
					myBest.score = reply.score;
					beta = reply.score;
				}
			} else {
				i++;
				continue;
			}

			if (alpha >= beta) {
				return myBest;
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
		int oppColor = (this.color + 1) % 2;
		boolean b = this.board.addMove(m, oppColor);

		this.turn++;

		return b;
	}

	// If the Move m is legal, records the move as a move by "this" player
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method is used to help set up "Network problems" for your
	// player to solve.
	public boolean forceMove(Move m) {
		return this.board.addMove(m, this.color);
	}

	private MoveArrayList getMoves(int side) {
		MoveArrayList list = new MoveArrayList();
		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {
				if (this.board.shouldAdd(this.turn)) {
					System.out.println("Add");
					Move m = new Move();
					m.x1 = i;
					m.y1 = j;
					m.moveKind = Move.ADD;
					list.add(m);
				} else {
					System.out.println("step");
					int iter = side;
					/**
					 * if (side == MachinePlayer.BLACK) { chips =
					 * this.board.blackPieces(); } else { chips =
					 * this.board.whitePieces(); }
					 */
					while (iter < this.board.getTotalChips()) {
						Chip temp = this.board.getChip((iter + 1) % 2); // TODO
																		// SKETCH
						Move m = new Move();
						m.x1 = i;
						m.y1 = j;
						m.moveKind = Move.STEP;
						m.x2 = temp.x;
						m.y2 = temp.y;
						list.add(m);
						iter++;
					}

				}
			}
		}

		int temp = 0;
		return list;
	}
}