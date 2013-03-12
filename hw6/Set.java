/* Set.java */

import list.*;

/**
 *  A Set is a collection of Comparable elements stored in sorted order.
 *  Duplicate elements are not permitted in a Set.
 **/
public class Set {
  List elements;
  

  /**
   * Set ADT invariants:
   *  1)  The Set's elements must be precisely the elements of the List.
   *  2)  The List must always contain Comparable elements, and those elements 
   *      must always be sorted in ascending order.
   *  3)  No two elements in the List may be equal according to compareTo().
   **/

  /**
   *  Constructs an empty Set. 
   *
   *  Performance:  runs in O(1) time.
   **/
  public Set() { 
    this.elements = new DList();
  }

  /**
   *  cardinality() returns the number of elements in this Set.
   *
   *  Performance:  runs in O(1) time.
   **/
  public int cardinality() {
    // Replace the following line with your solution.
    return this.elements.length();
  }

  /**
   *  insert() inserts a Comparable element into this Set.
   *
   *  Sets are maintained in sorted order.  The ordering is specified by the
   *  compareTo() method of the java.lang.Comparable interface.
   *
   *  Performance:  runs in O(this.cardinality()) time.
 * @throws InvalidNodeException 
   **/
  public void insert(Comparable c) {
    if (this.elements.length() == 0) {
    	this.elements.insertFront(c);
    }
    else {
    	ListNode element = this.elements.front();
    	
    	// Test if the two are comparable
    	try {
        	while (element.isValidNode() && c.compareTo(element.item()) > 0) {
        		element = element.next();
        	}
        	if (!element.isValidNode()) {
        		this.elements.insertBack(c);

        	}
        	else if (element.isValidNode()) {
        		
        		if (c.compareTo(element.item()) < 0) {
        	
    	    		element.insertBefore(c);
        		}
        	}
    	} catch (InvalidNodeException e){};

    }
  }

  /**
   *  union() modifies this Set so that it contains all the elements it
   *  started with, plus all the elements of s.  The Set s is NOT modified.
   *  Make sure that duplicate elements are not created.
   *
   *  Performance:  Must run in O(this.cardinality() + s.cardinality()) time.
   *
   *  Your implementation should NOT copy elements of s or "this", though it
   *  will copy _references_ to the elements of s.  Your implementation will
   *  create new _nodes_ for the elements of s that are added to "this", but
   *  you should reuse the nodes that are already part of "this".
   *
   *  DO NOT MODIFY THE SET s.
   *  DO NOT ATTEMPT TO COPY ELEMENTS; just copy _references_ to them.
   **/
  public void union(Set s) {
    // Your solution here.
	  int i = 0;
	  int j = 0;
	  if (s.cardinality() == 0) {
		  return;
	  }
	  
	  else if (this.cardinality() == 0) {
		  try {
			this.elements.insertFront(s.elements.front().item());
		} catch (InvalidNodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  ListNode sCurr = s.elements.front();
		  ListNode thisCurr = this.elements.front();
		  try {
			  while (sCurr.next().isValidNode()) {
				  sCurr = sCurr.next();
				  thisCurr.insertAfter(sCurr.item());
				  thisCurr = thisCurr.next();
			  }
		  } catch (InvalidNodeException e) {};
	  }
	  
	  else {
		  ListNode thisCurr = this.elements.front();
		  ListNode sCurr = s.elements.front();
		  int thislength = this.cardinality();
		  int slength = s.cardinality();
		  while (i < thislength && j < slength) {
			  try {
				if (((Comparable) thisCurr.item()).compareTo(sCurr.item()) < 0) {
					thisCurr = thisCurr.next();
					i++;
					  
				  }
				else if (((Comparable) thisCurr.item()).compareTo(sCurr.item()) == 0) {
					sCurr = sCurr.next();
					thisCurr = thisCurr.next();
					i++;
					j++;
				}
				else {
					thisCurr.insertBefore(sCurr.item());
					sCurr = sCurr.next();
					j++;
				}
			} catch (InvalidNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  
		  while (j < slength) {
			  try {
				this.elements.insertBack(sCurr.item());
				j++;
				sCurr = sCurr.next();
			} catch (InvalidNodeException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				
			}
			  
		  }
		  
	  }
  }

  /**
   *  intersect() modifies this Set so that it contains the intersection of
   *  its own elements and the elements of s.  The Set s is NOT modified.
   *
   *  Performance:  Must run in O(this.cardinality() + s.cardinality()) time.
   *
   *  Do not construct any new ListNodes during the execution of intersect.
   *  Reuse the nodes of "this" that will be in the intersection.
   *
   *  DO NOT MODIFY THE SET s.
   *  DO NOT CONSTRUCT ANY NEW NODES.
   *  DO NOT ATTEMPT TO COPY ELEMENTS.
   **/
  public void intersect(Set s) {
	  // if this set is the empty set, we don't have to do anything
	  if (this.cardinality() == 0) {
		  return;
	  }
	  
	  // Else if the other set is the empty set, we must remove every item from this set
	  else if (s.cardinality() == 0) {
		  while (this.elements.front().isValidNode()) {
			  try {
				this.elements.front().remove();
			} catch (InvalidNodeException e) {
			}
		  }
	  }
	  
	  // Else
	  else {
		  ListNode thisCurr = this.elements.front();
		  ListNode sCurr = s.elements.front();
		  int i = 0;
		  int j = 0;
		  // While there are still elements in this set
		  int thislength = this.cardinality();
		  ListNode prev;
		  
		  while (i < thislength && j < s.cardinality()) {
			  try {
				if (((Comparable) thisCurr.item()).compareTo(sCurr.item()) < 0) {
						prev = thisCurr;
						thisCurr = thisCurr.next();
						
						prev.remove();
						i++;
					  
				  }
				else if (((Comparable) thisCurr.item()).compareTo(sCurr.item()) == 0) {
					sCurr = sCurr.next();
					thisCurr = thisCurr.next();
					i++;
					j++;
				}
				else {
					sCurr = sCurr.next();
					j++;
				}
			} catch (InvalidNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  
		  while (i < thislength) {
			  try {
				this.elements.back().remove();
			} catch (InvalidNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  i++; 
		  }
		  
	  }
	  
    
  }

  /**
   *  toString() returns a String representation of this Set.  The String must
   *  have the following format:
   *    {  } for an empty Set.  No spaces before "{" or after "}"; two spaces
   *            between them.
   *    {  1  2  3  } for a Set of three Integer elements.  No spaces before
   *            "{" or after "}"; two spaces before and after each element.
   *            Elements are printed with their own toString method, whatever
   *            that may be.  The elements must appear in sorted order, from
   *            lowest to highest according to the compareTo() method.
   *
   *  WARNING:  THE AUTOGRADER EXPECTS YOU TO PRINT SETS IN _EXACTLY_ THIS
   *            FORMAT, RIGHT UP TO THE TWO SPACES BETWEEN ELEMENTS.  ANY
   *            DEVIATIONS WILL LOSE POINTS.
   **/
  public String toString() {
    // Replace the following line with your solution.
	  String representation = "{";
	  if (this.cardinality() == 0) {
		  return "{  }";
	  }
	  int length;
	  ListNode current = this.elements.front();
	  for (length = this.cardinality(); length > 0; length--) {
		  try {
			representation = representation + "  " + current.item().toString();
			current = current.next();
		} catch (InvalidNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
    return representation + "  }";
  }

  public static void main(String[] argv) throws InvalidNodeException {
    Set s = new Set();
    s.insert(new Integer(3));
    s.insert(new Integer(4));
    s.insert(new Integer(3));
    System.out.println("Set s = " + s);

    Set s2 = new Set();
    s2.insert(new Integer(4));
    s2.insert(new Integer(5));
    s2.insert(new Integer(5));
    System.out.println("Set s2 = " + s2);

    Set s3 = new Set();
    s3.insert(new Integer(5));
    s3.insert(new Integer(3));
    s3.insert(new Integer(8));
    System.out.println("Set s3 = " + s3);

    s.union(s2);
    System.out.println("After s.union(s2), s = " + s);

    s.intersect(s3);
    System.out.println("After s.intersect(s3), s = " + s);

    System.out.println("s.cardinality() = " + s.cardinality());
    // You may want to add more (ungraded) test code here.
    
    
    
  }
}
