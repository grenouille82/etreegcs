package de.ovgu.dke.clustering.evaluation;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;

public class Purity extends AbstractEvaluationMeasure<FlatClusterModel>
{

	public double compute(FlatClusterModel model) 
	{
		ClusterConfusionMatrix confMatrix = 
			ClusterConfusionMatrix.createClusterConfusionMatrix(model);
		return compute(model, confMatrix);
	}
	
	public double compute(FlatClusterModel model, ClusterConfusionMatrix confusionMatrix)
	{
		if(model == null)
			throw new NullPointerException();
		if(confusionMatrix == null)
			throw new NullPointerException();
		
		overallValue = 0d;
		clusterValues.clear();
		
		int nLabels   = confusionMatrix.numberOfClassLabels();
		int n = confusionMatrix.totalCount();
		for(Cluster c : model.getClusters())
		{
			int clusterIdx  = confusionMatrix.indexOfCluster(c);
			int clusterSize = confusionMatrix.totalClusterCount(clusterIdx);
			
			double maxP = Double.NEGATIVE_INFINITY;
			for(int labelIdx=0; labelIdx<nLabels; labelIdx++)
			{
				double p = (double) confusionMatrix.count(clusterIdx, labelIdx)/clusterSize;
				if(p > maxP)
					maxP = p; 
			}
			
			clusterValues.put(c.getId(), maxP);
			overallValue += maxP*clusterSize/n;
		}
		return overallValue;
	}

	public String getName() 
	{
		return "Purity";
	}
}
