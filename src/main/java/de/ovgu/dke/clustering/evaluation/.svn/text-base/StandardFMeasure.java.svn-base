package de.ovgu.dke.clustering.evaluation;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.ClusterModel;

public class StandardFMeasure<T extends ClusterModel<?>> extends AbstractFMeasure<T>
{

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
}
