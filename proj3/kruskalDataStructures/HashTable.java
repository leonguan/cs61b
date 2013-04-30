/* HashTable.java */

package kruskalDataStructures;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTable implements Dictionary {

  /**
   *  Place any data fields here.
   **/
  int buckets;
  int size;
  int collisions;
  DList[] hashTable;
  
  /*
   * Helper Functions for testing and finding primes
   */

  private boolean isPrime(int x) {
	  for (int i = 2; i < Math.sqrt(x); i++) {
		  if (x % i == 0) {
			  return false;
		  }
	  }
	  return true;
  }
  
  private int makePrime(int x) {
	  while (!isPrime(x)) {
		  x++;
	  }
	  return x;
  }


  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTable(int sizeEstimate) {
    // Your solution here.
	buckets = makePrime(sizeEstimate);
	size = 0;
	hashTable = new DList[buckets];

  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTable() {
    // Your solution here.
	buckets = 101;
	size = 0;
	hashTable = new DList[buckets];

  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    // Replace the following line with your solution.
	  // return ((ai + b) mod p) mod N
	int largePrime = makePrime(buckets * buckets * 37);
	int converted = (239 * code + 701) % largePrime;
	if (converted < 0) {
		converted += largePrime;
	} 
    return converted % buckets;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    // Replace the following line with your solution.
    return size;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
    return (size == 0);
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.
	Entry newEntry = new Entry();
	newEntry.key = key;
	newEntry.value = value;
	int bucket = compFunction(key.hashCode());
	if (hashTable[bucket] == null) {
		hashTable[bucket] = new DList();
	} else {
		collisions++;
	}
	hashTable[bucket].insertBack(newEntry);
	size++;
    return newEntry;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
 * @throws  
   **/

  public Entry find(Object key) {
    // Replace the following line with your solution.
	  int bucket = compFunction(key.hashCode());
	  if (hashTable[bucket] != null) {
		  ListNode elem = hashTable[bucket].front();
		  if (elem == null) {
			  return null;
		  }
		  while (elem.isValidNode()) {
			  try {
				  if (((Entry) elem.item()).key().equals(key)) {
					  
					  return (Entry) elem.item();
				  }
				  elem = elem.next();

			  } catch (InvalidNodeException e) {
				  return null;
			  }

		  }
	  }
	  return null;


  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    // Replace the following line with your solution.
	int bucket = compFunction(key.hashCode());
	ListNode elem = hashTable[bucket].front();
	if (elem == null) {
		return null;
	}
	while (elem.isValidNode()) {
		try {
			Entry entry = (Entry) elem.item();
			if (entry.key().equals(key)) {
				elem.remove();
				size--;
//
//				if (hashTable[bucket].length() == 0) {
//					hashTable[bucket] = null;
//				}

				return entry;
			}
			elem = elem.next();

		} catch (InvalidNodeException e) {
			return null;
		}
	}
    return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
	hashTable = new DList[buckets];
	size = 0;
  }
  
  public int countCollisions() {
	  return collisions;
  }
  
  public void printKeys() {
	  for (int i = 0; i < buckets; i++) {
		  if (hashTable[i] == null) {
			  System.out.print("Empty ");
		  } else {
			  System.out.print(hashTable[i].length() + " ");
		  }
	  }
  }
  
  public static void main(String[] args) throws InvalidNodeException {

  }

}
