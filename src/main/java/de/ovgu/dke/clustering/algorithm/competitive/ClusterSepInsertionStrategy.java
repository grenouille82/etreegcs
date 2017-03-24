package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Collection;
import java.util.List;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Settings;

public class ClusterSepInsertionStrategy implements TreeNodeInsertionStrategy
{
	private final static double DEFAULT_INSERTION_THRESHOLD = 0.8d;

	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.COSINE;
	
	private double insertionThreshold;
	
	private DistanceMeasure metric;

	public ClusterSepInsertionStrategy()
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
				List<CompetitiveTreeNode> testNodes = tree.siblings(parent);
				testNodes.addAll(insNodes);
				ArrayIndexedMST<CompetitiveTreeNode> mst = 
					CompetitiveUtil.computeMinimumSpanningTree((CompetitiveTreeNode[]) testNodes.toArray(new CompetitiveTreeNode[0]), metric);
				
				double cs = mst.minDistance()/mst.maxDistance();
				System.out.println("sep="+cs +"\t"+ insNodes.size());
				if(cs >= insertionThreshold) {
					CompetitiveTreeNode grand = tree.parentNode(parent);
					System.out.println("rem: " + parent + "\t" + grand );
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
		insertionThreshold = DEFAULT_INSERTION_THRESHOLD;
		
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

}
