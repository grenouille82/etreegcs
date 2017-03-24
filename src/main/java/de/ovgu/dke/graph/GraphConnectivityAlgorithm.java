package de.ovgu.dke.graph;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import de.ovgu.dke.util.UnmodifiableIterator;

public class GraphConnectivityAlgorithm<G extends Graph<? extends Vertex,? extends Edge<? extends Vertex>>> 
implements GraphAlgorithm<G>  
{
	private Collection<G> components;
	
	private HashMap<Vertex, Integer> connectionMap;
	
	private int nComponents;
	
	public void process(G graph) 
	{
		if(graph == null)
			throw new NullPointerException();

		initialize(graph.numberOfVertices());
		Iterator<? extends Vertex> it = graph.vertices();
		while(it.hasNext()) {
			Vertex v = it.next();
			if(connectionMap.get(v) == null) {
				computeConnectivityDFS(v, graph);
				nComponents++;		
			}
		}
		extractComponents(graph);
	}
	
	public boolean areConnected(Vertex v, Vertex w)
	{
		return connectionMap.get(v) == connectionMap.get(w);
	}
	
	public int numberOfComponents()
	{
		return nComponents;
	}
	
	public Iterator<G> components()
	{
		return new UnmodifiableIterator<G>(components);
	}
	
	private final void computeConnectivityDFS(Vertex v, G graph)
	{
		connectionMap.put(v, nComponents);
		Iterator<? extends Edge<? extends Vertex>> it = graph.incidentEdges(v);
		while(it.hasNext()) {
			Edge<? extends Vertex> edge = it.next();
			Vertex w = edge.getOpposite(v);
			if(connectionMap.get(w) == null)
				computeConnectivityDFS(w, graph);
		}
	}
	
	private final void extractComponents(G graph)
	{
		if(nComponents != 1) {
			List<Vertex> ccArray[] = new List[nComponents];  
			for(Entry<Vertex, Integer> entry : connectionMap.entrySet())
			{
				int component = entry.getValue();
				if(ccArray[component] == null)
					ccArray[component] = new LinkedList<Vertex>();
				ccArray[component].add(entry.getKey());
			}
			
			for(int i=0; i<nComponents; i++)
			{
				G subGraph = (G) graph.extractSubGraph(ccArray[i]);
				components.add(subGraph);
			}
		} else
			components.add(graph);
	}
	
	private final void initialize(int capacity)
	{
		connectionMap = new HashMap<Vertex, Integer>(capacity);
		components	  = new LinkedList<G>();
		nComponents	  = 0;
	}

}
