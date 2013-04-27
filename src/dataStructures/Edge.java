package dataStructures;

public class Edge {

	protected int weight;
	protected DListNode node1;
	protected DListNode node2;
	

	
	public Edge(DListNode d1, DListNode d2, int weight) {
		node1 = d1;
		node2 = d2;
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
	
	public DListNode getNode1() {
		return node1;
	}
	
	public DListNode getNode2() {
		return node2;
	}
	
	
}
