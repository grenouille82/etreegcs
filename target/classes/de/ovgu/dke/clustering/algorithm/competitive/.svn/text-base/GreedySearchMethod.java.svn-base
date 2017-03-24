package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Iterator;
import java.util.List;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Settings;

public class GreedySearchMethod implements WinnerSearchMethod
{

	public SearchResult findWinner(double[] input, CompetitiveTree tree, DistanceMeasure metric) 
	{
		if(input == null)
			throw new NullPointerException();
		if(tree == null)
			throw new NullPointerException();
		if(metric == null)
			throw new NullPointerException();
		if(tree.isEmpty())
			throw new IllegalArgumentException("cannot applied for an empty tree");
		
		CompetitiveTreeNode winnerNode = null;
		if(tree.size() == 1) {
			winnerNode = tree.getRoot();
		} else {
			winnerNode = searchForWinnerRecursive(tree.getRoot(), input, tree, metric);
		}
		
		double minDist = Double.POSITIVE_INFINITY;
		CompetitiveVertex winnerVertex = null;
		
		CompetitiveGraph graph = winnerNode.getGraph();
		Iterator<CompetitiveVertex> it = graph.vertices();
		while(it.hasNext()) 
		{
			CompetitiveVertex vertex = it.next();
			double dist = metric.getDistance(input, vertex.getWeightVector());
			if(dist < minDist) {
				minDist = dist;
				winnerVertex = vertex;
			}
		}
		
		return new SearchResult(winnerVertex, winnerNode, minDist);
	}
	
	public void applyDefaultSettings() {}

	public void applySettings(Settings settings) {}

	public Settings getDefaultSettings() { return null; }

	public Settings getSettings() { return null; }

	
	private final CompetitiveTreeNode searchForWinnerRecursive(CompetitiveTreeNode node, double[] input, 
															   CompetitiveTree tree, DistanceMeasure metric)
	{
		if(tree.isLeaf(node))
			return node;
		
		CompetitiveTreeNode winner = null;
		double minDist = Double.POSITIVE_INFINITY;
		
		List<CompetitiveTreeNode> children = tree.childNodes(node);
		for(CompetitiveTreeNode candidate : children)
		{
			double dist = metric.getDistance(input, candidate.getWeightVector());
			if(dist < minDist) {
				minDist = dist;
				winner	= candidate;
			}
		}
		return searchForWinnerRecursive(winner, input, tree, metric);
	}

	
}
