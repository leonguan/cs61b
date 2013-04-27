package utils;

/**
 * Extends the ArrayList class. This subclass is used specifically to hold Integer objects.
 * @author James Jia
 *
 */
public class IntegerArrayList {

	private Integer[] arraylist;
	private int size;

	/**
	 * Constructs an ArrayList that is expected to hold up to size Integers.
	 * Creates a size-length Integer array to hold the Integers added to the IntegerArrayList.
	 * @param size - predicted size of IntegerArrayList
	 */
	public IntegerArrayList(int size) {
		this.arraylist = new Integer[size];
		this.size = 0;
	}

	/**
	 * A no-arg constructor that defaults to produce a IntegerArrayList of size 10.
	 */
	public IntegerArrayList() {
		this(10);
	}

	
	/**
	 * "Clones" an IntegerArrayList by creating a new one of the same size and copying the objects over
	 * to the new IntegerArrayList
	 * @param list - IntegerArrayList to copy
	 */
	public IntegerArrayList(IntegerArrayList list) {
		this.arraylist = new Integer[list.arraylist.length];
		int i = 0;
		while (i < list.size()) {
			this.add(i, list.get(i));
			i++;
		}
		this.size = list.size();

	}


	/**
	 * Adds m to the indicated index in the IntegerArrayList
	 * If the IntegerArrayList's array is not large enough, a new array of twice the size is constructed
	 * and references of the objects are copied from the original array to the new one.
	 * Throws an IndexOutOfBoundsException if the index is larger than the current size of the IntegerArrayList
	 * @param m - Integer to add to ArrayList
	 */
	public void add(Integer m) {
		this.add(this.size(), m);
	}

	/**
	 * Adds Integer m at the specified index of the ArrayList
	 * @param index - index to add object
	 * @param m - Integer to add to ArrayList
	 */
	public void add(int index, Integer m) {
		if (this.size() == this.arraylist.length) {
			Integer[] newArray = new Integer[this.arraylist.length * 2];
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

	/**
	 * Returns the Integer at the given index.
	 * @param index - index to retrieve from
	 * @return Integer object at the specified index.
	 */
	public Integer get(int index) {
		return this.arraylist[index];
	}

	/**
	 * Checks whether or not the IntegerArrayList contains a given Integer object
	 * @param target - The Integer we wish to check whether it is in the ArrayList or not
	 * @return bool of whether the ArrayList contains the target object
	 */
	public boolean contains(Integer target) {
		for (int i = 0; i < this.size(); i++) {
			if (target == this.get(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes an object target from the IntegerArrayList
	 * @param target - Object to remove
	 */
	public void remove(Integer target) {
		for (int i = 0; i < this.size(); i++) {
			if (target == this.get(i)) {
				this.add(i, null);
			}
		}
	}

	/**
	 * Returns the number of objects in the IntegerArrayList
	 * @return size of the ArrayList as an int value
	 */
	public int size() {
		return this.size;
	}
	
	/**
	 * A toString() method produced for testing purposes
	 */
	public String toString(){
		String str = "Nodes Visited: ";
		for (int i = 0; i< this.arraylist.length;i++){
			str=str + " " + this.arraylist[i]+ " ";
		}
		return str;
	}
}
