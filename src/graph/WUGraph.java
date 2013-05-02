/* WUGraph.java */

package graph;

import list.*;
import hashTable.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 * edges keeps count of the number of edges in "this" graph
 * vertexList keeps a list of DListNodes which contain the vertices of "this" graph
 * vertexTable and edgeTable are HashTables that contain entries that have vertices and edges as values
 */

public class WUGraph {

	protected int edges;
	protected DList vertexList;
	protected HashTable vertexTable;
	protected HashTable edgeTable;

	/**
	 * WUGraph() constructs a graph having no vertices or edges.
	 *
	 * Running time:  O(1).
	 */
	public WUGraph() {
		edges = 0;
		vertexList = new DList();
		vertexTable = new HashTable();
		edgeTable = new HashTable();
	}

	/**
	 * vertexCount() returns the number of vertices in the graph.
	 *
	 * Running time:  O(1).
	 */
	public int vertexCount() {
		return vertexList.length();
	}

	/**
	 * edgeCount() returns the number of edges in the graph.
	 *
	 * Running time:  O(1).
	 */
	public int edgeCount() {
		return edges;
	}

	/**
	 * getVertices() returns an array containing all the objects that serve
	 * as vertices of the graph.  The array's length is exactly equal to the
	 * number of vertices.  If the graph has no vertices, the array has length
	 * zero.
	 *
	 * (NOTE:  Do not return any internal data structure you use to represent
	 * vertices!  Return only the same objects that were provided by the
	 * calling application in calls to addVertex().)
	 * 
	 * @return an array of the objects that serve as vertices of the graph
	 *
	 * Running time:  O(|V|).
	 */
	public Object[] getVertices() {
		Object[] vertexArray = new Object[vertexCount()];
		ListNode currNode = vertexList.front();
		try {
			for (int i = 0; i < vertexCount(); i++) {
				vertexArray[i] = currNode.item();
				currNode = currNode.next();
			}
		}
		catch (InvalidNodeException e) {

		}
		return vertexArray;
	}

	/**
	 * addVertex() adds a vertex (with no incident edges) to the graph.  The
	 * vertex's "name" is the object provided as the parameter "vertex". The internal 
	 * data structures that represent the graph are changed accordingly. 
	 * If this object is already a vertex of the graph, the graph is unchanged.
	 *
	 * @param vertex is the object the user wants to add to the graph
	 * 
	 * Running time:  O(1).
	 */
	public void addVertex(Object vertex) {
		if (!isVertex(vertex)) {
			vertexList.insertBack(vertex);
			Vertex v = new Vertex(vertex, (DListNode) vertexList.back());
			vertexTable.insert(vertex, v); 
		}
	}

	/**
	 * removeVertex() removes a vertex from the graph.  All edges incident on the
	 * deleted vertex are removed as well.  If the parameter "vertex" does not
	 * represent a vertex of the graph, the graph is unchanged. The internal data
	 * structures representing the graph are changed accordingly. 
	 * 
	 * @param vertex is the object that the user wants to remove from the graph
	 *
	 * Running time:  O(d), where d is the degree of "vertex".
	 */
	public void removeVertex(Object vertex) {
		if (isVertex(vertex)) {
			Vertex v = (Vertex) vertexTable.find(vertex).value();
			if (v.degree() > 0) {
				try {
					DListNode currNode = (DListNode) v.adjacencyList().front();
					while (currNode != null) {
						Object o = ((Vertex) currNode.item()).getItem();
						currNode = (DListNode) currNode.next();
						removeEdge(vertex, o);
					}
				} catch (InvalidNodeException e) {

				}
			}
			vertexTable.remove(vertex);
			try {
				v.getNode().remove();
			}
			catch (InvalidNodeException e) {

			}
		}
	}

	/**
	 * isVertex() returns true if the parameter "vertex" represents a vertex of
	 * the graph.
	 * 
	 * @param vertex is the object the user wants to check
	 * @return true if vertex is in the graph
	 *
	 * Running time:  O(1).
	 */
	public boolean isVertex(Object vertex) {
		return vertexTable.find(vertex) != null;
	}

	/**
	 * degree() returns the degree of a vertex.  Self-edges add only one to the
	 * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
	 * of the graph, zero is returned.
	 * 
	 * @param vertex is the object the user wants to check
	 * @return the number of vertices connected to vertex
	 *
	 * Running time:  O(1).
	 */
	public int degree(Object vertex) {
		if (isVertex(vertex)) {
			return ((Vertex) vertexTable.find(vertex).value()).degree();
		} else {
			return 0;
		}
	}

	/**
	 * getNeighbors() returns a new Neighbors object referencing two arrays.  The
	 * Neighbors.neighborList array contains each object that is connected to the
	 * input object by an edge.  The Neighbors.weightList array contains the
	 * weights of the corresponding edges.  The length of both arrays is equal to
	 * the number of edges incident on the input vertex.  If the vertex has
	 * degree zero, or if the parameter "vertex" does not represent a vertex of
	 * the graph, null is returned (instead of a Neighbors object).
	 *
	 * The returned Neighbors object, and the two arrays, are both newly created.
	 * No previously existing Neighbors object or array is changed.
	 *
	 * (NOTE:  In the neighborList array, do not return any internal data
	 * structure you use to represent vertices!  Return only the same objects
	 * that were provided by the calling application in calls to addVertex().)
	 * 
	 * @param vertex is the object the user wants to inquire about
	 * @return a Neighbor object that contains two arrays
	 *
	 * Running time:  O(d), where d is the degree of "vertex".
	 * @throws InvalidNodeException 
	 */
	public Neighbors getNeighbors(Object vertex) {
		if (isVertex(vertex)) {
			try {
				Vertex v = (Vertex) vertexTable.find(vertex).value();
				if (v.degree() == 0) {
					return null;
				}
				Neighbors neighbors = new Neighbors();
				neighbors.neighborList = new Object[v.degree()];
				neighbors.weightList = new int[v.degree()];
				DListNode currNode = (DListNode) v.adjacencyList().front();

				for (int i = 0; i < v.degree(); i++) {
					Object obj = ((Vertex) currNode.item()).getItem();					
					neighbors.neighborList[i] = obj;
					VertexPair vp = new VertexPair(vertex, obj);
					neighbors.weightList[i] = ((Edge) edgeTable.find(vp).value()).getWeight();
					currNode = (DListNode) currNode.next();
				}
				return neighbors; 
			}
			catch (InvalidNodeException e) {

			}
		}
		return null;
	}

	/**
	 * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
	 * u and v does not represent a vertex of the graph, the graph is unchanged.
	 * The edge is assigned a weight of "weight".  If the edge is already
	 * contained in the graph, the weight is updated to reflect the new value.
	 * Self-edges (where u == v) are allowed.
	 * 
	 * @param u is the object to start an edge with
	 * @param v is the object to end an edge with
	 * @param weight is the weight of the edge
	 *
	 * Running time:  O(1).
	 */
	public void addEdge(Object u, Object v, int weight) {
		if (isVertex(u) && isVertex(v)) {
			if (isEdge(u, v)) {
				//Update weight of existing edge
				VertexPair vp = new VertexPair(u, v);
				((Edge) edgeTable.find(vp).value()).setWeight(weight);
			} else if (u.equals(v)) {
				//Create a self-edge
				Vertex uInternal = (Vertex) vertexTable.find(u).value();
				DList uList = uInternal.adjacencyList();
				uList.insertBack(uInternal);
				DListNode uBack = (DListNode) uList.back();
				VertexPair vp = new VertexPair(u, u);
				Edge edge = new Edge(uBack, uBack, weight);
				edgeTable.insert(vp, edge);
				edges++;
			} else {
				Vertex uInternal = (Vertex) vertexTable.find(u).value();
				Vertex vInternal = (Vertex) vertexTable.find(v).value();
				DList uList = uInternal.adjacencyList();
				DList vList = vInternal.adjacencyList();
				uList.insertBack(vInternal);
				vList.insertBack(uInternal);
				DListNode uBack = (DListNode) uList.back();
				DListNode vBack = (DListNode) vList.back();
				VertexPair vp = new VertexPair(u, v);
				Edge edge = new Edge(uBack, vBack, weight);
				edgeTable.insert(vp, edge);
				edges++;
			}
		}
	}

	/**
	 * removeEdge() removes an edge (u, v) from the graph.  If either of the
	 * parameters u and v does not represent a vertex of the graph, the graph
	 * is unchanged.  If (u, v) is not an edge of the graph, the graph is
	 * unchanged.
	 * 
	 * @param u is the start of the edge
	 * @param v is the end of the edge
	 *
	 * Running time:  O(1).
	 */
	public void removeEdge(Object u, Object v) {
		if (isEdge(u, v)) {
			VertexPair vp = new VertexPair(u, v);
			Edge e = (Edge) edgeTable.find(vp).value();
			try {
				e.getNode1().remove();
				e.getNode2().remove();

			} catch (InvalidNodeException e1) {

			}
			edgeTable.remove(vp);
			edges--;
		}
	}

	/**
	 * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
	 * if (u, v) is not an edge (including the case where either of the
	 * parameters u and v does not represent a vertex of the graph).
	 * 
	 * @param u is the start of the edge
	 * @param v is the end of the edge
	 * @return true if an edge exists between u and v
	 * 
	 * Running time:  O(1).
	 */
	public boolean isEdge(Object u, Object v) {
		if (isVertex(u) && isVertex(v)) {
			VertexPair vp = new VertexPair(u, v);
			if (edgeTable.find(vp) != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
	 * an edge (including the case where either of the parameters u and v does
	 * not represent a vertex of the graph).
	 *
	 * (NOTE:  A well-behaved application should try to avoid calling this
	 * method for an edge that is not in the graph, and should certainly not
	 * treat the result as if it actually represents an edge with weight zero.
	 * However, some sort of default response is necessary for missing edges,
	 * so we return zero.  An exception would be more appropriate, but
	 * also more annoying.)
	 * 
	 * @param u is the start of the edge
	 * @param v is the end of the edge
	 * @return the weight of the edge defined by u and v
	 *
	 * Running time:  O(1).
	 */
	public int weight(Object u, Object v) {
		if (isEdge(u, v)) {
			VertexPair vp = new VertexPair(u, v);
			return ((Edge) edgeTable.find(vp).value()).getWeight();
		}
		return 0;
	}

}
