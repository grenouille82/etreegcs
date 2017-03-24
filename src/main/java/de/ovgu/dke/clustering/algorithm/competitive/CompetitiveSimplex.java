package de.ovgu.dke.clustering.algorithm.competitive;

import de.ovgu.dke.util.Copyable;

public final class CompetitiveSimplex implements Copyable<CompetitiveSimplex>
{
	private int dimension;
	
	private CompetitiveVertex[] vertices;
	
	public CompetitiveSimplex(CompetitiveVertex... vertices)
	{
		if(vertices == null)
			throw new NullPointerException();
		this.vertices = vertices;
		dimension = vertices.length;	
	}
	
	public CompetitiveSimplex(CompetitiveSimplex anotherSimplex)
	{
		if(anotherSimplex == null)
			throw new NullPointerException();
		
		dimension = anotherSimplex.dimension;
		vertices  = new CompetitiveVertex[dimension];
		System.arraycopy(anotherSimplex.vertices, 0, vertices, 0, dimension);
	}
	
	public CompetitiveVertex[] getVertices()
	{
		return vertices;
	}
	
	public int dimension()
	{
		return dimension;
	}
	
	public boolean contains(CompetitiveVertex vertex)
	{
		for(CompetitiveVertex v : vertices)
		{
			if(v.equals(vertex)) 
				return true;
		}
		return false;
	}
	
	
	public int hashCode()
	{
		int hash = 19;
		for(CompetitiveVertex v : vertices)
			hash ^= 53*v.hashCode();
		return hash;
	}
	
	public boolean equals(Object anotherObject)
	{
		if(anotherObject instanceof CompetitiveSimplex)
			return equals((CompetitiveSimplex) anotherObject);
		return false;
	}
	
	public boolean equals(CompetitiveSimplex anotherSimplex)
	{
		if(anotherSimplex != null) {
			if(hashCode() == anotherSimplex.hashCode()) {
				for(CompetitiveVertex v : vertices) 
				{
					if(!anotherSimplex.contains(v)) 
						return false;
				}
				return true;
			}
		}
		return false;
	}
	
	public String toString()
	{
		StringBuilder s = new StringBuilder();
        s.append("Simplex[ ");
        for (int i = 0; i < dimension; i++) {
            s.append(vertices[i].toString());
            if (i < dimension - 1) s.append(" ,");
        }
        s.append(']');
        return s.toString();
	}

	public CompetitiveSimplex copy() 
	{
		return new CompetitiveSimplex(this);
	}
	
}
