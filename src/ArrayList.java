
public class ArrayList {
	private Object[] arraylist;
	private int indexOfLast;
	private int arraysize;
	
	public ArrayList(Object object) {
		this(object, 10);
	}
	
	public ArrayList(Object object, int size) {
		this.arraylist = new Object[size];
		this.indexOfLast = 0;
		this.arraysize = size;
	}
	
	public void add(Object object) {
		this.add(this.indexOfLast, object);
	}
	
	public void add(int index, Object object) {
		if (this.indexOfLast == this.arraysize - 1) {
			Object[] newArray = new Object[this.arraysize * 2];
			for (int i = 0; i < this.arraysize; i++) {
				newArray[i] = this.arraylist[i];
			}
		}
		this.arraylist[index] = object;
		this.indexOfLast++;
	}
	
	public Object get(int index) {
		return this.arraylist[index];
	}
	
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	public int size() {
		return this.indexOfLast;
	}
	
	public void remove(int index) {
		
	}
}
