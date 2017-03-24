package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Iterator;
import java.util.List;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Settings;

public class NaiveSearchMethod implements WinnerSearchMethod 
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
		
		double minDist = Double.POSITIVE_INFINITY;
		CompetitiveVertex winnerVertex = null;
		CompetitiveTreeNode winnerNode = null;
		List<CompetitiveTreeNode> leafs = tree.getLeafs();
		for(CompetitiveTreeNode node : leafs)
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

	public void applyDefaultSettings() {}

	public void applySettings(Settings settings) {}

	public Settings getDefaultSettings() { return null; }

	public Settings getSettings() { return null; }
}
