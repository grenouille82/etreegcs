package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;


import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;


public class GlobalSearchMethod implements WinnerSearchMethod
{
	private static int DEFAULT_K = 3;
	
	private int k;

	public GlobalSearchMethod()
	{
		this(DEFAULT_K);
	}
	
	public GlobalSearchMethod(int k)
	{
		if(k<=0)
			throw new IllegalArgumentException();
		this.k = k;
	}
	
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
		
		List<CompetitiveTreeNode> candidates = null;
		if(tree.size() == 1) {
			candidates = new ArrayList<CompetitiveTreeNode>(1);
			candidates.add(tree.getRoot());
		} else {
			candidates = globalSearch(tree.getRoot(), input, tree, metric);
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
	
	public void applyDefaultSettings() 
	{
		k = DEFAULT_K;
	}

	public void applySettings(Settings settings) 
	{
		if(settings != null) {
			Parameter p = settings.getParameter("k");
			if(p != null)
				setK(Integer.parseInt(p.getValue()));
		}
	}

	public Settings getDefaultSettings() 
	{
		Settings setting = new Settings();
		setting.setParameter(new Parameter("k", Integer.toString(DEFAULT_K)));
		return setting;
	}

	public Settings getSettings() 
	{
		Settings setting = new Settings();
		setting.setParameter(new Parameter("k", Integer.toString(k)));
		return setting; 
	}
	
	public void setK(int k)
	{
		if(k<=0)
			throw new IllegalArgumentException();
		this.k = k;
	}
	
	public int getK()
	{
		return k;
	}
	
	private final List<CompetitiveTreeNode> globalSearch(CompetitiveTreeNode node, double[] input, 
											  			 CompetitiveTree tree, DistanceMeasure metric)
	{
		TreeSet<WeightedNode> candidates = new TreeSet<WeightedNode>();
		
		List<CompetitiveTreeNode> children = tree.childNodes(node);
		while(!children.isEmpty()) 
		{
			for(CompetitiveTreeNode candidate : children) 
			{
				double dist = metric.getDistance(candidate.getWeightVector(), input);
				candidates.add(new WeightedNode(candidate, dist));
			}
			int i=0;
			children = new LinkedList<CompetitiveTreeNode>();
			for(WeightedNode candidate : candidates)
			{
				if(!tree.isLeaf(candidate.node)) 
					children.addAll(tree.childNodes(candidate.node));
				if(++i >= k)
					break;
			}
		}
		
		ArrayList<CompetitiveTreeNode> retVal = new ArrayList<CompetitiveTreeNode>(k);
		int i=0;
		for(WeightedNode candidate : candidates)
		{
			retVal.add(candidate.node);
			if(++i >= k)
				break;
		}
		
		return retVal;
	}

	private static class WeightedNode implements Comparable<WeightedNode>
	{
		double weight;
		CompetitiveTreeNode node;
		
		public WeightedNode(CompetitiveTreeNode node, double weight)
		{
			this.node	= node;
			this.weight	= weight;
		}

		public int compareTo(WeightedNode o) 
		{
			if(this.weight < o.weight)
				return -1;
			else if(this.weight > o.weight)
				return 1;
			else
				return 0;
		}
		
		
	}
}
