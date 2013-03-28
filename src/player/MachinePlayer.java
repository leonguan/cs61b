/* MachinePlayer.java */

package player;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 */
public class MachinePlayer extends Player {

	private int color;
	private int searchDepth;
	private Board board;

	// Creates a machine player with the given color. Color is either 0 (black)
	// or 1 (white). (White has the first move.)
	public MachinePlayer(int color) {
		this.color = color;
		this.board = new Board();
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
		int searched = 0;
		while (searched < this.searchDepth) {
			for (int i = 0; i < this.board.BOARD_SIZE; i++) {
				for (int j = 0; j < this.board.BOARD_SIZE; j++) {
					
				}
			}
			searched++;
		}
		return new Move();
	}

	public Move chooseMove(int side,int alpha, int beta){
		Move myBest = new Move();
		Move reply;
		
		return new Move();
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

}