package de.ovgu.dke.clustering.evaluation;

import java.util.Collection;

import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.PrototypeCluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.ObjectSet;

public class SumSquaredError extends AbstractEvaluationMeasure<FlatClusterModel>
implements UnsupervisedEvaluationMeasure<FlatClusterModel>
{	
	public double compute(FlatClusterModel model) 
	{
		if(model == null)
			throw new NullPointerException();
		
		overallValue = 0d;
		clusterValues.clear();
		
		DistanceMeasure metric = model.getDistanceMetric();
		ObjectSet dataset = model.getDataset();
		int n = model.numberOfClusters();
		for(int i=0; i<n; i++)
		{
			double error = 0d;
			if(!(model.getClusterAt(i) instanceof PrototypeCluster))
				throw new IllegalArgumentException("evaluation measure only " +
						"							applicable to prototype clusters");
			
			PrototypeCluster c = (PrototypeCluster) model.getClusterAt(i);
			double[] centroid  = c.getCentroid();
			Collection<Integer> data = c.getData();
			for(int datapoint : data)
			{
				double[] v = dataset.get(datapoint).getRepresentation();
				error += MathUtil.square(metric.getDistance(centroid, v));
			}
			
			overallValue += error;
			
			clusterValues.put(c.getId(), error);
		}
		
		return overallValue;
	}

	public String getName() 
	{
		return "SumSquaredError";
	}

}
