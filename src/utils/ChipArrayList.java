package utils;
import player.Chip;

/**
 * Extends the ArrayList class. This subclass is used specifically to hold Chip objects.
 * @author Andrew Liu
 *
 */
public class ChipArrayList extends ArrayList {
	private Chip[] arraylist;
	private int size;

	// Constructs an ArrayList that is expected to hold up to size Chips.
	// Creates a size-length Chip array to hold the Chips added to the ArrayList.
	public ChipArrayList(int size) {
		this.arraylist = new Chip[size];
		this.size = 0;
	}
	// A no-arg constructor that defaults to produce a ChipArrayList of size 10.
	public ChipArrayList() {
		this(10);
	}
	
	// Adds chip to the end of the ChipArrayList
	public void add(Chip chip) {
		this.add(this.size(), chip);
	}
	
	// Adds chip to the indicated index in the ChipArrayList
	// If the ChipArrayList's array is not large enough, a new array of twice the size is constructed
	// and references of the objects are copied from the original array to the new one.
	// Throws an IndexOutOfBoundsException if the index is larger than the current size of the ChipArrayList
	public void add(int index, Chip chip) {
		if (this.size() == this.arraylist.length) {
			Chip[] newArray = new Chip[this.arraylist.length * 2];
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
		this.arraylist[index] = chip;
	}
	
	// Returns the Chip at the given index in the ArrayList
	public Chip get(int index) {
		return this.arraylist[index];
	}
	
	// Returns the size of the ChipArrayList
	public int size() {
		return this.size;
	}

}
