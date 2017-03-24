package de.ovgu.dke.graph;

public abstract class AbstractVertex implements Vertex 
{
	private final int id;
	
	public AbstractVertex(int id)
	{
		if(id < 0)
			throw new IllegalArgumentException(
			"id must be greater than or equal 0:  " + id);
		this.id = id;
	}
	
	public AbstractVertex(Vertex anotherVertex)
	{
		if(anotherVertex == null)
			throw new NullPointerException();
		id = anotherVertex.getId();
	}
	
	public int getId() 
	{
		return id;
	}
	
	public boolean equals(Object anotherObject)
	{
		if(anotherObject instanceof Vertex) {
			return equals((Vertex) anotherObject);
		}
		return false;
	}
	
	public boolean equals(Vertex anotherVertex)
	{
		if(this == anotherVertex)
			return true;
		if(anotherVertex != null) {
			return id == anotherVertex.getId();
		}
		return false;
	}
	
	public int hashCode()
	{
		return id;
	}
	
	public String toString()
	{
		return "Vertex_"+id;
	}
}
