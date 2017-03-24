package de.ovgu.dke.clustering.evaluation;

import java.util.HashMap;


import de.ovgu.dke.clustering.model.ClusterModel;
import de.ovgu.dke.util.Settings;

/**
 * 
 * @author mhermkes
 *
 * @param <T>
 */
public abstract class AbstractEvaluationMeasure<T extends ClusterModel<?>> implements EvaluationMeasure<T> 
{
	protected HashMap<Integer, Double> clusterValues;
	
	protected double overallValue;
	
	public AbstractEvaluationMeasure()
	{
		initialize();
	}

	public double getOverallValue() 
	{
		return overallValue;
	}

	public double getValue(int clusterId) 
	{
		Double retVal = clusterValues.get(clusterId);
		return (retVal == null) ? Double.NaN : retVal;
	}

	public void applyDefaultSettings() {}

	public void applySettings(Settings settings) { }

	public Settings getDefaultSettings() { return null;	}

	public Settings getSettings() { return null; }

	private final void initialize()
	{
		overallValue  = Double.NaN;
		clusterValues = new HashMap<Integer, Double>();
	}
}
