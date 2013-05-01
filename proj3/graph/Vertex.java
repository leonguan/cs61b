package graph;

import list.*;

class Vertex {

	/**
	 *  degree references the number of edges attached to this Vertex.
	 *  item references the Object that this Vertex contains.
	 *  adjancencyList references a DList that contains the vertices connected to this Vertex
	 *  node references the DListNode in vertexList
	 *
	 */
	private Object item;
	private DList adjacencyList;
	private DListNode node;


	protected Vertex(Object item, DListNode node) {
		this.item = item;
		this.node = node;
		adjacencyList = new DList();
	}

	protected int degree() {
		return adjacencyList.length();
	}
	
	protected Object getItem() {
		return item;
	}

	protected DList adjacencyList() {
		return adjacencyList;
	}

	protected DListNode getNode() {
		return node;
	}
	
	/**
	 * HashCode generates a hash based on the two vertices. 
	 * Note that it does not require weight because there is at most 
	 * one edge between any two vertices.
	 * 
	 * @return int hashCode
	 */
	public int hashCode() {
		if (o1.equals(o2)) {
			return o1.hashCode() + 1;
		} else {
			return o1.hashCode() + o2.hashCode();
		}
	}


	

}
