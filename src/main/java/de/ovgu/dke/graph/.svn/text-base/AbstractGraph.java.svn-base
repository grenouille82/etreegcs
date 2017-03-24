package de.ovgu.dke.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import de.ovgu.dke.util.UnmodifiableIterator;

public abstract class AbstractGraph<V extends Vertex, E extends Edge<V>>
implements Graph<V, E> 
{
	protected LinkedHashMap<V, Collection<E>> adjacencyList;
	
	protected int nVertices;
	protected int nEdges;
	
	public AbstractGraph()
	{
		initialize(20);
	}
	
	public AbstractGraph(Collection<? extends V> vertices)
	{
		if(vertices == null)
			throw new NullPointerException();
		
		initialize(vertices.size());
		for(V v : vertices)
			adjacencyList.put(v, new LinkedList<E>());
		nVertices = vertices.size();
	}
	
	public AbstractGraph(Graph<? extends V, ? extends E> anotherGraph)
	{
		if(anotherGraph == null)
			throw new NullPointerException();
		
		initialize(anotherGraph.numberOfVertices());
		
		nVertices = anotherGraph.numberOfVertices();
		nEdges 	  = anotherGraph.numberOfEdges();
		
		Iterator<? extends V> vIt = anotherGraph.vertices();
		while(vIt.hasNext()) 
		{
			V v = vIt.next();
			Collection<E> edges = new LinkedList<E>();
			Iterator<? extends E> eIt = anotherGraph.incidentEdges(v);
			while(eIt.hasNext()) 
			{
				E e = eIt.next();
				edges.add((E) e.copy());
			}
			adjacencyList.put((V) v.copy(), edges);
		}
	}
	
	public AbstractGraph(AbstractGraph<? extends V,? extends E> anotherGraph)
	{
		if(anotherGraph == null)
			throw new NullPointerException();
		
		initialize(anotherGraph.numberOfVertices());
		
		nVertices = anotherGraph.numberOfVertices();
		nEdges 	  = anotherGraph.numberOfEdges();
		
		V vertex = null;
		Collection<? extends E> edges = null;
		for(Entry<? extends V, ? extends Collection<? extends E>> entry : 
			anotherGraph.adjacencyList.entrySet()) 
		{
			vertex 	= entry.getKey();
			edges	= entry.getValue();
			
			Collection<E> copiedEdges = new LinkedList<E>();
			for(E e : edges)
			{
				//throw a runtime exception if the copy() method of the edge wasn't
				//ridiclously implemented
				copiedEdges.add((E) e.copy());
			}
//			throw a runtime exception if the copy() method of the vertex wasn't
			//ridiclously implemented
			adjacencyList.put((V) vertex.copy(), copiedEdges);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#addVertex(V)
	 */
	public boolean addVertex(V v)
	{
		if(v == null)
			throw new NullPointerException();
		
		boolean retVal = false;
		if(!adjacencyList.containsKey(v)) {
			adjacencyList.put(v, new LinkedList<E>());
			retVal = true;
			nVertices++;
		}
		return retVal;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#removeVertex(de.ovgu.dke.clustering.algorithm.gcs.Vertex)
	 */
	public boolean removeVertex(Vertex v)
	{
		if(v == null)
			throw new NullPointerException();
		
		boolean retVal = false;
		Collection<E> edges = adjacencyList.remove(v);
		if(edges != null) {
			for(E e : edges) {
				V opposite = e.getOpposite(v);
				Collection<E> oEdges = adjacencyList.get(opposite);
				oEdges.remove(e);
			}
			retVal = true;
			nVertices--;
			nEdges -= edges.size();
		}
		return retVal;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#addEdge(E)
	 */
	public boolean addEdge(E e)
	{
		if(e == null)
			throw new NullPointerException();
		
		boolean retVal = false;
		
		V v1 = e.getSource();
		V v2 = e.getDestination();
		boolean newVertex = addVertex(v1);
		newVertex = addVertex(v2);
		
		Collection<E> edges = null;
		if(newVertex || !areConnected(v1, v2)) {
			edges = adjacencyList.get(v1);
			edges.add(e);
			edges = adjacencyList.get(v2);
			edges.add(e);
			retVal = true;
			nEdges++;
		} 
		return retVal;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#removeEdge(de.ovgu.dke.clustering.algorithm.gcs.Edge)
	 */
	public boolean removeEdge(Edge<? extends Vertex> e)
	{
		if(e == null)
			throw new NullPointerException();
		
		boolean retVal = false;
		Vertex v1 = e.getSource();
		Vertex v2 = e.getDestination();
		Collection<E> edges1 = adjacencyList.get(v1);
		Collection<E> edges2 = adjacencyList.get(v2);
		if(edges1 != null && edges2 != null) {
			retVal = edges1.remove(e);
			if(retVal) {
				edges2.remove(e);
				nEdges--;
			}
		}
		return retVal;
	}
	
	public E removeEdge(Vertex v1, Vertex v2)
	{
		if(v1 == null)
			throw new NullPointerException();
		if(v2 == null)
			throw new NullPointerException();

		E retVal = null;
		Collection<E> edges = adjacencyList.get(v1);
		if(edges != null) {
			Iterator<E> it = edges.iterator();
			while(it.hasNext()) {
				E edge = it.next();
				if(edge.contains(v2)) {
					it.remove();
					Collection<E> oppositeEdges = adjacencyList.get(v2);
					oppositeEdges.remove(edge);
					nEdges--;
					retVal = edge;
					break;
				}
			}
		}
		
		return retVal;
	}
	
	public E getEdge(Vertex v1, Vertex v2)
	{
		if(v1 == null)
			throw new NullPointerException();
		if(v2 == null)
			throw new NullPointerException();
		
		E retVal = null;
		Collection<E> edges = adjacencyList.get(v1);
		Vertex opposite = v2;
		if(edges != null) {
			Collection<E> tmpEdges = adjacencyList.get(v2);
			if(tmpEdges != null) {
				if(tmpEdges.size()<edges.size()) {
					edges = tmpEdges;
					opposite = v1;
				}
				for(E e : edges) 
				{
					if(e.contains(opposite)) {
						retVal = e;
						break;
					}
				}
			}
		}
		
		return retVal;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#areConnected(V, V)
	 */
	public boolean areConnected(Vertex v1, Vertex v2)
	{
		return getEdge(v1, v2) != null;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#degree(de.ovgu.dke.clustering.algorithm.gcs.Vertex)
	 */
	public int degree(Vertex v) 
	{
		if(v == null)
			throw new NullPointerException();
		Collection<E> edges = adjacencyList.get(v);
		return (edges != null) ? edges.size() : 0;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#numberOfVertices()
	 */
	public int numberOfVertices()
	{
		return nVertices;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#numberOfEdges()
	 */
	public int numberOfEdges()
	{
		return nEdges;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#containsVertex(de.ovgu.dke.clustering.algorithm.gcs.Vertex)
	 */
	public boolean containsVertex(Vertex v)
	{
		return adjacencyList.containsKey(v);
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#containsEdge(de.ovgu.dke.clustering.algorithm.gcs.Edge)
	 */
	public boolean containsEdge(Edge<? extends Vertex> e)
	{
		if(e == null)
			throw new NullPointerException();
		
		boolean retVal = false;
		
		Vertex v1 = e.getSource();
		Vertex v2 = e.getDestination();
		
		Collection<E> edges = adjacencyList.get(v1);
		if(edges != null) {
			Collection<E> tmpEdges = adjacencyList.get(v2);
			if(tmpEdges != null) {
				if(tmpEdges.size()<edges.size()) 
					edges = tmpEdges;
				retVal = edges.contains(e);
			}
		}
		
		return retVal;
	}
	
	public Iterator<E> incidentEdges(Vertex v)
	{
		if(v == null)
			throw new NullPointerException();
		
		Collection<E> edges = adjacencyList.get(v);
		return (edges != null) ? edges.iterator() : null;
		//return (edges != null) ? new UnmodifiableIterator<E>(edges) : null;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.clustering.algorithm.gcs.Graph#vertices()
	 */
	public Iterator<V> vertices()
	{
		return new UnmodifiableIterator<V>(adjacencyList.keySet());
	}
	
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		for(Entry<V, Collection<E>> entry: adjacencyList.entrySet())
		{
			V vertex = entry.getKey();
			Collection<E> edges = entry.getValue();
			s.append(vertex.toString());
			s.append(": ");
			
			int i=1;
			for(E e : edges)
			{
				s.append(e.toString());
				if(i++ < edges.size())
					s.append(", ");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o instanceof Graph) {
			Graph<? extends Vertex, ? extends Edge> anotherGraph = 
				(Graph<? extends Vertex, ? extends Edge>) o;
			if(nVertices == anotherGraph.numberOfVertices() && 
			   nEdges == anotherGraph.numberOfEdges()) {
				Iterator<? extends Vertex> vIt = anotherGraph.vertices();
				while(vIt.hasNext())
				{
					if(!adjacencyList.containsKey(vIt.next()))
						return false;
				}
				
				vIt = anotherGraph.vertices();
				while(vIt.hasNext())
				{
					Vertex v = vIt.next();
					Collection<E> edges = adjacencyList.get(v);
					Iterator<? extends Edge> eIt = anotherGraph.incidentEdges(v);
					while(eIt.hasNext())
					{
						if(!edges.contains(eIt.next()))
							return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean equals(AbstractGraph<? extends V, ? extends E> anotherGraph)
	{
		if(this == anotherGraph)
			return true;
		if(nVertices == anotherGraph.nVertices && nEdges == anotherGraph.nEdges) 
			return adjacencyList.equals(anotherGraph.adjacencyList);
		return false;
	}
	
	public int hashCode()
	{
		int hash = 19;
		return hash += 37*adjacencyList.hashCode();
	}
	
	public boolean isEmpty()
	{
		return nVertices == 0;
	}
	
	protected void initialize(int capacity)
	{
		adjacencyList = new LinkedHashMap<V, Collection<E>>(capacity);
	}
}
