package utils;
import player.Move;

public class MoveArrayList extends ArrayList {
	private Move[] arraylist;
	private int size;

	public MoveArrayList(int size) {
		this.arraylist = new Move[size];
		this.size = 0;
	}

	public MoveArrayList() {
		this(10);
	}

	public void add(Move m) {
		this.add(this.size(), m);
	}

	public void add(int index, Move m) {
		if (this.size() == this.arraylist.length) {
			Move[] newArray = new Move[this.arraylist.length * 2];
			for (int i = 0; i < this.arraylist.length; i++) {
				newArray[i] = this.arraylist[i];
			}
			this.arraylist = newArray;
		}
		
		if (index > this.size) {
			throw new IndexOutOfBoundsException();
		}
		if (this.arraylist[index] == null) {
			this.size++;
		}
		this.arraylist[index] = m;
	}

	public Move get(int index) {
		return this.arraylist[index];
	}

	public int size() {
		return this.size;
	}

}
