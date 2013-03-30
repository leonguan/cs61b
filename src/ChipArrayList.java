import player.Chip;


public class ChipArrayList extends ArrayList {
	private Chip[] arraylist;
	private int arraysize;
	private int size;

	
	public ChipArrayList(int size) {
		this.arraylist = new Chip[size];
		this.arraysize = size;
		this.size = 0;
	}
	
	public ChipArrayList() {
		this(10);
	}
	
	public void add(Chip chip) {
		this.add(this.size(), chip);
	}
	
	public void add(int index, Chip chip) {
		if (this.size() == this.arraysize) {
			Chip[] newArray = new Chip[this.arraysize * 2];
			for (int i = 0; i < this.arraysize; i++) {
				newArray[i] = this.arraylist[i];
				this.arraylist = newArray;
				this.arraysize = this.arraysize * 2;
			}
		}
		if (index >= this.size) {
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
