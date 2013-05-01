package graph;

import list.*;

class Vertex {

	/**
	 *  degree references the number of edges attached to this Vertex.
	 *  myGraph references the Graph that contains this Vertex.
	 *  item references the Object that this Vertex contains.
	 *
	 */
	private int degree;
	private WUGraph myGraph;
	private Object item;
	private DList adjacencyList;
	private DListNode node;


	protected Vertex(Object item, DListNode node, WUGraph graph) {
		myGraph = graph;
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


	protected WUGraph getParent() {
		return myGraph;
	}

	protected DList adjacencyList() {
		return adjacencyList;
	}

	protected DListNode getNode() {
		return node;
	}


	

}
