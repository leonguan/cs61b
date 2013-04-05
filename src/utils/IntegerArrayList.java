package utils;

/**
 * Extends the ArrayList class. This subclass is used specifically to hold Chip objects.
 * @author James Jia
 *
 */
public class IntegerArrayList {

	private Integer[] arraylist;
	private int size;

	public IntegerArrayList(int size) {
		this.arraylist = new Integer[size];
		this.size = 0;
	}

	public IntegerArrayList() {
		this(10);
	}

	public IntegerArrayList(IntegerArrayList list) {
		this.arraylist = new Integer[list.arraylist.length];
		int i = 0;
		while (i < list.size()) {
			this.add(i, list.get(i));
			i++;
		}
		this.size = list.size();

	}

	public void add(Integer m) {
		this.add(this.size(), m);
	}

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

	public Integer get(int index) {
		return this.arraylist[index];
	}

	public boolean contains(Integer target) {
		for (int i = 0; i < this.size(); i++) {
			if (target == this.get(i)) {
				return true;
			}
		}
		return false;
	}

	public void remove(Integer target) {
		for (int i = 0; i < this.size(); i++) {
			if (target == this.get(i)) {
				this.add(i, null);
			}
		}
	}

	public int size() {
		return this.size;
	}
	
	public String toString(){
		String str = "Nodes Visited: ";
		for (int i = 0; i< this.arraylist.length;i++){
			str=str + " " + this.arraylist[i]+ " ";
		}
		return str;
	}
}
