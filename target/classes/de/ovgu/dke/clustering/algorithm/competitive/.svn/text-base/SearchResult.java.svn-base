package de.ovgu.dke.clustering.algorithm.competitive;

public final class SearchResult 
{
	private CompetitiveVertex winnerVertex;
	private CompetitiveTreeNode winnerNode;
	
	private double distance;
	
	public SearchResult(CompetitiveVertex vertex, CompetitiveTreeNode node, double distance)
	{
		if(vertex == null)
			throw new NullPointerException();
		if(node == null)
			throw new NullPointerException();
		winnerNode 	 = node;
		winnerVertex = vertex;
		this.distance = distance;
	}
	
	public CompetitiveVertex getWinnerVertex()
	{
		return winnerVertex;
	}
	
	public CompetitiveTreeNode getWinnerNode()
	{
		return winnerNode;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
}
