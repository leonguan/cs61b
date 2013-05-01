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


	

}
