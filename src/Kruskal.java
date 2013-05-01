/* Kruskal.java */

import graph.*;
import set.*;
import hashTable.*;
import queue.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

	/**
	 * minSpanTree() returns a WUGraph that represents the minimum spanning tree
	 * of the WUGraph g.  The original WUGraph g is NOT changed.
	 */
	public static WUGraph minSpanTree(WUGraph g) {

		WUGraph t = new WUGraph();
		LinkedQueue q = new LinkedQueue();
		Object[] vertices = g.getVertices();
		HashTable hashTable = new HashTable();
		
		for (int i = 0; i < vertices.length; i++) {

			// 1. Add vertices to T
			t.addVertex(vertices[i]);

			// 2. Make a list of edges in G
			Neighbors neighbors = g.getNeighbors(vertices[i]);
			Object[] connected = neighbors.neighborList;
			int[] weights = neighbors.weightList;
			
			for (int j = 0; j < connected.length; j++) {
				KruskalEdge edge = new KruskalEdge(vertices[i], connected[j], weights[j]);
				VertexPair vp = new VertexPair(vertices[i], connected[j]);
				if (q.isEmpty() || (hashTable.find(vp) == null)) {
					q.enqueue(edge);
					hashTable.insert(vp, edge);
				}
			}
		}

		// 3. Sort edges

		quickSort(q);	

		// 4. Find edges of T
		
		DisjointSets dSet = new DisjointSets(vertices.length);
		HashTable vertexTable = new HashTable();
		for (int i = 0; i < vertices.length; i++) {
			vertexTable.insert(vertices[i], i);
		}

		for (int i = 1; i <= q.size(); i++) {
			KruskalEdge edge = (KruskalEdge) q.nth(i);
			Object o1 = edge.getObj1();
			Object o2 = edge.getObj2();
			int i1 = (Integer) ((Entry) (vertexTable.find(o1))).value();
			int i2 = (Integer) ((Entry) (vertexTable.find(o2))).value();
			int root1 = dSet.find(i1);
			int root2 = dSet.find(i2);
			if (i1 != i2 && root1 != root2) {
				dSet.union(root1, root2);
				t.addEdge(o1, o2, edge.getWeight());
			}
		}
		return t;
	}

	// Quicksort Algorithm

	/**
	 *  partition() partitions qIn using the pivot item.  On completion of
	 *  this method, qIn is empty, and its items have been moved to qSmall,
	 *  qEquals, and qLarge, according to their relationship to the pivot.
	 *  @param qIn is a LinkedQueue of Comparable objects.
	 *  @param pivot is a Comparable item used for partitioning.
	 *  @param qSmall is a LinkedQueue, in which all items less than pivot
	 *    will be enqueued.
	 *  @param qEquals is a LinkedQueue, in which all items equal to the pivot
	 *    will be enqueued.
	 *  @param qLarge is a LinkedQueue, in which all items greater than pivot
	 *    will be enqueued.  
	 **/   
	public static void partition(LinkedQueue qIn, Comparable pivot, 
			LinkedQueue qSmall, LinkedQueue qEquals, 
			LinkedQueue qLarge) {
		try {
			while (!qIn.isEmpty()) {
				int k = ((Comparable) ((KruskalEdge) qIn.front()).getWeight()).compareTo(pivot);
				if (k < 0) {
					qSmall.enqueue(qIn.dequeue());
				} else if (k == 0) {
					qEquals.enqueue(qIn.dequeue());
				} else {
					qLarge.enqueue(qIn.dequeue());
				}
			}
		}
		catch (QueueEmptyException e) {

		}
	}


	/**
	 *  quickSort() sorts q from smallest to largest using quicksort.
	 *  @param q is a LinkedQueue of Comparable objects.
	 **/
	public static void quickSort(LinkedQueue q) {
		// Your solution here.
		int i = (int) (q.size() * Math.random());
		if (q.isEmpty() || q.size() == 1) {
			// do nothing
		} else {
			try {
				Comparable pivot = (Comparable) ((KruskalEdge) q.nth(i)).getWeight();
				LinkedQueue qSmall = new LinkedQueue();
				LinkedQueue qEquals = new LinkedQueue();
				LinkedQueue qLarge = new LinkedQueue();
				partition(q, pivot, qSmall, qEquals, qLarge);
				if (qSmall.size() == 1 && qEquals.size() == 1 && qLarge.size() == 1) {
					q.append(qSmall);
					q.append(qEquals);
					q.append(qLarge);
				} else if (qSmall.size() == 0 && qEquals.size() == 1 && qLarge.size() == 1) {
					q.append(qEquals);
					q.append(qLarge);
				} else if (qSmall.size() == 1 && qEquals.size() == 0 && qLarge.size() == 1) {
					q.append(qSmall);
					q.append(qLarge);
				} else if (qSmall.size() == 1 && qEquals.size() == 1 && qLarge.size() == 0) {
					q.append(qSmall);
					q.append(qEquals);
				} else {
					quickSort(qSmall);
					quickSort(qLarge);
				}
				q.append(qSmall);
				q.append(qEquals);
				q.append(qLarge);
			}
			catch (Exception e) {

			}
		}
	}





}
