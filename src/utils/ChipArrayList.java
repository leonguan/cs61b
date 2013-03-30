package utils;
import player.Chip;


public class ChipArrayList extends ArrayList {
	private Chip[] arraylist;
	private int size;

	
	public ChipArrayList(int size) {
		this.arraylist = new Chip[size];
		this.size = 0;
	}
	
	public ChipArrayList() {
		this(10);
	}
	
	public void add(Chip chip) {
		this.add(this.size(), chip);
	}
	
	public void add(int index, Chip chip) {
		if (this.size() == this.arraylist.length) {
			Chip[] newArray = new Chip[this.arraylist.length * 2];
			for (int i = 0; i < this.arraylist.length; i++) {
				newArray[i] = this.arraylist[i];
				this.arraylist = newArray;
			}
		}
		if (index > this.size) {
			throw new IndexOutOfBoundsException();
		}
		if (this.arraylist[index] == null) {
			this.size++;
		}
		this.arraylist[index] = chip;
	}
	
	public Chip get(int index) {
		return this.arraylist[index];
	}


}
