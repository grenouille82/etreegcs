package de.ovgu.dke.clustering.evaluation;

import java.util.Collection;
import java.util.HashMap;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.tree.Tree;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class NonLeafFMeasure 
{
	protected static double DEFAULT_BETA = 1d;
	
	protected HashMap<String, Double> classValues;
	
	protected double overallValue;
	
	protected double beta;
	
	protected Tree<String> referenceLabelHierarchy;
	
	public NonLeafFMeasure()
	{
		initialize();
	}
	
	public void setReferenceHierarchy(Tree<String> hierarchy)
	{
		if(hierarchy == null)
			throw new NullPointerException();
		referenceLabelHierarchy = hierarchy;
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
		ClusterConfusionMatrix matrix = ClusterConfusionMatrix.createClusterConfusionMatrix(model, true);
		return compute(model, matrix);
	}
	
	public double compute(HierarchicalClusterModel model, ClusterConfusionMatrix confMatrix)
	{
		if(model == null)
			throw new NullPointerException();
		if(confMatrix == null)
			throw new NullPointerException();
		
		classValues.clear();
		overallValue = 0d;
		
		Collection<String> classLabels 	= referenceLabelHierarchy.elements();
		Collection<Cluster> clusters 	= confMatrix.getClusters();
		
		double n = 0;
		for(String label : classLabels)
		{
			if(!referenceLabelHierarchy.isLeaf(label) && !referenceLabelHierarchy.isRoot(label)) {
				double maxFscore = Double.NEGATIVE_INFINITY;
				for(Cluster cluster : clusters)
				{
					double prec = precision(cluster, label, confMatrix);
					double rec	= recall(cluster, label, confMatrix);
					
					//System.out.println(prec + "\t" + rec);
					double fscore = (MathUtil.square(beta)+1d)*prec*rec /
									(rec+MathUtil.square(beta)*prec);
					if(Double.isNaN(fscore) || Double.isInfinite(fscore))
						fscore = 0d;
					if(fscore > maxFscore)
						maxFscore = fscore;
					//System.out.println(fscore);
				}
				classValues.put(label, maxFscore);
				double weight = 0d;
				Tree<String> subTree = referenceLabelHierarchy.getSubTree(label);
				for(String l : subTree)
				{
					if(confMatrix.containsClassLabel(l))
						weight += confMatrix.totalClassLabelCount(l);
				}
				n += weight;
				overallValue += (double) weight*maxFscore;
			}
		}
		overallValue /= n;
		return overallValue;
	}

	protected double precision(Cluster cluster, String classLabel, 
			   				   ClusterConfusionMatrix confMatrix) 
	{
		Tree<String> subTree = referenceLabelHierarchy.getSubTree(classLabel);
		double a = 0d;
		double b = confMatrix.totalClusterCount(cluster);
		for(String label : subTree)
		{
			if(confMatrix.containsClassLabel(label))
				a += confMatrix.count(cluster, label);
		}
		double precision = a/b;
		if(Double.isNaN(precision))
			precision = 0d;
		return precision;
	}

	protected double recall(Cluster cluster, String classLabel, 
							ClusterConfusionMatrix confMatrix) 
	{
		Tree<String> subTree = referenceLabelHierarchy.getSubTree(classLabel);
		double a = 0d;
		double b = 0d;
		for(String label : subTree)
		{
			if(confMatrix.containsClassLabel(label)) {
				a += confMatrix.count(cluster, label);
				b += confMatrix.totalClassLabelCount(label);
			}
		}
		double recall = a/b;				
		if(Double.isNaN(recall))
			recall = 0d;
		return recall;
	}
	
	private final void initialize()
	{
		overallValue = Double.NaN;
		classValues	 = new HashMap<String, Double>();
		beta = DEFAULT_BETA;
	}
}
