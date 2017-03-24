package de.ovgu.dke.clustering.algorithm.competitive;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Settings;

public class LocalSearchMethod implements WinnerSearchMethod 
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
		
		LinkedList<CompetitiveTreeNode> candidates = null;
		if(tree.size() == 1) {
			candidates = new LinkedList<CompetitiveTreeNode>();
			candidates.add(tree.getRoot());
		} else {
			candidates = localSearch(tree.getRoot(), input, tree, metric);
		}
		
		CompetitiveTreeNode winnerNode = null;
		CompetitiveVertex winnerVertex = null;
		double minDist = Double.POSITIVE_INFINITY;	
		for(CompetitiveTreeNode node : candidates)
		{
			CompetitiveGraph graph = node.getGraph();
			boolean found = false;
			Iterator<CompetitiveVertex> it = graph.vertices();
			while(it.hasNext()) 
			{
				CompetitiveVertex vertex = it.next();
				double dist = metric.getDistance(vertex.getWeightVector(), input);
				if(dist<minDist) {
					minDist = dist;
					winnerVertex = vertex;
					found = true;
				}
			}
			
			if(found)
				winnerNode = node;
		}
		
		return new SearchResult(winnerVertex, winnerNode, minDist);
	}

	private final LinkedList<CompetitiveTreeNode> localSearch(CompetitiveTreeNode node, double[] input, 
									   			   			  CompetitiveTree tree, DistanceMeasure metric)
	{
		CompetitiveTreeNode winner = null;
		CompetitiveTreeNode second = null;
		double distWinner = Double.POSITIVE_INFINITY;
		double distSecond = Double.POSITIVE_INFINITY;
		
		List<CompetitiveTreeNode> children = tree.childNodes(node);
		for(CompetitiveTreeNode candidate : children)
		{
			double dist = metric.getDistance(input, candidate.getWeightVector());
			if(dist < distSecond) {
				distSecond = dist;
				second = candidate;
				if(distSecond < distWinner) {
					double tmpDist = distSecond;
					CompetitiveTreeNode tmpN = second;
					distSecond 	= distWinner;
					second	 	= winner;
					distWinner 	= tmpDist;
					winner		= tmpN;
				}
			}
		}

		LinkedList<CompetitiveTreeNode> retVal = new LinkedList<CompetitiveTreeNode>();
		if(!tree.isLeaf(winner))
			retVal.addAll(localSearch(winner, input, tree, metric));
		else
			retVal.add(winner);
		
		if(second!=winner) {
			if(!tree.isLeaf(second))
				second = greedySearch(second, input, tree, metric);
			retVal.add(winner);
		}
		
		return retVal;
	}
	
	public void applyDefaultSettings() {}

	public void applySettings(Settings settings) {}

	public Settings getDefaultSettings() { return null; }

	public Settings getSettings() { return null; }

	
	private final CompetitiveTreeNode greedySearch(CompetitiveTreeNode node, double[] input, 
												   CompetitiveTree tree, DistanceMeasure metric)
	{		
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
		if(tree.isLeaf(winner))
			return node;
		else
			return greedySearch(winner, input, tree, metric);
	}
}
