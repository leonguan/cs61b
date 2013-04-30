package graph;

import list.*;

public class Vertex {

	/**
	 *  degree references the number of edges attached to this Vertex.
	 *  myGraph references the Graph that contains this Vertex.
	 *  item references the Object that this Vertex contains.
	 *
	 */
	protected int degree;
	protected WUGraph myGraph;
	protected Object item;
	protected DList adjacencyList;
	protected DListNode node;


	public Vertex(Object item, DListNode node, WUGraph graph) {
		myGraph = graph;
		this.item = item;
		this.node = node;
		adjacencyList = new DList();
	}

	public int degree() {
		return adjacencyList.length();
	}
	
	public Object getItem() {
		return item;
	}


	public WUGraph getParent() {
		return myGraph;
	}

	public DList adjacencyList() {
		return adjacencyList;
	}

	public DListNode getNode() {
		return node;
	}


	

}
