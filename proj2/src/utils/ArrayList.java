package utils;

/**
 * ArrayList is the superclass of the other 3 ArrayList classes and implements many features of
 * Java's ArrayList data structure.
 * Specifically, it implements the add, size, remove, and get methods.
 * The various ArrayLists are implemented using an instance variable that points to an array and
 * another instance variable that keeps track of the index of the current last object.
 * When a new object is added to the ArrayList, the ArrayList checks whether the current array is
 * large enough; if not, it creates a new array and copies all of the items from the current array.
 * @author Andrew Liu
 *
 */
public class ArrayList {
	private Object[] arraylist;
	private int size;

	/**Constructs an ArrayList that is expected to hold up to size Objects.
	 * Creates a size-length Object array to hold the Objects added to the ArrayList.
	 * @param size - predicted size of the ArrayList
	 */
	public ArrayList(int size) {
		this.arraylist = new Object[size];
		this.size = 0;
	}
	
	/**
	 * A no-arg constructor that defaults to produce an ArrayList of size 10.
	 */
	public ArrayList() {
		this(10);
	}
	
	/**
	 * Adds object to the end of the ArrayList
	 * @param object - the object to be added to the ArrayList
	 */
	public void add(Object object) {
		this.add(this.size(), object);
	}
	
	/**
	 * Adds object to the indicated index in the ArrayList
	 * If the ArrayList's array is not large enough, a new array of twice the size is constructed
	 * and references of the objects are copied from the original array to the new one.
	 * Throws an IndexOutOfBoundsException if the index is larger than the current size of the ArrayList
	 * @param index - the index of the ArrayList to add the object
	 * @param object - the object to add to the ArrayList
	 */

	public void add(int index, Object object) {
		if (this.size() == this.arraylist.length) {
			Object[] newArray = new Object[this.arraylist.length * 2];
			for (int i = 0; i < this.arraylist.length; i++) {
				newArray[i] = this.arraylist[i];
				this.arraylist = newArray;
			}
		}
		if (index >= this.size) {
			throw new IndexOutOfBoundsException();
		}
		if (this.arraylist[index] == null) {
			this.size++;
		}
		this.arraylist[index] = object;
	}
	
	/**
	 * Returns the object at the given index in the ArrayList
	 * @param index
	 * @return object
	 */
	public Object get(int index) {
		return this.arraylist[index];
	}
	
	/**
	 * Returns true if the ArrayList is empty.
	 * @return bool
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * Returns the size of the ArrayList.
	 * @return int - size of ArrayList
	 */
	public int size() {
		return this.size;
	}
	
	/**
	 * Removes the object at the given index.
	 * UNLIKE Java's implementation, our remove method does not return the object because we never need it.
	 * @param index - index of object to remove
	 */
	public void remove(int index) {
		if (index > this.arraylist.length || this.arraylist[index] == null) {
			System.out.println("Nothing at index " + index);
		}
		else {
			this.arraylist[index] = null;
			this.size--;
		}
	}
}
