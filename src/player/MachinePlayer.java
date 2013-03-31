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
		Best move = chooseMove(this.color, -1, 1, this.searchDepth);
		Move m = move.m;
		System.out.println(move.toString());
		this.board = new Board(this.board, m, this.color);
		return m;
	}

	public Best chooseMove(int side, double alpha, double beta, int depth) {
		Best myBest = new Best();
		Best reply;
		if (this.board.isValidNetwork(side, false, false)) {
			if (side == this.color) {
				myBest.score = 1;
			} else {
				myBest.score = -1;
			}
			return myBest;
		}
		if (depth == 0) {
			myBest.score = this.board.eval(side);
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
			System.out.println(m);
			if (this.board.validMove(m, side)) {
				Board currBoard = this.board;
				Board afterMove = new Board(this.board, m, side);
				System.out.println("asdf: " + afterMove);
				this.board = afterMove;
				reply = chooseMove((side + 1) % 2, alpha, beta, depth - 1);
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
		return this.board.addMove(m, oppColor);
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
				if (this.board.shouldAdd()) {
					System.out.println("Add");
					Move m = new Move();
					m.x1 = i;
					m.y1 = j;
					m.moveKind = Move.ADD;
					System.out.println(m);
					list.add(m);
				} else {
					System.out.println("step");
					int iter = 0;
					ChipArrayList chips = null;
					if (side == MachinePlayer.BLACK) {
						chips = this.board.blackPieces();
					} else {
						chips = this.board.whitePieces();
					}
					while (iter < chips.size()) {
						Chip temp = chips.get(iter);
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
		while (temp < list.size()){
			System.out.println(list.get(temp));
			temp++;
		}
		return list;
	}
}