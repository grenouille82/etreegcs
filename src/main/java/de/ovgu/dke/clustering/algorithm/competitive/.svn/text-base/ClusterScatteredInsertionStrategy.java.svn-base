package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Settings;

public class ClusterScatteredInsertionStrategy implements TreeNodeInsertionStrategy 
{

	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.COSINE;
	private DistanceMeasure metric;

	public ClusterScatteredInsertionStrategy()
	{
		applyDefaultSettings();
	}
	
	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent,
					   CompetitiveTree tree) 
	{
		insert(insNodes, parent, tree, metric);
	}
	
	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent, 
					   CompetitiveTree tree, DistanceMeasure metric) 
	{
		if(insNodes == null)
			throw new NullPointerException();
		if(parent == null)
			throw new NullPointerException();
		if(tree == null)
			throw new NullPointerException();
		if(metric == null)
			throw new NullPointerException();
		if(insNodes.isEmpty())
			throw new IllegalArgumentException("the number of inserted nodes must be greater than 0");
		
		if(insNodes.size() == 1) {
			CompetitiveTreeNode grand = tree.parentNode(parent);
			tree.removeNode(parent);
			CompetitiveTreeNode node = insNodes.iterator().next();
			tree.appendNode(grand, node);
		} else {
			if(tree.isRoot(parent)) {
				for(CompetitiveTreeNode node : insNodes)
					tree.appendNode(parent, node);
			} else {
				CompetitiveTreeNode grand = tree.parentNode(parent);
				ArrayList<CompetitiveTreeNode> testCandidates = new ArrayList<CompetitiveTreeNode>(tree.childNodes(grand));
				double currScattering = computeClusterScattering(testCandidates, metric);
				testCandidates.remove(parent);
				testCandidates.addAll(insNodes);
				double scattering = computeClusterScattering(testCandidates, metric);
				
				System.out.println("currScattering " + currScattering + "\t scattering" + scattering);
				
				if(scattering <= currScattering) {
					tree.removeNode(parent);
					for(CompetitiveTreeNode node : insNodes)
						tree.appendNode(grand, node);
				} else
					for(CompetitiveTreeNode node : insNodes)
						tree.appendNode(parent, node);
			}
		}
	}

	public void setDistanceMeasure(DistanceMeasure metric) 
	{
		if(metric == null)
			throw new NullPointerException();
		this.metric = metric;
	}

	public void applyDefaultSettings() 
	{
		metric = DEFAULT_METRIC;
		
	}

	public void applySettings(Settings settings) {
		// TODO Auto-generated method stub
		
	}

	public Settings getDefaultSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	public Settings getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	private final double computeClusterScattering(ArrayList<CompetitiveTreeNode> candidates, DistanceMeasure metric)
	{	
	
		double maxIntraDist = Double.NEGATIVE_INFINITY;
		double minInterDist = Double.POSITIVE_INFINITY;
		
		for(CompetitiveTreeNode node : candidates)
		{
			double intraDist = node.getError()/node.getSignalCounter();
			if(intraDist > maxIntraDist)
				maxIntraDist = intraDist;
		}
		
		for(int i=0, n=candidates.size(); i<n; i++)
		{
			CompetitiveTreeNode a = candidates.get(i);
			for(int j=i+1; j<n; j++)
			{
				CompetitiveTreeNode b = candidates.get(j);
				double dist = metric.getDistance(a.getWeightVector(), b.getWeightVector());
				if(dist < minInterDist)
					minInterDist = dist;
			}
		}
		System.out.println("intra " + maxIntraDist + "\tinter" +  minInterDist);
		
		
		return maxIntraDist/minInterDist;
	}
}
