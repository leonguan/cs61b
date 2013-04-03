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
		this.searchDepth = 4;
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
		Best move = chooseMove(this.turn, -1000000, 1000000, this.searchDepth);
		System.out.println("Computer turn: " + turn);
		Move m = move.m;
		if (m == null) {
			MoveArrayList l = this.getMoves(this.color);
			for (int iter = 0; iter < l.size(); iter++) {
				m = l.get(iter);
				if (this.board.validMove(m, turn)) {
					break;
				}
			}
		}
		// System.out.println("Score: " + move.score);
		this.board = new Board(this.board, m, this.turn);
		// System.out.println("COMPUTER Score: " + board.eval(this.color));
		this.turn++;
		return m;
	}

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
		int chipIndex;
		if (side == MachinePlayer.BLACK) {
			chipIndex = 2;
		} else {
			chipIndex = 1;
		}
		while (i < moves.size()) {
			Move m = moves.get(i);
			if (this.board.validMove(m, turn)) {
				Board currBoard = this.board;
				Board afterMove = new Board(this.board, m, turn);
				this.board = afterMove;
				if (this.board.hasChipsInBothGoals(side)) {
					while (chipIndex < 21) {
						IntegerArrayList list = new IntegerArrayList();
						Chip currChip = this.board.getChip(chipIndex);
						if (currChip != null
								&& (currChip.getX() == 0 || currChip.getY() == 0)) {
							if (this.board.isValidNetwork(side, 0, -1,
									currChip, list)) {
								System.out.println("Color: " + side
										+ " won on turn: " + turn);
								if (side == this.color) {
									myBest.score = 100000 - 100 * turn;
								} else {
									myBest.score = -100000 + 100 * turn;
								}
								m = new Move();
								m.moveKind = Move.ADD;
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
		System.out.println("OPP TURN: " + turn);
		if (this.board.validMove(m, (this.color + 1) % 2)) {
			this.board = new Board(this.board, m, this.turn);
			this.turn++;
		} else {
			return false;
		}
		IntegerArrayList list = new IntegerArrayList();
		int chipIndex;
		if (this.color == MachinePlayer.BLACK) {
			chipIndex = 1;
		} else {
			chipIndex = 2;
		}
		while (chipIndex < 21) {
			Chip currChip = this.board.getChip(chipIndex);
			if (currChip != null
					&& (currChip.getX() == 0 || currChip.getY() == 0)) {
				if (this.board.isValidNetwork((this.color + 1) % 2, 0, -1,
						currChip, list)) {
				}
			}
			chipIndex += 2;
		}
		return true;
	}

	// If the Move m is legal, records the move as a move by "this" player
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method is used to help set up "Network problems" for your
	// player to solve.
	public boolean forceMove(Move m) {
		if (this.board.validMove(m, this.color)) {
			this.board = new Board(this.board, m, this.turn);
			this.turn++;
			return true;
		}
		return false;
	}

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
}