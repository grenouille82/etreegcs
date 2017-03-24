package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import de.ovgu.dke.graph.AbstractGraph;
import de.ovgu.dke.graph.Graph;
import de.ovgu.dke.graph.Vertex;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.UnmodifiableIterator;

public class CompetitiveGraph extends AbstractGraph<CompetitiveVertex, CompetitiveEdge> 
implements CompetitiveDataStructure
{	
	private Collection<CompetitiveSimplex> simplices;
	
	public CompetitiveGraph() 
	{
		super();
		simplices = new LinkedList<CompetitiveSimplex>();
	}
	
	public CompetitiveGraph(Collection<? extends CompetitiveVertex> vertices)
	{
		super(vertices);
		simplices = new LinkedList<CompetitiveSimplex>();
	}
	
	
	public CompetitiveGraph(Graph<? extends CompetitiveVertex, ? extends CompetitiveEdge> anotherGraph)
	{
		super(anotherGraph);
		simplices = new LinkedList<CompetitiveSimplex>();
	}

	public Collection<CompetitiveVertex> getNeighbors(CompetitiveVertex vertex)
	{
		if(vertex == null)
			throw new NullPointerException();
		
		Collection<CompetitiveVertex> retVal = new LinkedList<CompetitiveVertex>();
		Iterator<CompetitiveEdge> it = incidentEdges(vertex);
		while(it.hasNext()) 
		{
			CompetitiveEdge e = it.next();
			retVal.add(e.getOpposite(vertex));
		}
		return retVal;
	}
	
	
	public void addSimplex(CompetitiveSimplex simplex)
	{
		if(simplex == null)
			throw new NullPointerException();
		
		simplices.add(simplex);
		CompetitiveVertex[] vertices = simplex.getVertices();
		
		//add new wertices
		for(CompetitiveVertex v : vertices)
			addVertex(v);
		//add new conncetions between the vertices of the simplex 
		//if not present
		for(int i=0; i<simplex.dimension(); i++)
			for(int j=i+1; j<simplex.dimension(); j++)
				addEdge(new CompetitiveEdge(vertices[i], vertices[j]));
	}
	
	public Collection<CompetitiveVertex> removeSimplex(CompetitiveSimplex simplex)
	{
		if(simplex == null)
			throw new NullPointerException();
	
	//	System.out.println(this);
		
		Collection<CompetitiveVertex> remVertices = new LinkedList<CompetitiveVertex>();
		
		if(simplices.remove(simplex)) {
			CompetitiveVertex[] vertices = simplex.getVertices();
			int n = vertices.length;
			for(int i=0; i<n; i++)
			{
				for(int j=i+1; j<n; j++)
				{
					boolean found = false;
					for(CompetitiveSimplex s : simplices)
					{
						if(s.contains(vertices[i]) && s.contains(vertices[j])) {
							found = true;
							break;
						}
					}
					
					if(!found) {
						removeEdge(vertices[i], vertices[j]);
						if(degree(vertices[i]) == 0) {
							removeVertex(vertices[i]);
							remVertices.add(vertices[i]);
						}
						if(degree(vertices[j]) == 0) {
							removeVertex(vertices[j]);
							remVertices.add(vertices[j]);
						}
					}
				}
			}
		}	
		return remVertices;
	}
	
	public int numberOfSimplices()
	{
		return simplices.size();
	}
	
	public Iterator<CompetitiveSimplex> simplices()
	{
		return new UnmodifiableIterator<CompetitiveSimplex>(simplices.iterator());
	}


	public CompetitiveGraph copy() 
	{
		return new CompetitiveGraph(this);
	}	
	
	public CompetitiveGraph extractSubGraph(Collection<? extends CompetitiveVertex> vertices) 
	{
		if(vertices == null)
			throw new NullPointerException();
		
		CompetitiveGraph subGraph = new CompetitiveGraph();
		for(CompetitiveVertex v : vertices)
		{
			if(adjacencyList.containsKey(v))
				subGraph.addVertex(v);
		}
		
		int edgeCnt = 0;

		for(Entry<CompetitiveVertex, Collection<CompetitiveEdge>> e : subGraph.adjacencyList.entrySet()) 
		{
			CompetitiveVertex v = e.getKey();
			Iterator<CompetitiveEdge> it = incidentEdges(v);
			while(it.hasNext()) {
				CompetitiveEdge edge = it.next();
				CompetitiveVertex w = edge.getOpposite(v);
				if(subGraph.containsVertex(w)) {
					e.getValue().add(edge);
					edgeCnt++;
					//System.out.println(edgeCnt);
				}
			}
		}
	
		subGraph.nEdges = edgeCnt/2;
	
		for(CompetitiveSimplex simplex : simplices) 
		{
			boolean exists = true;
			for(CompetitiveVertex v : simplex.getVertices())
			{
				if(!subGraph.containsVertex(v)) {
					exists = false;
					break;
				}
			}
			if(exists)
				subGraph.simplices.add(simplex);
		}
		
		return subGraph;
	}
	

	public double totalError()
	{
		double retVal = 0d;
		for(CompetitiveVertex v : adjacencyList.keySet())
			retVal += v.getError();
		return retVal;
	}
	
	public double meanError()
	{
		return totalError()/nVertices;
	}
	
	public double weightedMeanError()
	{
		double retVal = 0d;
		for(CompetitiveVertex v : adjacencyList.keySet())
			retVal += v.getError();
		return retVal;
	}
	
	public double maxError()
	{
		double retVal = Double.NEGATIVE_INFINITY;
		for(CompetitiveVertex v : adjacencyList.keySet())
			retVal = Math.max(retVal, v.getError());
		return retVal;
	}
	
	public double minError()
	{
		double retVal = Double.POSITIVE_INFINITY;
		for(CompetitiveVertex v : adjacencyList.keySet())
			retVal = Math.min(retVal, v.getError());
		return retVal;
	}
	
	public double avgDistance(DistanceMeasure metric)
	{
		if(metric == null)
			throw new NullPointerException();
		
		double retVal = 0d;
		CompetitiveVertex[] vertices  = adjacencyList.keySet().toArray(new CompetitiveVertex[0]);
		for(int i=0; i<nVertices; i++)
			for(int j=i+1; j<nVertices; j++)
				retVal += metric.getDistance(vertices[i].getWeightVector(), 
											 vertices[j].getWeightVector());
		
		return retVal/ (double) (nVertices*(nVertices-1)/2);
	}
	
	public double minDistance(DistanceMeasure metric)
	{
		if(metric == null)
			throw new NullPointerException();
		double retVal = Double.POSITIVE_INFINITY;
		CompetitiveVertex[] vertices  = adjacencyList.keySet().toArray(new CompetitiveVertex[0]);
		for(int i=0; i<nVertices; i++)
			for(int j=i+1; j<nVertices; j++)
			{
				double dist = metric.getDistance(vertices[i].getWeightVector(), 
						 						 vertices[j].getWeightVector());
				if(dist<retVal)
					retVal = dist;
			}
		return retVal;
	}
	
	public double maxDistance(DistanceMeasure metric)
	{
		if(metric == null)
			throw new NullPointerException();
		double retVal = Double.NEGATIVE_INFINITY;
		CompetitiveVertex[] vertices  = adjacencyList.keySet().toArray(new CompetitiveVertex[0]);
		for(int i=0; i<nVertices; i++)
			for(int j=i+1; j<nVertices; j++)
			{
				double dist = metric.getDistance(vertices[i].getWeightVector(), 
						 						 vertices[j].getWeightVector());
				if(dist > retVal)
					retVal = dist;
			}
		return retVal;
	}

	public double totalError(ObjectSet dataset, DistanceMeasure metric)
	{
		double retVal = 0;
		for(int i=0, n=dataset.size(); i<n; i++)
		{
			double v[] = dataset.get(i).getRepresentation();
			retVal += MathUtil.square(findMinDistance(v, metric));
		}
		return retVal;
	}
	
	private double findMinDistance(double[] v, DistanceMeasure metric)
	{
		double retVal = Double.POSITIVE_INFINITY;
		CompetitiveVertex[] vertices  = adjacencyList.keySet().toArray(new CompetitiveVertex[0]);
		for(int i=0; i<nVertices; i++)
		{
			double dist = metric.getDistance(v, vertices[i].getWeightVector());
			if(dist < retVal)
				retVal = dist;
		}
		return retVal;
	}
	
	public String toString()
	{
		return super.toString() +"\n" + simplices.toString();
	}

	@Override
	public Graph<CompetitiveVertex, CompetitiveEdge> extractSubGraph(Collection<? extends Vertex> vertices) 
	{
		if(vertices == null)
			throw new NullPointerException();
		
		CompetitiveGraph subGraph = new CompetitiveGraph();
		for(Vertex v : vertices)
		{
			if(adjacencyList.containsKey(v))
				subGraph.addVertex((CompetitiveVertex) v);
		}
		
		
		
		int edgeCnt = 0;

		for(Entry<CompetitiveVertex, Collection<CompetitiveEdge>> e : subGraph.adjacencyList.entrySet()) 
		{
			CompetitiveVertex v = e.getKey();
			Iterator<CompetitiveEdge> it = incidentEdges(v);
			while(it.hasNext()) {
				CompetitiveEdge edge = it.next();
				CompetitiveVertex w = edge.getOpposite(v);
				if(subGraph.containsVertex(w)) {
					e.getValue().add(edge);
					edgeCnt++;
					//System.out.println(edgeCnt);
				}
			}
		}
	
		subGraph.nEdges = edgeCnt/2;
	
		for(CompetitiveSimplex simplex : simplices) 
		{
			boolean exists = true;
			for(CompetitiveVertex v : simplex.getVertices())
			{
				if(!subGraph.containsVertex(v)) {
					exists = false;
					break;
				}
			}
			if(exists)
				subGraph.simplices.add(simplex);
		}
		
		return subGraph;
	}
}
