package de.ovgu.dke.clustering.evaluation;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.ClusterNode;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.tree.PostOrderTreeIterator;
import de.ovgu.dke.tree.PreOrderTreeIterator;
import de.ovgu.dke.tree.Tree;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class InstanceBasedFMeasure 
{
	protected static double DEFAULT_BETA = 1d;
	
	protected HashMap<String, Double> classValues;
	
	protected double overallValue;
	
	protected double beta;
	
	protected Tree<String> referenceLabelHierarchy;
	
	//temporary stuff
	protected HashMap<String, Collection<String>> labelCotopies;
	protected HashMap<ClusterNode, Collection<ClusterNode>> clusterCotopies;
	
	public InstanceBasedFMeasure() 
	{
		initialize();
	}

	public double getOverallValue() 
	{
		return overallValue;
	}

	public double getValue(String classLabel) 
	{
		Double retVal = classValues.get(classLabel);
		return (retVal == null) ? Double.NaN : retVal;
	}

	public void setReferenceHierarchy(Tree<String> hierarchy)
	{
		if(hierarchy == null)
			throw new NullPointerException();
		referenceLabelHierarchy = hierarchy;
		labelCotopies = computeLabelCotopies(hierarchy);
	}
	
	public void applyDefaultSettings() 
	{
		beta = DEFAULT_BETA;
	}

	public void applySettings(Settings settings) 
	{
		if(settings != null) {
			Parameter[] parameters = settings.getParameters();
			for(Parameter p : parameters)
			{
				if(p.getName().equalsIgnoreCase("beta")) 
					beta = Double.parseDouble(p.getValue());
			}
		}
	}

	public Settings getDefaultSettings() 
	{ 
		Settings settings = new Settings();
		settings.setParameter(new Parameter("beta", String.valueOf(DEFAULT_BETA)));
		return settings;	
	}

	public Settings getSettings() 
	{ 
		Settings settings = new Settings();
		settings.setParameter(new Parameter("beta", String.valueOf(beta)));
		return settings; 
	}
	
	public double compute(HierarchicalClusterModel model)
	{
		ClusterConfusionMatrix matrix = 
			ClusterConfusionMatrix.createClusterConfusionMatrix(model, false);
		return compute(model, matrix);
	}
	
	public double compute(HierarchicalClusterModel model, ClusterConfusionMatrix confMatrix)
	{
		if(model == null)
			throw new NullPointerException();
		if(confMatrix == null)
			throw new NullPointerException();
		if(referenceLabelHierarchy == null)
			throw new IllegalArgumentException();
		
		classValues.clear();
		overallValue 	= 0d;
		clusterCotopies = computeClusterCotopies(model);
		
		Collection<String> classLabels 	= confMatrix.getClassLabels();
		Collection<? extends ClusterNode> clusters 	= model.getClusters();
		int n = confMatrix.totalCount();
		for(String label : classLabels)
		{
			int classSize = confMatrix.totalClassLabelCount(label);
			double prec = 0d;
			double rec	= 0d;
			for(ClusterNode cluster : clusters)
			{
				double weight = (double) confMatrix.count(cluster, label)/classSize;
				prec += precision(cluster, label, confMatrix)*weight;
				rec	 += recall(cluster, label, confMatrix)*weight;
			}
			double fscore = (MathUtil.square(beta)+1d)*prec*rec /
							(rec+MathUtil.square(beta)*prec);
			if(Double.isNaN(fscore) || Double.isInfinite(fscore))
				fscore = 0d;
			classValues.put(label, fscore);
			overallValue += (double) classSize*fscore/n;
		}

		return overallValue;
	}

	protected double precision(ClusterNode cluster, String classLabel, 
			   				   ClusterConfusionMatrix confMatrix) 
	{
		Collection<ClusterNode> clusterCotopy = clusterCotopies.get(cluster);
		Collection<String> labelCotopy = labelCotopies.get(classLabel);
		
		int a = unionSumCotopyCounts(clusterCotopy, labelCotopy, confMatrix);
		int b = sumClusterCotopyCounts(clusterCotopy, confMatrix);
		
		double precision = (double) a/b;
		if(Double.isNaN(precision) || Double.isInfinite(precision))
			precision = 0d;
		return precision;
	}

	protected double recall(ClusterNode cluster, String classLabel, 
							ClusterConfusionMatrix confMatrix) 
	{
		Collection<ClusterNode> clusterCotopy = clusterCotopies.get(cluster);
		Collection<String> labelCotopy = labelCotopies.get(classLabel);
		
		int a = unionSumCotopyCounts(clusterCotopy, labelCotopy, confMatrix);
		int b = sumLabelCotopyCounts(labelCotopy, confMatrix);
		
		double recall = (double) a/b; 				
		if(Double.isNaN(recall) || Double.isInfinite(recall))
			recall = 0d;
		return recall;
	}
	
	private final int unionSumCotopyCounts(Collection<ClusterNode> clusters, 
			Collection<String> classLabels, ClusterConfusionMatrix confMatrix)
	{
		int retVal = 0;
		for(ClusterNode cluster : clusters)
		{
			int clusterIdx = confMatrix.indexOfCluster(cluster);
			if(clusterIdx != -1) {
				for(String label : classLabels)
				{
					int labelIdx = confMatrix.indexOfClassLabel(label);
					if(labelIdx != -1)
						retVal += confMatrix.count(clusterIdx, labelIdx);
				}
			}
		}
		return retVal;
	}
	
	private final int sumLabelCotopyCounts(Collection<String> classLabels, ClusterConfusionMatrix confMatrix)
	{
		int retVal = 0;
		for(String label : classLabels)
		{
			int idx = confMatrix.indexOfClassLabel(label);
			if(idx != -1)
				retVal += confMatrix.totalClassLabelCount(idx);
		}
		return retVal;
	}
	
	private final int sumClusterCotopyCounts(Collection<ClusterNode> clusters, ClusterConfusionMatrix confMatrix)
	{
		int retVal = 0;
		for(Cluster cluster : clusters)
		{
			int idx = confMatrix.indexOfCluster(cluster);
			if(idx != -1)
				retVal += confMatrix.totalClassLabelCount(idx);
		}
		return retVal;
	}
	
	private final HashMap<String, Collection<String>> computeLabelCotopies(Tree<String> labelTree)
	{
		HashMap<String, Collection<String>> retVal = new HashMap<String, Collection<String>>();
		for(String label : labelTree)
		{
			Collection<String> cotopies = getCotopyClassLabels(label, labelTree);
			retVal.put(label, cotopies);
		}
		return retVal;
	}
	
	private final HashMap<ClusterNode, Collection<ClusterNode>> computeClusterCotopies(HierarchicalClusterModel model)
	{
		HashMap<ClusterNode, Collection<ClusterNode>> retVal = new HashMap<ClusterNode, Collection<ClusterNode>>();
		
		Tree<Integer> clusterTree = model.getClusterStructure();
		Collection<? extends ClusterNode> clusters = model.getClusters();
		for(ClusterNode cluster : clusters)
		{
			Collection<ClusterNode> cotopies = getCotopyClusters(cluster, clusterTree, model);
			retVal.put(cluster, cotopies);
		}
		return retVal;
	}
	
	private final Collection<String> getCotopyClassLabels(String classLabel, Tree<String> labelTree)
	{
		Collection<String> retVal = labelTree.getPathFromRoot(classLabel);
		Tree.Node<String> node = labelTree.getNodeByElement(classLabel);
		PreOrderTreeIterator<String> it = new PreOrderTreeIterator<String>(labelTree, node);
		while(it.hasNext())
		{
			String candidate = it.next();
			if(!candidate.equals(classLabel))
				retVal.add(candidate);
		}
		return retVal;
	}
	
	private final Collection<ClusterNode> getCotopyClusters(ClusterNode cluster, 
			Tree<Integer> clusterTree, HierarchicalClusterModel model)
	{
		Collection<ClusterNode> retVal = new LinkedList<ClusterNode>();
		Collection<Integer> path = clusterTree.getPathFromRoot(cluster.getId());
		for(Integer id : path)
			retVal.add(model.getCluster(id));
		
		Tree.Node<Integer> node = clusterTree.getNodeByElement(cluster.getId());
		PreOrderTreeIterator<Integer> it = new PreOrderTreeIterator<Integer>(clusterTree, node);
		while(it.hasNext())
		{
			Integer id = it.next();
			if(!id.equals(cluster.getId()))
				retVal.add(model.getCluster(id));
		}
		
		return retVal;
	}
	
	private final void initialize()
	{
		overallValue = Double.NaN;
		classValues	 = new HashMap<String, Double>();
		beta = DEFAULT_BETA;
	}
}
