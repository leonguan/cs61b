
public class ArrayList {
	private Object[] arraylist;
	private int size;

	
	public ArrayList(int size) {
		this.arraylist = new Object[size];
		this.size = 0;
	}
	
	public ArrayList() {
		this(10);
	}
	
	public void add(Object object) {
		this.add(this.size(), object);
	}
	
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
	
	public Object get(int index) {
		return this.arraylist[index];
	}
	
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	public int size() {
		return this.size;
	}
	
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
