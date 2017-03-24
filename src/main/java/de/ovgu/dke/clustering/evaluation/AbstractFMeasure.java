package de.ovgu.dke.clustering.evaluation;

import java.util.Collection;
import java.util.HashMap;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.ClusterModel;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public abstract class AbstractFMeasure<T extends ClusterModel<?>>
{
	protected static double DEFAULT_BETA = 1d;
	
	protected HashMap<String, Double> classValues;
	
	protected double overallValue;
	
	protected double beta;
	
	public AbstractFMeasure()
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
	
	public double compute(T model)
	{
		ClusterConfusionMatrix matrix = null;
		if(model instanceof FlatClusterModel)
			matrix = ClusterConfusionMatrix.createClusterConfusionMatrix((FlatClusterModel) model);
		else if(model instanceof HierarchicalClusterModel)
			matrix = ClusterConfusionMatrix.createClusterConfusionMatrix((HierarchicalClusterModel) model, true);
		else throw new IllegalArgumentException("");
		return compute(model, matrix);
	}
	
	public double compute(T model, ClusterConfusionMatrix confMatrix)
	{
		if(model == null)
			throw new NullPointerException();
		if(confMatrix == null)
			throw new NullPointerException();
		
		classValues.clear();
		overallValue = 0d;
		
		Collection<String> classLabels 	= confMatrix.getClassLabels();
		Collection<Cluster> clusters 	= confMatrix.getClusters();
		
		int n = confMatrix.totalCount();
		for(String label : classLabels)
		{
			double maxFscore = Double.NEGATIVE_INFINITY;
			for(Cluster cluster : clusters)
			{
				double prec = precision(cluster, label, confMatrix);
				double rec	= recall(cluster, label, confMatrix);
				double fscore = (MathUtil.square(beta)+1d)*prec*rec /
								(rec+MathUtil.square(beta)*prec);
				if(Double.isNaN(fscore) || Double.isInfinite(fscore))
					fscore = 0d;
				if(fscore > maxFscore)
					maxFscore = fscore;
				//System.out.println(fscore);
			}
			classValues.put(label, maxFscore);
			overallValue += (double) confMatrix.totalClassLabelCount(label)*maxFscore/n;
		}

		return overallValue;
	}

	protected double precision(Cluster cluster, String classLabel, 
			   				   ClusterConfusionMatrix confMatrix) 
	{
		double precision = (double) confMatrix.count(cluster, classLabel) / 
						   confMatrix.totalClusterCount(cluster);
		if(Double.isNaN(precision))
			precision = 0d;
		return precision;
	}

	protected double recall(Cluster cluster, String classLabel, 
							ClusterConfusionMatrix confMatrix) 
	{
		double recall = (double) confMatrix.count(cluster, classLabel) / 
						confMatrix.totalClassLabelCount(classLabel);
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
