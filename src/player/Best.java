package player;
/**
 * The Best object contains a potential move, m, to make
 * and the eval score of the Board that would result from that move.
 * @author James Jia
 *
 */
public class Best {
	int score;
	Move m;
	
	// A Best object is initialized with a default score of 0 and no assigned move, m.
	public Best(){
		score = 0;
		m = null;
	}
	
	// A Best object can be initialized with an integer representing the evaluated score of the Board
	// and a Move m that would result in the Board with such a score.
	public Best(int score, Move m){
		this.score = score;
		this.m = m;
	}
	// Returns a stringified representation for a Best object.
	// Mainly used for testing purposes.
	// Returns The score and move as part of a String.
	public String toString(){
		return "Score: " + score + " move: " + m.toString();
	}
}
