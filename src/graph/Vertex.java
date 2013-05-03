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

  /**
   * Constructs a Vertex that stores an object and a Node referencing 
   * the node in the list of Vertices of the graph that Vertex is a 
   * part of.
   *
   * @param item is the object being stored in this
   * @param node is a node referencing a DListNode stored in a Graph's 
   * vertexList
   */
	protected Vertex(Object item, DListNode node) {
		this.item = item;
		this.node = node;
		adjacencyList = new DList();
	}

  /**
   * degree() gives the number of Vertices/edge that this is 
   * connected to.
   *
   * @return an int representing the degree
   */
	protected int degree() {
		return adjacencyList.length();
	}
	
  /**
   * getItem() returns the item being stored
   *
   * @return object being stored in this
   */
	protected Object getItem() {
		return item;
	}

  /**
   * adjacencyList() returns a list that stores the adjacent vertices
   *
   * @return DList that stores vertices
   */
	protected DList adjacencyList() {
		return adjacencyList;
	}

  /**
   * getNode() returns the node being stored in this.
   *
   * @return DListNode
   */
	protected DListNode getNode() {
		return node;
	}


	

}
