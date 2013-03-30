package player;

public class Best {
	double score;
	Move m;
	
	public Best(){
		score = 0;
		m = null;
	}
	
	public Best(int score, Move m){
		this.score = score;
		this.m = m;
	}
	public String toString(){
		return "Score: " + score + " move: " + m.toString();
	}
}
