package de.ovgu.dke.graph;

import java.util.Iterator;


public final class GraphUtil
{
	private GraphUtil() {};
	
	/**
	 * 
	 * @param <V>
	 * @param <E>
	 * @param dest
	 * @param src
	 */
	public static <V extends Vertex, E extends Edge<V>> void merge(Graph<? super V ,? super E> dest, 
																   Graph<? extends V, ? extends E> src)
	{
		if(dest == null)
			throw new NullPointerException();
		if(src == null)
			throw new NullPointerException();
		
		if(dest != src) {
			Iterator<? extends V> vIt = src.vertices();
			while(vIt.hasNext()) 
			{
				V vertex = vIt.next();
				Iterator<? extends E> eIt = src.incidentEdges(vertex);
				while(eIt.hasNext()) 
					dest.addEdge(eIt.next());	
			}
		}
	}
}
