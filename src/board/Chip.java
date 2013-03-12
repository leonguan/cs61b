package board;

public class Chip {

	private int color;
	private int y;
	private int x;

	/**
	 * @param args
	 */
	public Chip (int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public int X() {
		return this.x;
	}
	
	public int Y() {
		return this.y;
	}
	
	public int Color() {
		return this.color;
	}

}
