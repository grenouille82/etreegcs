package de.ovgu.dke.clustering.evaluation;

import de.ovgu.dke.clustering.model.ClusterModel;
import de.ovgu.dke.util.Parametrizable;

public interface EvaluationMeasure<T extends ClusterModel<?>>
extends Parametrizable
{
	public double getValue(int clusterId);
	
	public double getOverallValue();
	
	public double compute(T model);
	
	public String getName();
}
