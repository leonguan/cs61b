
public class ArrayList {
	private Object[] arraylist;
	private int arraysize;
	private int size;

	
	public ArrayList(int size) {
		this.arraylist = new Object[size];
		this.arraysize = size;
		this.size = 0;
	}
	
	public ArrayList() {
		this(10);
	}
	
	public void add(Object object) {
		this.add(this.size(), object);
	}
	
	public void add(int index, Object object) {
		if (this.size() == this.arraysize) {
			Object[] newArray = new Object[this.arraysize * 2];
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
		if (index > this.arraysize || this.arraylist[index] == null) {
			System.out.println("Nothing at index " + index);
		}
		else {
			this.arraylist[index] = null;
			this.size--;
		}
	}
}
