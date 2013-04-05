package utils;
import player.Move;
/**
 * Extends the ArrayList class. This subclass is used specifically to hold Integer objects.
 * @author Matthew Miller
 */
public class MoveArrayList extends ArrayList {
	private Move[] arraylist;
	private int size;

	// Constructs an ArrayList that is expected to hold up to size number of Moves.
	// Creates a size-length Move array to hold the Moves added to the ArrayList.
	public MoveArrayList(int size) {
		this.arraylist = new Move[size];
		this.size = 0;
	}

	// A no-arg constructor that defaults to produce a IntegerArrayList of size 10.
	public MoveArrayList() {
		this(10);
	}

	// Adds a Move to the end of the MoveArrayList
	public void add(Move m) {
		this.add(this.size(), m);
	}

	// Adds m to the indicated index in the MoveArrayList
	// If the MoveArrayList's array is not large enough, a new array of twice the size is constructed
	// and references of the objects are copied from the original array to the new one.
	// Throws an IndexOutOfBoundsException if the index is larger than the current size of the MoveArrayList
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

	// Returns the Move at the specified index of the MoveArrayList
	public Move get(int index) {
		return this.arraylist[index];
	}

	// Returns the size of the MoveArrayList
	public int size() {
		return this.size;
	}

}
