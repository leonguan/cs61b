/* MachinePlayer.java */

package player;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 * 
 * @author James Jia
 */
public class MachinePlayer extends Player {
	final static byte BLACK = 0;
	final static byte WHITE = 1;
	private byte color;
	private byte searchDepth;
	private Board board;
	private byte turn = 1;
	private Move[] allAddMoves = new Move[64];

	// Creates a machine player with the given color. Color is either 0 (black)
	// or 1 (white). (White has the first move.)
	public MachinePlayer(int color) {
		this.color = (byte) color;
		this.board = new Board();
		this.searchDepth = 3;
	}

	// Creates a machine player with the given color and search depth. Color is
	// either 0 (black) or 1 (white). (White has the first move.)
	public MachinePlayer(int color, int searchDepth) {
		this(color);
		if (searchDepth >= 1) {
			this.searchDepth = (byte) searchDepth;
		}
	}

	// Returns a new move by "this" player. Internally records the move (updates
	// the internal game board) as a move by "this" player.
	public Move chooseMove() {
		Best move = findBest(this.turn, -1000000, 1000000, this.searchDepth);
		Move m = move.m;
		if (m == null) {
			m = new Move();
			m.moveKind = Move.QUIT;
		}
		this.board.addMove(m, this.turn);
		this.turn++;
		return m;
	}

	private Best findBest(byte turn, int alpha, int beta, byte depth) {
		Best myBest = new Best();
		Best reply = new Best();
		byte side = (byte) (turn % 2);
		byte otherSide = (byte) ((side + 1) % 2);
		int i = 0;
		Move[] moves = getMoves(side);
		Move m;
		if (side == this.color)
			myBest.score = alpha;
		else
			myBest.score = beta;
		while (i < moves.length) {
			m = moves[i];
			if (m == null) {
				i++;
				continue;
			}
			if (this.board.validMove(m, turn)) {
				this.board.addMove(m, turn);
				if (this.board.isValidNetwork(side)) {
					// System.out.println("SIDE: "+side);
					if (side == this.color) {
						myBest.score = 100000 - 100 * turn;
					} else {
						myBest.score = -100000 + 100 * turn;
					}
					myBest.m = m;
					this.board.removeMove(m, turn);
					return myBest;
				}
				if (this.board.isValidNetwork(otherSide)) {
					if (otherSide == this.color) {
						myBest.score = 100000 - 100 * turn;
					} else {
						myBest.score = -100000 + 100 * turn;
					}
					myBest.m = m;
					this.board.removeMove(m, turn);
					return myBest;
				}
				if (depth == 1) {
					reply.m = m;
					reply.score = this.board.eval(this.color);
				} else {
					reply = findBest((byte) (turn + 1), alpha, beta,
							(byte) (depth - 1));
				}
				this.board.removeMove(m, turn);
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
				if (alpha >= beta)
					return myBest;
			}
			i += 1;
		}
		return myBest;
	}

	private Move[] getMoves(byte side) {
		Move[] results = null;
		Move m;
		int count = 0;
		byte[][] chips = new byte[Board.MAX_CHIPS / 2][2];
		boolean breakOut = false;
		byte chipsFound;
		boolean shouldAdd = this.board.shouldAdd(this.turn);
		if (shouldAdd) {
			if (this.allAddMoves[0] != null) {
				return this.allAddMoves;
			}
		} else {
			results = new Move[Board.MAX_CHIPS / 2 * Board.BOARD_SIZE
					* Board.BOARD_SIZE];
		}
		for (byte i = 0; i < Board.BOARD_SIZE; i++) {
			for (byte j = 0; j < Board.BOARD_SIZE; j++) {
				if (shouldAdd) {
					m = new Move();
					m.x1 = i;
					m.y1 = j;
					m.moveKind = Move.ADD;
					this.allAddMoves[count] = m;
					count += 1;
				} else {
					chipsFound = 0;
					if (chips[0][0] != 0 || chips[0][1] != 0) {
						for (byte x = 0; x < Board.MAX_CHIPS / 2; x++) {
							m = new Move();
							m.x1 = i;
							m.y1 = j;
							m.x2 = chips[x][0];
							m.y2 = chips[x][1];
							m.moveKind = Move.STEP;
							results[count] = m;
							count += 1;
						}
					} else {
						for (byte y = 0; y < Board.BOARD_SIZE; y++) {
							for (byte x = 0; x < Board.BOARD_SIZE; x++) {
								if (this.board.isCornerOutOfBounds(x, y, side)) {
									continue;
								}
								if (this.board.fetchValue(x, y) != 0
										&& this.board.fetchValue(x, y) % 2 == side) {
									m = new Move();
									m.x1 = i;
									m.y1 = j;
									m.x2 = x;
									m.y2 = y;
									m.moveKind = Move.STEP;
									results[count] = m;
									chips[chipsFound][0] = x;
									chips[chipsFound][1] = y;

									count += 1;
									chipsFound += 1;
									if (chipsFound == Board.MAX_CHIPS / 2) {
										breakOut = true;
									}
								}
								if (breakOut)
									break;
							}
							if (breakOut)
								break;
						}
					}
				}
			}
		}
		if (shouldAdd) {
			return this.allAddMoves;
		}
		return results;
	}

	// If the Move m is legal, records the move as a move by the opponent
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method allows your opponents to inform you of their moves.
	public boolean opponentMove(Move m) {
		if (this.board.validMove(m, this.turn)) {
			this.board.addMove(m, this.turn);
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
			this.board.addMove(m, this.turn);
			this.turn++;
			return true;
		}
		return false;
	}

	public Board getBoard() {
		return this.board;
	}

}
