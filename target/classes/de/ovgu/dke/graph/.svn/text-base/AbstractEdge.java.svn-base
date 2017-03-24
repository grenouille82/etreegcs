package de.ovgu.dke.graph;

public abstract class AbstractEdge<V extends Vertex> implements Edge<V>
{
	private final V srcVertex;
	private final V destVertex;
	
	public AbstractEdge(V srcVertex, V destVertex)
	{
		if(srcVertex == null)
			throw new NullPointerException();
		if(destVertex == null)
			throw new NullPointerException();
		
		this.srcVertex 	= srcVertex;
		this.destVertex	= destVertex;
	}
	
	public AbstractEdge(Edge<? extends V> anotherEdge)
	{
		if(anotherEdge == null)
			throw new NullPointerException();
		srcVertex 	= anotherEdge.getSource();
		destVertex	= anotherEdge.getDestination();
	}
	
	public V getSource()
	{
		return srcVertex;
	}
	
	public V getDestination()
	{
		return destVertex;
	}
	
	public boolean contains(Vertex v) 
	{
		return srcVertex.equals(v) || destVertex.equals(v);
	}
	
	public V getOpposite(Vertex v) 
	{
		if(v == null)
			throw new NullPointerException();
		
		if(srcVertex.equals(v)) 
			return destVertex;
	    else if(destVertex.equals(v)) 
	    	return srcVertex;
	    else 
	    	return null;
	}
	
	public int hashCode() 
	{
		int hash = 17*srcVertex.hashCode();
		hash ^= 19*destVertex.hashCode();
		return hash;
	}
	
	public boolean equals(Object anotherObject) 
	{
		if(anotherObject instanceof Edge) {
			return equals((Edge) anotherObject);
		}
		return false;
	}
	
	public boolean equals(Edge<?> anotherEdge)
	{
		if(anotherEdge == this)
			return true;
		if(anotherEdge != null) {
			return anotherEdge.contains(srcVertex) &&
				   anotherEdge.contains(destVertex);
		}
		return false;
	}
	
	public String toString() 
	{
		return "e[" + srcVertex + "," + destVertex + "]";	        
	}
}
