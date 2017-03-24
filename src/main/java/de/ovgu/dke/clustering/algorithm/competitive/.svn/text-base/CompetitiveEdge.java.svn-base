package de.ovgu.dke.clustering.algorithm.competitive;

import de.ovgu.dke.graph.AbstractEdge;

public class CompetitiveEdge extends AbstractEdge<CompetitiveVertex>
{
	private int age;
	
	public CompetitiveEdge(CompetitiveVertex srcVertex, CompetitiveVertex destVertex) 
	{
		super(srcVertex, destVertex);
	}

	public CompetitiveEdge(CompetitiveEdge anotherEdge)
	{
		super(anotherEdge);
	}
	
	public CompetitiveEdge copy() 
	{
		return new CompetitiveEdge(this);
	}
	
	public int getAge() { return age; }
	
	public void resetAge() 
	{
		age = 0;
	}
	
	public void incrementAge()
	{
		age++;
	}
}
