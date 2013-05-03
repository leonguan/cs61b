

public class KruskalEdge {

	
	protected Object o1;
	protected Object o2;
	protected int weight;
	

	
	public KruskalEdge(Object o1, Object o2, int weight) {
		this.o1 = o1;
		this.o2 = o2;
		this.weight = weight;
	}
	
	
	/**
	 *  setWeight() assigns an int to the node's weight value.  If this node is invalid,
	 *  throws an exception.
	 *
	 *  @param integer value of weight.
	 *  @exception InvalidNodeException if this node is not valid.
	 *
	 *  Performance:  runs in O(1) time.
	 */

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 *  getWeight() returns the node's weight value.  If this node is invalid,
	 *  throws an exception.
	 *
	 *  @param node the node whose predecessor is sought.
	 *  @return the weight of this node.
	 *  @exception InvalidNodeException if this node is not valid.
	 *
	 *  Performance:  runs in O(1) time.
	 */
	public int getWeight() {
		return weight;
	}
	
	public Object getObj1() {
		return o1;
	}
	
	public Object getObj2() {
		return o2;
	}
	
	/**
	 * equals() returns true if this VertexPair represents the same unordered
	 * pair of objects as the parameter "o".  The order of the pair does not
	 * affect the equality test, so (u, v) is found to be equal to (v, u).
	 */
	public boolean equals(Object o) {
		if (o instanceof KruskalEdge) {
			return ((o1.equals(((KruskalEdge) o).o1)) &&
					(o2.equals(((KruskalEdge) o).o2))) ||
					((o1.equals(((KruskalEdge) o).o2)) &&
							(o2.equals(((KruskalEdge) o).o1)));
		} else {
			return false;
		}
	}
	
  /**
	 * hashCode() returns a hashCode equal to the sum of the hashCodes of each
	 * of the two objects of the pair, so that the order of the objects will
	 * not affect the hashCode.  Self-edges are treated differently:  we don't
	 * add an object's hashCode to itself, since the result would always be even.
	 * We add one to the hashCode so that a self-edge will not collide with the
	 * object itself if vertices and edges are stored in the same hash table.
	 */
	public int hashCode() {
		if (o1.equals(o2)) {
			return o1.hashCode() + 1;
		} else {
			return o1.hashCode() + o2.hashCode();
		}
	}	
}
