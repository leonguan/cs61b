package player;

import utils.ChipArrayList;

public class Chip {
	protected int x;
	protected int y;
	protected int color;
	private Board board;
	
	// A chip knows about its color, which board it is from, its position, and the existence of
	// every chip of the same color on the same board.
	public Chip(int x, int y, int color, Board board) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.board = board;
		
	}

}
