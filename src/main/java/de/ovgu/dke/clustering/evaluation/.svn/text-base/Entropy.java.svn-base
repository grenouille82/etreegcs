package de.ovgu.dke.clustering.evaluation;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.util.MathUtil;

public class Entropy extends AbstractEvaluationMeasure<FlatClusterModel>
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
		
		int nLabels = confusionMatrix.numberOfClassLabels();
		int n = confusionMatrix.totalCount();
		for(Cluster c : model.getClusters())
		{
			int clusterIdx  = confusionMatrix.indexOfCluster(c);
			int clusterSize = confusionMatrix.totalClusterCount(clusterIdx);
			
			double e = 0d;
			if(clusterSize != 0) {
				for(int labelIdx=0; labelIdx<nLabels; labelIdx++)
				{
					double p = (double) confusionMatrix.count(clusterIdx, labelIdx)/clusterSize;
					if(p!=0d)
						e -= p*MathUtil.log2(p); 
				}
			}
			
			clusterValues.put(c.getId(), e);
			overallValue += e*clusterSize/n;
		}
		return overallValue;
	}

	public String getName() 
	{
		return "Entropy";
	}
}
